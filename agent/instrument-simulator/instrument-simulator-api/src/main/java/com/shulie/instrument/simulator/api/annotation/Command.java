/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shulie.instrument.simulator.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 模块命令注解
 * <p>
 * 有这个注解的方法只能接收的类型基本类型与包装类型
 * <ul>
 * <li>命令参数: {@code Map<String,String>}</li>
 * <li>命令参数: {@code Map<String,String[]>}</li>
 * <li>命令参数: {@code String}</li>
 * <li>文本输出: {@code PrintWriter}</li>
 * </ul>
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Command {

    /**
     * 命令名称
     *
     * @return 命令名称
     */
    String value();

    /**
     * 命令描述
     *
     * @return
     */
    String description() default "";
}
