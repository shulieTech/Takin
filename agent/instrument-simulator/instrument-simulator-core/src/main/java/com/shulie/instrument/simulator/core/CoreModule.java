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

import com.shulie.instrument.simulator.api.*;
import com.shulie.instrument.simulator.api.instrument.EnhanceTemplate;
import com.shulie.instrument.simulator.api.resource.*;
import com.shulie.instrument.simulator.compatible.transformer.SimulatorClassFileTransformer;
import com.shulie.instrument.simulator.core.classloader.ClassLoaderFactory;
import com.shulie.instrument.simulator.core.classloader.ClassLoaderService;
import com.shulie.instrument.simulator.core.inject.ClassInjector;
import com.shulie.instrument.simulator.core.manager.CoreLoadedClassDataSource;
import com.shulie.instrument.simulator.core.manager.CoreModuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

import static com.shulie.instrument.simulator.api.ModuleException.ErrorCode.MODULE_LOAD_ERROR;

/**
 * 仿真器模块内核封装对象
 */
public class CoreModule {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 模块描述
     */
    private final ModuleSpec moduleSpec;

    /**
     * 模块归属的jar
     */
    private final File jarFile;

    /**
     * 模块类加载器工厂
     */
    private ClassLoaderFactory classLoaderFactory;

    /**
     * 模块
     */
    private final ExtensionModule module;

    /**
     * 模块的类转换器
     */
    private final Set<SimulatorClassFileTransformer> simulatorClassFileTransformers
            = new LinkedHashSet<SimulatorClassFileTransformer>();

    /**
     * 模块所持有的可释放资源
     */
    private final List<ReleaseResource<?>> releaseResources
            = new ArrayList<ReleaseResource<?>>();

    /**
     * 是否已经激活
     */
    private boolean isActivated;

    /**
     * 是否已被加载
     */
    private boolean isLoaded;

    /**
     * 类数据源
     */
    private CoreLoadedClassDataSource coreLoadedClassDataSource;

    /**
     * 模块事件观察者
     */
    private ModuleEventWatcher moduleEventWatcher;

    /**
     * 增强模板
     */
    private EnhanceTemplate enhanceTemplate;

    /**
     * 模块控制器
     */
    private ModuleController moduleController;

    /**
     * 对象管理器
     */
    private ObjectManager objectManager;

    /**
     * 模块管理器
     */
    private ModuleManager moduleManager;

    /**
     * 仿真器配置
     */
    private SimulatorConfig simulatorConfig;

    /**
     * 内核模块管理器
     */
    private CoreModuleManager coreModuleManager;

    /**
     * 类注入器
     */
    private ClassInjector classInjector;

    /**
     * 动态属性管理器
     */
    private DynamicFieldManager dynamicFieldManager;

    /**
     * 模块业务对象
     *
     * @param moduleSpec         模块描述
     * @param jarFile            模块归属Jar文件
     * @param classLoaderFactory 模块加载ClassLoader
     * @param module             模块
     * @param coreModuleManager  模块管理器
     */
    public CoreModule(final ModuleSpec moduleSpec,
                      final File jarFile,
                      final ClassLoaderFactory classLoaderFactory,
                      final ExtensionModule module,
                      final CoreModuleManager coreModuleManager) {
        this.moduleSpec = moduleSpec;
        this.jarFile = jarFile;
        this.classLoaderFactory = classLoaderFactory;
        this.module = module;
        this.coreModuleManager = coreModuleManager;
    }

    public ModuleSpec getModuleSpec() {
        return moduleSpec;
    }

    /**
     * 判断模块是否已被激活
     *
     * @return TRUE:已激活;FALSE:未激活
     */
    public boolean isActivated() {
        return isActivated;
    }

    /**
     * 标记模块激活状态
     *
     * @param isActivated 模块激活状态
     * @return this
     */
    public CoreModule markActivated(boolean isActivated) {
        this.isActivated = isActivated;
        return this;
    }

    /**
     * 判断模块是否已经被加载
     *
     * @return TRUE:被加载;FALSE:未被加载
     */
    public boolean isLoaded() {
        return isLoaded;
    }


