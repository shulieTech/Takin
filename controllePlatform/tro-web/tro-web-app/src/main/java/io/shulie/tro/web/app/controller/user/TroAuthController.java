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

package io.shulie.tro.web.app.controller.user;

import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.auth.TroAuthService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fanxx
 * @Date: 2020/9/3 下午2:22
 * @Description: 权限管理
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "authmanage", value = "权限管理")
public class TroAuthController {

    @Autowired
    TroAuthService troAuthService;

    /**
     * 获取用户菜单资源
     */
    @GetMapping("auth/user/resource")
    public Response getUserMenu() {
        return Response.success(troAuthService.getUserMenu());
    }

    /**
     * 获取用户功能权限
     *
     * @return
     */
    @GetMapping("auth/resource/user/action")
    public Response getUserAction() {
        return Response.success(troAuthService.getUserAction());
    }

}
