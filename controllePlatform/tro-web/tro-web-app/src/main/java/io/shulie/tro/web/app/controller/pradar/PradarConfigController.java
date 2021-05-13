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

package io.shulie.tro.web.app.controller.pradar;

import java.util.List;

import io.shulie.tro.common.beans.annotation.ModuleDef;
import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.constant.BizOpConstants.OpTypes;
import io.shulie.tro.web.app.context.OperationLogContext;
import io.shulie.tro.web.app.context.OperationLogContextHolder;
import io.shulie.tro.web.app.request.pradar.PradarZKConfigUpdateRequest;
import io.shulie.tro.web.app.response.pradar.PradarZKConfigResponse;
import io.shulie.tro.web.app.service.pradar.PradarConfigService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * pradar ZK开关、配置
 *
 * @author cyf
 * create: 2020-02-02
 */
@RequestMapping("/api/pradar/switch")
@Api(tags = "PradarSwitch", value = "pradar开关、配置")
@RestController
public class PradarConfigController {

    @Autowired
    PradarConfigService pradarConfigService;

    @ApiOperation("获取配置列表")
    @GetMapping("/list")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.PRADAR_CONFIG,
        needAuth = ActionTypeEnum.QUERY
    )
    public PagingList<PradarZKConfigResponse> pageActivities() {
        List<PradarZKConfigResponse> configList = pradarConfigService.getConfigList();
        return PagingList.of(configList, configList.size());
    }

    @PostMapping("/update")
    @ApiOperation("PRADAR配置修改")
    @ModuleDef(
        moduleName = BizOpConstants.Modules.PRADAR,
        subModuleName = BizOpConstants.SubModules.PRADAR_CONFIG,
        logMsgKey = BizOpConstants.Message.PRADAR_CONFIG_UPDATE
    )
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.PRADAR_CONFIG,
        needAuth = ActionTypeEnum.UPDATE
    )
    public void updateActivity(@Validated @RequestBody PradarZKConfigUpdateRequest request) {
        OperationLogContextHolder.operationType(OpTypes.UPDATE);
        pradarConfigService.updateConfig(request.getId(), request.getValue());
    }
}
