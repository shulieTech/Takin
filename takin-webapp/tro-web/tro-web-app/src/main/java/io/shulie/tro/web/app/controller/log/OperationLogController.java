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

package io.shulie.tro.web.app.controller.log;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.request.log.OperationLogQueryRequest;
import io.shulie.tro.web.app.response.log.OperationLogResponse;
import io.shulie.tro.web.app.service.log.OperationLogService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fanxx
 * @Date: 2020/9/23 8:32 下午
 * @Description:
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "操作日志")
@Slf4j
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @GetMapping("operation/log/list")
    @ApiOperation("操作日志列表")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.OPERATIONLOG,
        needAuth = ActionTypeEnum.QUERY
    )
    public PagingList<OperationLogResponse> listLog(OperationLogQueryRequest request) {
        request.setCurrent(request.getCurrent() + 1);
        return operationLogService.list(request);
    }
}

