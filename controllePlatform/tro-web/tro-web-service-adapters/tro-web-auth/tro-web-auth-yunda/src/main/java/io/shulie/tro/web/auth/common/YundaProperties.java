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

package io.shulie.tro.web.auth.common;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author: fanxx
 * @Date: 2020/12/25 3:45 下午
 * @Description:
 */
@Component
public class YundaProperties {
    /**
     * 韵达内部链接
     */
    @Value("${yunda.inner.url}")
    String YUNDA_INNER_URL;

    /**
     * 韵达重定向回调地址
     */
    @Value("${yunda.redirect.url}")
    String YUNDA_REDIRECT_URL;

    /**
     * 韵达统一授权url
     */
    @Value("${yunda.url}")
    String YUNDA_URL;

    /**
     * 韵达退出url
     */
    @Value("${yunda.logout.url}")
    String YUNDA_LOGOUT_URL;
    /**
     * 韵达统一授权 应用注册码（韵达使用）
     */
    @Value("${yunda.authId}")
    String authid;

    /**
     * 韵达获取组织APPID
     */
    @Value("${yunda.dept.app.id}")
    String YUNDA_DEPT_APP_ID;

    /**
     * 韵达获取组织秘钥
     */
    @Value("${yunda.dept.secret.key}")
    String YUNDA_DEPT_SECRET_KEY;

    /**
     * 韵达获取组织链接
     */
    @Value("${yunda.dept.url}")
    String YUNDA_DEPT_URL;

    /**
     * 韵达科技板块组织id
     */
    @Value("${yunda.dept.science.org.id}")
    String YUNDA_DEPT_SCIENCE_ORG_ID;

    @PostConstruct
    public void init() {
        YundaBaseInfo.YUNDA_INNER_URL = this.YUNDA_INNER_URL;
        YundaBaseInfo.YUNDA_REDIRECT_URL = this.YUNDA_REDIRECT_URL;
        YundaBaseInfo.YUNDA_URL = this.YUNDA_URL;
        YundaBaseInfo.YUNDA_LOGOUT_URL = this.YUNDA_LOGOUT_URL;
        YundaBaseInfo.authId = this.authid;
        YundaBaseInfo.YUNDA_DEPT_APP_ID = this.YUNDA_DEPT_APP_ID;
        YundaBaseInfo.YUNDA_DEPT_SECRET_KEY = this.YUNDA_DEPT_SECRET_KEY;
        YundaBaseInfo.YUNDA_DEPT_URL = this.YUNDA_DEPT_URL;
        YundaBaseInfo.YUNDA_DEPT_SCIENCE_ORG_ID = this.YUNDA_DEPT_SCIENCE_ORG_ID;
    }
}
