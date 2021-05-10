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
package com.shulie.instrument.simulator.agent.module;

import java.util.List;
import java.util.Set;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 7:48 下午
 */
public interface JavaModule {
    /**
     * 是否支持
     *
     * @return true|false
     */
    boolean isSupported();

    /**
     * 模块是否命名
     *
     * @return true|false
     */
    boolean isNamed();

    /**
     * 返回模块名称
     *
     * @return 模块名称
     */
    String getName();

    /**
     * 返回模块提供者列表
     *
     * @return 模块提供者列表
     */
    List<Providers> getProviders();

    /**
     * 对目标模块添加读权限
     *
     * @param target 目标模块
     */
    void addReads(JavaModule target);

    /**
     * 对目标模块导出包名
     *
     * @param packageName 包名
     * @param target      目标模块
     */
    void addExports(String packageName, JavaModule target);

    /**
     * 对目标模块开放包权限
     *
     * @param packageName 包名
     * @param target      目标模块
     */
    void addOpens(String packageName, JavaModule target);

    /**
     * 添加使用
     *
     * @param target 目标类
     */
    void addUses(Class<?> target);

    /**
     * 添加提供者
     *
     * @param service
     * @param providerList
     */
    void addProvides(Class<?> service, List<Class<?>> providerList);

    /**
     * 判断是否对目标模块导出包
     *
     * @param packageName      包名
     * @param targetJavaModule 目标模块
     * @return true|false
     */
    boolean isExported(String packageName, JavaModule targetJavaModule);

    /**
     * 判断是否对目标模块开放包
     *
     * @param packageName      包名
     * @param targetJavaModule 目标模块
     * @return true|false
     */
    boolean isOpen(String packageName, JavaModule targetJavaModule);

    /**
     * 目标模块是否可读
     *
     * @param targetJavaModule 目标模块
     * @return true|false
     */
    boolean canRead(JavaModule targetJavaModule);

    /**
     * 目标类是否可读
     *
     * @param targetClazz 目标类
     * @return true|false
     */
    boolean canRead(Class<?> targetClazz);

    /**
     * 获取模块类加载器
     *
     * @return 模块类加载器
     */
    ClassLoader getClassLoader();

    /**
     * 获取模块所有包名
     *
     * @return 包列表
     */
    Set<String> getPackages();
}