    /**
     * 标记模块加载状态
     *
     * @param isLoaded 模块加载状态
     * @return this
     */
    public CoreModule markLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
        return this;
    }

    /**
     * 是否是中间件扩展模块,如果是中间件扩展模块，则会根据业务类加载器生成多实例拦截器
     * 并且该模块中的类会根据业务类加载器生成多个类
     *
     * @return
     */
    public boolean isMiddlewareModule() {
        return moduleSpec.isMiddlewareModule();
    }

    /**
     * 获取ModuleJar文件
     *
     * @return ModuleJar文件
     */
    public File getJarFile() {
        return jarFile;
    }

    /**
     * 获取对应的ModuleJarClassLoader
     *
     * @return ModuleJarClassLoader
     */
    public ClassLoaderFactory getClassLoaderFactory() {
        return classLoaderFactory;
    }

    /**
     * 获取模块ID
     *
     * @return 模块ID
     */
    public String getUniqueId() {
        return moduleSpec.getModuleId();
    }

    /**
     * 获取模块优先级
     *
     * @return 模块优先级
     */
    public int getPriority() {
        return moduleSpec.getPriority();
    }

    /**
     * 获取模块支持的启动类型
     *
     * @return 获取启动类型
     */
    public LoadMode[] getSupportedModes() {
        return moduleSpec.getSupportedModes();
    }

    /**
     * 获取模块版本号
     *
     * @return 模块版本号
     */
    public String getVersion() {
        return moduleSpec.getVersion();
    }

    /**
     * 获取模块作者
     *
     * @return 模块作者
     */
    public String getAuthor() {
        return moduleSpec.getAuthor();
    }

    /**
     * 获取模块是否启动加载
     *
     * @return 模块启动是否加载
     */
    public boolean isActiveOnLoad() {
        return moduleSpec.isActiveOnLoad();
    }

    /**
     * 获取模块实例
     *
     * @return 模块实例
     */
    public ExtensionModule getModule() {
        return module;
    }

    /**
     * 获取模块所创建的SimulatorClassFileTransformer集合
     *
     * @return 模块所创建的SimulatorClassFileTransformer集合
     */
    public Set<SimulatorClassFileTransformer> getSimulatorClassFileTransformers() {
        return simulatorClassFileTransformers;
    }

    /**
     * 获取模块所编织的类个数
     *
     * @return 模块所编织的类个数
     */
    public int getEffectClassCount() {
        int effectClassCout = 0;
        for (final SimulatorClassFileTransformer simulatorClassFileTransformer : simulatorClassFileTransformers) {
            effectClassCout += simulatorClassFileTransformer.getAffectStatistic().getEffectClassCount();
        }
        return effectClassCout;
    }

    /**
     * 获取模块所编织的方法个数
     *
     * @return 模块所编织的方法个数
     */
    public int getEffectMethodCount() {
        int effectMethodCount = 0;
        for (final SimulatorClassFileTransformer simulatorClassFileTransformer : simulatorClassFileTransformers) {
            effectMethodCount += simulatorClassFileTransformer.getAffectStatistic().getEffectMethodCount();
        }
        return effectMethodCount;
    }

    @Override
    public String toString() {
        return String.format(
                "module[id=%s;class=%s;]",
                moduleSpec,
                module.getClass()
        );
    }


    /**
     * 在模块下追加一个可释放资源
     *
     * @param resource 可释放资源封装
     * @param <T>      资源实体
     * @return 资源实体本身
     */
    public <T> T append(ReleaseResource<T> resource) {
        if (null == resource
                || null == resource.get()) {
            return null;
        }
        synchronized (releaseResources) {
            releaseResources.add(resource);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("SIMULATOR: append resource={} in module[id={};]", resource.get(), moduleSpec);
        }
        return resource.get();
    }

    /**
     * 在当前模块下释放一个可释放资源
     *
     * @param target 待释放的资源实体
     */
    public void release(Object target) {
        synchronized (releaseResources) {
            final Iterator<ReleaseResource<?>> resourceRefIt = releaseResources.iterator();
            while (resourceRefIt.hasNext()) {
                final ReleaseResource<?> resourceRef = resourceRefIt.next();

                // 删除掉无效的资源
                if (null == resourceRef) {
                    resourceRefIt.remove();
                    if (logger.isInfoEnabled()) {
                        logger.info("SIMULATOR: remove null resource in module={}", moduleSpec);
                    }
                    continue;
                }

                // 删除掉已经被GC掉的资源
                final Object resource = resourceRef.get();
                if (null == resource) {
                    resourceRefIt.remove();
                    if (logger.isInfoEnabled()) {
                        logger.info("SIMULATOR: remove empty resource in module={}", moduleSpec);
                    }
                    continue;
                }

                if (target.equals(resource)) {
                    resourceRefIt.remove();
                    if (logger.isDebugEnabled()) {
                        logger.debug("SIMULATOR: release resource={} in module={}", resourceRef.get(), moduleSpec);
                    }
                    try {
                        resourceRef.release();
                    } catch (Exception cause) {
                        logger.warn("SIMULATOR: release resource occur error in module={};", moduleSpec, cause);
                    }
                }
            }
        }
    }

    /**
     * 在当前模块下移除所有可释放资源
     */
    public void releaseAll() {
        synchronized (releaseResources) {
            final Iterator<ReleaseResource<?>> resourceRefIt = releaseResources.iterator();
            while (resourceRefIt.hasNext()) {
                final ReleaseResource<?> resourceRef = resourceRefIt.next();
                resourceRefIt.remove();
                if (null != resourceRef) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("SIMULATOR: release resource={} in module={}", resourceRef.get(), moduleSpec);
                    }
                    try {
                        resourceRef.release();
                    } catch (Exception cause) {
                        logger.warn("SIMULATOR: release resource occur error in module={};", moduleSpec, cause);
                    }
                }
            }
            /**
             * 如果是远程模块仓库，释放资源时删除掉对应的jar文件
             */
            if (this.simulatorConfig.getModuleRepositoryMode() == ModuleRepositoryMode.REMOTE) {
                this.jarFile.delete();
            }
            this.coreModuleManager = null;
            this.coreLoadedClassDataSource = null;
            this.moduleEventWatcher = null;
            this.enhanceTemplate = null;
            this.moduleController = null;
            this.moduleManager = null;
            this.dynamicFieldManager.destroy();
            this.dynamicFieldManager = null;
            this.simulatorConfig = null;
            this.classLoaderFactory.release();
            this.classLoaderFactory = null;
        }
    }

    public ClassLoader getClassLoader(ClassLoader businessClassLoader) {
        return this.classLoaderFactory.getClassLoader(businessClassLoader);
    }

    /**
     * 构建监听器
     *
     * @param listener 事件监听器定义
     * @throws ModuleException 可能会由于初始化事件监听器出现异常
     */
    public void injectResource(Object listener) throws ModuleException {
        try {
            coreModuleManager.injectResource(listener, this);
        } catch (Throwable e) {
            logger.error("SIMULATOR: can't inject resource to listener class! listener:{}", listener, e);
            throw new ModuleException(getUniqueId(), MODULE_LOAD_ERROR, e);
        }
    }

    public ClassLoaderService getClassLoaderService() {
        return coreModuleManager.getClassLoaderService();
    }

    public CoreLoadedClassDataSource getCoreLoadedClassDataSource() {
        return coreLoadedClassDataSource;
    }

    public void setCoreLoadedClassDataSource(CoreLoadedClassDataSource coreLoadedClassDataSource) {
        this.coreLoadedClassDataSource = coreLoadedClassDataSource;
    }

    public ModuleEventWatcher getModuleEventWatcher() {
        return moduleEventWatcher;
    }

    public void setModuleEventWatcher(ModuleEventWatcher moduleEventWatcher) {
        this.moduleEventWatcher = moduleEventWatcher;
    }

    public ModuleController getModuleController() {
        return moduleController;
    }

    public void setModuleController(ModuleController moduleController) {
        this.moduleController = moduleController;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public void setObjectManager(ObjectManager objectManager) {
        this.objectManager = objectManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public void setModuleManager(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public SimulatorConfig getSimulatorConfig() {
        return simulatorConfig;
    }

    public void setSimulatorConfig(SimulatorConfig simulatorConfig) {
        this.simulatorConfig = simulatorConfig;
    }

    public ClassInjector getClassInjector() {
        return classInjector;
    }

    public void setClassInjector(ClassInjector classInjector) {
        this.classInjector = classInjector;
    }

    public EnhanceTemplate getEnhanceTemplate() {
        return enhanceTemplate;
    }

    public void setEnhanceTemplate(EnhanceTemplate enhanceTemplate) {
        this.enhanceTemplate = enhanceTemplate;
    }

    public DynamicFieldManager getDynamicFieldManager() {
        return dynamicFieldManager;
    }

    public void setDynamicFieldManager(DynamicFieldManager dynamicFieldManager) {
        this.dynamicFieldManager = dynamicFieldManager;
    }
}
