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

import com.shulie.instrument.simulator.api.*;
import com.shulie.instrument.simulator.api.instrument.EnhanceTemplate;
import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;
import com.shulie.instrument.simulator.api.resource.*;
import com.shulie.instrument.simulator.compatible.transformer.SimulatorClassFileTransformer;
import com.shulie.instrument.simulator.core.CoreConfigure;
import com.shulie.instrument.simulator.core.CoreModule;
import com.shulie.instrument.simulator.core.classloader.ClassLoaderFactory;
import com.shulie.instrument.simulator.core.classloader.ClassLoaderService;
import com.shulie.instrument.simulator.core.classloader.ModuleClassLoader;
import com.shulie.instrument.simulator.core.classloader.impl.ClassLoaderFactoryImpl;
import com.shulie.instrument.simulator.core.enhance.weaver.EventListenerHandler;
import com.shulie.instrument.simulator.core.inject.ClassInjector;
import com.shulie.instrument.simulator.core.inject.impl.ModuleJarClassInjector;
import com.shulie.instrument.simulator.core.instrument.DefaultEnhanceTemplate;
import com.shulie.instrument.simulator.core.manager.CoreLoadedClassDataSource;
import com.shulie.instrument.simulator.core.manager.CoreModuleManager;
import com.shulie.instrument.simulator.core.manager.ModuleLoaders;
import com.shulie.instrument.simulator.core.manager.ProviderManager;
import com.shulie.instrument.simulator.core.util.ModuleSpecUtils;
import com.shulie.instrument.simulator.core.util.SimulatorGuard;
import com.shulie.instrument.simulator.core.util.VersionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import static com.shulie.instrument.simulator.api.ModuleException.ErrorCode.*;
import static com.shulie.instrument.simulator.core.manager.impl.DefaultCoreModuleManager.ModuleLifeCycleType.*;
import static org.apache.commons.lang.reflect.FieldUtils.writeField;

/**
 * 默认的模块管理实现
 */
public class DefaultCoreModuleManager implements CoreModuleManager {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final CoreConfigure config;
    private final Instrumentation inst;
    private ClassFileTransformer defaultClassFileTransformer;
    private final ClassInjector classInjector;
    private final CoreLoadedClassDataSource classDataSource;
    private final ProviderManager providerManager;

    /**
     * 系统模块目录列表
     */
    private final File[] systemModuleLibs;

    /**
     * 用户模块目录列表
     */
    private final File[] userModuleLibs;

    /**
     * 类加载器服务
     */
    private ClassLoaderService classLoaderService;

    /**
     * 仿真器配置
     */
    private SimulatorConfig simulatorConfig;

    /**
     * 模块命令调用器
     */
    private ModuleCommandInvoker moduleCommandInvoker;

    /**
     * 禁用的模块列表
     */
    private final List<String> disabledModules;

    // 已加载的模块集合
    private final Map<String, CoreModule> loadedModuleMap = new ConcurrentHashMap<String, CoreModule>();

    /**
     * 所有等待加载的模块
     */
    private Queue<Runnable> waitLoadModules = new ConcurrentLinkedQueue<Runnable>();

    /**
     * 模块模块管理
     *
     * @param config          模块核心配置
     * @param inst            inst
     * @param classDataSource 已加载类数据源
     * @param providerManager 服务提供者管理器
     */
    public DefaultCoreModuleManager(final CoreConfigure config,
                                    final Instrumentation inst,
                                    final CoreLoadedClassDataSource classDataSource,
                                    final ProviderManager providerManager,
                                    final ClassLoaderService classLoaderService) {
        this.config = config;
        this.simulatorConfig = new DefaultSimulatorConfig(config);
        this.inst = inst;
        this.classDataSource = classDataSource;
        this.providerManager = providerManager;
        this.systemModuleLibs = getSystemModuleLibFiles(config.getSystemModuleLibPath());

        this.userModuleLibs = ModuleLoaders.getModuleLoader(config.getModuleRepositoryMode()).loadModuleLibs(config.getAppName(), config.getUserModulePaths());
        this.classLoaderService = classLoaderService;
        this.moduleCommandInvoker = new DefaultModuleCommandInvoker(this);
        this.disabledModules = config.getDisabledModules();
        this.classInjector = new ModuleJarClassInjector(this.simulatorConfig);
    }

    @Override
    public void onStartup() {
        this.providerManager.onStart(config.getNamespace(), simulatorConfig);
        /**
         * 针对需要开放给业务类加载器的类文件转换器，这个类文件转换器中负责对业务类加载器进行 jar 包的注入
         */
        this.defaultClassFileTransformer = new InternalClassFileTransformer(classInjector);
        this.inst.addTransformer(this.defaultClassFileTransformer);

        /**
         * 如果已经加载的类则直接执行注入操作
         */
        for (Map.Entry<String, List<File>> entry : simulatorConfig.getBizClassLoaderInjectFiles().entrySet()) {
            List<Class<?>> classes = classDataSource.findForReTransform(entry.getKey());
            if (CollectionUtils.isNotEmpty(classes)) {
                for (Class<?> clazz : classes) {
                    classInjector.injectClass(clazz.getClassLoader(), entry.getKey());
                }
            }
        }
    }

    @Override
    public void onShutdown() {
        this.providerManager.onShutdown(config.getNamespace(), simulatorConfig);
        this.inst.removeTransformer(this.defaultClassFileTransformer);
    }

