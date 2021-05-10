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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * URLClassloader 的类注入处理器
 */
public class URLClassLoaderHandler implements ClassInjector {

    private final Logger logger = LoggerFactory.getLogger(URLClassLoaderHandler.class.getName());

    private static final Method ADD_URL;

    static {
        try {
            ADD_URL = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            ADD_URL.setAccessible(true);
        } catch (Exception e) {
            throw new SimulatorException("Cannot access URLClassLoader.addURL(URL)", e);
        }
    }

    private final SimulatorConfig simulatorConfig;

    public URLClassLoaderHandler(SimulatorConfig simulatorConfig) {
        if (simulatorConfig == null) {
            throw new NullPointerException("pluginConfig must not be null");
        }
        this.simulatorConfig = simulatorConfig;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void injectClass(ClassLoader classLoader, String className) {
        try {
            if (classLoader instanceof URLClassLoader) {
                final URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
                injectClass0(urlClassLoader, className);
                return;
            }
        } catch (Exception e) {
            logger.warn("SIMULATOR: Failed to load plugin class {} with classLoader {}", className, classLoader, e);
            throw new SimulatorException("Failed to load plugin class " + className + " with classLoader " + classLoader, e);
        }
        throw new SimulatorException("invalid ClassLoader");
    }


    private void injectClass0(URLClassLoader classLoader, String className) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
        final URL[] urls = classLoader.getURLs();
        List<File> defineUrls = simulatorConfig.getBizClassLoaderInjectFiles().get(className);
        if (urls != null) {
            for (File f : defineUrls) {
                URL url = null;
                try {
                    url = f.toURI().toURL();
                } catch (MalformedURLException e) {
                    continue;
                }
                final boolean hasPluginJar = hasPluginJar(urls, url);

                if (!hasPluginJar) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("SIMULATOR: add Jar:{}", defineUrls);
                    }
                    ADD_URL.invoke(classLoader, url);
                }
            }
        }
    }

    private boolean hasPluginJar(URL[] urls, URL u) {
        String urlSpec = u.toExternalForm();
        for (URL url : urls) {
            final String externalForm = url.toExternalForm();
            if (urlSpec.equals(externalForm)) {
                return true;
            }
        }
        return false;
    }


}
