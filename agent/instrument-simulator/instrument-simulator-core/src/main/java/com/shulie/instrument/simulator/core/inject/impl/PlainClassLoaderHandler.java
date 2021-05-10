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
import com.shulie.instrument.simulator.core.inject.util.ExtensionFilter;
import com.shulie.instrument.simulator.core.inject.util.FileBinary;
import com.shulie.instrument.simulator.core.inject.util.JarReader;
import com.shulie.instrument.simulator.core.inject.util.classreading.ClassLoadingChecker;
import com.shulie.instrument.simulator.core.inject.util.classreading.SimpleClassMetadata;
import com.shulie.instrument.simulator.core.inject.util.classreading.SimpleClassMetadataReader;
import com.shulie.instrument.simulator.message.ConcurrentWeakHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarFile;

/**
 * 普通 classloader 的类注入处理器
 */
public class PlainClassLoaderHandler implements ClassInjector {
    private final Logger logger = LoggerFactory.getLogger(PlainClassLoaderHandler.class.getName());
    private final boolean isDebug = logger.isDebugEnabled();

    private static final Method DEFINE_CLASS;
    private static final Method FIND_LOADED_CLASS;

    private final Map<String, List<JarReader>> pluginJarReaders;

    private static final ConcurrentMap<ClassLoader, ClassLoaderAttachment> classLoaderAttachment = new ConcurrentWeakHashMap<ClassLoader, ClassLoaderAttachment>();


    static {
        try {
            DEFINE_CLASS = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            DEFINE_CLASS.setAccessible(true);
            FIND_LOADED_CLASS = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
            FIND_LOADED_CLASS.setAccessible(true);
        } catch (Exception e) {
            throw new SimulatorException("Cannot access ClassLoader.defineClass(String, byte[], int, int)", e);
        }
    }

    private final SimulatorConfig simulatorConfig;

    public PlainClassLoaderHandler(SimulatorConfig simulatorConfig) {
        if (simulatorConfig == null) {
            throw new NullPointerException("pluginConfig must not be null");
        }
        this.simulatorConfig = simulatorConfig;
        this.pluginJarReaders = new HashMap<String, List<JarReader>>();
        for (Map.Entry<String, List<File>> entry : simulatorConfig.getBizClassLoaderInjectFiles().entrySet()) {
            List<JarReader> jarReaders = new ArrayList<JarReader>();
            for (File file : entry.getValue()) {
                JarFile jarFile = null;
                try {
                    jarFile = new JarFile(file);
                } catch (IOException e) {
                    logger.warn("SIMULATOR: Failed to load jar file {}. className:{}", file.getName(), entry.getKey());
                    continue;
                }
                jarReaders.add(new JarReader(jarFile));
            }
            this.pluginJarReaders.put(entry.getKey(), jarReaders);
        }
    }


    @Override
    @SuppressWarnings("unchecked")
    public void injectClass(ClassLoader classLoader, String className) {
        try {
            injectClass0(classLoader, className);
        } catch (Exception e) {
            logger.warn("SIMULATOR: Failed to load plugin class {} with classLoader {}", className, classLoader, e);
            throw new SimulatorException("Failed to load plugin class " + className + " with classLoader " + classLoader, e);
        }
    }

    private void injectClass0(ClassLoader classLoader, String className) throws IllegalAccessException, InvocationTargetException {
        if (isDebug) {
            logger.debug("SIMULATOR: injectClass0 className:{} cl:{}", className, classLoader);

        }
        final List<String> pluginJarPath = simulatorConfig.getBizClassLoaderURLExternalForms().get(className);
        if (pluginJarPath == null || pluginJarPath.isEmpty()) {
            return;
        }

        final ClassLoaderAttachment attachment = getClassLoaderAttachment(classLoader);

        final PluginLock pluginLock = attachment.getPluginLock(pluginJarPath);
        synchronized (pluginLock) {
            if (!pluginLock.isLoaded()) {
                pluginLock.setLoaded();
                defineJarClass(classLoader, attachment, className);
            }
        }
    }

