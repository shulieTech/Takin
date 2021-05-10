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
package com.shulie.instrument.simulator.core.manager;

import com.shulie.instrument.simulator.api.ModuleException;
import com.shulie.instrument.simulator.core.CoreModule;
import com.shulie.instrument.simulator.core.classloader.ClassLoaderService;

import java.io.File;
import java.util.Collection;

/**
 * 模块管理
 */
public interface CoreModuleManager {
    /**
     * 启动
     */
    void onStartup();

    /**
     * 刷新仿真器模块
     *
     * @param isForce 是否强制刷新
     * @throws ModuleException 模块加载失败
     */
    void flush(boolean isForce) throws ModuleException;

    /**
     * 仿真器重置
     *
     * @return this
     * @throws ModuleException 仿真器重置失败
     */
    CoreModuleManager reset() throws ModuleException;

    /**
     * 加载指定模块文件
     *
     * @param file
     * @return
     * @throws ModuleException
     */
    CoreModuleManager load(File file) throws ModuleException;

    /**
     * 激活模块
     *
     * @param coreModule 模块业务对象
     * @throws ModuleException 激活模块失败
     */
    void active(CoreModule coreModule) throws ModuleException;

    /**
     * 冻结模块
     * 模块冻结时候将会失去所有事件的监听
     *
     * @param coreModule              模块业务对象
     * @param isIgnoreModuleException 是否忽略模块异常
     *                                强制冻结模块将会主动忽略冻结失败情况，强行将模块所有的事件监听行为关闭
     * @throws ModuleException 冻结模块失败
     */
    void frozen(CoreModule coreModule, boolean isIgnoreModuleException) throws ModuleException;

    /**
     * 列出所有的模块
     *
     * @return 模块集合
     */
    Collection<CoreModule> list();

    /**
     * 获取模块
     *
     * @param uniqueId 模块ID
     * @return 模块
     */
    CoreModule get(String uniqueId);

    /**
     * 获取模块
     *
     * @param uniqueId 模块ID
     * @return 模块
     * @throws ModuleException 当模块不存在时抛出模块不存在异常
     */
    CoreModule getThrowsExceptionIfNull(String uniqueId) throws ModuleException;

    /**
     * 卸载模块
     *
     * @param coreModule              模块
     * @param isIgnoreModuleException 是否忽略模块异常
     * @return 返回被卸载的模块
     * @throws ModuleException 卸载模块失败
     */
    CoreModule unload(CoreModule coreModule, boolean isIgnoreModuleException) throws ModuleException;

    /**
     * 对目标对象进行资源注入
     *
     * @param target     目标对象
     * @param coreModule CoreModule
     * @throws ModuleException 当对象无访问权限时则抛出此异常
     */
    void injectResource(Object target, CoreModule coreModule) throws ModuleException;

    /**
     * 卸载所有模块
     */
    void unloadAll();

    /**
     * 关闭
     */
    void onShutdown();

    /**
     * 获取 ClassLoader 服务
     * ClassLoaderService 管理所有的模块导出的类引用的问题
     *
     * @return ClassLoader服务
     */
    ClassLoaderService getClassLoaderService();
}
