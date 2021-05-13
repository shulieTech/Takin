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

package io.shulie.tro.web.app.conf;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.alibaba.druid.support.http.StatViewServlet;

@WebServlet(urlPatterns = "/druid/*",
    initParams = {
        @WebInitParam(name = "allow", value = ""),// IP白名单 (没有配置或者为空，则允许所有访问)
        @WebInitParam(name = "deny", value = "192.168.0.0"),// IP黑名单 (存在共同时，deny优先于allow)
        //		@WebInitParam(name="loginUsername",value="admin"),// 用户名
        //		@WebInitParam(name="loginPassword",value="123456"),// 密码
        @WebInitParam(name = "resetEnable", value = "false"),// 禁用HTML页面上的"Reset All"功能
        @WebInitParam(name = "sessionStatMaxCount", value = "1000"), //session最大1000个
        @WebInitParam(name = "profileEnable", value = "true") //配置profileEnable能够监控单个url调用的sql列表。
    })
public class DruidStatViewServlet extends StatViewServlet {
    private static final long serialVersionUID = 1L;
}
