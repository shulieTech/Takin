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

package io.shulie.tro.web.app.init.fix;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import io.shulie.tro.web.amdb.bean.common.EntranceTypeEnum;
import io.shulie.tro.web.common.constant.FeaturesConstants;
import io.shulie.tro.web.common.util.ActivityUtil;
import io.shulie.tro.web.common.util.ActivityUtil.EntranceJoinEntity;
import io.shulie.tro.web.data.mapper.mysql.BusinessLinkManageTableMapper;
import io.shulie.tro.web.data.mapper.mysql.LinkManageTableMapper;
import io.shulie.tro.web.data.model.mysql.BusinessLinkManageTableEntity;
import io.shulie.tro.web.data.model.mysql.LinkManageTableEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2021-01-14
 */
@Component
@Slf4j
public class LinkManageFixer {

    @Value("${link.fix.enable:false}")
    private Boolean enableLinkFix;

    @Resource
    private LinkManageTableMapper linkManageTableMapper;

    @Resource
    private BusinessLinkManageTableMapper businessLinkManageTableMapper;

    public void fix() {
        if (enableLinkFix) {
            log.info("开始修复旧的业务活动数据数据");
            List<BusinessLinkManageTableEntity> businessLinkManageTableEntities = businessLinkManageTableMapper
                .selectList(new LambdaQueryWrapper<>());
            LambdaQueryWrapper<LinkManageTableEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            List<LinkManageTableEntity> linkManageTableEntities = linkManageTableMapper.selectList(lambdaQueryWrapper);

            if (CollectionUtils.isEmpty(businessLinkManageTableEntities)) {
                return;
            }
            Map<Long, LinkManageTableEntity> collect = linkManageTableEntities.stream().collect(
                Collectors.toMap(LinkManageTableEntity::getLinkId, i -> i));

            businessLinkManageTableEntities.forEach(item -> {

                LinkManageTableEntity linkManageTableEntity = collect.get(Long.parseLong(item.getRelatedTechLink()));

                if (linkManageTableEntity == null) {
                    return;
                }

                if (linkManageTableEntity.getFeatures() != null) {
                    return;
                }

                try {
                    String entrace = item.getEntrace();

                    String[] split = entrace.split("\\|");
                    if (split.length > 3) {
                        String[] split1 = {"", "", ""};
                        split1[0] = split[0];
                        split1[1] = split[1];
                        split1[2] = split[split.length - 1];
                        split = split1;
                    }

                    ActivityUtil.EntranceJoinEntity entranceJoinEntity = new EntranceJoinEntity();
                    entranceJoinEntity.setApplicationName(split[0]);
                    entranceJoinEntity.setRpcType("0");
                    String s2 = split[2];
                    String method = "GET";
                    if (s2.startsWith("POST/")) {
                        s2 = s2.startsWith("POST/") ? s2.replace("POST/", "") : s2;
                        method = "POST";
                    }
                    if (s2.startsWith("PUT/")) {
                        s2 = s2.startsWith("PUT/") ? s2.replace("PUT/", "") : s2;
                        method = "PUT";
                    }
                    if (s2.startsWith("DELETE/")) {
                        s2 = s2.startsWith("DELETE/") ? s2.replace("DELETE/", "") : s2;
                        method = "DELETE";
                    }
                    if (s2.startsWith("GET/")) {
                        s2 = s2.startsWith("GET/") ? s2.replace("GET/", "") : s2;
                        method = "GET";
                    }
                    s2 = s2.startsWith("http:///") ? s2.replace("http:///", "") : s2;
                    s2 = s2.startsWith("http://") ? s2.replace("http://", "") : s2;
                    entranceJoinEntity.setMethodName(method);
                    entranceJoinEntity.setServiceName(s2);
                    Map<String, String> features = Maps.newHashMap();
                    features.put(FeaturesConstants.SERVER_MIDDLEWARE_TYPE_KEY, EntranceTypeEnum.HTTP.getType());
                    features.put(FeaturesConstants.EXTEND_KEY, "");
                    features.put(FeaturesConstants.RPC_TYPE_KEY, "0");
                    features.put(FeaturesConstants.METHOD_KEY, method);
                    features.put(FeaturesConstants.SERVICE_NAME_KEY, s2);
                    LinkManageTableEntity entity = new LinkManageTableEntity();
                    entity.setLinkId(linkManageTableEntity.getLinkId());
                    entity.setEntrace(ActivityUtil.toEntrance(entranceJoinEntity));
                    entity.setFeatures(JSON.toJSONString(features));
                    linkManageTableMapper.updateById(entity);

                    BusinessLinkManageTableEntity businessLinkManageTableEntity = new BusinessLinkManageTableEntity();
                    businessLinkManageTableEntity.setEntrace(ActivityUtil.toEntrance(entranceJoinEntity));
                    businessLinkManageTableEntity.setLinkId(item.getLinkId());
                    businessLinkManageTableMapper.updateById(businessLinkManageTableEntity);

                } catch (Exception e) {
                    log.error("业务活动入口数据修正失败，数据为：" + JSON.toJSONString(item));
                }
            });
        }
    }
}
