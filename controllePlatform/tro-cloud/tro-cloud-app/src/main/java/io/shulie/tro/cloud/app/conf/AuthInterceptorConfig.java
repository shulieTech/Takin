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

package io.shulie.tro.cloud.app.conf;

import io.shulie.tro.cloud.app.intercepter.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author shulie
 * @Date: 2019/9/20 15:14
 * @Description:
 */
@Configuration
public class AuthInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        /**
         * 登陆拦截器
         */
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**")
            .excludePathPatterns("/login/**")
            .excludePathPatterns("/**/api/login/**")
            .excludePathPatterns("/**/api/logout/**")
            .excludePathPatterns("/**/doc.html/**")
            .excludePathPatterns("/**/swagger-ui.html/**")
            .excludePathPatterns("/**/webjars/**")
            .excludePathPatterns("/**/swagger-resources/**")
            .excludePathPatterns("/**/api-docs/**")
            .excludePathPatterns("/", "/css/**", "/js/**", "/img/**")
            .excludePathPatterns("/**/api/confcenter/wbmnt/query/bwlist")
            .excludePathPatterns("/**/api/confcenter/interface/query/needUpload")
            .excludePathPatterns("/**/api/confcenter/applicationmnt/update/applicationAgent")
            .excludePathPatterns("/**/api/confcenter/shadowTableConfig/queryAppShadowTableConfig")
            .excludePathPatterns("/**/api/preventcheat/applicationConfig/queryConfig")
            .excludePathPatterns("/**/api/shadow/job/queryByAppName")
            .excludePathPatterns("/**/api/link/ds/agent/report")
            .excludePathPatterns("/**/api/link/ds/configs/pull")
            .excludePathPatterns("/**/agent/**")
            .excludePathPatterns("/**/api/application/agent/access/status")
            .excludePathPatterns("/**/api/isolation/query/rockemtMqIsoQuery")
            .excludePathPatterns("/**/api/application/center/app/info")
            .excludePathPatterns("/**/api/application/center/app/switch/agent")
            .excludePathPatterns("/**/api/application/pradar/switch/status")
            .excludePathPatterns("/**/api/link/guard/guardmanage")
            .excludePathPatterns("/**/api/collector/**")
            .excludePathPatterns("/**/api/scenemanage/taskResultNotify")
            .excludePathPatterns("/**/api/scene/task/taskResultNotify")
            .excludePathPatterns("/**/api/engine/callback")
            .excludePathPatterns("/**/api/file/download")
            // 下载需要鉴权
            //.excludePathPatterns("/**/api/file/downloadFileByPath")
            .excludePathPatterns("/**/api/bigfile/upload")
            .excludePathPatterns("/**/api/bigfile/download")
            .excludePathPatterns("/**/api/license/**")
            .excludePathPatterns("/**/api/scene/task/initCallback")
            .excludePathPatterns("/**/api/noauth/**")
        ;

    }
}
