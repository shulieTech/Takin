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

package io.shulie.tro.web.amdb.api.impl;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import com.google.common.collect.Lists;
import io.shulie.amdb.common.dto.link.entrance.ServiceInfoDTO;
import io.shulie.amdb.common.dto.link.topology.LinkTopologyDTO;
import io.shulie.amdb.common.enums.RpcType;
import io.shulie.amdb.common.request.link.ServiceQueryParam;
import io.shulie.amdb.common.request.link.TopologyQueryParam;
import io.shulie.tro.web.amdb.api.ApplicationEntranceClient;
import io.shulie.tro.web.amdb.bean.common.AmdbResult;
import io.shulie.tro.web.amdb.bean.common.EntranceTypeEnum;
import io.shulie.tro.web.amdb.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.tro.properties.AmdbClientProperties;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-12-29
 */
@Component
@Slf4j
public class ApplicationEntranceClientImpl implements ApplicationEntranceClient {

    //public static final String APPLICATION_ENTRANCES_PATH = "/amdb/link/getEntranceList";
    public static final String APPLICATION_ENTRANCES_PATH = "/amdb/link/getServiceList";

    public static final String APPLICATION_ENTRANCES_TOPOLOGY_PATH = "/amdb/link/getLinkTopology";

    public static final String APPLICATION_ENTRANCES_UNKNOWN_UPDATE_TO_OUTER = "/amdb/link/updateUnKnowNode";

    @Autowired
    private AmdbClientProperties properties;

