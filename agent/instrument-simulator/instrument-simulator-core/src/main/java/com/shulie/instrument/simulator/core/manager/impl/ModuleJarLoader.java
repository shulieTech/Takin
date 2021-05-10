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

import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.LoadMode;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleSpec;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.core.classloader.ClassLoaderFactory;
import com.shulie.instrument.simulator.core.classloader.ClassLoaderService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

class ModuleJarLoader {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 等待加载的模块jar文件
     */
    private final File moduleJarFile;

    /**
     * 仿真器加载模式
     */
    private final LoadMode loadMode;

    private final ModuleSpec moduleSpec;
    private final ClassLoaderService classLoaderService;

    ModuleJarLoader(final ModuleSpec moduleSpec,
                    final LoadMode loadMode,
                    final ClassLoaderService classLoaderService) {
        this.moduleJarFile = moduleSpec.getFile();
        this.loadMode = loadMode;
        this.moduleSpec = moduleSpec;
        this.classLoaderService = classLoaderService;
    }


    private boolean loadingModules(final SimulatorConfig simulatorConfig,
                                   final ClassLoaderFactory moduleClassLoader,
                                   final ModuleLoadCallback moduleLoadCallback) {
        final ServiceLoader<ExtensionModule> moduleServiceLoader = ServiceLoader.load(ExtensionModule.class, moduleClassLoader.getDefaultClassLoader());
        final Iterator<ExtensionModule> it = moduleServiceLoader.iterator();
        List<ExtensionModule> moduleList = new ArrayList<ExtensionModule>();
        while (it.hasNext()) {
            final ExtensionModule module;
            try {
                module = it.next();
            } catch (Throwable cause) {
                logger.warn("SIMULATOR: loading module instance failed: instance occur error, will be ignored. module-jar={}", moduleJarFile, cause);
                continue;
            }

            final Class<?> classOfModule = module.getClass();

            // 判断模块是否实现了@ModuleInfo标记
            if (!classOfModule.isAnnotationPresent(ModuleInfo.class)) {
                logger.warn("SIMULATOR: loading module instance failed: not implements @Information, will be ignored. class={};module-jar={};",
                        classOfModule,
                        moduleJarFile
                );
                continue;
            }

            final ModuleInfo info = classOfModule.getAnnotation(ModuleInfo.class);

            // 判断模块要求的启动模式和容器的启动模式是否匹配
            if (!ArrayUtils.contains(info.supportedModes(), loadMode)) {
                logger.warn("SIMULATOR: loading module instance failed: launch-mode is not match module required, will be ignored. module={};launch-mode={};required-mode={};class={};module-jar={};",
                        moduleSpec.getModuleId(),
                        loadMode,
                        StringUtils.join(moduleSpec.getSupportedModes(), ","),
                        classOfModule,
                        moduleJarFile
                );
                continue;
            }
            if(module != null) {
                moduleList.add(module);
            }
        }
        if (moduleList.size() != 1) {
            logger.warn("SIMULATOR: loading module has more than 1 module definition. {};",
                    moduleJarFile
            );
            return false;
        }

        boolean loadingSuccess = false;
        for (ExtensionModule module : moduleList) {
            final Class<?> classOfModule = module.getClass();
            final ModuleInfo info = classOfModule.getAnnotation(ModuleInfo.class);
            moduleSpec.setClassOfModule(classOfModule);
            moduleSpec.setModule(module);
            moduleSpec.loadModuleInfo(info);
            try {
                if (null != moduleLoadCallback) {
                    moduleLoadCallback.onLoad(simulatorConfig, moduleSpec, moduleSpec.getClassOfModule(), moduleSpec.getModule(), moduleJarFile, moduleClassLoader);
                }
                loadingSuccess = true;
            } catch (Throwable cause) {
                logger.warn("SIMULATOR: loading module instance failed: MODULE-LOADER-PROVIDER denied, will be ignored. module={};class={};module-jar={};",
                        moduleSpec.getModuleId(),
                        moduleSpec.getClassOfModule(),
                        moduleJarFile,
                        cause
                );
                continue;
            }
        }

        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: loaded module-jar completed, loaded module in module-jar={}, modules={}",
                    moduleJarFile,
                    moduleSpec.getModuleId()
            );
        }
        return loadingSuccess;
    }


    void load(final SimulatorConfig simulatorConfig, final ModuleLoadCallback moduleLoadCallback) throws IOException {

        boolean hasModuleLoadedSuccessFlag = false;
        ClassLoaderFactory classLoaderFactory = null;
        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: prepare loading module-jar={};", moduleJarFile);
        }
        try {
            classLoaderFactory = classLoaderService.getModuleClassLoaderFactory(moduleSpec.getModuleId());

            final ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            ClassLoader moduleClassLoader = classLoaderFactory.getDefaultClassLoader();
            Thread.currentThread().setContextClassLoader(moduleClassLoader);

            try {
                hasModuleLoadedSuccessFlag = loadingModules(simulatorConfig, classLoaderFactory, moduleLoadCallback);
            } finally {
                Thread.currentThread().setContextClassLoader(contextClassLoader);
            }

        } finally {
            if (!hasModuleLoadedSuccessFlag
                    && null != classLoaderFactory) {
                logger.warn("SIMULATOR: loading module-jar completed, but NONE module loaded, will be close ModuleJarClassLoader. module-jar={};", moduleJarFile);
                classLoaderFactory.release();
            }
        }

    }

    /**
     * 模块加载回调
     */
    public interface ModuleLoadCallback {

        /**
         * 模块加载回调
         *
         * @param simulatorConfig    仿真器配置
         * @param moduleSpec         模块描述
         * @param moduleClass        模块类
         * @param module             模块实例
         * @param moduleJarFile      模块所在Jar文件
         * @param classLoaderFactory 负责加载模块的ClassLoader工厂
         * @throws Throwable 加载回调异常
         */
        void onLoad(SimulatorConfig simulatorConfig,
                    ModuleSpec moduleSpec,
                    Class moduleClass,
                    ExtensionModule module,
                    File moduleJarFile,
                    ClassLoaderFactory classLoaderFactory) throws Throwable;

    }

}