    /**
     * 获取系统模块加载文件/目录(集合)
     *
     * @param path 路径
     * @return 用户模块加载文件/目录(集合)
     */
    public synchronized File[] getSystemModuleLibFiles(String path) {
        if (StringUtils.isBlank(path)) {
            return new File[0];
        }
        final Collection<File> foundModuleJarFiles = new LinkedHashSet<File>();
        final File fileOfPath = new File(path);
        if (fileOfPath.isDirectory()) {
            foundModuleJarFiles.addAll(FileUtils.listFiles(new File(path), new String[]{"jar"}, true));
        } else {
            if (StringUtils.endsWithIgnoreCase(fileOfPath.getPath(), ".jar")) {
                foundModuleJarFiles.add(fileOfPath);
            }
        }

        return foundModuleJarFiles.toArray(new File[]{});
    }

    /**
     * 通知模块生命周期
     *
     * @param coreModule 内核模块
     * @param eventType  模块生命周期事件类型
     * @throws ModuleException 当触发模块生命周期出现错误时抛出模块异常
     */
    private void callAndFireModuleLifeCycle(final CoreModule coreModule, final ModuleLifeCycleType eventType) throws ModuleException {
        /**
         * 查看是否配置指定了模块不启用，如果指定了则直接忽略
         */
        final String moduleId = coreModule.getUniqueId();
        if (!coreModule.getSimulatorConfig().getBooleanProperty(moduleId + ".enabled", true)) {
            return;
        }

        if (coreModule.getModule() instanceof ModuleLifecycle) {
            final ModuleLifecycle moduleLifecycle = (ModuleLifecycle) coreModule.getModule();
            switch (eventType) {

                case MODULE_LOAD: {
                    try {
                        moduleLifecycle.onLoad();
                    } catch (Throwable throwable) {
                        throw new ModuleException(moduleId, MODULE_LOAD_ERROR, throwable);
                    }
                    break;
                }

                case MODULE_UNLOAD: {
                    try {
                        moduleLifecycle.onUnload();
                    } catch (Throwable throwable) {
                        throw new ModuleException(coreModule.getUniqueId(), MODULE_UNLOAD_ERROR, throwable);
                    }
                    break;
                }

                case MODULE_ACTIVE: {
                    try {
                        moduleLifecycle.onActive();
                        //发布事件
                        GlobalSwitch.switchOn(coreModule.getUniqueId());
                    } catch (Throwable throwable) {
                        throw new ModuleException(coreModule.getUniqueId(), MODULE_ACTIVE_ERROR, throwable);
                    }
                    break;
                }

                case MODULE_FROZEN: {
                    try {
                        moduleLifecycle.onFrozen();
                        GlobalSwitch.switchOff(coreModule.getUniqueId());
                    } catch (Throwable throwable) {
                        throw new ModuleException(coreModule.getUniqueId(), MODULE_FROZEN_ERROR, throwable);
                    }
                    break;
                }

            }// switch
        }

        // 这里要对LOAD_COMPLETED事件做特殊处理
        // 因为这个事件处理失败不会影响模块变更行为，只做简单的日志处理
        if (eventType == MODULE_LOAD_COMPLETED
                && coreModule.getModule() instanceof LoadCompleted) {
            try {
                ((LoadCompleted) coreModule.getModule()).loadCompleted();
            } catch (Throwable cause) {
                logger.warn("SIMULATOR: loading module occur error when load-completed. module={};", coreModule.getUniqueId(), cause);
            }
        }

    }