    private ClassLoaderAttachment getClassLoaderAttachment(ClassLoader classLoader) {

        final ClassLoaderAttachment exist = classLoaderAttachment.get(classLoader);
        if (exist != null) {
            return exist;
        }
        final ClassLoaderAttachment newInfo = new ClassLoaderAttachment();
        final ClassLoaderAttachment old = classLoaderAttachment.putIfAbsent(classLoader, newInfo);
        if (old != null) {
            return old;
        }
        return newInfo;
    }

    private void defineJarClass(ClassLoader classLoader, ClassLoaderAttachment attachment, String className) {
        if (isDebug) {
            logger.debug("SIMULATOR: define Jar:{}", simulatorConfig.getBizClassLoaderInjectFiles().get(className));
        }

        List<FileBinary> fileBinaryList = readJar(this.pluginJarReaders.get(className), className);

        Map<String, SimpleClassMetadata> classEntryMap = parse(fileBinaryList);

        for (Map.Entry<String, SimpleClassMetadata> entry : classEntryMap.entrySet()) {

            final SimpleClassMetadata classMetadata = entry.getValue();
            ClassLoadingChecker classLoadingChecker = new ClassLoadingChecker();
            classLoadingChecker.isFirstLoad(classMetadata.getClassName());
            define0(classLoader, attachment, classMetadata, classEntryMap, classLoadingChecker);
        }
    }

    private List<FileBinary> readJar(List<JarReader> pluginJarReaders, String className) {
        List<FileBinary> binaries = new ArrayList<FileBinary>();
        try {
            for (JarReader reader : pluginJarReaders) {
                binaries.addAll(reader.read(ExtensionFilter.CLASS_FILTER));
            }
            return binaries;
        } catch (IOException ex) {
            throw new RuntimeException(simulatorConfig.getBizClassLoaderURLExternalForms().get(className) + " read fail." + ex.getMessage(), ex);
        }
    }

    private Map<String, SimpleClassMetadata> parse(List<FileBinary> fileBinaryList) {
        Map<String, SimpleClassMetadata> parseMap = new HashMap<String, SimpleClassMetadata>();
        for (FileBinary fileBinary : fileBinaryList) {
            SimpleClassMetadata classNode = parseClass(fileBinary);
            parseMap.put(classNode.getClassName(), classNode);
        }
        return parseMap;
    }

    private SimpleClassMetadata parseClass(FileBinary fileBinary) {
        byte[] fileBinaryArray = fileBinary.getFileBinary();
        SimpleClassMetadata classMetadata = SimpleClassMetadataReader.readSimpleClassMetadata(fileBinaryArray, fileBinary.getPath());
        return classMetadata;
    }

    private void define0(ClassLoader classLoader, ClassLoaderAttachment attachment, SimpleClassMetadata currentClass, Map<String, SimpleClassMetadata> classMetaMap, ClassLoadingChecker classLoadingChecker) {
        if ("java.lang.Object".equals(currentClass.getClassName())) {
            return;
        }
        if (attachment.containsClass(currentClass.getClassName())) {
            return;
        }

        final String superName = currentClass.getSuperClassName();
        if (isDebug) {
            logger.debug("SIMULATOR: className:{} super:{}", currentClass.getClassName(), superName);
        }
        if (!"java.lang.Object".equals(superName)) {
            if (!isSkipClass(superName, classLoadingChecker)) {
                SimpleClassMetadata superClassBinary = classMetaMap.get(superName);
                if (isDebug) {
                    logger.debug("SIMULATOR: superClass dependency define super:{} ori:{}", superClassBinary.getClassName(), currentClass.getClassName());
                }
                define0(classLoader, attachment, superClassBinary, classMetaMap, classLoadingChecker);
            }
        }

        final List<String> interfaceList = currentClass.getInterfaceNames();
        for (String interfaceName : interfaceList) {
            if (!isSkipClass(interfaceName, classLoadingChecker)) {
                SimpleClassMetadata interfaceClassBinary = classMetaMap.get(interfaceName);
                if (isDebug) {
                    logger.debug("SIMULATOR: interface dependency define interface:{} ori:{}", interfaceClassBinary.getClassName(), interfaceClassBinary.getClassName());
                }
                define0(classLoader, attachment, interfaceClassBinary, classMetaMap, classLoadingChecker);
            }
        }

        Class<?> clazz = defineClass(classLoader, currentClass);
        if (clazz != null) {
            attachment.putClass(currentClass.getClassName(), clazz);
        }

    }

