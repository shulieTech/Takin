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
import com.shulie.instrument.simulator.api.ModuleSpec;
import com.shulie.instrument.simulator.core.classloader.ClassLoaderFactory;
import com.shulie.instrument.simulator.core.classloader.ClassLoaderService;
import com.shulie.instrument.simulator.core.util.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassLoader Service Implementation
 */
public class DefaultClassLoaderService implements ClassLoaderService {
    private final static Logger logger = LoggerFactory.getLogger(DefaultClassLoaderService.class);

    private static final List<String> SUN_REFLECT_GENERATED_ACCESSOR = new ArrayList<String>();

    /* export class and classloader relationship cache */
    private ConcurrentHashMap<String, ClassLoaderFactory> exportExactlyClassesClassLoaderMap = new ConcurrentHashMap<String, ClassLoaderFactory>();
    private ConcurrentHashMap<String, ClassLoaderFactory> exportExactlyPackagesClassLoaderMap = new ConcurrentHashMap<String, ClassLoaderFactory>();
    private ConcurrentHashMap<String, ClassLoaderFactory> exportPrefixPackagesClassLoaderMap = new ConcurrentHashMap<String, ClassLoaderFactory>();
    private ConcurrentHashMap<String, ClassLoaderFactory> exportSuffixPackagesClassLoaderMap = new ConcurrentHashMap<String, ClassLoaderFactory>();

    /* export cache and classloader relationship cache */
    private ConcurrentHashMap<String, List<ClassLoaderFactory>> exportExactlyResourceClassLoaderMap = new ConcurrentHashMap<String, List<ClassLoaderFactory>>();
    private ConcurrentHashMap<String, List<ClassLoaderFactory>> exportPrefixResourceClassLoaderMap = new ConcurrentHashMap<String, List<ClassLoaderFactory>>();
    private ConcurrentHashMap<String, List<ClassLoaderFactory>> exportSuffixResourceClassLoaderMap = new ConcurrentHashMap<String, List<ClassLoaderFactory>>();
    private ConcurrentHashMap<String, ModuleSpec> moduleSpecMap = new ConcurrentHashMap<String, ModuleSpec>();
    private ConcurrentHashMap<String, ClassLoaderFactory> moduleClassLoaderMap = new ConcurrentHashMap<String, ClassLoaderFactory>();

    private ClassLoader simulatorClassLoader;
    private ClassLoader systemClassLoader;

    static {
        SUN_REFLECT_GENERATED_ACCESSOR.add("sun.reflect.GeneratedMethodAccessor");
        SUN_REFLECT_GENERATED_ACCESSOR.add("sun.reflect.GeneratedConstructorAccessor");
        SUN_REFLECT_GENERATED_ACCESSOR.add("sun.reflect.GeneratedSerializationConstructorAccessor");
    }

    private static class ClassLoaderServiceImplHolder {
        public final static ClassLoaderService INSTANCE = new DefaultClassLoaderService();
    }

    public static ClassLoaderService getClassLoaderService() {
        return ClassLoaderServiceImplHolder.INSTANCE;
    }

