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

import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.controller.openapi.response.application.ApplicationListResponse;
import io.shulie.tro.web.app.service.ApplicationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.controller.openapi
 * @date 2021/3/25 10:54 上午
 */
@RestController
@RequestMapping(APIUrls.TRO_OPEN_API_URL + "/application")
public class ApplicationOpenApi {

    @Autowired
    private ApplicationService applicationService;
    @GetMapping("/center/list")
    @ApiOperation("应用列表查询接口")
    public List<ApplicationListResponse> getApplications(@ApiParam(name = "appNames", value = "应用名称,支持批量，逗号隔开")
        String appNames) {
        return applicationService.getApplicationList(appNames);
    }
}
