/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.web.app.service.application.impl;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.pamirs.tro.entity.domain.vo.ApplicationVo;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.request.application.ApplicationNodeDashBoardQueryRequest;
import io.shulie.tro.web.app.request.application.ApplicationNodeQueryRequest;
import io.shulie.tro.web.app.response.application.ApplicationNodeDashBoardResponse;
import io.shulie.tro.web.app.response.application.ApplicationNodeResponse;
import io.shulie.tro.web.app.service.ApplicationService;
import io.shulie.tro.web.app.service.application.ApplicationNodeService;
import io.shulie.tro.web.data.dao.application.ApplicationDAO;
import io.shulie.tro.web.data.dao.application.ApplicationNodeDAO;
import io.shulie.tro.web.data.param.application.ApplicationNodeQueryParam;
import io.shulie.tro.web.data.result.application.ApplicationNodeResult;
import io.shulie.tro.web.data.result.application.ApplicationResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2020-09-23 19:31
 * @Description:
 */
@Service
@Slf4j
public class ApplicationNodeServiceImpl implements ApplicationNodeService {

    @Autowired
    private ApplicationNodeDAO applicationNodeDAO;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationDAO applicationDAO;

    @Override
    public PagingList<ApplicationNodeResponse> pageNodes(ApplicationNodeQueryRequest request) {
        ApplicationNodeQueryParam queryParam = new ApplicationNodeQueryParam();
        Response<ApplicationVo> applicationVoResponse = applicationService.getApplicationInfo(
            String.valueOf(request.getApplicationId()));
        ApplicationVo applicationVo = applicationVoResponse.getData();
        if (Objects.isNull(applicationVo)) {
            return PagingList.empty();
        }
        queryParam.setCurrent(request.getCurrent());
        queryParam.setPageSize(request.getPageSize());
        queryParam.setApplicationNames(Arrays.asList(applicationVo.getApplicationName()));
        queryParam.setIp(request.getIp());
        PagingList<ApplicationNodeResult> applicationNodes = applicationNodeDAO.pageNodes(queryParam);
        if (applicationNodes.isEmpty()) {
            return PagingList.empty();
        }
        List<ApplicationNodeResponse> responseNodes = applicationNodes.getList().stream().map(instance -> {
            ApplicationNodeResponse response = new ApplicationNodeResponse();
            BeanUtils.copyProperties(instance, response);
            return response;
        }).collect(Collectors.toList());
        responseNodes.sort(Comparator.comparing(ApplicationNodeResponse::getAgentId));
        return PagingList.of(responseNodes, applicationNodes.getTotal());
    }

    @Override
    public ApplicationNodeResponse getNodeByAgentId(String agentId) {
        ApplicationNodeResult applicationNode = applicationNodeDAO.getNodeByAgentId(agentId);
        ApplicationNodeResponse response = new ApplicationNodeResponse();
        BeanUtils.copyProperties(applicationNode, response);
        return response;
    }

    @Override
    public ApplicationNodeDashBoardResponse getApplicationNodeAmount(ApplicationNodeDashBoardQueryRequest request) {
        ApplicationNodeDashBoardResponse response = new ApplicationNodeDashBoardResponse();
        response.setNodeTotalCount(0);
        response.setNodeOnlineCount(0);
        Response<ApplicationVo> applicationVoResponse = applicationService.getApplicationInfo(
            String.valueOf(request.getApplicationId()));
        ApplicationVo applicationVo = applicationVoResponse.getData();
        if (Objects.isNull(applicationVo.getId())) {
            return response;
        }
        String applicationName = applicationVo.getApplicationName();
        List<ApplicationResult> applicationResultList = applicationDAO.getApplicationByName(
            Arrays.asList(applicationName));
        response.setNodeTotalCount(applicationVo.getNodeNum());
        if (CollectionUtils.isEmpty(applicationResultList)) {
            response.setErrorMsg("在线节点数与节点总数不一致");
            return response;
        }
        response.setNodeOnlineCount(applicationResultList.get(0).getInstanceInfo().getInstanceOnlineAmount());
        if (!response.getNodeOnlineCount().equals(response.getNodeTotalCount())) {
            response.setErrorMsg("在线节点数与节点总数不一致");
            return response;
        }
        ApplicationNodeQueryParam queryParam = new ApplicationNodeQueryParam();
        queryParam.setCurrent(0);
        queryParam.setPageSize(99999);
        queryParam.setApplicationNames(Arrays.asList(applicationVo.getApplicationName()));
        PagingList<ApplicationNodeResult> applicationNodes = applicationNodeDAO.pageNodes(queryParam);
        List<ApplicationNodeResult> applicationNodeResultList = applicationNodes.getList();
        if (CollectionUtils.isNotEmpty(applicationNodeResultList)) {
            Long count = applicationNodeResultList.stream().map(ApplicationNodeResult::getAgentVersion).distinct()
                .count();
            if (count > 1) {
                response.setErrorMsg("agent版本不一致");
            }
        }
        return response;
    }
}
