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
package com.shulie.instrument.simulator.core.manager.impl;

import com.shulie.instrument.simulator.api.LoadMode;
import com.shulie.instrument.simulator.api.ModuleSpec;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.core.classloader.ClassLoaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 模块目录加载器
 * 用于从${module.lib}中加载所有的仿真器模块
 */
class ModuleLibLoader {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // 模块描述
    private final ModuleSpec moduleSpec;

    // 仿真器加载模式
    private final LoadMode loadMode;
    private final ClassLoaderService classLoaderService;

    ModuleLibLoader(final ModuleSpec moduleSpec,
                    final LoadMode loadMode,
                    final ClassLoaderService classLoaderService) {
        this.moduleSpec = moduleSpec;
        this.loadMode = loadMode;
        this.classLoaderService = classLoaderService;
    }

    /**
     * 加载Module
     *
     * @param moduleJarLoadCallback 模块文件加载回调
     * @param moduleLoadCallback    模块加载回掉
     */
    void load(final SimulatorConfig simulatorConfig, final ModuleJarLoadCallback moduleJarLoadCallback,
              final ModuleJarLoader.ModuleLoadCallback moduleLoadCallback) {
        try {
            moduleJarLoadCallback.onLoad(simulatorConfig, moduleSpec, moduleSpec.getFile());
            new ModuleJarLoader(moduleSpec, loadMode, classLoaderService).load(simulatorConfig, moduleLoadCallback);
        } catch (Throwable cause) {
            logger.warn("SIMULATOR: loading module-jar occur error! module-jar={};", moduleSpec.getFile(), cause);
        }

    }

    /**
     * 模块文件加载回调
     */
    public interface ModuleJarLoadCallback {

        /**
         * 模块文件加载回调
         *
         * @param simulatorConfig 仿真器配置
         * @param moduleSpec      模块描述
         * @param moduleJarFile   模块文件
         * @throws Throwable 加载回调异常
         */
        void onLoad(SimulatorConfig simulatorConfig, ModuleSpec moduleSpec, File moduleJarFile) throws Throwable;

    }

}
