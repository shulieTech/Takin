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
package com.shulie.instrument.simulator.api.resource;

import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleException;
import com.shulie.instrument.simulator.api.ModuleSpec;

import java.io.File;
import java.util.Collection;
import java.util.Set;

/**
 * 模块管理器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface ModuleManager {

    /**
     * 刷新仿真器模块
     * <p>
     * 刷新仿真器模块将会对当前已经加载的用户模块进行卸载并再次重新加载,
     * 通常用于模块JAR文件更新之后对模块进行再次的重新加载
     * </p>
     *
     * @param isForce 是否强制刷新
     *                <p>强制刷新将会强制卸载所有已经加载的用户模块，并重新加载当前用户模块目录下所有的模块</p>
     *                <p>
     *                普通刷新将会寻找变动的用户模块文件，对已经加载但模块文件已经变动（删除or变更）的模块进行卸载。
     *                并重新加载当前用户模块目录中新增的模块
     *                </p>
     * @throws ModuleException 刷新模块失败
     */
    void flush(boolean isForce) throws ModuleException;

    /**
     * 仿真器模块重置
     * 仿真器模块重置时会强制冻结和强制卸载当前所有模块，并对系统模块和用户模块进行重新加载;
     * 加载过程中如果发生模块加载失败，则主动忽略掉加载失败的模块
     *
     * @throws ModuleException 模块重启失败
     */
    void reset() throws ModuleException;

    /**
     * 卸载指定模块扩展
     *
     * @param uniqueId 模块ID
     * @throws ModuleException 卸载模块失败
     */
    void unload(String uniqueId) throws ModuleException;

    /**
     * 加载指定的模块
     *
     * @param file
     * @throws ModuleException
     */
    void load(File file) throws ModuleException;

    /**
     * 激活模块扩展
     * 激活的模块能正常的感知Event
     *
     * @param uniqueId 模块ID
     * @throws ModuleException 激活模块失败
     */
    void active(String uniqueId) throws ModuleException;

    /**
     * 冻结模块扩展
     * 冻结的模块将不会感知到Event,但代码的插桩还在
     *
     * @param uniqueId 模块ID
     * @throws ModuleException 冻结模块失败
     */
    void frozen(String uniqueId) throws ModuleException;

    /**
     * 列出所有的模块扩展
     *
     * @return 模块集合
     */
    Collection<ExtensionModule> list();

    /**
     * 列出所有的模块描述
     *
     * @return
     */
    Collection<ModuleSpec> listModuleSpecs();

    /**
     * 根据模块 ID 获取模块描述
     *
     * @param moduleId
     * @return
     */
    ModuleSpec getModuleSpec(String moduleId);

    /**
     * 获取模块
     *
     * @param uniqueId 模块ID
     * @return 模块
     */
    ExtensionModule get(String uniqueId);

    /**
     * 获取模块当前渲染的类个数
     *
     * @param uniqueId 模块ID
     * @return 模块当前渲染的类个数
     * @throws ModuleException 模块不存在
     */
    int getEffectClassCount(String uniqueId) throws ModuleException;

    /**
     * 获取模块当前渲染的方法个数
     *
     * @param uniqueId 模块ID
     * @return 模块当前渲染的方法个数
     * @throws ModuleException 模块不存在
     */
    int getEffectMethodCount(String uniqueId) throws ModuleException;

    /**
     * 获取模块激活状态，判断当前模块是否已经被激活
     *
     * @param uniqueId 模块ID
     * @return true:已激活;false:未激活
     * @throws ModuleException 模块不存在
     */
    boolean isActivated(String uniqueId) throws ModuleException;

    /**
     * 获取模块加载状态，判断当前模块是否已经被加载
     *
     * @param uniqueId 模块ID
     * @return true:已加载;false:未加载
     * @throws ModuleException 模块不存在
     */
    boolean isLoaded(String uniqueId) throws ModuleException;

    /**
     * 获取模块所在的Jar文件
     *
     * @param uniqueId 模块ID
     * @return 模块所在的Jar文件
     * @throws ModuleException 模块不存在
     */
    File getJarFile(String uniqueId) throws ModuleException;

}
