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
package com.shulie.instrument.simulator.spi;

import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;

import java.io.File;

/**
 * 模块加载链
 * 在模块加载时会被调用，如果需要阻止模块的加载可以通过抛出异常来阻止，如果在加载模块时需要扩展一些逻辑
 * 可以使用此接口进行一些额外扩展
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface ModuleLoadingChain {

    /**
     * 加载模块<br>
     * <p>
     * 1. 所有模块都将会通过此方法完成模块的加载<br>
     * 2. 如果判定当前模块加载不通过,可以通过抛出异常的形式来通知当前模块加载失败,Simulator将会跳过加载失败的模块<br>
     * 3. 整个模块的加载为一个链式的加载过程<br>
     * </p>
     *
     * @param simulatorConfig   仿真器配置
     * @param moduleClass       模块类
     * @param module            模块实例
     * @param moduleJarFile     模块所在Jar文件
     * @param moduleClassLoader 负责加载模块的ClassLoader
     * @throws Throwable 模块加载异常
     */
    void loading(final SimulatorConfig simulatorConfig, final Class moduleClass, final ExtensionModule module, final File moduleJarFile,
                 final ClassLoader moduleClassLoader) throws Throwable;

}
