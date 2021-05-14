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

import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.request.application.ApplicationErrorQueryRequest;
import io.shulie.tro.web.app.response.application.ApplicationErrorResponse;
import io.shulie.tro.web.app.service.application.ApplicationErrorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 应用错误信息
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "ApplicationErrorController", value = "应用错误信息")
public class ApplicationErrorController {

    @Autowired
    private ApplicationErrorService applicationErrorService;

    @GetMapping("application/error/list")
    @ApiOperation("应用异常列表")
    public List<ApplicationErrorResponse> list(ApplicationErrorQueryRequest request) {
        return applicationErrorService.list(request);
    }
}