    private Class<?> defineClass(ClassLoader classLoader, SimpleClassMetadata classMetadata) {
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        if (isDebug) {
            logger.debug("SIMULATOR: define class:{} cl:{}", classMetadata.getClassName(), classLoader);
        }
        // for debug
        byte[] classBytes = classMetadata.getClassBinary();
        final Integer offset = 0;
        final Integer length = classBytes.length;
        try {
            Class<?> clazz = (Class<?>) FIND_LOADED_CLASS.invoke(classLoader, classMetadata.getClassName());
            if (clazz == null) {
                clazz = (Class<?>) DEFINE_CLASS.invoke(classLoader, classMetadata.getClassName(), classBytes, offset, length);
            }
            return clazz;
        } catch (IllegalAccessException e) {
            throw handleDefineClassFail(e, classLoader, classMetadata);
        } catch (InvocationTargetException e) {
            /**
             * 判断是否有类加载不到的情况，如果有则忽略此种情况并打印日志，避免插件中写了一些无关的类而因为加载的问题导致奔溃
             */
            Throwable t = getClassNotFound(e);
            if (t != null) {
                logger.error("SIMULATOR: load class failed:{} msg:{}", classMetadata.getClassName(), t.getMessage());
            } else {
                throw handleDefineClassFail(e, classLoader, classMetadata);
            }
        } catch (NoClassDefFoundError e) {
            logger.error("SIMULATOR: load class failed:{} msg:{}", classMetadata.getClassName(), e.getMessage());
        }
        return null;
    }

    /**
     * 获取是否有classNotFound异常
     *
     * @param e
     * @return
     */
    private Throwable getClassNotFound(Throwable e) {
        if (e == null) {
            return null;
        }
        if (e instanceof ClassNotFoundException) {
            return e;
        }
        if (e instanceof NoClassDefFoundError) {
            return e;
        }
        return getClassNotFound(e.getCause());
    }

    private RuntimeException handleDefineClassFail(Throwable throwable, ClassLoader classLoader, SimpleClassMetadata classMetadata) {

        logger.warn("SIMULATOR: {} define fail classMetadata:{} cl:{} Caused by:{}", classMetadata.getClassName(), classMetadata, classLoader, throwable.getMessage(), throwable);

        return new RuntimeException(classMetadata.getClassName() + " define fail Caused by:" + throwable.getMessage(), throwable);
    }


    private boolean isSkipClass(final String className, final ClassLoadingChecker classLoadingChecker) {
        if (!classLoadingChecker.isFirstLoad(className)) {
            if (isDebug) {
                logger.debug("SIMULATOR: skip already loaded class:{}", className);
            }
            return true;
        }

        return false;
    }

    private class ClassLoaderAttachment {

        private final ConcurrentMap<List<String>, PluginLock> pluginLock = new ConcurrentHashMap<List<String>, PluginLock>();

        private final ConcurrentMap<String, Class<?>> classCache = new ConcurrentHashMap<String, Class<?>>();

        public PluginLock getPluginLock(List<String> jarFile) {
            final PluginLock exist = this.pluginLock.get(jarFile);
            if (exist != null) {
                return exist;
            }

            final PluginLock newPluginLock = new PluginLock();
            final PluginLock old = this.pluginLock.putIfAbsent(jarFile, newPluginLock);
            if (old != null) {
                return old;
            }
            return newPluginLock;
        }

        public void putClass(String className, Class<?> clazz) {
            final Class<?> duplicatedClass = this.classCache.putIfAbsent(className, clazz);
            if (duplicatedClass != null) {
                if (logger.isWarnEnabled()) {
                    logger.warn("SIMULATOR: duplicated pluginClass {}", className);
                }
            }
        }

        public Class<?> getClass(String className) {
            return this.classCache.get(className);
        }

        public boolean containsClass(String className) {
            return this.classCache.containsKey(className);
        }
    }

    private static class PluginLock {

        private boolean loaded = false;

        public boolean isLoaded() {
            return this.loaded;
        }

        public void setLoaded() {
            this.loaded = true;
        }

    }

}