    @Override
    public List<ServiceInfoDTO> getApplicationEntrances(String applicationName, String entranceType) {
        String url = properties.getUrl().getAmdb() + APPLICATION_ENTRANCES_PATH;
        ServiceQueryParam entranceQueryParam = new ServiceQueryParam();
        EntranceTypeEnum entranceTypeEnum = EntranceTypeEnum.getEnumByName(entranceType);
        switch (entranceTypeEnum) {
            case HTTP:
                entranceQueryParam.setRpcType(RpcType.TYPE_WEB_SERVER + "");
                break;
            case DUBBO:
                entranceQueryParam.setRpcType(RpcType.TYPE_RPC + "");
                break;
            case KAFKA:
            case RABBITMQ:
            case ROCKETMQ:
                entranceQueryParam.setRpcType(RpcType.TYPE_MQ + "");
                entranceQueryParam.setMiddlewareName(entranceTypeEnum.getType());
                break;
            case ELASTICJOB:
                entranceQueryParam.setRpcType(RpcType.TYPE_JOB + "");
                entranceQueryParam.setMiddlewareName("ELASTIC-JOB");
                break;
        }
        entranceQueryParam.setAppName(applicationName);
        entranceQueryParam.setFieldNames("appName,serviceName,methodName,middlewareName,rpcType");
        try {
            String responseEntity = HttpClientUtil.sendGet(url, entranceQueryParam);
            if (StringUtils.isBlank(responseEntity)) {
                log.error("前往amdb查询入口信息返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(entranceQueryParam));
                return null;
            }
            AmdbResult<List<ServiceInfoDTO>> amdbResponse = JSON.parseObject(responseEntity,
                new TypeReference<AmdbResult<List<ServiceInfoDTO>>>() {
                });
            if (amdbResponse == null || !amdbResponse.getSuccess()) {
                log.error("前往amdb查询入口信息返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(entranceQueryParam));
                return Lists.newArrayList();
            }
            return amdbResponse.getData();
        } catch (Exception e) {
            log.error("前往amdb查询入口信息返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(entranceQueryParam), e);
            return Lists.newArrayList();
        }
    }

    @Override
    public LinkTopologyDTO getApplicationEntrancesTopology(String applicationName, String linkId, String serviceName,
        String method, String rpcType, String extend) {
        String url = properties.getUrl().getAmdb() + APPLICATION_ENTRANCES_TOPOLOGY_PATH;
        TopologyQueryParam topologyQueryParam = new TopologyQueryParam();
        topologyQueryParam.setAppName(applicationName);
        if (method != null) {
            topologyQueryParam.setMethod(method);
        }
        if (rpcType != null) {
            topologyQueryParam.setRpcType(rpcType);
        }
        if (serviceName != null) {
            topologyQueryParam.setServiceName(serviceName);
        }
        if (extend != null) {
            topologyQueryParam.setExtend(extend);
        }
        try {
            String responseEntity = HttpClientUtil.sendGet(url, topologyQueryParam);
            if (StringUtils.isBlank(responseEntity)) {
                log.error("前往amdb查询拓扑图信息返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(topologyQueryParam));
                return null;
            }
            AmdbResult<LinkTopologyDTO> amdbResponse = JSON.parseObject(responseEntity,
                new TypeReference<AmdbResult<LinkTopologyDTO>>() {
                });
            if (amdbResponse == null || !amdbResponse.getSuccess()) {
                log.error("前往amdb查询拓扑图信息返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(topologyQueryParam));
                return null;
            }
            LinkTopologyDTO data = amdbResponse.getData();
            if (data == null) {
                return data;
            }
            data.getNodes().forEach(node -> {
                if (node.getNodeName() == null) {
                    node.setNodeName("");
                }
            });
            return data;
        } catch (Exception e) {
            log.error("前往amdb查询拓扑图信息返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(topologyQueryParam), e);
            return null;
        }
    }

    @Override
    public Boolean updateUnknownNodeToOuter(String applicationName, String linkId, String serviceName, String method,
        String rpcType, String extend, String nodeId) {
        String url = properties.getUrl().getAmdb() + APPLICATION_ENTRANCES_UNKNOWN_UPDATE_TO_OUTER;
        TopologyQueryParam topologyQueryParam = new TopologyQueryParam();
        topologyQueryParam.setAppName(applicationName);
        if (method != null) {
            topologyQueryParam.setMethod(method);
        }
        if (rpcType != null) {
            topologyQueryParam.setRpcType(rpcType);
        }
        if (serviceName != null) {
            topologyQueryParam.setServiceName(serviceName);
        }
        if (extend != null) {
            topologyQueryParam.setExtend(extend);
        }
        topologyQueryParam.setId(nodeId);
        try {
            String responseEntity = HttpClientUtil.sendGet(url, topologyQueryParam);
            if (StringUtils.isBlank(responseEntity)) {
                log.error("前往amdb更新未知应用返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(topologyQueryParam));
                return null;
            }
            AmdbResult amdbResponse = JSON.parseObject(responseEntity,
                new TypeReference<AmdbResult>() {
                });
            if (amdbResponse == null || !amdbResponse.getSuccess()) {
                log.error("前往amdb更新未知应用返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(topologyQueryParam));
                return null;
            }
            return amdbResponse.getSuccess();
        } catch (Exception e) {
            log.error("前往amdb更新未知应用返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(topologyQueryParam), e);
            return false;
        }
    }

    @Override
    public List<ServiceInfoDTO> getMqTopicGroups(String applicationName) {
        String url = properties.getUrl().getAmdb() + APPLICATION_ENTRANCES_PATH;
        ServiceQueryParam entranceQueryParam = new ServiceQueryParam();
        entranceQueryParam.setRpcType(RpcType.TYPE_MQ + "");
        entranceQueryParam.setAppName(applicationName);
        entranceQueryParam.setFieldNames("appName,serviceName,methodName,middlewareName,rpcType");
        try {
            String responseEntity = HttpClientUtil.sendGet(url, entranceQueryParam);
            if (StringUtils.isBlank(responseEntity)) {
                log.error("前往amdb查询MQ消费者返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(entranceQueryParam));
                return null;
            }
            AmdbResult<List<ServiceInfoDTO>> amdbResponse = JSON.parseObject(responseEntity,
                new TypeReference<AmdbResult<List<ServiceInfoDTO>>>() {
                });
            if (amdbResponse == null || !amdbResponse.getSuccess()) {
                log.error("前往amdb查询MQ消费者返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(entranceQueryParam));
                return Lists.newArrayList();
            }
            return amdbResponse.getData();
        } catch (Exception e) {
            log.error("前往amdb查询MQ消费者返回异常,请求地址：{}，请求参数：{}", url, JSON.toJSONString(entranceQueryParam), e);
            return Lists.newArrayList();
        }
    }
}
