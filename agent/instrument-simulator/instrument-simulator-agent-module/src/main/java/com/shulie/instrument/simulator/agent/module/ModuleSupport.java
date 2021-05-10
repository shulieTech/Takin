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
package com.shulie.instrument.simulator.agent.module;


import com.shulie.instrument.simulator.agent.module.exception.ModuleException;
import com.shulie.instrument.simulator.message.boot.util.JvmUtils;
import com.shulie.instrument.simulator.message.boot.version.JvmVersion;
import jdk.internal.module.Modules;

import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 模块支持类，包含对模块支持的一些通用操作集合
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/12/9 8:23 下午
 */
public class ModuleSupport {
    private final static String MODULE_NAME = "simulator.agent";

    private final Instrumentation instrumentation;

    private final ModuleLogger logger = ModuleLogger.getLogger(this.getClass().getName());

    private final JavaModule javaBaseModule;
    private final JavaModule bootstrapModule;
    private final List<String> allowedProviders;

    ModuleSupport(Instrumentation instrumentation, List<String> allowedProviders) {
        if (instrumentation == null) {
            throw new NullPointerException("instrumentation");
        }
        if (allowedProviders == null) {
            throw new NullPointerException("allowedProviders");
        }
        this.instrumentation = instrumentation;
        this.javaBaseModule = wrapJavaModule(Object.class);
        this.bootstrapModule = wrapJavaModule(this.getClass());
        this.allowedProviders = allowedProviders;

    }

    /**
     * 初始化模块的一些参数
     */
    public void init() {
        JavaModule bootstrapModule = getBootstrapModule();
        JavaModule baseModule = getJavaBaseModule();
        baseModule.addExports("jdk.internal.loader", bootstrapModule);
        baseModule.addExports("jdk.internal.misc", bootstrapModule);
        baseModule.addExports("jdk.internal.module", bootstrapModule);

    }

    /**
     * 定义 agent 模块
     *
     * @param classLoader
     * @param jarFileList
     */
    public void defineAgentModule(ClassLoader classLoader, URL[] jarFileList) {
        if (isExistModule(MODULE_NAME)) {
            return;
        }

        final JavaModule agentModule = newAgentModule(classLoader, jarFileList);

        prepareAgentModule(classLoader, agentModule);

        addPermissionToLog4jModule(agentModule);
        addPermissionToGuiceModule(agentModule);

    }

    /**
     * 新建 Agent 模块
     *
     * @param classLoader
     * @param jarFileList
     * @return
     */
    private JavaModule newAgentModule(ClassLoader classLoader, URL[] jarFileList) {
        ModuleBuilder moduleBuilder = new ModuleBuilder();
        final Module agentModule = moduleBuilder.defineModule(MODULE_NAME, classLoader, jarFileList);
        return wrapJavaModule(agentModule);
    }

    /**
     * 判断某个模块是否存在
     *
     * @param moduleName 模块名称
     * @return
     */
    private boolean isExistModule(String moduleName) {
        ModuleBuilder moduleBuilder = new ModuleBuilder();
        return moduleBuilder.isExistsModule(moduleName);
    }

    /**
     * 添加日志模块的授权
     *
     * @param agentModule
     */
    private void addPermissionToLog4jModule(JavaModule agentModule) {
        JavaModule xmlModule = loadModule("java.xml");
        agentModule.addReads(xmlModule);
        JavaModule desktopModule = loadModule("java.desktop");
        agentModule.addReads(desktopModule);
    }

    /**
     * 添加 guice 模块的授权
     *
     * @param agentModule
     */
    private void addPermissionToGuiceModule(JavaModule agentModule) {
        JavaModule loggingModule = loadModule("java.logging");
        agentModule.addReads(loggingModule);

        JavaModule javaBaseModule = getJavaBaseModule();
        javaBaseModule.addOpens("java.lang", agentModule);
    }

