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

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.request.application.ApplicationNodeDashBoardQueryRequest;
import io.shulie.tro.web.app.request.application.ApplicationNodeQueryRequest;
import io.shulie.tro.web.app.response.application.ApplicationNodeDashBoardResponse;
import io.shulie.tro.web.app.response.application.ApplicationNodeResponse;
import io.shulie.tro.web.app.service.application.ApplicationNodeService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: mubai
 * @Date: 2020-09-23 19:51
 * @Description:
 */

@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "ApplicationNodeController", value = "应用实例（节点）管理")
public class ApplicationNodeController {

    @Autowired
    private ApplicationNodeService applicationNodeService;

    @ApiOperation("根据应用id获取节点信息")
    @GetMapping("application/node/list")
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.APPLICATION_MANAGE,
            needAuth = ActionTypeEnum.QUERY
    )
    public PagingList<ApplicationNodeResponse> getNodesByAppId(ApplicationNodeQueryRequest request) {
        return applicationNodeService.pageNodes(request);
    }

    @ApiOperation("根据应用id获取节点汇总信息")
    @GetMapping("application/node/dashboard")
    public ApplicationNodeDashBoardResponse getNodeDashBoardByAppId(ApplicationNodeDashBoardQueryRequest request) {
        return applicationNodeService.getApplicationNodeAmount(request);
    }

    @ApiOperation("根据agentId获取节点信息")
    @GetMapping("application/node/info")
    public ApplicationNodeResponse getNodeByAgentId(@Validated @NotNull String agentId) {
        return applicationNodeService.getNodeByAgentId(agentId);
    }
}
