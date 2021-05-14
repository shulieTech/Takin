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

package io.shulie.tro.web.app.controller.activity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.shulie.tro.common.beans.annotation.ModuleDef;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.amdb.bean.common.EntranceTypeEnum;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.constant.BizOpConstants.Vars;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.request.activity.ActivityCreateRequest;
import io.shulie.tro.web.app.request.activity.ActivityQueryRequest;
import io.shulie.tro.web.app.request.activity.ActivityUpdateRequest;
import io.shulie.tro.web.app.request.activity.ActivityVerifyRequest;
import io.shulie.tro.web.app.response.activity.ActivityListResponse;
import io.shulie.tro.web.app.response.activity.ActivityResponse;
import io.shulie.tro.web.app.response.activity.ActivityVerifyResponse;
import io.shulie.tro.web.app.service.ActivityService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.shulie.tro.web.common.util.business.EntranceNameUtils;
import io.shulie.tro.web.common.util.business.EntranceNameUtils.EntranceNameVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务活动
 *
 * @author shiyajian
 * create: 2020-12-29
 */
@RequestMapping("/api/activities")
@Api(tags = "业务活动管理", value = "业务活动管理")
@RestController
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @ApiOperation("添加业务活动")
    @PostMapping("/create")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.LINK_CARDING,
        subModuleName = BizOpConstants.SubModules.BUSINESS_ACTIVITY,
        logMsgKey = BizOpConstants.Message.MESSAGE_BUSINESS_ACTIVITY_CREATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.BUSINESS_ACTIVITY,
        needAuth = ActionTypeEnum.CREATE
    )
    public void createActivity(@Validated @RequestBody ActivityCreateRequest request) {
        EntranceNameUtils.checkEntranceName(request.getEntranceName());
        EntranceNameVO entranceNameVO = EntranceNameUtils.getEntranceNameVO(request.getEntranceName(),request.getType().getType());
        request.setServiceName(entranceNameVO.getServiceName());
        request.setMethod(entranceNameVO.getMethod());
        request.setRpcType(EntranceNameUtils.getRpcType(request.getType().getType()));
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.CREATE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.BUSINESS_ACTIVITY, request.getActivityName());
        OperationLogContextHolder.addVars(Vars.ENTRANCE_TYPE,request.getType().name());
        OperationLogContextHolder.addVars(Vars.APPLICATION_NAME,request.getApplicationName());
        OperationLogContextHolder.addVars(Vars.SERVICE_NAME,request.getServiceName());
        activityService.createActivity(request);
    }


    private String getRpcType(EntranceTypeEnum type) {
        // 判断逻辑
        return "1";
    }
    @PutMapping("/update")
    @ApiOperation("修改业务活动")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.LINK_CARDING,
        subModuleName = BizOpConstants.SubModules.BUSINESS_ACTIVITY,
        logMsgKey = BizOpConstants.Message.MESSAGE_BUSINESS_ACTIVITY_UPDATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.BUSINESS_ACTIVITY,
        needAuth = ActionTypeEnum.UPDATE
    )
    public void updateActivity(@Validated @RequestBody ActivityUpdateRequest request) {
        EntranceNameUtils.checkEntranceName(request.getEntranceName());
        EntranceNameVO entranceNameVO = EntranceNameUtils.getEntranceNameVO(request.getEntranceName(),request.getType().getType());
        request.setServiceName(entranceNameVO.getServiceName());
        request.setMethod(entranceNameVO.getMethod());
        request.setRpcType(EntranceNameUtils.getRpcType(request.getType().getType()));
        OperationLogContextHolder.operationType(BizOpConstants.OpTypes.UPDATE);
        OperationLogContextHolder.addVars(BizOpConstants.Vars.BUSINESS_ACTIVITY, request.getActivityName());
        OperationLogContextHolder.addVars(Vars.ENTRANCE_TYPE,request.getType().name());
        OperationLogContextHolder.addVars(Vars.APPLICATION_NAME,request.getApplicationName());
        OperationLogContextHolder.addVars(Vars.SERVICE_NAME,request.getServiceName());
        activityService.updateActivity(request);
    }


    @ApiOperation("删除业务活动")
    @DeleteMapping(value = "/delete")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.LINK_CARDING,
        subModuleName = BizOpConstants.SubModules.BUSINESS_ACTIVITY,
        logMsgKey = BizOpConstants.Message.MESSAGE_BUSINESS_ACTIVITY_DELETE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.BUSINESS_ACTIVITY,
        needAuth = ActionTypeEnum.DELETE
    )
    public void deleteActivity(@Valid @NotNull @RequestParam Long activityId){
        activityService.deleteActivity(activityId);
    }

    @ApiOperation("业务活动列表")
    @GetMapping
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.BUSINESS_ACTIVITY,
        needAuth = ActionTypeEnum.QUERY
    )
    public PagingList<ActivityListResponse> pageActivities(@Valid ActivityQueryRequest request) {
        return activityService.pageActivities(request);
    }

    @ApiOperation("获得单个业务活动的详情")
    @GetMapping("/activity")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.BUSINESS_ACTIVITY,
        needAuth = ActionTypeEnum.QUERY
    )
    public ActivityResponse getActivityById(@Valid @RequestParam Long id) {
        return activityService.getActivityById(id);
    }

}
