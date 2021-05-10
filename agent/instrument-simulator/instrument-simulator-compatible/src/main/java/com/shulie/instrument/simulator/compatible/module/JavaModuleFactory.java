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
package com.shulie.instrument.simulator.compatible.module;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 8:11 下午
 */
public interface JavaModuleFactory {
    /**
     * 从目标类包装 JavaModule
     *
     * @param clazz 目标类
     * @return JavaModule
     */
    JavaModule wrapFromClass(Class<?> clazz);

    /**
     * 从模块包装 JavaModule
     *
     * @param module 模块
     * @return JavaModule
     */
    JavaModule wrapFromModule(Object module);

    /**
     * 是否是命名的模块
     *
     * @param module 模块
     * @return true|false
     */
    boolean isNamedModule(Object module);

    /**
     * 从 ClassLoader 中获取未命名的模块
     *
     * @param classLoader 类加载器
     * @return 模块
     */
    Object getUnnamedModule(ClassLoader classLoader);

    /**
     * 获取类对应的模块
     *
     * @param clazz 目标类
     * @return 模块
     */
    Object getModule(Class<?> clazz);
}
