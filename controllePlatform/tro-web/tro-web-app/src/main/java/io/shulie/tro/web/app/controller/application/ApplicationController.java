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

import java.util.List;
import java.util.stream.Collectors;

import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.constant.BizOpConstants.ModuleCode;
import io.shulie.tro.web.app.service.ApplicationService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.shulie.tro.web.common.vo.WebOptionEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shiyajian
 * create: 2020-12-29
 */
@RestController
@RequestMapping("/api/application" )
@Api(tags = "应用管理", value = "应用管理" )
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    /**
     * 获得所有应用的名字
     */
    @GetMapping("/names")
    @ApiOperation("获得所有的应用名称")
    @AuthVerification(
        moduleCode = ModuleCode.APPLICATION_MANAGE,
        needAuth = ActionTypeEnum.QUERY
    )
    public List<WebOptionEntity> getApplicationName() {
        return applicationService.getApplicationName().stream()
            .map(item -> {
                WebOptionEntity webOptionEntity = new WebOptionEntity();
                webOptionEntity.setLabel(item);
                webOptionEntity.setValue(item);
                return webOptionEntity;
            }).collect(Collectors.toList());
    }
}
