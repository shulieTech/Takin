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

import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.controller.openapi.response.user.LoginRecordTotalResp;
import io.shulie.tro.web.app.input.user.LoginRecordSearchInput;
import io.shulie.tro.web.app.output.user.LoginRecordTotalOutput;
import io.shulie.tro.web.app.service.user.LoginRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.controller.openapi
 * @date 2021/4/8 11:06 上午
 */
@RestController
@RequestMapping(APIUrls.TRO_OPEN_API_URL + "loginrecord")
@Api(tags = "loginRecordTotal", value = "登录记录")
public class LoginRecordOpenApi {
    @Autowired
    private LoginRecordService loginRecordService;

    /**
     * 统计
     * @param startTime
     * @param endTime
     * @param userName
     * @param ip
     * @return
     */
    @GetMapping("/total")
    @ApiOperation("统计登录记录")
    public LoginRecordTotalResp getApplications(
        @RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
        @RequestParam("userName") String userName, @RequestParam("ip") String ip
    ) {
        LoginRecordSearchInput input = new  LoginRecordSearchInput();
        input.setStartTime(startTime);
        input.setEndTime(endTime);
        input.setUserName(userName);
        input.setIp(ip);
        LoginRecordTotalOutput output =  loginRecordService.getTotal(input);
        LoginRecordTotalResp resp = new LoginRecordTotalResp();
        BeanUtils.copyProperties(output,resp);
        return resp;
    }
}
