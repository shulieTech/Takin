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
import com.shulie.instrument.simulator.api.ModuleSpec;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.core.CoreConfigure;
import com.shulie.instrument.simulator.core.classloader.ProviderClassLoader;
import com.shulie.instrument.simulator.core.manager.ProviderManager;
import com.shulie.instrument.simulator.message.boot.util.JvmUtils;
import com.shulie.instrument.simulator.message.boot.version.JvmVersion;
import com.shulie.instrument.simulator.spi.ModuleJarLoadingChain;
import com.shulie.instrument.simulator.spi.ModuleLoadingChain;
import com.shulie.instrument.simulator.spi.SimulatorLifecycle;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 默认服务提供管理器实现
 */
public class DefaultProviderManager implements ProviderManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Collection<ModuleJarLoadingChain> moduleJarLoadingChains = new ArrayList<ModuleJarLoadingChain>();
    private final Collection<ModuleLoadingChain> moduleLoadingChains = new ArrayList<ModuleLoadingChain>();
    private final Collection<SimulatorLifecycle> startupChains = new ArrayList<SimulatorLifecycle>();
    private final CoreConfigure config;

    public DefaultProviderManager(final CoreConfigure config) {
        this.config = config;
        try {
            init(config);
        } catch (Throwable cause) {
            logger.warn("SIMULATOR: loading simulator's provider-lib[{}] failed.", config.getProviderLibPath(), cause);
        }
    }

    private void init(final CoreConfigure config) {
        final File providerLibDir = new File(config.getProviderLibPath());
        if (!providerLibDir.exists()
                || !providerLibDir.canRead()) {
            logger.warn("SIMULATOR: loading provider-lib[{}] was failed, doest existed or access denied.", providerLibDir);
            return;
        }

        for (final Object file : FileUtils.listFiles(providerLibDir, new String[]{"jar"}, false)) {
            File providerJarFile = (File) file;
            try {
                final ProviderClassLoader providerClassLoader = new ProviderClassLoader(providerJarFile, getClass().getClassLoader());
                // load ModuleJarLoadingChain
                inject(moduleJarLoadingChains, ModuleJarLoadingChain.class, providerClassLoader, providerJarFile);

                // load ModuleLoadingChain
                inject(moduleLoadingChains, ModuleLoadingChain.class, providerClassLoader, providerJarFile);

                // load SimulatorLifecycleChain
                inject(startupChains, SimulatorLifecycle.class, providerClassLoader, providerJarFile);

                if (logger.isInfoEnabled()) {
                    logger.info("SIMULATOR: loading provider-jar[{}] was success.", providerJarFile);
                }
            } catch (IllegalAccessException cause) {
                logger.warn("SIMULATOR: loading provider-jar[{}] occur error, inject provider resource failed.", providerJarFile, cause);
            } catch (IOException ioe) {
                logger.warn("SIMULATOR: loading provider-jar[{}] occur error, ignore load this provider.", providerJarFile, ioe);
            }

        }

    }

    private <T> void inject(final Collection<T> collection,
                            final Class<T> clazz,
                            final ClassLoader providerClassLoader,
                            final File providerJarFile) throws IllegalAccessException {
        final ServiceLoader<T> serviceLoader = ServiceLoader.load(clazz, providerClassLoader);
        for (final T provider : serviceLoader) {
            injectResource(provider);
            collection.add(provider);
            if (logger.isInfoEnabled()) {
                logger.info("SIMULATOR: loading provider[{}] was success from provider-jar[{}], impl={}",
                        clazz.getName(), providerJarFile, provider.getClass().getName());
            }
        }
    }

    private static Field[] getFieldsWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        final List<Field> annotatedFieldsList = getFieldsListWithAnnotation(cls, annotationCls);
        return annotatedFieldsList.toArray(new Field[0]);
    }

    private static List<Field> getFieldsListWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        final List<Field> allFields = getAllFieldsList(cls);
        final List<Field> annotatedFields = new ArrayList<Field>();
        for (final Field field : allFields) {
            if (field.getAnnotation(annotationCls) != null) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }

    private static List<Field> getAllFieldsList(final Class<?> cls) {
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    private void injectResource(final Object provider) throws IllegalAccessException {
        final Field[] resourceFieldArray = getFieldsWithAnnotation(provider.getClass(), Resource.class);
        if (ArrayUtils.isEmpty(resourceFieldArray)) {
            return;
        }
        for (final Field resourceField : resourceFieldArray) {
            final Class<?> fieldType = resourceField.getType();
            // SimulatorConfig注入
            if (SimulatorConfig.class.isAssignableFrom(fieldType)) {
                final SimulatorConfig simulatorConfig = new DefaultSimulatorConfig(config);
                FieldUtils.writeField(resourceField, provider, simulatorConfig, true);
            }
        }
    }

    @Override
    public void loading(final SimulatorConfig simulatorConfig, final ModuleSpec moduleSpec, final File moduleJarFile) throws Throwable {
        for (final ModuleJarLoadingChain chain : moduleJarLoadingChains) {
            chain.loading(simulatorConfig, moduleSpec, moduleJarFile);
        }
    }

    @Override
    public void loading(final SimulatorConfig simulatorConfig,
                        final Class moduleClass,
                        final ExtensionModule module,
                        final File moduleJarFile,
                        final ClassLoader moduleClassLoader) throws Throwable {
        for (final ModuleLoadingChain chain : moduleLoadingChains) {
            chain.loading(
                    simulatorConfig,
                    moduleClass,
                    module,
                    moduleJarFile,
                    moduleClassLoader
            );
        }
    }

    @Override
    public void onStart(String namespace, SimulatorConfig simulatorConfig) {
        for (final SimulatorLifecycle simulatorLifecycle : startupChains) {
            simulatorLifecycle.onStart(namespace, simulatorConfig);
        }
    }

    @Override
    public void onShutdown(String namespace, SimulatorConfig simulatorConfig) {
        for (final SimulatorLifecycle simulatorLifecycle : startupChains) {
            simulatorLifecycle.onShutdown(namespace, simulatorConfig);
        }
    }
}
