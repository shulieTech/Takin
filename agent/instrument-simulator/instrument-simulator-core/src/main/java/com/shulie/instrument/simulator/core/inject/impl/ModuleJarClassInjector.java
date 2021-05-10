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
package com.shulie.instrument.simulator.core.inject.impl;

import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.core.exception.SimulatorException;
import com.shulie.instrument.simulator.core.inject.ClassInjector;
import com.shulie.instrument.simulator.core.inject.ModuleClassInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLClassLoader;
import java.util.Set;

/**
 * 模块 jar 包 的类注入处理器
 */
public class ModuleJarClassInjector implements ModuleClassInjector {
    private final Logger logger = LoggerFactory.getLogger(ModuleJarClassInjector.class.getName());

    private final ClassInjector bootstrapClassLoaderHandler;
    private final ClassInjector urlClassLoaderHandler;
    private final ClassInjector plainClassLoaderHandler;
    private final SimulatorConfig simulatorConfig;
    private final Set<String> classNames;

    public ModuleJarClassInjector(SimulatorConfig simulatorConfig) {
        if (simulatorConfig == null) {
            throw new NullPointerException("SimulatorConfig must not be null");
        }
        this.bootstrapClassLoaderHandler = new BootstrapClassLoaderHandler(simulatorConfig);
        this.urlClassLoaderHandler = new URLClassLoaderHandler(simulatorConfig);
        this.plainClassLoaderHandler = new PlainClassLoaderHandler(simulatorConfig);
        this.simulatorConfig = simulatorConfig;
        this.classNames = simulatorConfig.getBizClassLoaderInjectFiles().keySet();
    }

    @Override
    public SimulatorConfig getSimulatorConfig() {
        return simulatorConfig;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void injectClass(ClassLoader classLoader, String className) {
        /**
         * 如果不包含则忽略
         */
        if (!classNames.contains(className)) {
            return;
        }
        try {
            if (classLoader == null) {
                bootstrapClassLoaderHandler.injectClass(null, className);
                return;
            } else if (classLoader instanceof URLClassLoader) {
                final URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
                urlClassLoaderHandler.injectClass(urlClassLoader, className);
                return;
            } else {
                plainClassLoaderHandler.injectClass(classLoader, className);
                return;
            }
        } catch (Throwable e) {
            // fixed for LinkageError
            logger.warn("SIMULATOR: Failed to load plugin class {} with classLoader {}", className, classLoader, e);
            throw new SimulatorException("Failed to load plugin class " + className + " with classLoader " + classLoader, e);
        }
    }

}
