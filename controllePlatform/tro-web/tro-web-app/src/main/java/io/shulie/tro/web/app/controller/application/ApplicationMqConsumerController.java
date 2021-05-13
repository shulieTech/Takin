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

package io.shulie.tro.web.app.controller.application;

import javax.validation.constraints.NotNull;

import io.shulie.tro.common.beans.annotation.ModuleDef;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.constant.BizOpConstants.Message;
import io.shulie.tro.web.app.request.application.ShadowConsumerCreateRequest;
import io.shulie.tro.web.app.request.application.ShadowConsumerDeleteRequest;
import io.shulie.tro.web.app.request.application.ShadowConsumerQueryRequest;
import io.shulie.tro.web.app.request.application.ShadowConsumerUpdateRequest;
import io.shulie.tro.web.app.request.application.ShadowConsumersOperateRequest;
import io.shulie.tro.web.app.response.application.ShadowConsumerResponse;
import io.shulie.tro.web.app.service.ShadowConsumerService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
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
 * @author shiyajian
 * create: 2021-02-04
 */
@RestController
@RequestMapping("/api/consumers")
@Api(tags = "ApplicationMqConsumerController", value = "影子消费者")
public class ApplicationMqConsumerController {

    @Autowired
    private ShadowConsumerService shadowConsumerService;

    @GetMapping("/get")
    @ApiOperation("查看影子消费者")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.QUERY
    )
    public ShadowConsumerResponse getMqConsumerById(@Validated @NotNull @RequestParam("id")Long id){
        return shadowConsumerService.getMqConsumerById(id);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询影子消费者")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.QUERY
    )
    public PagingList<ShadowConsumerResponse> pageMqConsumers(@Validated ShadowConsumerQueryRequest request){
        return shadowConsumerService.pageMqConsumers(request);
    }

    @PostMapping("/create")
    @ApiOperation("创建影子消费者")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.SHADOW_CONSUMER,
        logMsgKey = Message.MESSAGE_SHADOW_CONSUMER_CREATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.CREATE
    )
    public void createMqConsumers(@Validated @RequestBody ShadowConsumerCreateRequest request){
        shadowConsumerService.createMqConsumers(request);
    }

    @PutMapping("/update")
    @ApiOperation("修改影子消费者")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.SHADOW_CONSUMER,
        logMsgKey = Message.MESSAGE_SHADOW_CONSUMER_UPDATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.UPDATE
    )
    public void updateMqConsumers(@Validated @RequestBody ShadowConsumerUpdateRequest request){
        shadowConsumerService.updateMqConsumers(request);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除影子消费者")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.SHADOW_CONSUMER,
        logMsgKey = Message.MESSAGE_SHADOW_CONSUMER_DELETE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.DELETE
    )
    public void deleteMqConsumers(@Validated @RequestBody ShadowConsumerDeleteRequest request){
        shadowConsumerService.deleteMqConsumers(request.getIds());
    }

    @PostMapping("/operate")
    @ApiOperation("（加入/取消）影子消费者")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.APPLICATION_MANAGE,
        subModuleName = BizOpConstants.SubModules.SHADOW_CONSUMER,
        logMsgKey = Message.MESSAGE_SHADOW_CONSUMER_ADD_REMOVE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.ENABLE_DISABLE
    )
    public void operateMqConsumers(@Validated @RequestBody ShadowConsumersOperateRequest request) {
        shadowConsumerService.operateMqConsumers(request);
    }

}
