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

package io.shulie.tro.web.data.dao.activity.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.amdb.bean.common.EntranceTypeEnum;
import io.shulie.tro.web.common.constant.FeaturesConstants;
import io.shulie.tro.web.common.util.ActivityUtil;
import io.shulie.tro.web.common.util.ActivityUtil.EntranceJoinEntity;
import io.shulie.tro.web.data.dao.activity.ActivityDAO;
import io.shulie.tro.web.data.mapper.mysql.BusinessLinkManageTableMapper;
import io.shulie.tro.web.data.mapper.mysql.LinkManageTableMapper;
import io.shulie.tro.web.data.model.mysql.BusinessLinkManageTableEntity;
import io.shulie.tro.web.data.model.mysql.LinkManageTableEntity;
import io.shulie.tro.web.data.param.activity.ActivityCreateParam;
import io.shulie.tro.web.data.param.activity.ActivityExistsQueryParam;
import io.shulie.tro.web.data.param.activity.ActivityQueryParam;
import io.shulie.tro.web.data.param.activity.ActivityUpdateParam;
import io.shulie.tro.web.data.result.activity.ActivityListResult;
import io.shulie.tro.web.data.result.activity.ActivityResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-12-30
 */
@Component
@Slf4j
public class ActivityDAOImpl implements ActivityDAO {

    @Resource
    private LinkManageTableMapper linkManageTableMapper;

    @Resource
    private BusinessLinkManageTableMapper businessLinkManageTableMapper;

    @Override
    public List<Long> exists(ActivityExistsQueryParam param) {
        LambdaQueryWrapper<BusinessLinkManageTableEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(
            BusinessLinkManageTableEntity::getLinkId
        );
        if (param.getActivityName() != null) {
            wrapper.eq(BusinessLinkManageTableEntity::getLinkName, param.getActivityName());
        }
        if (param.getServiceName() != null) {
            wrapper.eq(BusinessLinkManageTableEntity::getEntrace,
                ActivityUtil.buildEntrance(param.getApplicationName(), param.getMethod(), param.getServiceName(),
                    param.getRpcType()));
        }
        wrapper.eq(BusinessLinkManageTableEntity::getIsDeleted, 0);
        List<BusinessLinkManageTableEntity> businessLinkManageTableEntities = businessLinkManageTableMapper.selectList(
            wrapper);
        if (CollectionUtils.isEmpty(businessLinkManageTableEntities)) {
            return Lists.newArrayList();
        }
        return businessLinkManageTableEntities.stream()
            .map(BusinessLinkManageTableEntity::getLinkId)
            .collect(Collectors.toList());
    }

    @Override
    public int createActivity(ActivityCreateParam param) {

        // 兼容老版本,先创建技术链路
        LinkManageTableEntity linkManageTableEntity = new LinkManageTableEntity();
        linkManageTableEntity.setLinkName(param.getActivityName());
        String entrance = ActivityUtil.buildEntrance(param.getApplicationName(), param.getMethod(),
            param.getServiceName(), param.getRpcType());
        linkManageTableEntity.setEntrace(entrance);
        linkManageTableEntity.setChangeBefore(param.getChangeBefore());
        linkManageTableEntity.setChangeAfter(param.getChangeAfter());
        linkManageTableEntity.setIsChange(param.getIsChange() ? 1 : 0);
        linkManageTableEntity.setCustomerId(param.getCustomerId());
        linkManageTableEntity.setUserId(param.getUserId());
        linkManageTableEntity.setIsChange(0);
        linkManageTableEntity.setApplicationName(param.getApplicationName());
        Map<String, String> map = new HashMap<>();
        map.put(FeaturesConstants.EXTEND_KEY, param.getExtend());
        map.put(FeaturesConstants.METHOD_KEY, param.getMethod());
        map.put(FeaturesConstants.RPC_TYPE_KEY, param.getRpcType());
        map.put(FeaturesConstants.SERVICE_NAME_KEY, param.getServiceName());
        map.put(FeaturesConstants.SERVER_MIDDLEWARE_TYPE_KEY, param.getType().getType());
        linkManageTableEntity.setFeatures(JSON.toJSONString(map));

        // 再创建业务链路
        int insert1 = linkManageTableMapper.insert(linkManageTableEntity);

        BusinessLinkManageTableEntity businessLinkManageTableEntity = new BusinessLinkManageTableEntity();
        businessLinkManageTableEntity.setLinkName(param.getActivityName());
        businessLinkManageTableEntity.setEntrace(entrance);
        businessLinkManageTableEntity.setRelatedTechLink(String.valueOf(linkManageTableEntity.getLinkId()));
        businessLinkManageTableEntity.setLinkLevel(param.getActivityLevel());
        businessLinkManageTableEntity.setIsChange(0);
        businessLinkManageTableEntity.setIsCore(param.getIsCore());
        businessLinkManageTableEntity.setBusinessDomain(param.getBusinessDomain());
        businessLinkManageTableEntity.setIsDeleted(0);
        businessLinkManageTableEntity.setCustomerId(param.getCustomerId());
        businessLinkManageTableEntity.setUserId(param.getUserId());
        businessLinkManageTableEntity.setCanDelete(0);
        int insert = businessLinkManageTableMapper.insert(businessLinkManageTableEntity);
        return insert1 & insert;
    }