    @Override
    public boolean isSunReflectClass(String className) {
        for (String sunAccessor : SUN_REFLECT_GENERATED_ACCESSOR) {
            if (className.startsWith(sunAccessor)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void unload(String moduleName, ClassLoaderFactory classLoaderFactory) {
        ModuleSpec moduleSpec = this.moduleSpecMap.remove(moduleName);
        if (moduleSpec == null) {
            return;
        }
        unloadExportClassAndResourceCache(moduleSpec, classLoaderFactory);
    }

    @Override
    public void load(ModuleSpec moduleSpec, ClassLoaderFactory classLoaderFactory) {
        this.moduleSpecMap.putIfAbsent(moduleSpec.getModuleId(), moduleSpec);
        loadExportClassAndResourceCache(moduleSpec, classLoaderFactory);
    }

    /**
     * 卸载模块导出的类和资源
     *
     * @param moduleSpec         模块描述
     * @param classLoaderFactory 模块类加载器工厂
     */
    private void unloadExportClassAndResourceCache(ModuleSpec moduleSpec, ClassLoaderFactory classLoaderFactory) {
        for (String exportIndex : moduleSpec.getExportClasses()) {
            exportExactlyClassesClassLoaderMap.remove(exportIndex);
        }
        for (String exportIndex : moduleSpec.getExportExactlyPackages()) {
            exportExactlyPackagesClassLoaderMap.remove(exportIndex);
        }
        for (String exportIndex : moduleSpec.getExportPrefixPackages()) {
            exportPrefixPackagesClassLoaderMap.remove(exportIndex);
        }
        for (String exportIndex : moduleSpec.getExportSuffixPackages()) {
            exportSuffixPackagesClassLoaderMap.remove(exportIndex);
        }
        for (String resource : moduleSpec.getExportResources()) {
            List<ClassLoaderFactory> moduleClassLoaders = exportExactlyResourceClassLoaderMap.get(resource);
            if (moduleClassLoaders == null) {
                continue;
            }
            moduleClassLoaders.remove(classLoaderFactory);
        }

        for (String resource : moduleSpec.getExportPrefixResources()) {
            List<ClassLoaderFactory> moduleClassLoaders = exportPrefixResourceClassLoaderMap.get(resource);
            if (moduleClassLoaders == null) {
                continue;
            }
            moduleClassLoaders.remove(classLoaderFactory);
        }

        for (String resource : moduleSpec.getExportSuffixResources()) {
            List<ClassLoaderFactory> moduleClassLoaders = exportSuffixResourceClassLoaderMap.get(resource);
            if (moduleClassLoaders == null) {
                continue;
            }
            moduleClassLoaders.remove(classLoaderFactory);
        }
    }

    /**
     * 加载导出类和资源缓存
     *
     * @param moduleSpec         模块描述
     * @param classLoaderFactory 模块类加载器
     */
    private void loadExportClassAndResourceCache(ModuleSpec moduleSpec, ClassLoaderFactory classLoaderFactory) {
        this.moduleClassLoaderMap.putIfAbsent(moduleSpec.getModuleId(), classLoaderFactory);

        for (String exportIndex : moduleSpec.getExportClasses()) {
            exportExactlyClassesClassLoaderMap.putIfAbsent(exportIndex, classLoaderFactory);
        }
        for (String exportIndex : moduleSpec.getExportExactlyPackages()) {
            exportExactlyPackagesClassLoaderMap.putIfAbsent(exportIndex, classLoaderFactory);
        }
        for (String exportIndex : moduleSpec.getExportPrefixPackages()) {
            exportPrefixPackagesClassLoaderMap
                    .putIfAbsent(exportIndex, classLoaderFactory);
        }
        for (String exportIndex : moduleSpec.getExportSuffixPackages()) {
            exportSuffixPackagesClassLoaderMap
                    .putIfAbsent(exportIndex, classLoaderFactory);
        }
        for (String resource : moduleSpec.getExportResources()) {
            List<ClassLoaderFactory> moduleClassLoaders = exportExactlyResourceClassLoaderMap.get(resource);
            if (moduleClassLoaders == null) {
                moduleClassLoaders = new LinkedList<ClassLoaderFactory>();
                List<ClassLoaderFactory> list = exportExactlyResourceClassLoaderMap.putIfAbsent(resource, moduleClassLoaders);
                if (list != null) {
                    moduleClassLoaders = list;
                }
            }
            moduleClassLoaders.add(classLoaderFactory);
        }

        for (String resource : moduleSpec.getExportPrefixResources()) {
            List<ClassLoaderFactory> moduleClassLoaders = exportPrefixResourceClassLoaderMap.get(resource);
            if (moduleClassLoaders == null) {
                moduleClassLoaders = new LinkedList<ClassLoaderFactory>();
                List<ClassLoaderFactory> list = exportPrefixResourceClassLoaderMap.putIfAbsent(resource, moduleClassLoaders);
                if (list != null) {
                    moduleClassLoaders = list;
                }
            }
            moduleClassLoaders.add(classLoaderFactory);
        }

        for (String resource : moduleSpec.getExportSuffixResources()) {
            List<ClassLoaderFactory> moduleClassLoaders = exportSuffixResourceClassLoaderMap.get(resource);
            if (moduleClassLoaders == null) {
                moduleClassLoaders = new LinkedList<ClassLoaderFactory>();
                List<ClassLoaderFactory> list = exportSuffixResourceClassLoaderMap.putIfAbsent(resource, moduleClassLoaders);
                if (list != null) {
                    moduleClassLoaders = list;
                }
            }
            moduleClassLoaders.add(classLoaderFactory);
        }
    }

    @Override
    public boolean isClassInImport(String moduleName, String className) {
        ModuleSpec moduleSpec = moduleSpecMap.get(moduleName);
        if (moduleSpec == null) {
            logger.warn("SIMULATOR: invoke module {} isClassImport {} error cause by moduleSpec is not found. default return false.", moduleName, className);
            return false;
        }

        for (String importName : moduleSpec.getImportClasses()) {
            if (className.equals(importName)) {
                return true;
            }
        }

        String packageName = ClassUtils.getPackageName(className);
        for (String pattern : moduleSpec.getImportExactlyPackages()) {
            if (packageName.equals(pattern)) {
                return true;
            }
        }

        for (String pattern : moduleSpec.getImportPrefixPackages()) {
            if (packageName.startsWith(pattern)) {
                return true;
            }
        }

        for (String pattern : moduleSpec.getImportSuffixPackages()) {
            if (packageName.endsWith(pattern)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ClassLoaderFactory findExportClassLoaderFactory(String className) {
        ClassLoaderFactory exportClassLoaderFactory = exportExactlyClassesClassLoaderMap.get(className);
        String packageName = ClassUtils.getPackageName(className);
        if (exportClassLoaderFactory == null) {
            exportClassLoaderFactory = exportExactlyPackagesClassLoaderMap.get(packageName);
        }
        while (!ClassUtils.DEFAULT_PACKAGE.equals(packageName) && StringUtils.isNotBlank(packageName) && exportClassLoaderFactory == null) {
            exportClassLoaderFactory = exportPrefixPackagesClassLoaderMap.get(packageName);
            packageName = ClassUtils.getPackageName(packageName);
        }

        if (exportClassLoaderFactory == null) {
            while (!ClassUtils.DEFAULT_PACKAGE.equals(packageName) && StringUtils.isNotBlank(packageName) && exportClassLoaderFactory == null) {
                exportClassLoaderFactory = exportSuffixPackagesClassLoaderMap.get(packageName);
                packageName = ClassUtils.getNextPackageName(packageName);
            }
        }

        return exportClassLoaderFactory;
    }

    @Override
    public boolean isResourceInImport(String moduleName, String resourceName) {
        ModuleSpec moduleSpec = moduleSpecMap.get(moduleName);
        if (moduleSpec == null) {
            logger.warn("SIMULATOR: invoke module {} isResourceInImport error cause by moduleSpec is not found. default return false.");
            return false;
        }

        for (String importResource : moduleSpec.getImportExactlyResources()) {
            if (importResource.equals(resourceName)) {
                return true;
            }
        }

        for (String importResource : moduleSpec.getImportPrefixResources()) {
            if (resourceName.startsWith(importResource)) {
                return true;
            }
        }

        for (String importResource : moduleSpec.getImportSuffixResources()) {
            if (resourceName.endsWith(importResource)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<ClassLoaderFactory> findExportResourceClassLoadersInOrder(String resourceName) {

        if (exportExactlyResourceClassLoaderMap.containsKey(resourceName)) {
            return exportExactlyResourceClassLoaderMap.get(resourceName);
        }

        for (String resource : exportPrefixResourceClassLoaderMap.keySet()) {
            if (resourceName.startsWith(resource)) {
                return exportPrefixResourceClassLoaderMap.get(resource);
            }
        }

        for (String resource : exportSuffixResourceClassLoaderMap.keySet()) {
            if (resourceName.endsWith(resource)) {
                return exportSuffixResourceClassLoaderMap.get(resource);
            }
        }
        return null;
    }

    @Override
    public ClassLoader getSimulatorClassLoader() {
        return simulatorClassLoader;
    }

    @Override
    public ClassLoader getSystemClassLoader() {
        return systemClassLoader;
    }

    @Override
    public ClassLoaderFactory getModuleClassLoaderFactory(String moduleName) {
        return this.moduleClassLoaderMap.get(moduleName);
    }

    @Override
    public void init() throws ModuleRuntimeException {
        simulatorClassLoader = this.getClass().getClassLoader();
        systemClassLoader = ClassLoader.getSystemClassLoader();
    }

    @Override
    public void dispose() throws ModuleRuntimeException {
        SUN_REFLECT_GENERATED_ACCESSOR.clear();

        exportExactlyClassesClassLoaderMap.clear();
        exportExactlyClassesClassLoaderMap = null;

        exportExactlyPackagesClassLoaderMap.clear();
        exportExactlyPackagesClassLoaderMap = null;

        exportPrefixPackagesClassLoaderMap.clear();
        exportPrefixPackagesClassLoaderMap = null;

        exportSuffixPackagesClassLoaderMap.clear();
        exportExactlyClassesClassLoaderMap = null;

        exportExactlyResourceClassLoaderMap.clear();
        exportExactlyResourceClassLoaderMap = null;

        exportPrefixResourceClassLoaderMap.clear();
        exportPrefixResourceClassLoaderMap = null;

        exportSuffixResourceClassLoaderMap.clear();
        exportSuffixResourceClassLoaderMap = null;

        moduleSpecMap.clear();
        moduleSpecMap = null;

        moduleClassLoaderMap.clear();
        moduleClassLoaderMap = null;
    }
}
