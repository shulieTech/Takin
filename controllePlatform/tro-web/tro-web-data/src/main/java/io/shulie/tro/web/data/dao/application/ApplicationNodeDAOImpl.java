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

package io.shulie.tro.web.data.dao.application;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.http.DateUtil;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.amdb.api.ApplicationClient;
import io.shulie.tro.web.amdb.bean.query.application.ApplicationNodeQueryDTO;
import io.shulie.tro.web.amdb.bean.result.application.ApplicationNodeDTO;
import io.shulie.tro.web.data.param.application.ApplicationNodeQueryParam;
import io.shulie.tro.web.data.result.application.ApplicationNodeResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2020-09-23 19:15
 * @Description:
 */

@Service
public class ApplicationNodeDAOImpl implements ApplicationNodeDAO {

    @Autowired
    private ApplicationClient applicationClient;

    @Override
    public PagingList<ApplicationNodeResult> pageNodes(ApplicationNodeQueryParam param) {
        //分批从amdb获取应用节点数据
        List<String> appNames = param.getApplicationNames();
        List<ApplicationNodeResult> applicationNodeResultList;
        List<ApplicationNodeDTO> applicationNodeDTOTotalList = Lists.newArrayList();
        int BATCH_SIZE = 100;
        long TOTAL_COUNT = 0;
        for (int from = 0, to = 0, size = appNames.size(); from < size; from = to) {
            to = Math.min(from + BATCH_SIZE, size);
            List<String> pageAppNameList = appNames.subList(from, to);
            ApplicationNodeQueryDTO applicationQueryDTO = new ApplicationNodeQueryDTO();
            applicationQueryDTO.setAppNames(String.join(",", pageAppNameList));
            applicationQueryDTO.setCurrentPage(param.getCurrent() + 1);
            applicationQueryDTO.setPageSize(param.getPageSize());
            if (StringUtils.isNotBlank(param.getIp())) {
                applicationQueryDTO.setIpAddress(param.getIp());
            }
            PagingList<ApplicationNodeDTO> applicationNodeDTOPagingList = applicationClient.pageApplicationNodes(
                applicationQueryDTO);
            if (!applicationNodeDTOPagingList.isEmpty()) {
                TOTAL_COUNT = TOTAL_COUNT + applicationNodeDTOPagingList.getTotal();
                applicationNodeDTOTotalList.addAll(applicationNodeDTOPagingList.getList());
            }
        }
        if (!applicationNodeDTOTotalList.isEmpty()) {
            applicationNodeResultList = applicationNodeDTOTotalList.stream().map(applicationNodeDTO -> {
                ApplicationNodeResult applicationInstanceResult = new ApplicationNodeResult();
                applicationInstanceResult.setAppId(String.valueOf(applicationNodeDTO.getAppId()));
                applicationInstanceResult.setAppName(applicationNodeDTO.getAppName());
                applicationInstanceResult.setAgentId(String.valueOf(applicationNodeDTO.getAgentId()));
                applicationInstanceResult.setAgentLanguage(applicationNodeDTO.getAgentLanguage());
                applicationInstanceResult.setAgentVersion(applicationNodeDTO.getAgentVersion());
                applicationInstanceResult.setNodeIp(applicationNodeDTO.getIpAddress());
                applicationInstanceResult.setProcessNo(applicationNodeDTO.getProgressId());
                applicationInstanceResult.setMd5Value(applicationNodeDTO.getAgentMd5());
                applicationInstanceResult.setUpdateTime(
                    DateUtil.getDate(applicationNodeDTO.getAgentUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
                return applicationInstanceResult;
            }).collect(Collectors.toList());
            return PagingList.of(applicationNodeResultList, TOTAL_COUNT);
        } else {
            return PagingList.empty();
        }
    }

    @Override
    public ApplicationNodeResult getNodeByAgentId(String agentId) {
        ApplicationNodeResult applicationNode = new ApplicationNodeResult();
        applicationNode.setAgentId(agentId);
        applicationNode.setAgentLanguage("java");
        applicationNode.setAgentVersion("1.1");
        applicationNode.setCreateTime("2020-01-01 11:11:11");
        applicationNode.setNodeIp("127.0.0.1");
        applicationNode.setProcessNo("987");
        applicationNode.setMd5Value("sssssscdscve");
        applicationNode.setUpdateTime("2020-01-01 11:11:11");
        return applicationNode;
    }

}
