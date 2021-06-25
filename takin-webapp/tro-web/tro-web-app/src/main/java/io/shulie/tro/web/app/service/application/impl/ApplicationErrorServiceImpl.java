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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.alibaba.excel.util.CollectionUtils;
import com.alibaba.fastjson.JSONObject;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.util.DateUtils;
import com.pamirs.tro.entity.domain.dto.NodeUploadDataDTO;
import com.pamirs.tro.entity.domain.entity.ExceptionInfo;
import com.pamirs.tro.entity.domain.entity.TApplicationMnt;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.output.application.ApplicationExceptionOutput;
import io.shulie.tro.web.app.request.application.ApplicationErrorQueryRequest;
import io.shulie.tro.web.app.response.application.ApplicationErrorResponse;
import io.shulie.tro.web.app.service.ApplicationService;
import io.shulie.tro.web.app.service.application.ApplicationErrorService;
import io.shulie.tro.web.app.service.impl.ApplicationServiceImpl;
import io.shulie.tro.web.common.enums.application.AppExceptionCodeEnum;
import io.shulie.tro.web.data.dao.application.ApplicationDAO;
import io.shulie.tro.web.data.dao.application.ApplicationNodeDAO;
import io.shulie.tro.web.data.result.application.ApplicationResult;
import io.shulie.tro.web.data.result.application.InstanceInfoResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/10/16 11:41 上午
 * @Description:
 */
@Component
@Slf4j
public class ApplicationErrorServiceImpl implements ApplicationErrorService {