    /**
     * agent 模块准备
     *
     * @param classLoader
     * @param agentModule
     */
    private void prepareAgentModule(final ClassLoader classLoader, JavaModule agentModule) {
        JavaModule bootstrapModule = getBootstrapModule();
        agentModule.addReads(bootstrapModule);
        JavaModule prefsModule = loadModule("java.prefs");
        agentModule.addReads(prefsModule);

        JavaModule baseModule = getJavaBaseModule();
        baseModule.addOpens("java.net", agentModule);
        baseModule.addOpens("java.nio", agentModule);
        baseModule.addOpens("java.io", agentModule);
        baseModule.addOpens("java.math", agentModule);
        baseModule.addOpens("java.security", agentModule);
        baseModule.addOpens("java.text", agentModule);
        baseModule.addOpens("java.time", agentModule);
        baseModule.addOpens("java.util", agentModule);

        // for Java9DefineClass
        baseModule.addExports("jdk.internal.misc", agentModule);
        baseModule.addExports("jdk.internal.module", agentModule);
        final JvmVersion version = JvmUtils.getVersion();
        if (version.onOrAfter(JvmVersion.JAVA_11)) {
            final String internalAccessModule = "jdk.internal.access";
            if (baseModule.getPackages().contains(internalAccessModule)) {
                baseModule.addExports(internalAccessModule, agentModule);
            } else {
                logger.info(internalAccessModule + " package not found");
            }
        }

        agentModule.addReads(baseModule);

        addExportsToUnnamed(baseModule, "jdk.internal.module", null);
        final JavaModule instrumentModule = loadModule("java.instrument");
        agentModule.addReads(instrumentModule);

        final JavaModule managementModule = loadModule("java.management");
        agentModule.addReads(managementModule);

        final JavaModule jdkManagement = loadModule("jdk.management");
        agentModule.addReads(jdkManagement);

        final JavaModule jdkUnsupported = loadModule("jdk.unsupported");
        agentModule.addReads(jdkUnsupported);

        final JavaModule javaSql = loadModule("java.sql");
        agentModule.addReads(javaSql);

        Class<?> extensionModuleClass = forName("com.shulie.instrument.simulator.api.ExtensionModule", classLoader);
        agentModule.addUses(extensionModuleClass);

        Class<?> moduleJarLoadingChainClass = forName("com.shulie.instrument.simulator.spi.ModuleJarLoadingChain", classLoader);
        agentModule.addUses(moduleJarLoadingChainClass);

        Class<?> moduleLoadingChainClass = forName("com.shulie.instrument.simulator.spi.ModuleLoadingChain", classLoader);
        agentModule.addUses(moduleLoadingChainClass);

        Class<?> moduleJarUnLoadSpiClass = forName("com.shulie.instrument.simulator.api.spi.ModuleJarUnLoadSpi", classLoader);
        agentModule.addUses(moduleJarUnLoadSpiClass);

        Class<?> simulatorLifecycleSpiClass = forName("com.shulie.instrument.simulator.spi.SimulatorLifecycle", classLoader);
        agentModule.addUses(simulatorLifecycleSpiClass);

        agentModule.addUses(forName("ch.qos.logback.classic.spi.Configurator", classLoader));

        addExportsToUnnamed(agentModule, "com.shulie.instrument.simulator.core", this.getClass().getClassLoader());
        addExportsToUnnamed(agentModule, "com.shulie.instrument.simulator.core.server", this.getClass().getClassLoader());
        addExportsToUnnamed(agentModule, "com.shulie.instrument.simulator.api.spi", this.getClass().getClassLoader());
        addExportsToUnnamed(agentModule, "com.shulie.instrument.simulator.api", this.getClass().getClassLoader());

        addModuleToReadsUnnamedModule(agentModule);

        List<Providers> providersList = agentModule.getProviders();
        for (Providers providers : providersList) {
            final String service = providers.getService();
            if (isAllowedProvider(service)) {
                logger.info("load provider:" + providers);
                Class<?> serviceClass = forName(providers.getService(), classLoader);
                List<Class<?>> providerClassList = loadProviderClassList(providers.getProviders(), classLoader);
                agentModule.addProvides(serviceClass, providerClassList);
            }
        }
    }

    /**
     * 给定指模块添加未命令模块的读权限
     *
     * @param agentModule
     */
    private void addModuleToReadsUnnamedModule(JavaModule agentModule) {
        agentModule.addReads(wrapJavaModule(ClassLoader.getSystemClassLoader().getUnnamedModule()));
        agentModule.addReads(wrapJavaModule(ClassLoader.getPlatformClassLoader().getUnnamedModule()));
        agentModule.addReads(wrapJavaModule(jdk.internal.loader.BootLoader.getUnnamedModule()));
    }

    /**
     * 添加指定包对未命名模块的导出
     *
     * @param javaModule
     * @param packageName
     * @param classLoader
     */
    private void addExportsToUnnamed(JavaModule javaModule, String packageName, ClassLoader classLoader) {
        javaModule.addExports(packageName, wrapJavaModule(jdk.internal.loader.BootLoader.getUnnamedModule()));
        javaModule.addExports(packageName, wrapJavaModule(ClassLoader.getPlatformClassLoader().getUnnamedModule()));
        javaModule.addExports(packageName, wrapJavaModule(ClassLoader.getSystemClassLoader().getUnnamedModule()));
        if (classLoader != null && classLoader != ClassLoader.getPlatformClassLoader() && classLoader != ClassLoader.getSystemClassLoader()) {
            javaModule.addExports(packageName, wrapJavaModule(classLoader.getUnnamedModule()));
        }

    }


    /**
     * 是否允许此 Provider
     *
     * @param serviceName service 名称
     * @return true|false
     */
    public boolean isAllowedProvider(String serviceName) {
        return allowedProviders.contains(serviceName);
    }

    /**
     * 加载所有的 Provider 类
     *
     * @param classNameList 类名列表
     * @param classLoader   类加载器
     * @return 所有的 Provider 类
     */
    private List<Class<?>> loadProviderClassList(List<String> classNameList, ClassLoader classLoader) {
        List<Class<?>> providerClassList = new ArrayList<>();
        for (String providerClassName : classNameList) {
            Class<?> providerClass = forName(providerClassName, classLoader);
            providerClassList.add(providerClass);
        }
        return providerClassList;
    }

    /**
     * 根据类名和类加载器加载类
     *
     * @param className
     * @param classLoader
     * @return
     */
    private Class<?> forName(String className, ClassLoader classLoader) {
        try {
            return Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            throw new ModuleException(className + " not found Caused by:" + e.getMessage(), e);
        }
    }

    /**
     * 根据模块名加载模块
     *
     * @param moduleName 模块名称
     * @return
     */
    private JavaModule loadModule(String moduleName) {
        logger.info("loadModule:" + moduleName);
        final Module module = Modules.loadModule(moduleName);
        return wrapJavaModule(module);
    }

    /**
     * 根据类包装成模块
     *
     * @param clazz
     * @return
     */
    private JavaModule wrapJavaModule(Class clazz) {
        return new Java9Module(instrumentation, clazz.getModule());
    }

    /**
     * 包装成自定义的 JavaModule
     *
     * @param module 模块
     * @return JavaModule
     */
    private JavaModule wrapJavaModule(Module module) {
        return new Java9Module(instrumentation, module);
    }

    /**
     * 获取 java base 模块
     *
     * @return
     */
    private JavaModule getJavaBaseModule() {
        return javaBaseModule;
    }

    /**
     * 获取 agent boot 模块
     *
     * @return
     */
    private JavaModule getBootstrapModule() {
        return bootstrapModule;
    }

}
