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

package io.shulie.tro.cloud.web.entrypoint.controller.user;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pamirs.tro.entity.domain.vo.user.UserLoginParam;
import io.shulie.tro.cloud.biz.service.user.TroUserService;
import io.shulie.tro.cloud.common.constants.APIUrls;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fanxx
 * @Date: 2020/3/24 下午8:39
 * @Description:
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "loginmanage", value = "登录管理")
public class LoginController {

    @Resource
    private TroUserService troUserService;

    /**
     * 用户登录
     */
    @PostMapping(value = "login")
    public ResponseResult login(@RequestBody UserLoginParam userLoginParam,
        HttpServletRequest request, HttpServletResponse response) {
        return troUserService.login(userLoginParam, request, response);
    }

    /**
     * 用户登出
     */
    @GetMapping(value = "logout")
    public ResponseResult login(HttpServletRequest request) {
        return troUserService.logout(request);
    }
}
