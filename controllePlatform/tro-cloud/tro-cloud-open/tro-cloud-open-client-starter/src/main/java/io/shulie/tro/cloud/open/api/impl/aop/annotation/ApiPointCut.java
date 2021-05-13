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

package io.shulie.tro.cloud.open.api.impl.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.shulie.tro.cloud.common.exception.TroCloudExceptionEnum;

/**
 * api 调用, 切入点注解类
 *
 * @author liuchuan
 * @date 2021/4/25 10:43 上午
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiPointCut {

    /**
     * 切入点的名称
     *
     * @return 切入点的名称
     */
    String name();

    /**
     * 错误码
     * 由使用方提供
     *
     * @return 错误码
     */
    TroCloudExceptionEnum errorCode();

}