    @Override
    public ActivityResult getActivityById(Long activityId) {

        BusinessLinkManageTableEntity businessLinkManageTableEntity = businessLinkManageTableMapper.selectById(
            activityId);
        if (businessLinkManageTableEntity == null || businessLinkManageTableEntity.getIsDeleted() == 1) {
            log.error("查询{}对应的业务活动失败，对应链路为空", activityId);
            return null;
        }
        String relatedTechLink = businessLinkManageTableEntity.getRelatedTechLink();

        LinkManageTableEntity linkManageTableEntity = linkManageTableMapper.selectById(relatedTechLink);
        if (linkManageTableEntity == null || linkManageTableEntity.getIsDeleted() == 1) {
            log.error("查询{}对应的业务活动失败，对应链路为空", activityId);
            return null;
        }

        if (StringUtils.isBlank(linkManageTableEntity.getFeatures())) {
            throw new RuntimeException("历史的业务数据无法使用，请重新配置或者数据订正");
        }

        ActivityResult result = new ActivityResult();
        result.setActivityId(businessLinkManageTableEntity.getLinkId());
        result.setActivityName(businessLinkManageTableEntity.getLinkName());
        EntranceJoinEntity entranceJoinEntity = ActivityUtil.covertEntrance(businessLinkManageTableEntity.getEntrace());
        result.setApplicationName(entranceJoinEntity.getApplicationName());
        result.setEntranceName(entranceJoinEntity.getServiceName());// entranceName
        result.setIsChange(businessLinkManageTableEntity.getIsChange() == 1);
        result.setUserId(businessLinkManageTableEntity.getUserId());
        result.setCustomerId(businessLinkManageTableEntity.getCustomerId());
        result.setActivityLevel(businessLinkManageTableEntity.getLinkLevel());
        result.setIsCore(businessLinkManageTableEntity.getIsCore());
        result.setBusinessDomain(businessLinkManageTableEntity.getBusinessDomain());
        Map<String, String> features;
        if (StringUtils.isNotBlank(linkManageTableEntity.getFeatures())) {
            features = JSON.parseObject(linkManageTableEntity.getFeatures(), Map.class);
        } else {
            features = Maps.newHashMap();
        }
        result.setExtend(features.get(FeaturesConstants.EXTEND_KEY));
        result.setMethod(features.get(FeaturesConstants.METHOD_KEY));
        result.setRpcType(features.get(FeaturesConstants.RPC_TYPE_KEY));
        result.setServiceName(features.get(FeaturesConstants.SERVICE_NAME_KEY));
        result.setType(
            EntranceTypeEnum.getEnumByType(features.get(FeaturesConstants.SERVER_MIDDLEWARE_TYPE_KEY)));
        return result;
    }

    @Override
    public int updateActivity(ActivityUpdateParam updateParam) {
        BusinessLinkManageTableEntity businessLinkManageTableEntity = businessLinkManageTableMapper.selectById(
            updateParam.getActivityId());
        LinkManageTableEntity linkManageTableEntity = linkManageTableMapper.selectById(
            businessLinkManageTableEntity.getRelatedTechLink());
        if (updateParam.getActivityName() != null) {
            businessLinkManageTableEntity.setLinkName(updateParam.getActivityName());
            linkManageTableEntity.setLinkName(updateParam.getActivityName());
        }

        if (StringUtils.isNotBlank(updateParam.getApplicationName())) {
            linkManageTableEntity.setApplicationName(updateParam.getApplicationName());
        }

        if (updateParam.getEntranceName() != null) {
            String entrance = ActivityUtil.buildEntrance(updateParam.getApplicationName(), updateParam.getMethod(),
                updateParam.getServiceName(), updateParam.getRpcType());
            businessLinkManageTableEntity.setEntrace(entrance);
            linkManageTableEntity.setEntrace(entrance);
            Map<String, String> features = JSON.parseObject(linkManageTableEntity.getFeatures(), Map.class);
            if (features == null) {
                features = Maps.newHashMap();
            }
            if (StringUtils.isNotBlank(updateParam.getExtend())) {
                features.put(FeaturesConstants.EXTEND_KEY, updateParam.getExtend());
            }
            if (StringUtils.isNotBlank(updateParam.getMethod())) {
                features.put(FeaturesConstants.METHOD_KEY, updateParam.getMethod());
            }
            if (StringUtils.isNotBlank(updateParam.getRpcType())) {
                features.put(FeaturesConstants.RPC_TYPE_KEY, updateParam.getRpcType());
            }
            if (StringUtils.isNotBlank(updateParam.getServiceName())) {
                features.put(FeaturesConstants.SERVICE_NAME_KEY, updateParam.getServiceName());
            }
            if (updateParam.getType() !=null) {
                features.put(FeaturesConstants.SERVER_MIDDLEWARE_TYPE_KEY, updateParam.getType().getType());
            }
            linkManageTableEntity.setFeatures(JSON.toJSONString(features));
        }
        if (StringUtils.isNotBlank(updateParam.getActivityLevel())) {
            businessLinkManageTableEntity.setLinkLevel(updateParam.getActivityLevel());
        }
        if (StringUtils.isNotBlank(updateParam.getBusinessDomain())) {
            businessLinkManageTableEntity.setBusinessDomain(updateParam.getBusinessDomain());
        }
        if (updateParam.getIsCore() != null) {
            businessLinkManageTableEntity.setIsCore(updateParam.getIsCore());
        }
        int i = businessLinkManageTableMapper.updateById(businessLinkManageTableEntity);
        int i1 = linkManageTableMapper.updateById(linkManageTableEntity);
        return i & i1;
    }

