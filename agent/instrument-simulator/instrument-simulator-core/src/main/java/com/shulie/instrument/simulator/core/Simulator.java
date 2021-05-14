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
package com.shulie.instrument.simulator.core;

import com.shulie.instrument.simulator.core.classloader.ClassLoaderService;
import com.shulie.instrument.simulator.core.classloader.impl.DefaultClassLoaderService;
import com.shulie.instrument.simulator.core.enhance.weaver.EventListenerHandler;
import com.shulie.instrument.simulator.core.manager.CoreModuleManager;
import com.shulie.instrument.simulator.core.manager.impl.DefaultCoreLoadedClassDataSource;
import com.shulie.instrument.simulator.core.manager.impl.DefaultCoreModuleManager;
import com.shulie.instrument.simulator.core.manager.impl.DefaultProviderManager;
import com.shulie.instrument.simulator.core.util.MessageUtils;
import com.shulie.instrument.simulator.api.guard.SimulatorGuard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.List;

/**
 * 仿真器
 */
public class Simulator {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    /**
     * 需要提前加载的Simulator工具类
     */
    private final static List<String> earlyLoadSimulatorClassNameList = new ArrayList<String>();

    static {
        earlyLoadSimulatorClassNameList.add("com.shulie.instrument.simulator.core.util.SimulatorClassUtils");
        earlyLoadSimulatorClassNameList.add("com.shulie.instrument.simulator.core.util.matcher.structure.ClassStructureImplByAsm");
    }

    private final CoreConfigure config;
    private final CoreModuleManager coreModuleManager;
    private final ClassLoaderService classLoaderService;

    public Simulator(final CoreConfigure config,
                     final Instrumentation inst) {
        EventListenerHandler.getSingleton();
        this.config = config;
        this.classLoaderService = new DefaultClassLoaderService();
        this.classLoaderService.init();
        this.coreModuleManager = SimulatorGuard.getInstance().doGuard(CoreModuleManager.class, new DefaultCoreModuleManager(
                config,
                inst,
                new DefaultCoreLoadedClassDataSource(inst, config.isEnableUnsafe()),
                new DefaultProviderManager(config),
                classLoaderService
        ));

        init();
    }

    private void init() {
        doEarlyLoadSimulatorClass();
        MessageUtils.init(config.getNamespace());
        this.coreModuleManager.onStartup();
    }

    /**
     * 提前加载某些必要的类
     */
    private void doEarlyLoadSimulatorClass() {
        for (String className : earlyLoadSimulatorClassNameList) {
            try {
                Class.forName(className);
            } catch (ClassNotFoundException e) {
                //加载Simulator内部的类，不可能加载不到
            }
        }
    }

    /**
     * 获取模块管理器
     *
     * @return 模块管理器
     */
    public CoreModuleManager getCoreModuleManager() {
        return coreModuleManager;
    }

    /**
     * 销毁仿真器
     */
    public void destroy() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("SIMULATOR: prepare to destroying simulator instance. namespace: {}, appName: {}", config.getNamespace(), config.getAppName());
        }

        // 卸载所有的模块
        coreModuleManager.unloadAll();

        // 清理Messager
        MessageUtils.clean(config.getNamespace());

        this.coreModuleManager.onShutdown();
        //关闭classLoader service
        classLoaderService.dispose();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("SIMULATOR: simulator instance is destroyed success. namespace: {}, appName: {}", config.getNamespace(), config.getAppName());
        }
    }

}
