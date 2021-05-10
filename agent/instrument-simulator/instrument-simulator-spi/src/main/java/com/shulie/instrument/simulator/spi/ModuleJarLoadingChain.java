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

import com.shulie.instrument.simulator.api.ModuleSpec;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;

import java.io.File;

/**
 * 模块Jar文件加载链
 * 一般可用于模块 jar 文件加载前的验证、授权等一些操作
 * 当模块 Jar 文件加载失败可以通过抛出异常来阻止后续进一步加载
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface ModuleJarLoadingChain {

    /**
     * 加载模块Jar文件 <br>
     * <p>
     * 1. 可以在这个实现中对目标期待加载的模块Jar文件进行解密,签名验证等操作<br>
     * 2. 如果判定加载失败,可以通过抛出异常的形式中断加载,Simulator将会跳过此模块Jar文件的加载<br>
     * 3. 整个模块文件的加载为一个链式的加载过程<br>
     * </p>
     *
     * @param simulatorConfig 仿真器配置
     * @param moduleSpec      模块描述
     * @param moduleJarFile   期待被加载模块Jar文件
     * @throws Throwable 模块文件加载异常
     */
    void loading(final SimulatorConfig simulatorConfig, ModuleSpec moduleSpec, File moduleJarFile) throws Throwable;

}