    /**
     * 加载并注册模块
     * <p>1. 如果模块已经存在则返回已经加载过的模块</p>
     * <p>2. 如果模块不存在，则进行常规加载</p>
     * <p>3. 如果模块初始化失败，则抛出异常</p>
     *
     * @param module             模块对象
     * @param moduleJarFile      模块所在JAR文件
     * @param classLoaderFactory 负责加载模块的ClassLoader工厂
     * @throws ModuleException 加载模块失败
     */
    private synchronized void load(final ModuleSpec moduleSpec,
                                   final ExtensionModule module,
                                   final File moduleJarFile,
                                   final ClassLoaderFactory classLoaderFactory) throws ModuleException {

        if (loadedModuleMap.containsKey(moduleSpec.getModuleId())) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: module already loaded. module={};", moduleSpec);
            }
            return;
        }

        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: loading module, module={};class={};module-jar={};",
                    moduleSpec,
                    module.getClass().getName(),
                    moduleJarFile
            );
        }

        // 初始化模块信息
        final CoreModule coreModule = new CoreModule(moduleSpec, moduleJarFile, classLoaderFactory, module, this);

        /**
         * 注入CoreModule需要的资源
         */
        injectCoreModule(coreModule);
        // 注入@Resource资源
        injectResource(coreModule.getModule(), coreModule);

        callAndFireModuleLifeCycle(coreModule, MODULE_LOAD);

        // 设置为已经加载
        coreModule.markLoaded(true);

        // 如果模块标记了加载时自动激活，则需要在加载完成之后激活模块
        markActiveOnLoadIfNecessary(coreModule);

        // 注册到模块列表中
        loadedModuleMap.put(moduleSpec.getModuleId(), coreModule);

        // 通知生命周期，模块加载完成
        callAndFireModuleLifeCycle(coreModule, MODULE_LOAD_COMPLETED);

    }

    private void injectCoreModule(final CoreModule coreModule) {
        coreModule.setCoreLoadedClassDataSource(classDataSource);
        final ModuleEventWatcher moduleEventWatcher = coreModule.append(
                new ReleaseResource<ModuleEventWatcher>(
                        SimulatorGuard.getInstance().doGuard(
                                ModuleEventWatcher.class,
                                new DefaultModuleEventWatcher(inst, classDataSource, coreModule, config.isEnableUnsafe(), config.getNamespace())
                        )
                ) {
                    @Override
                    public void release() {
                        if (logger.isInfoEnabled()) {
                            logger.info("SIMULATOR: release all SimulatorClassFileTransformer for module={}", coreModule.getUniqueId());
                        }
                        final ModuleEventWatcher moduleEventWatcher = get();
                        if (null != moduleEventWatcher) {
                            for (final SimulatorClassFileTransformer simulatorClassFileTransformer
                                    : new ArrayList<SimulatorClassFileTransformer>(coreModule.getSimulatorClassFileTransformers())) {
                                moduleEventWatcher.delete(simulatorClassFileTransformer.getWatchId());
                            }
                        }
                        moduleEventWatcher.close();
                    }
                });
        coreModule.setModuleEventWatcher(moduleEventWatcher);
        coreModule.setModuleController(new DefaultModuleController(coreModule, this));
        coreModule.setObjectManager(new DefaultObjectManager(simulatorConfig.getInstrumentation()));
        coreModule.setModuleManager(new DefaultModuleManager(this));
        coreModule.setSimulatorConfig(simulatorConfig);
        coreModule.setEnhanceTemplate(new DefaultEnhanceTemplate(moduleEventWatcher));
        coreModule.setClassInjector(new ModuleJarClassInjector(coreModule.getSimulatorConfig()));
        coreModule.setDynamicFieldManager(new DefaultDynamicFieldManager(coreModule.getUniqueId()));
    }

    private static List<Field> getFieldsListWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            for (final Field field : declaredFields) {
                if (field.getAnnotation(annotationCls) != null) {
                    allFields.add(field);
                }
            }
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    @Override
    public void injectResource(final Object target, CoreModule coreModule) throws ModuleException {
        if (target == null) {
            return;
        }
        if (target instanceof ModuleLifecycleAdapter) {
            ((ModuleLifecycleAdapter) target).setModuleName(coreModule.getUniqueId());
        }
        try {
            for (final Field resourceField : getFieldsListWithAnnotation(target.getClass(), Resource.class)) {
                final Class<?> fieldType = resourceField.getType();

                // LoadedClassDataSource对象注入
                if (LoadedClassDataSource.class.isAssignableFrom(fieldType)) {
                    writeField(
                            resourceField,
                            target,
                            coreModule.getCoreLoadedClassDataSource(),
                            true
                    );
                }

                // ModuleEventWatcher对象注入
                else if (ModuleEventWatcher.class.isAssignableFrom(fieldType)) {
                    writeField(
                            resourceField,
                            target,
                            coreModule.getModuleEventWatcher(),
                            true
                    );
                }

                // ModuleController对象注入
                else if (ModuleController.class.isAssignableFrom(fieldType)) {
                    writeField(
                            resourceField,
                            target,
                            coreModule.getModuleController(),
                            true
                    );
                }

                // ModuleManager对象注入
                else if (ModuleManager.class.isAssignableFrom(fieldType)) {
                    writeField(
                            resourceField,
                            target,
                            coreModule.getModuleManager(),
                            true
                    );
                }

                // SimulatorConfig注入
                else if (SimulatorConfig.class.isAssignableFrom(fieldType)) {
                    writeField(
                            resourceField,
                            target,
                            coreModule.getSimulatorConfig(),
                            true
                    );
                }
                //EnhanceTemplate注入
                else if (EnhanceTemplate.class.isAssignableFrom(fieldType)) {
                    writeField(
                            resourceField,
                            target,
                            coreModule.getEnhanceTemplate(),
                            true
                    );
                }
                // ModuleCommandInvoker 注入
                else if (ModuleCommandInvoker.class.isAssignableFrom(fieldType)) {
                    writeField(resourceField, target, moduleCommandInvoker, true);
                }
                // ObjectManager 注入
                else if (ObjectManager.class.isAssignableFrom(fieldType)) {
                    writeField(resourceField, target, coreModule.getObjectManager(), true);
                }
                // DynamicFieldManager 注入
                else if (DynamicFieldManager.class.isAssignableFrom(fieldType)) {
                    writeField(resourceField, target, coreModule.getDynamicFieldManager(), true);
                }
                // 其他情况需要输出日志警告
                else {
                    logger.warn("SIMULATOR: module inject @Resource ignored: field not found. module={};class={};type={};field={};",
                            coreModule.getUniqueId(),
                            coreModule.getModule().getClass().getName(),
                            fieldType.getName(),
                            resourceField.getName()
                    );
                }

            }
        } catch (IllegalAccessException cause) {
            throw new ModuleException(coreModule.getUniqueId(), MODULE_LOAD_ERROR, cause);
        }
    }

    private void markActiveOnLoadIfNecessary(final CoreModule coreModule) throws ModuleException {
        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: active module when OnLoad, module={}", coreModule.getUniqueId());
        }
        if (coreModule.isActiveOnLoad()) {
            active(coreModule);
        }
    }

    /**
     * 卸载并删除注册模块
     * <p>1. 如果模块原本就不存在，则幂等此次操作</p>
     * <p>2. 如果模块存在则尝试进行卸载</p>
     * <p>3. 卸载模块之前会尝试冻结该模块</p>
     *
     * @param coreModule              等待被卸载的模块
     * @param isIgnoreModuleException 是否忽略模块异常
     * @throws ModuleException 卸载模块失败
     */
    @Override
    public synchronized CoreModule unload(final CoreModule coreModule,
                                          final boolean isIgnoreModuleException) throws ModuleException {

        if (!coreModule.isLoaded()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: module already unLoaded. module={};", coreModule.getUniqueId());
            }
            return coreModule;
        }

        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: unloading module, module={};class={};",
                    coreModule.getUniqueId(),
                    coreModule.getModule().getClass().getName()
            );
        }

        // 尝试冻结模块
        frozen(coreModule, isIgnoreModuleException);

        // 通知生命周期
        try {
            callAndFireModuleLifeCycle(coreModule, MODULE_UNLOAD);
        } catch (ModuleException meCause) {
            if (isIgnoreModuleException) {
                logger.warn("SIMULATOR: unload module occur error, ignored. module={};class={};code={};",
                        meCause.getUniqueId(),
                        coreModule.getModule().getClass().getName(),
                        meCause.getErrorCode(),
                        meCause
                );
            } else {
                throw meCause;
            }
        }

        // 从模块注册表中删除
        loadedModuleMap.remove(coreModule.getUniqueId());
        // 标记模块为：已卸载
        coreModule.markLoaded(false);

        // 释放所有可释放资源
        coreModule.releaseAll();

        //卸载所有导出的资源
        classLoaderService.unload(coreModule.getUniqueId(), coreModule.getClassLoaderFactory());

        // 尝试关闭ClassLoader
        closeModuleJarClassLoaderIfNecessary(coreModule.getClassLoaderFactory());

        return coreModule;
    }

    @Override
    public void unloadAll() {
        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: force unloading all loaded modules:{}", loadedModuleMap.keySet());
        }
        //先卸载所有有开关依赖的模块
        List<CoreModule> modules = new ArrayList<CoreModule>(loadedModuleMap.values());
        Iterator<CoreModule> it = modules.iterator();
        //先卸载无导出的模块
        while (it.hasNext()) {
            CoreModule coreModule = it.next();
            if (CollectionUtils.isEmpty(coreModule.getModuleSpec().getExportPackages())
                    && CollectionUtils.isEmpty(coreModule.getModuleSpec().getExportPrefixPackages())
                    && CollectionUtils.isEmpty(coreModule.getModuleSpec().getExportSuffixPackages())
                    && CollectionUtils.isEmpty(coreModule.getModuleSpec().getExportExactlyPackages())
                    && CollectionUtils.isEmpty(coreModule.getModuleSpec().getExportClasses())
                    && CollectionUtils.isEmpty(coreModule.getModuleSpec().getExportResources())
                    && CollectionUtils.isEmpty(coreModule.getModuleSpec().getExportExactlyResources())
                    && CollectionUtils.isEmpty(coreModule.getModuleSpec().getExportPrefixResources())
                    && CollectionUtils.isEmpty(coreModule.getModuleSpec().getExportSuffixResources())
            ) {
                try {
                    unload(coreModule, true);
                } catch (ModuleException cause) {
                    // 强制卸载不可能出错，这里不对外继续抛出任何异常
                    logger.warn("SIMULATOR: force unloading module occur error! module={};", coreModule.getUniqueId(), cause);
                }
                it.remove();
            }
        }

        //再卸载有导入的模块
        while (it.hasNext()) {
            CoreModule coreModule = it.next();
            if (CollectionUtils.isNotEmpty(coreModule.getModuleSpec().getImportPackages())
                    && CollectionUtils.isNotEmpty(coreModule.getModuleSpec().getImportPrefixPackages())
                    && CollectionUtils.isNotEmpty(coreModule.getModuleSpec().getImportSuffixPackages())
                    && CollectionUtils.isNotEmpty(coreModule.getModuleSpec().getImportExactlyPackages())
                    && CollectionUtils.isNotEmpty(coreModule.getModuleSpec().getImportClasses())
                    && CollectionUtils.isNotEmpty(coreModule.getModuleSpec().getImportResources())
                    && CollectionUtils.isNotEmpty(coreModule.getModuleSpec().getImportExactlyResources())
                    && CollectionUtils.isNotEmpty(coreModule.getModuleSpec().getImportPrefixResources())
                    && CollectionUtils.isNotEmpty(coreModule.getModuleSpec().getImportSuffixResources())
            ) {
                try {
                    unload(coreModule, true);
                } catch (ModuleException cause) {
                    // 强制卸载不可能出错，这里不对外继续抛出任何异常
                    logger.warn("SIMULATOR: force unloading module occur error! module={};", coreModule.getUniqueId(), cause);
                }
                it.remove();
            }
        }

        //再卸载所有的用户自定义模块
        while (it.hasNext()) {
            CoreModule coreModule = it.next();
            if (!coreModule.getModuleSpec().isSystemModule()) {
                try {
                    unload(coreModule, true);
                } catch (ModuleException cause) {
                    // 强制卸载不可能出错，这里不对外继续抛出任何异常
                    logger.warn("SIMULATOR: force unloading module occur error! module={};", coreModule.getUniqueId(), cause);
                }
                it.remove();
            }
        }


        // 强制卸载所有模块
        for (final CoreModule coreModule : modules) {
            try {
                unload(coreModule, true);
            } catch (ModuleException cause) {
                // 强制卸载不可能出错，这里不对外继续抛出任何异常
                logger.warn("SIMULATOR: force unloading module occur error! module={};", coreModule.getUniqueId(), cause);
            }
        }

    }

    @Override
    public ClassLoaderService getClassLoaderService() {
        return classLoaderService;
    }

    @Override
    public synchronized void active(final CoreModule coreModule) throws ModuleException {

        // 如果模块已经被激活，则直接幂等返回
        if (coreModule.isActivated()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: module already activated. module={};", coreModule.getUniqueId());
            }
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: active module, module={};class={};module-jar={};",
                    coreModule.getUniqueId(),
                    coreModule.getModule().getClass().getName(),
                    coreModule.getJarFile()
            );
        }

        // 通知生命周期
        callAndFireModuleLifeCycle(coreModule, MODULE_ACTIVE);

        // 激活所有监听器
        for (final SimulatorClassFileTransformer simulatorClassFileTransformer : coreModule.getSimulatorClassFileTransformers()) {
            List<BuildingForListeners> list = simulatorClassFileTransformer.getAllListeners();
            if (CollectionUtils.isNotEmpty(list)) {
                for (BuildingForListeners buildingForListeners : list) {
                    EventListenerHandler.getSingleton().active(
                            buildingForListeners.getListenerId(),
                            simulatorClassFileTransformer.getEventListeners().get(buildingForListeners.getListenerId()),
                            buildingForListeners.getEventTypes()
                    );
                }
            }


        }

        // 标记模块为：已激活
        coreModule.markActivated(true);
    }

    @Override
    public synchronized void frozen(final CoreModule coreModule,
                                    final boolean isIgnoreModuleException) throws ModuleException {

        // 如果模块已经被冻结(尚未被激活)，则直接幂等返回
        if (!coreModule.isActivated()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: module already frozen. module={};", coreModule.getUniqueId());
            }
            return;
        }

        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: frozen module, module={};class={};module-jar={};",
                    coreModule.getUniqueId(),
                    coreModule.getModule().getClass().getName(),
                    coreModule.getJarFile()
            );
        }

        // 通知生命周期
        try {
            callAndFireModuleLifeCycle(coreModule, MODULE_FROZEN);
        } catch (ModuleException meCause) {
            if (isIgnoreModuleException) {
                logger.warn("SIMULATOR: frozen module occur error, ignored. module={};class={};code={};",
                        meCause.getUniqueId(),
                        coreModule.getModule().getClass().getName(),
                        meCause.getErrorCode(),
                        meCause
                );
            } else {
                throw meCause;
            }
        }

        // 冻结所有监听器
        for (final SimulatorClassFileTransformer simulatorClassFileTransformer : coreModule.getSimulatorClassFileTransformers()) {
            for (BuildingForListeners buildingForListeners : simulatorClassFileTransformer.getAllListeners()) {
                EventListenerHandler.getSingleton()
                        .frozen(buildingForListeners.getListenerId());
            }

        }

        // 标记模块为：已冻结
        coreModule.markActivated(false);
    }

    @Override
    public Collection<CoreModule> list() {
        return loadedModuleMap.values();
    }

    @Override
    public CoreModule get(String uniqueId) {
        return loadedModuleMap.get(uniqueId);
    }

    @Override
    public CoreModule getThrowsExceptionIfNull(String uniqueId) throws ModuleException {
        final CoreModule coreModule = get(uniqueId);
        if (null == coreModule) {
            throw new ModuleException(uniqueId, MODULE_NOT_EXISTED);
        }
        return coreModule;
    }


    private boolean isOptimisticDirectoryContainsFile(final File directory,
                                                      final File child) {
        try {
            return directoryContains(directory, child);
        } catch (IOException cause) {
            // 如果这里能抛出异常，则说明directory或者child发生损坏
            // 需要返回TRUE以此作乐观推断，出错的情况也属于当前目录
            // 这个逻辑没毛病,主要是用来应对USER目录被删除引起IOException的情况
            logger.warn("SIMULATOR: occur OptimisticDirectoryContainsFile: directory={} or child={} maybe broken.", directory, child, cause);
            return true;
        }
    }

    public static boolean directoryContains(final File directory, final File child) throws IOException {

        // Fail fast against NullPointerException
        if (directory == null) {
            throw new IllegalArgumentException("Directory must not be null");
        }

        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("Not a directory: " + directory);
        }

        if (child == null) {
            return false;
        }

        if (!directory.exists() || !child.exists()) {
            return false;
        }

        // Canonicalize paths (normalizes relative paths)
        final String canonicalParent = directory.getCanonicalPath();
        final String canonicalChild = child.getCanonicalPath();

        return directoryContains(canonicalParent, canonicalChild);
    }

    public static boolean directoryContains(final String canonicalParent, final String canonicalChild)
            throws IOException {

        // Fail fast against NullPointerException
        if (canonicalParent == null) {
            throw new IllegalArgumentException("Directory must not be null");
        }

        if (canonicalChild == null) {
            return false;
        }

        if (IOCase.SYSTEM.checkEquals(canonicalParent, canonicalChild)) {
            return false;
        }

        return IOCase.SYSTEM.checkStartsWith(canonicalChild, canonicalParent);
    }

    private boolean isSystemModule(final File child) {
        return isOptimisticDirectoryContainsFile(new File(config.getSystemModuleLibPath()), child);
    }

    /**
     * 用户模块文件加载回调
     */
    final private class InnerModuleJarLoadCallback implements ModuleLibLoader.ModuleJarLoadCallback {
        @Override
        public void onLoad(final SimulatorConfig simulatorConfig, final ModuleSpec moduleSpec, final File moduleJarFile) throws Throwable {
            providerManager.loading(simulatorConfig, moduleSpec, moduleJarFile);
        }
    }

    /**
     * 用户模块加载回调
     */
    final private class InnerModuleLoadCallback implements ModuleJarLoader.ModuleLoadCallback {
        @Override
        public void onLoad(final SimulatorConfig simulatorConfig,
                           final ModuleSpec moduleSpec,
                           final Class moduleClass,
                           final ExtensionModule module,
                           final File moduleJarFile,
                           final ClassLoaderFactory classLoaderFactory) throws Throwable {

            // 如果之前已经加载过了相同ID的模块，则放弃当前模块的加载
            if (loadedModuleMap.containsKey(moduleSpec.getModuleId())) {
                final CoreModule existedCoreModule = get(moduleSpec.getModuleId());
                if (logger.isInfoEnabled()) {
                    logger.info("SIMULATOR: module already loaded, ignore load this module. expected:module={};class={};loader={}|existed:class={};loader={};",
                            moduleSpec.getModuleId(),
                            moduleClass, classLoaderFactory,
                            existedCoreModule.getModule().getClass().getName(),
                            existedCoreModule.getClassLoaderFactory()
                    );
                }
                return;
            }

            // 需要经过ModuleLoadingChain的过滤
            providerManager.loading(
                    simulatorConfig,
                    moduleClass,
                    module,
                    moduleJarFile,
                    classLoaderFactory.getDefaultClassLoader()
            );

            // 之前没有加载过，这里进行加载
            if (logger.isInfoEnabled()) {
                logger.info("SIMULATOR: found new module, prepare to load. module={};class={};loader={};",
                        moduleSpec.getModuleId(),
                        moduleClass,
                        classLoaderFactory
                );
            }

            // 这里进行真正的模块加载
            load(moduleSpec, module, moduleJarFile, classLoaderFactory);
        }
    }

    @Override
    public synchronized void flush(final boolean isForce) throws ModuleException {
        if (isForce) {
            forceFlush();
        } else {
            softFlush();
        }
    }

    private List<File> getAllModuleLibJar(File[] libDirs) {
        List<File> files = new ArrayList<File>();
        for (File file : libDirs) {
            loadJar(file, files);
        }
        return files;
    }

    private void loadJar(File file, List<File> files) {
        if (!file.exists() || !file.canRead()) {
            return;
        }
        if (file.isFile()) {
            if (file.getName().endsWith(".jar")) {
                files.add(file);
            }
            return;
        }
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            for (File f : subFiles) {
                loadJar(f, files);
            }
        }
    }

    private void loadModule(final ModuleSpec moduleSpec) {
        /**
         * 过滤掉非法的模块
         */
        if (moduleSpec.isLoaded()) {
            return;
        }
        // 用户模块加载目录，加载用户模块目录下的所有模块
        // 对模块访问权限进行校验
        if (moduleSpec.getFile().exists() && moduleSpec.getFile().canRead()) {
            new ModuleLibLoader(moduleSpec, config.getLaunchMode(), classLoaderService)
                    .load(simulatorConfig,
                            new InnerModuleJarLoadCallback(),
                            new InnerModuleLoadCallback()
                    );
        } else {
            logger.warn("SIMULATOR: module-lib not access, ignore flush load this lib. path={}, module-id={}", moduleSpec.getFile(), moduleSpec.getModuleId());
        }
    }

    /**
     * 模块加载
     *
     * @param moduleSpec
     * @param action
     */
    private void loadModule(final ModuleSpec moduleSpec, String action) {
        if (!moduleSpec.getFile().exists() || !moduleSpec.getFile().canRead()) {
            moduleSpec.setValid(false);
            logger.warn("SIMULATOR: {} modules[{}]: module-lib can not access, cause by file is not exists or can't read. module-lib={}, exists={}, canRead={}",
                    action, moduleSpec.getModuleId(), moduleSpec.getFile(), moduleSpec.getFile().exists(), moduleSpec.getFile().canRead());
            return;
        }

        /**
         * 如果是版本不支持则也没办法正常加载 Module
         */
        if (!(VersionUtils.isLeVersion(moduleSpec.getSinceVersion(), simulatorConfig.getSimulatorVersion()) && VersionUtils.isGeVersion(moduleSpec.getUntilVersion(), simulatorConfig.getSimulatorVersion()))) {
            moduleSpec.setValid(false);
            logger.warn("SIMULATOR: {} modules[{}]: module is not enabled, cause by module version is not support simulator version, will be ignored. module-lib={}, simulator-version:{} module-support-version:{}-{}",
                    action, moduleSpec.getModuleId(), moduleSpec.getFile(), simulatorConfig.getSimulatorVersion(), moduleSpec.getSinceVersion(), moduleSpec.getUntilVersion());
            return;
        }

        /**
         * 只有非必须的模块才可以设置禁用，不然则可以忽略
         */
        if (!moduleSpec.isMustUse()) {
            if (disabledModules.contains(moduleSpec.getModuleId())) {
                moduleSpec.setValid(false);
                logger.warn("SIMULATOR: {} modules[{}]: module is disabled, will be ignored. module-lib={}", action, moduleSpec.getModuleId(), moduleSpec.getFile());
                return;
            }
        }

        try {
            final ClassLoaderFactory moduleClassLoader = new ClassLoaderFactoryImpl(classLoaderService, config, moduleSpec.getFile(), moduleSpec.getModuleId(), moduleSpec.isMiddlewareModule());
            classLoaderService.load(moduleSpec, moduleClassLoader);
        } catch (Throwable e) {
            moduleSpec.setValid(false);
        }
        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: {} modules[{}]: load module success. module-lib={}", action, moduleSpec.getModuleId(), moduleSpec.getFile());
        }
        if (CollectionUtils.isNotEmpty(moduleSpec.getSwitchControls())) {
            /**
             * 如果开关已经是开启状态，则直接执行即可
             */
            if (GlobalSwitch.isAllSwitchOn(moduleSpec.getSwitchControls())) {
                loadModule(moduleSpec);
            } else {
                GlobalSwitch.registerMultiSwitchOnCallback(moduleSpec.getSwitchControls(), new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 当开启状态时执行加载
                         */
                        if (GlobalSwitch.isAllSwitchOn(moduleSpec.getSwitchControls())) {
                            loadModule(moduleSpec);
                        } else {
                            /**
                             * 否则重新注册开关,因为回调执行一次就会销毁
                             */
                            GlobalSwitch.registerMultiSwitchOnCallback(moduleSpec.getSwitchControls(), this);
                        }
                    }
                });
            }


        } else {
            loadModule(moduleSpec);
        }
    }

    private void loadModules(List<ModuleSpec> moduleSpecs, String action) {
        for (ModuleSpec moduleSpec : moduleSpecs) {
            loadModule(moduleSpec, action);
        }
    }

    @Override
    public CoreModuleManager load(File file) throws ModuleException {
        ModuleSpec moduleSpec = ModuleSpecUtils.loadModuleSpec(file, false);
        loadModule(moduleSpec, "load");
        return this;
    }

    @Override
    public synchronized CoreModuleManager reset() throws ModuleException {
        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: resetting all loaded modules:{}", loadedModuleMap.keySet());
        }

        waitLoadModules.clear();

        // 1. 强制卸载所有模块
        unloadAll();

        // 2. 先加载所有的系统模块
        List<File> systemModuleLibJars = getAllModuleLibJar(systemModuleLibs);
        List<ModuleSpec> systemModuleSpecs = ModuleSpecUtils.loadModuleSpecs(systemModuleLibJars, true);
        loadModules(systemModuleSpecs, "load");

        // 3. 加载所有用户自定义模块, 采用异步加载方式加载用户自定义模块
        List<File> userModuleLibJars = getAllModuleLibJar(userModuleLibs);
        List<ModuleSpec> userModuleSpecs = ModuleSpecUtils.loadModuleSpecs(userModuleLibJars, false);
        loadModules(userModuleSpecs, "load");
        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: resetting all loaded modules finished :{}", loadedModuleMap.keySet());
        }
        return this;
    }

    /**
     * 关闭ModuleJarClassLoader
     * 如ModuleJarClassLoader所加载上来的所有模块都已经被卸载，则该ClassLoader需要主动进行关闭
     *
     * @param classLoaderFactory 需要被关闭的ClassLoader工厂
     */
    private void closeModuleJarClassLoaderIfNecessary(final ClassLoaderFactory classLoaderFactory) {

        if (!(classLoaderFactory instanceof ModuleClassLoader)) {
            return;
        }

        // 查找已经注册的模块中是否仍然还包含有ModuleJarClassLoader的引用
        boolean hasRef = false;
        for (final CoreModule coreModule : loadedModuleMap.values()) {
            if (classLoaderFactory == coreModule.getClassLoaderFactory()) {
                hasRef = true;
                break;
            }
        }

        if (!hasRef) {
            if (logger.isInfoEnabled()) {
                logger.info("SIMULATOR: ModuleJarClassLoaderFactory={} will be close: all module unloaded.", classLoaderFactory);
            }
            classLoaderFactory.release();
        }

    }


    private boolean isChecksumCRC32Existed(long checksumCRC32) {
        for (final CoreModule coreModule : loadedModuleMap.values()) {
            if (coreModule.getClassLoaderFactory().getChecksumCRC32() == checksumCRC32) {
                return true;
            }
        }
        return false;
    }

    /**
     * 软刷新
     * 找出有变动的模块文件，有且仅有改变这些文件所对应的模块
     */
    private void softFlush() {
        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: soft-flushing modules:{}", loadedModuleMap.keySet());
        }

        final File systemModuleLibDir = new File(config.getSystemModuleLibPath());
        try {
            final ArrayList<File> appendJarFiles = new ArrayList<File>();
            final ArrayList<CoreModule> removeCoreModules = new ArrayList<CoreModule>();
            final ArrayList<Long> checksumCRC32s = new ArrayList<Long>();

            // 1. 找出所有有变动的文件(add/remove)
            for (final File jarFile : ModuleLoaders.getModuleLoader(config.getModuleRepositoryMode()).loadModuleLibs(config.getAppName(), config.getUserModulePaths())) {
                final long checksumCRC32;
                try {
                    checksumCRC32 = FileUtils.checksumCRC32(jarFile);
                } catch (IOException cause) {
                    logger.warn("SIMULATOR: soft-flushing module: compute module-jar CRC32 occur error. module-jar={};", jarFile, cause);
                    continue;
                }
                checksumCRC32s.add(checksumCRC32);
                // 如果CRC32已经在已加载的模块集合中存在，则说明这个文件没有变动，忽略
                if (isChecksumCRC32Existed(checksumCRC32)) {
                    if (logger.isInfoEnabled()) {
                        logger.info("SIMULATOR: soft-flushing module: module-jar is not changed, ignored. module-jar={};CRC32={};", jarFile, checksumCRC32);
                    }
                    continue;
                }

                if (logger.isInfoEnabled()) {
                    logger.info("SIMULATOR: soft-flushing module: module-jar is changed, will be flush. module-jar={};CRC32={};", jarFile, checksumCRC32);
                }
                appendJarFiles.add(jarFile);
            }

            // 2. 找出所有待卸载的已加载用户模块
            for (final CoreModule coreModule : loadedModuleMap.values()) {
                final ClassLoaderFactory classLoaderFactory = coreModule.getClassLoaderFactory();

                // 如果是系统模块目录则跳过
                if (isOptimisticDirectoryContainsFile(systemModuleLibDir, coreModule.getJarFile())) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("SIMULATOR: soft-flushing module: module-jar is in system-lib, will be ignored. module-jar={};system-lib={};",
                                coreModule.getJarFile(),
                                systemModuleLibDir
                        );
                    }
                    continue;
                }

                // 如果CRC32已经在这次待加载的集合中，则说明这个文件没有变动，忽略
                if (checksumCRC32s.contains(classLoaderFactory.getChecksumCRC32())) {
                    if (logger.isInfoEnabled()) {
                        logger.info("SIMULATOR: soft-flushing module: module-jar already loaded, ignored. module-jar={};CRC32={};",
                                coreModule.getJarFile(),
                                classLoaderFactory.getChecksumCRC32()
                        );
                    }
                    continue;
                }
                if (logger.isInfoEnabled()) {
                    logger.info("SIMULATOR: soft-flushing module: module-jar is changed, module will be reload/remove. module={};module-jar={};",
                            coreModule.getUniqueId(),
                            coreModule.getJarFile()
                    );
                }
                removeCoreModules.add(coreModule);
            }

            // 3. 删除remove
            for (final CoreModule coreModule : removeCoreModules) {
                unload(coreModule, true);
            }

            // 3. 加载所有用户自定义模块
            List<ModuleSpec> userModuleSpecs = ModuleSpecUtils.loadModuleSpecs(appendJarFiles, false);
            loadModules(userModuleSpecs, "soft-flush");
        } catch (Throwable cause) {
            logger.warn("SIMULATOR: soft-flushing modules: occur error.", cause);
        }

    }

    /**
     * 强制刷新
     * 对所有已经加载的用户模块进行强行卸载并重新加载
     *
     * @throws ModuleException 模块操作失败
     */
    private void forceFlush() throws ModuleException {
        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: force-flushing modules:{}", loadedModuleMap.keySet());
        }

        // 1. 卸载模块
        // 等待卸载的模块集合
        final Collection<CoreModule> waitingUnloadCoreModules = new ArrayList<CoreModule>();

        // 找出所有USER的模块，所以这些模块都卸载了
        for (final CoreModule coreModule : loadedModuleMap.values()) {
            // 如果判断是属于USER模块目录下的模块，则加入到待卸载模块集合，稍后统一进行卸载
            if (!isSystemModule(coreModule.getJarFile())) {
                waitingUnloadCoreModules.add(coreModule);
            }
        }

        // 记录下即将被卸载的模块ID集合
        if (logger.isInfoEnabled()) {
            final Set<String> uniqueIds = new LinkedHashSet<String>();
            for (final CoreModule coreModule : waitingUnloadCoreModules) {
                uniqueIds.add(coreModule.getUniqueId());
            }
            if (logger.isInfoEnabled()) {
                logger.info("SIMULATOR: force-flush modules: will be unloading modules : {}", uniqueIds);
            }
        }

        // 强制卸载掉所有等待卸载的模块集合中的模块
        for (final CoreModule coreModule : waitingUnloadCoreModules) {
            unload(coreModule, true);
        }

        // 2. 加载模块
        // 用户模块加载目录，加载用户模块目录下的所有模块
        // 对模块访问权限进行校验
        // 用户模块目录
        List<File> userModuleLibJars = getAllModuleLibJar(userModuleLibs);
        List<ModuleSpec> userModuleSpecs = ModuleSpecUtils.loadModuleSpecs(userModuleLibJars, false);
        loadModules(userModuleSpecs, "force-flush");

    }

    /**
     * 模块生命周期类型
     */
    enum ModuleLifeCycleType {

        /**
         * 模块加载
         */
        MODULE_LOAD,

        /**
         * 模块卸载
         */
        MODULE_UNLOAD,

        /**
         * 模块激活
         */
        MODULE_ACTIVE,

        /**
         * 模块冻结
         */
        MODULE_FROZEN,

        /**
         * 模块加载完成
         */
        MODULE_LOAD_COMPLETED
    }

}
