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

package io.shulie.tro.web.app.controller.openapi;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import io.shulie.tro.web.amdb.bean.common.EntranceTypeEnum;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.controller.openapi.request.activity.ActivityCreateApiRequest;
import io.shulie.tro.web.app.controller.openapi.response.activity.ActivityCreateApiResponse;
import io.shulie.tro.web.common.constant.FeaturesConstants;
import io.shulie.tro.web.data.mapper.mysql.BusinessLinkManageTableMapper;
import io.shulie.tro.web.data.mapper.mysql.LinkManageTableMapper;
import io.shulie.tro.web.data.model.mysql.BusinessLinkManageTableEntity;
import io.shulie.tro.web.data.model.mysql.LinkManageTableEntity;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author shiyajian
 * create: 2021-01-20
 */
@RestController
@RequestMapping(APIUrls.TRO_OPEN_API_URL + "/activities")
public class ActivityOpenApi {

    @Resource
    private LinkManageTableMapper linkManageTableMapper;

    @Resource
    private BusinessLinkManageTableMapper businessLinkManageTableMapper;

    public static final Long CUSTOMER_ID = 1L;

    @PostMapping("/create")
    @Transactional(rollbackFor = Throwable.class)
    public Response<ActivityCreateApiResponse> createActivity(@Valid @RequestBody ActivityCreateApiRequest request) {

        String entranceName1 = request.getEntranceName();
        String[] fixedPrefix = {"https://", "http://"};
        for (String prefix : fixedPrefix) {
            if (entranceName1.startsWith(prefix)) {
                entranceName1 = entranceName1.replace(prefix, "");
            }
        }
        request.setEntranceName(entranceName1);

        ActivityCreateApiResponse activityCreateApiResponse = new ActivityCreateApiResponse();
        LambdaQueryWrapper<BusinessLinkManageTableEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String activityName = request.getActivityName();
        String entranceName = buildEntrance(request.getApplicationName(), request.getEntranceMethod(), request.getEntranceName());
        lambdaQueryWrapper.eq(BusinessLinkManageTableEntity::getEntrace, entranceName);
        List<BusinessLinkManageTableEntity> businessLinkManageTableEntities = businessLinkManageTableMapper.selectList(
                lambdaQueryWrapper);
        if (CollectionUtils.isNotEmpty(businessLinkManageTableEntities)) {
            activityCreateApiResponse.setActivityId(businessLinkManageTableEntities.get(0).getLinkId());
            activityCreateApiResponse.setActivityName(businessLinkManageTableEntities.get(0).getLinkName());
            return Response.success(activityCreateApiResponse);
        }

        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(BusinessLinkManageTableEntity::getLinkName, activityName);
        businessLinkManageTableEntities = businessLinkManageTableMapper.selectList(
                lambdaQueryWrapper);
        if (CollectionUtils.isNotEmpty(businessLinkManageTableEntities)) {
            activityName = activityName + System.currentTimeMillis();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("serviceName", request.getApplicationName());
        jsonObject.put("applicationName", request.getApplicationName());
        LinkManageTableEntity linkManageTableEntity = new LinkManageTableEntity();
        linkManageTableEntity.setLinkName(activityName);
        linkManageTableEntity.setEntrace(entranceName);
        linkManageTableEntity.setChangeBefore(jsonObject.toJSONString());
        linkManageTableEntity.setChangeAfter(null);
        linkManageTableEntity.setChangeRemark(null);
        linkManageTableEntity.setIsChange(0);
        linkManageTableEntity.setIsJob(0);
        linkManageTableEntity.setCustomerId(CUSTOMER_ID);
        linkManageTableEntity.setUserId(request.getUserId());
        linkManageTableEntity.setIsDeleted(0);
        linkManageTableEntity.setApplicationName(request.getApplicationName());
        linkManageTableEntity.setChangeType(0);
        linkManageTableEntity.setCanDelete(0);
        linkManageTableEntity.setFeatures(buildFeature(request.getEntranceMethod(), request.getEntranceName()));
        linkManageTableMapper.insert(linkManageTableEntity);

        BusinessLinkManageTableEntity businessLinkManageTableEntity = new BusinessLinkManageTableEntity();
        businessLinkManageTableEntity.setLinkName(activityName);
        businessLinkManageTableEntity.setEntrace(entranceName);
        businessLinkManageTableEntity.setRelatedTechLink(String.valueOf(linkManageTableEntity.getLinkId()));
        businessLinkManageTableEntity.setLinkLevel(request.getActivityLevel());
        businessLinkManageTableEntity.setIsChange(0);
        businessLinkManageTableEntity.setIsCore(request.getIsCore());
        businessLinkManageTableEntity.setIsDeleted(0);
        businessLinkManageTableEntity.setCustomerId(CUSTOMER_ID);
        businessLinkManageTableEntity.setUserId(request.getUserId());
        businessLinkManageTableEntity.setBusinessDomain(request.getBusinessDomain());
        businessLinkManageTableEntity.setCanDelete(0);
        businessLinkManageTableMapper.insert(businessLinkManageTableEntity);
        //新增完业务活动后，把系统流程状态改为不可删除
        if (null != businessLinkManageTableEntity.getLinkId() && null != linkManageTableEntity.getLinkId()) {
            LinkManageTableEntity linkManageUpdateEntity = new LinkManageTableEntity();
            linkManageUpdateEntity.setLinkId(linkManageTableEntity.getLinkId());
            linkManageUpdateEntity.setCanDelete(1);
            linkManageTableMapper.updateById(linkManageUpdateEntity);
        }

        activityCreateApiResponse.setActivityId(businessLinkManageTableEntity.getLinkId());
        activityCreateApiResponse.setActivityName(businessLinkManageTableEntity.getLinkName());
        return Response.success(activityCreateApiResponse);
    }

    private String buildEntrance(String applicationName, String entranceMethod, String entranceName) {
        return applicationName + "|" + entranceMethod.toUpperCase() + "|" + entranceName + "|0";
    }

    private String buildFeature(String entranceMethod, String entranceName) {
        Map<String, String> features = Maps.newHashMap();
        features.put(FeaturesConstants.SERVER_MIDDLEWARE_TYPE_KEY, EntranceTypeEnum.HTTP.getType());
        features.put(FeaturesConstants.EXTEND_KEY, "");
        features.put(FeaturesConstants.RPC_TYPE_KEY, "0");
        features.put(FeaturesConstants.METHOD_KEY, entranceMethod);
        features.put(FeaturesConstants.SERVICE_NAME_KEY, entranceName);
        return JSON.toJSONString(features);
    }
}
