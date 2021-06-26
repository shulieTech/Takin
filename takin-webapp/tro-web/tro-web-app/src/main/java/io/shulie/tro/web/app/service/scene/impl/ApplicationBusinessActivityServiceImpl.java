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

package io.shulie.tro.web.app.service.scene.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
//import io.shulie.amdb.common.dto.link.topology.LinkNodeDTO;
//import io.shulie.amdb.common.dto.link.topology.LinkTopologyDTO;
//import io.shulie.amdb.common.enums.NodeTypeEnum;
import io.shulie.tro.web.amdb.api.ApplicationEntranceClient;
import io.shulie.tro.web.app.service.scene.ApplicationBusinessActivityService;
import io.shulie.tro.web.common.constant.FeaturesConstants;
import io.shulie.tro.web.data.mapper.mysql.BusinessLinkManageTableMapper;
import io.shulie.tro.web.data.mapper.mysql.LinkManageTableMapper;
import io.shulie.tro.web.data.model.mysql.BusinessLinkManageTableEntity;
import io.shulie.tro.web.data.model.mysql.LinkManageTableEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ApplicationBusinessActivityServiceImpl
 * @Description
 * @Author qianshui
 * @Date 2020/8/12 上午9:51
 */
@Service
@Slf4j
public class ApplicationBusinessActivityServiceImpl implements ApplicationBusinessActivityService {

    @Autowired
    private ApplicationEntranceClient applicationEntranceClient;

    @Resource
    private BusinessLinkManageTableMapper businessLinkManageTableMapper;

    @Resource
    private LinkManageTableMapper linkManageTableMapper;

    @Override
    public List<String> processAppNameByBusinessActiveId(Long businessActivityId) {
        BusinessLinkManageTableEntity businessLinkManageTableEntity = businessLinkManageTableMapper.selectById(
            businessActivityId);
        if (businessLinkManageTableEntity == null) {
            return Lists.newArrayList();
        }
        LinkManageTableEntity linkManageTableEntity = linkManageTableMapper.selectById(
            businessLinkManageTableEntity.getRelatedTechLink());
        if (linkManageTableEntity == null) {
            return Lists.newArrayList();
        }
        String features = linkManageTableEntity.getFeatures();
        Map<String, String> map = Maps.newHashMap();
        if (StringUtils.isNotBlank(features)) {
            map = JSON.parseObject(features, Map.class);
        }
        String serviceName = map.get(FeaturesConstants.SERVICE_NAME_KEY);
        String methodName = map.get(FeaturesConstants.METHOD_KEY);
        String rpcType = map.get(FeaturesConstants.RPC_TYPE_KEY);
        String extend = map.get(FeaturesConstants.EXTEND_KEY);
        //LinkTopologyDTO applicationEntrancesTopology = applicationEntranceClient.getApplicationEntrancesTopology(
        //    linkManageTableEntity.getApplicationName(),
        //    null, serviceName, methodName, rpcType, extend);
        return Lists.newArrayList();
        //if (applicationEntrancesTopology == null) {
        //    return Lists.newArrayList();
        //}
        //List<LinkNodeDTO> nodes = applicationEntrancesTopology.getNodes();
        //if (CollectionUtils.isEmpty(nodes)) {
        //    return Lists.newArrayList();
        //}
        //return nodes.stream()
        //    .filter(node -> node.getNodeType().equals(NodeTypeEnum.APP.getType()))
        //    .map(LinkNodeDTO::getNodeName)
        //    .collect(Collectors.toList());
    }
}