    @Autowired
    private ApplicationNodeDAO applicationNodeDAO;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationDAO applicationDAO;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<ApplicationErrorResponse> list(ApplicationErrorQueryRequest queryRequest) {
        List<ApplicationErrorResponse> responseList = Lists.newArrayList();
        Response<TApplicationMnt> applicationMntResponse = applicationService.getApplicationInfoForError(
            String.valueOf(queryRequest.getApplicationId()));
        TApplicationMnt tApplicationMnt = applicationMntResponse.getData();
        if (Objects.isNull(tApplicationMnt)) {
            return responseList;
        }
        String applicationName = tApplicationMnt.getApplicationName();
        List<ApplicationResult> applicationResultList = applicationDAO.getApplicationByName(
            Arrays.asList(applicationName));
        Integer totalNodeCount = tApplicationMnt.getNodeNum();
        if (CollectionUtils.isEmpty(applicationResultList) || !totalNodeCount.equals(applicationResultList.get(0)
            .getInstanceInfo().getInstanceOnlineAmount())) {
            ApplicationErrorResponse applicationErrorResponse = new ApplicationErrorResponse();
            applicationErrorResponse.setExceptionId("-");
            applicationErrorResponse.setAgentIdList(Arrays.asList("-"));
            applicationErrorResponse.setDescription("在线节点数与节点总数不一致");
            applicationErrorResponse.setTime(DateUtils.getNowDateStr());
            Integer onlineNodeNum = 0;
            if (!CollectionUtils.isEmpty(applicationResultList)) {
                onlineNodeNum = applicationResultList.get(0).getInstanceInfo().getInstanceOnlineAmount();
            }
            applicationErrorResponse.setDetail("设置节点数：" + totalNodeCount + "，上报的节点数：" + onlineNodeNum);
            responseList.add(applicationErrorResponse);
        }
        //        String appUniqueKey = queryRequest.getApplicationId() + ApplicationServiceImpl.PRADAR_SEPERATE_FLAG;
        //        Set<String> keys = redisTemplate.keys(appUniqueKey + "*");
        String appUniqueKey = queryRequest.getApplicationId() + ApplicationServiceImpl.PRADARNODE_KEYSET;
        Set<String> keys = redisTemplate.opsForSet().members(appUniqueKey);
        if (keys == null || keys.size() == 0) {
            return responseList;
        } else {
            for (String nodeKey : keys) {
                if (!redisTemplate.hasKey(nodeKey)) {
                    continue;
                }
                List<String> nodeUploadDataDTOList = redisTemplate.opsForList().range(nodeKey, 0, -1);
                if (CollectionUtils.isEmpty(nodeUploadDataDTOList)) {
                    continue;
                } else {
                    nodeUploadDataDTOList.forEach(n -> {
                        NodeUploadDataDTO nodeUploadDataDTO = JSONObject.parseObject(n, NodeUploadDataDTO.class);
                        Map<String, Object> exceptionMap = nodeUploadDataDTO.getSwitchErrorMap();
                        if (exceptionMap != null && exceptionMap.size() > 0) {
                            for (Map.Entry<String, Object> entry : exceptionMap.entrySet()) {
                                String message = String.valueOf(entry.getValue());
                                if (message.contains("errorCode")) {
                                    try {
                                        ExceptionInfo exceptionInfo = JSONObject.parseObject(message,
                                            ExceptionInfo.class);
                                        ApplicationErrorResponse applicationErrorResponse
                                            = new ApplicationErrorResponse();
                                        applicationErrorResponse.setExceptionId(exceptionInfo.getErrorCode());
                                        applicationErrorResponse.setAgentIdList(
                                            Arrays.asList(nodeUploadDataDTO.getAgentId()));
                                        applicationErrorResponse.setDescription(exceptionInfo.getMessage());
                                        applicationErrorResponse.setDetail(exceptionInfo.getDetail());
                                        applicationErrorResponse.setTime(nodeUploadDataDTO.getExceptionTime());
                                        responseList.add(applicationErrorResponse);
                                    } catch (Exception e) {
                                        log.error(message);
                                        log.error("异常转换失败：", e);
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
        return responseList;
    }

    @Override
    public List<ApplicationExceptionOutput> getAppException(List<String> appNames) {
        List<ApplicationExceptionOutput> outputs = Lists.newArrayList();
        List<ApplicationResult> applicationResultList = applicationDAO.getApplicationByName(appNames);
        applicationResultList.forEach(app -> {
            InstanceInfoResult result = app.getInstanceInfo();
            if (!result.getInstanceAmount().equals(result.getInstanceOnlineAmount())) {
                ApplicationExceptionOutput output = new ApplicationExceptionOutput();
                output.setApplicationName(app.getAppName());
                output.setAgentIds(Arrays.asList("-"));
                output.setCode(AppExceptionCodeEnum.EPC0001.getCode());
                output.setDescription(AppExceptionCodeEnum.EPC0001.getDesc());
                output.setTime(DateUtils.getNowDateStr());
                outputs.add(output);
            }
            String appUniqueKey = app.getAppId() + ApplicationServiceImpl.PRADAR_SEPERATE_FLAG;
            Set<String> keys = redisTemplate.keys(appUniqueKey + "*");
            if (keys != null) {
                for (String nodeKey : keys) {
                    List<String> nodeUploadDataDTOList = redisTemplate.opsForList().range(nodeKey, 0, -1);
                    if (CollectionUtils.isEmpty(nodeUploadDataDTOList)) {
                        continue;
                    } else {
                        nodeUploadDataDTOList.forEach(n -> {
                            NodeUploadDataDTO nodeUploadDataDTO = JSONObject.parseObject(n, NodeUploadDataDTO.class);
                            Map<String, Object> exceptionMap = nodeUploadDataDTO.getSwitchErrorMap();
                            if (exceptionMap != null && exceptionMap.size() > 0) {
                                for (Map.Entry<String, Object> entry : exceptionMap.entrySet()) {
                                    String message = String.valueOf(entry.getValue());
                                    if (message.contains("errorCode")) {
                                        try {
                                            ExceptionInfo exceptionInfo = JSONObject.parseObject(message,
                                                ExceptionInfo.class);
                                            ApplicationExceptionOutput output = new ApplicationExceptionOutput();
                                            output.setApplicationName(app.getAppName());
                                            output.setAgentIds(Arrays.asList(nodeUploadDataDTO.getAgentId()));
                                            output.setCode(exceptionInfo.getErrorCode());
                                            output.setDescription(exceptionInfo.getMessage());
                                            // todo 时间需要修改
                                            output.setTime(nodeUploadDataDTO.getExceptionTime());
                                            // todo 明细不全不传 exceptionInfo.getDetail()
                                            outputs.add(output);
                                        } catch (Exception e) {
                                            log.error(message);
                                            log.error("异常转换失败：", e);
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
        return outputs;
    }

}
