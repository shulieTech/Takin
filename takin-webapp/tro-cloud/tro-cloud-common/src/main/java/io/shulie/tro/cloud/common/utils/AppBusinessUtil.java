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

package io.shulie.tro.cloud.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 项目工具类 配合 springboot
 *
 * @author liuchuan
 * @date 2021/4/5 5:10 下午
 */
@Component
public class AppBusinessUtil implements ApplicationContextAware {

    @Value("${spring.profiles.active}")
    private String profiles;

    /**
     * springboot 上下文
     */
    public static ApplicationContext ac;

    /**
     * 启动环境
     */
    public static String env;

    /**
     * 是否是 local 环境
     *
     * @return 是否是
     */
    public static boolean isLocal() {
        return "local".equals(env);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
        env = profiles;
    }

}
