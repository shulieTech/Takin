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
package com.shulie.instrument.simulator.core.classloader;

import com.shulie.instrument.simulator.api.ModuleRuntimeException;
import com.shulie.instrument.simulator.api.ModuleSpec;

import java.util.List;

/**
 * ClassLoader Service
 */
public interface ClassLoaderService {
    /**
     * Class Loader Service init
     *
     * @throws ModuleRuntimeException 模块异常
     */
    void init() throws ModuleRuntimeException;

    /**
     * Class Loader Service dispose
     *
     * @throws ModuleRuntimeException 抛出模块异常
     */
    void dispose() throws ModuleRuntimeException;

    /**
     * Whether class is sun reflect related class
     *
     * @param className class name
     * @return 返回是否是sun.reflect包名类
     */
    boolean isSunReflectClass(String className);

    /**
     * 卸载某个模块
     *
     * @param moduleName        模块名称
     * @param moduleClassLoader 模块加载器工厂
     */
    void unload(String moduleName, ClassLoaderFactory moduleClassLoader);

    /**
     * 加载模块
     *
     * @param moduleSpec        描述描述
     * @param moduleClassLoader 模块类加载器工厂
     */
    void load(ModuleSpec moduleSpec, ClassLoaderFactory moduleClassLoader);

    /**
     * Whether class is in import-class
     *
     * @param pluginName plugin name
     * @param className  class name
     * @return 返回类是否是import列表中
     */
    boolean isClassInImport(String pluginName, String className);

    /**
     * Find classloaderFactory which export class for import class
     *
     * @param className class name
     * @return 根据类名查询导出的ClassLoaderFactory
     */
    ClassLoaderFactory findExportClassLoaderFactory(String className);

    /**
     * Whether resource is in import-resources
     *
     * @param moduleName   模块名称
     * @param resourceName 资源名称
     * @return 返回资源是否在import列表中
     */
    boolean isResourceInImport(String moduleName, String resourceName);

    /**
     * Find classloaders which export resource for import resource in priority orders for import-resources
     *
     * @param resourceName resource name
     * @return classloaderFactory list
     */
    List<ClassLoaderFactory> findExportResourceClassLoadersInOrder(String resourceName);

    /**
     * Get simulator classloader
     *
     * @return 返回仿真器类加载器
     */
    ClassLoader getSimulatorClassLoader();

    /**
     * Get system classloader
     *
     * @return 返回系统类加载器
     */
    ClassLoader getSystemClassLoader();

    /**
     * Get simulator Module ClassLoader Factory
     *
     * @param moduleName 模块名称
     * @return 返回模块对应的类加载器工厂
     */
    ClassLoaderFactory getModuleClassLoaderFactory(String moduleName);

}