    @Override
    public void deleteActivity(Long activityId) {
        BusinessLinkManageTableEntity businessLinkManageTableEntity = businessLinkManageTableMapper.selectById(
            activityId);
        String relatedTechLink = businessLinkManageTableEntity.getRelatedTechLink();
        LinkManageTableEntity linkManageTableEntity = linkManageTableMapper.selectById(relatedTechLink);

        BusinessLinkManageTableEntity updateBizLink = new BusinessLinkManageTableEntity();
        updateBizLink.setLinkId(businessLinkManageTableEntity.getLinkId());
        updateBizLink.setIsDeleted(1);
        businessLinkManageTableMapper.updateById(updateBizLink);

        LinkManageTableEntity updateLink = new LinkManageTableEntity();
        updateLink.setIsDeleted(1);
        updateLink.setLinkId(linkManageTableEntity.getLinkId());
        linkManageTableMapper.updateById(linkManageTableEntity);
    }

    @Override
    public PagingList<ActivityListResult> pageActivities(ActivityQueryParam param) {
        Page<BusinessLinkManageTableEntity> page = new Page<>();
        page.setCurrent(param.getCurrent() + 1);
        page.setSize(param.getPageSize());
        page.setOrders(Lists.newArrayList(OrderItem.desc("CREATE_TIME")));

        LambdaQueryWrapper<BusinessLinkManageTableEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(param.getActivityName())) {
            lambdaQueryWrapper.like(BusinessLinkManageTableEntity::getLinkName, param.getActivityName());
        }
        if (StringUtils.isNotBlank(param.getDomain())) {
            lambdaQueryWrapper.eq(BusinessLinkManageTableEntity::getBusinessDomain, param.getDomain());
        }
        if (param.getIsChange() != null) {
            lambdaQueryWrapper.eq(BusinessLinkManageTableEntity::getIsChange, param.getIsChange());
        }
        if (CollectionUtils.isNotEmpty(param.getUserIdList())) {
            lambdaQueryWrapper.in(BusinessLinkManageTableEntity::getUserId, param.getUserIdList());
        }
        lambdaQueryWrapper.eq(BusinessLinkManageTableEntity::getIsDeleted, 0);
        Page<BusinessLinkManageTableEntity> tableEntityPage = businessLinkManageTableMapper
                .selectPage(page, lambdaQueryWrapper);
        if (tableEntityPage.getSize() <= 0) {
            return PagingList.empty();
        }
        List<String> techLinkIds = tableEntityPage.getRecords().stream().map(
                BusinessLinkManageTableEntity::getRelatedTechLink).collect(
                Collectors.toList());
        if (CollectionUtils.isEmpty(techLinkIds)) {
            return PagingList.empty();
        }
        List<LinkManageTableEntity> linkManageTableEntities = linkManageTableMapper.selectBatchIds(techLinkIds);
        Map<Long, LinkManageTableEntity> linkMap = linkManageTableEntities.stream()
            .collect(Collectors.toMap(LinkManageTableEntity::getLinkId, e -> e));

        List<ActivityListResult> collect = tableEntityPage.getRecords().stream()
            .map(entity -> {
                ActivityListResult result = new ActivityListResult();
                result.setActivityId(entity.getLinkId());
                result.setActivityName(entity.getLinkName());
                result.setIsChange(entity.getIsChange());
                result.setIsCore(entity.getIsCore());
                result.setIsDeleted(entity.getIsDeleted());
                result.setUserId(entity.getUserId());
                result.setCreateTime(entity.getCreateTime());
                result.setUpdateTime(entity.getUpdateTime());
                result.setBusinessDomain(entity.getBusinessDomain());
                result.setCanDelete(entity.getCanDelete());
                LinkManageTableEntity linkManageTableEntity = linkMap.get(Long.parseLong(entity.getRelatedTechLink()));
                if (linkManageTableEntity != null) {

                }
                result.setManagerId(entity.getUserId());
                result.setActivityLevel(entity.getLinkLevel());
                result.setIsCore(entity.getIsCore());
                result.setBusinessDomain(entity.getBusinessDomain());
                return result;
            }).collect(Collectors.toList());
        return PagingList.of(collect, tableEntityPage.getTotal());
    }

}
