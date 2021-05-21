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
import com.shulie.instrument.simulator.jdk.api.boot.BootLoader;
import com.shulie.instrument.simulator.jdk.impl.boot.BootLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarFile;

/**
 * bootstrap classloader 的类注入处理器
 */
public class BootstrapClassLoaderHandler implements ClassInjector {

    private final Logger logger = LoggerFactory.getLogger(BootstrapClassLoaderHandler.class.getName());

    private final Object lock = new Object();
    private final SimulatorConfig simulatorConfig;
    private Set<String> isInjectedJarUrls = new HashSet<String>();
    private final boolean injectEnabled;

    public BootstrapClassLoaderHandler(SimulatorConfig simulatorConfig) {
        if (simulatorConfig == null) {
            throw new NullPointerException("SimulatorConfig must not be null");
        }
        this.simulatorConfig = simulatorConfig;
        this.injectEnabled = !simulatorConfig.getBizClassLoaderInjectFiles().isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void injectClass(ClassLoader classLoader, String className) {
        try {
            if (classLoader == null) {
                injectClass0(className);
                return;
            }
        } catch (Exception e) {
            logger.warn("SIMULATOR: Failed to load plugin class {} with classLoader {}", className, classLoader, e);
            throw new SimulatorException("Failed to load plugin class " + className + " with classLoader " + classLoader, e);
        }
        throw new SimulatorException("invalid ClassLoader");
    }

    private void injectClass0(String className) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        /**
         * 让此判断更快一些
         */
        if (!injectEnabled) {
            return;
        }
        synchronized (lock) {
            List<File> files = simulatorConfig.getBizClassLoaderInjectFiles().get(className);
            if (files != null) {
                for (File file : files) {
                    try {
                        /**
                         * 如果已经添加过的则忽略
                         */
                        if (!isInjectedJarUrls.add(file.toURI().toURL().toExternalForm())) {
                            continue;
                        }
                    } catch (MalformedURLException e) {
                    }
                    JarFile jarFile = null;
                    try {
                        jarFile = new JarFile(file);
                    } catch (IOException e) {
                        logger.warn("Failed to load jar file {}. className:{}", file.getName(), className);
                        continue;
                    }
                    simulatorConfig.getInstrumentation().appendToBootstrapClassLoaderSearch(jarFile);
                    try {
                        BootLoader bootLoader = BootLoaderFactory.newBootLoader();
                        bootLoader.addResource(file.toURI().toURL());
                    } catch (Throwable e) {
                        logger.warn("Failed to add jar to bootstrap classloader {}. className:{}", file.getName(), className);
                    }
                }
            }
        }
    }


}
