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
package com.shulie.instrument.simulator.core.classloader.impl;

import com.shulie.instrument.simulator.api.ModuleRuntimeException;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import com.shulie.instrument.simulator.api.util.ObjectIdUtils;
import com.shulie.instrument.simulator.core.CoreConfigure;
import com.shulie.instrument.simulator.core.classloader.ClassLoaderFactory;
import com.shulie.instrument.simulator.core.classloader.ClassLoaderService;
import com.shulie.instrument.simulator.core.classloader.ModuleClassLoader;
import com.shulie.instrument.simulator.message.boot.util.JvmUtils;
import com.shulie.instrument.simulator.message.boot.version.JvmVersion;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/13 11:34 下午
 */
public class ClassLoaderFactoryImpl implements ClassLoaderFactory {
    private final static Logger logger = LoggerFactory.getLogger(ClassLoaderFactoryImpl.class);

    private final ClassLoaderService classLoaderService;
    private final File moduleJarFile;
    private final String moduleId;
    private ConcurrentHashMap<Integer, ModuleClassLoader> classLoaderCache;
    private final long checksumCRC32;
    private final CoreConfigure config;
    private ModuleClassLoader defaultClassLoader;
    /**
     * 是否是中间件扩展模块
     */
    private boolean isMiddlewareModule;

    public ClassLoaderFactoryImpl(final ClassLoaderService classLoaderService, final CoreConfigure config, final File moduleJarFile, final String moduleId, final boolean isMiddlewareModule) throws IOException {
        this.classLoaderService = classLoaderService;
        this.moduleJarFile = moduleJarFile;
        this.moduleId = moduleId;
        this.config = config;
        this.isMiddlewareModule = isMiddlewareModule;
        this.classLoaderCache = new ConcurrentHashMap<Integer, ModuleClassLoader>();
        this.checksumCRC32 = FileUtils.checksumCRC32(moduleJarFile);
        this.defaultClassLoader = new ModuleClassLoader(classLoaderService, moduleJarFile, moduleId);
    }

    @Override
    public ClassLoader getDefaultClassLoader() {
        return defaultClassLoader;
    }

    @Override
    public ClassLoader getClassLoader(ClassLoader businessClassLoader) {
        if (businessClassLoader == null || !isMiddlewareModule) {
            return defaultClassLoader;
        }
        try {
            int id = ObjectIdUtils.instance.identity(businessClassLoader);
            if (id == 0) {
                return defaultClassLoader;
            }

            ModuleClassLoader moduleClassLoader = this.classLoaderCache.get(id);
            if (moduleClassLoader != null) {
                return moduleClassLoader;
            }
            moduleClassLoader = new ModuleClassLoader(classLoaderService, moduleJarFile, moduleId);
            ModuleClassLoader oldModuleClassLoader = classLoaderCache.putIfAbsent(id, moduleClassLoader);
            if (oldModuleClassLoader != null) {
                moduleClassLoader.closeIfPossible();
                moduleClassLoader = oldModuleClassLoader;
            }
            return moduleClassLoader;
        } catch (IOException e) {
            throw new ModuleRuntimeException("SIMULATOR: getModuleClassLoader err", ModuleRuntimeException.ErrorCode.MODULE_LOAD_ERROR);
        }
    }

    @Override
    public long getChecksumCRC32() {
        return checksumCRC32;
    }

    @Override
    public void release() {
        if (classLoaderCache == null) {
            return;
        }
        for (Map.Entry<Integer, ModuleClassLoader> entry : classLoaderCache.entrySet()) {
            entry.getValue().closeIfPossible();
        }
        defaultClassLoader.closeIfPossible();
        classLoaderCache.clear();
        classLoaderCache = null;
    }
}
