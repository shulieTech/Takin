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

package io.shulie.tro.cloud.app.init;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shiyajian
 * create: 2020-10-10
 */
@Component
public class Initializer implements InitializingBean {

    @Autowired
    private CloudSdkLoader cloudSdkLoader;

    /**
     * 所有项目启动需要做的事情都统一注册在这里
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        // 初始化加载云平台的sdk进来
        new Thread(() -> cloudSdkLoader.loadSDK()).start();
    }
}
