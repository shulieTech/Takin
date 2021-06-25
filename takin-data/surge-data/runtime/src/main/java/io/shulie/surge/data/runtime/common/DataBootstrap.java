/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.surge.data.runtime.common;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.shulie.surge.data.runtime.common.remote.impl.RemoteZkModule;
import io.shulie.surge.data.runtime.module.BaseConfigModule;
import io.shulie.surge.data.runtime.module.ZooKeeperClientModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 引导模块，负责 Log 启动以及初始化，主要用于安装各种 {@link Module}，
 * 最后执行 {@link #startRuntime()} 去创建 Log 运行时环境
 *
 * @author pamirs
 */
public final class DataBootstrap {

    private final Properties properties;
    private final Map<String, Object> moduleContext;

    private final List<Module> modules;
    private final List<PostInjectionCallback> postInjectionCallbacks;

    private DataBootstrap(Properties properties) {
        this.properties = mergeProperties(properties);
        this.moduleContext = new HashMap<String, Object>();
        this.modules = new ArrayList<Module>();
        this.postInjectionCallbacks = new ArrayList<PostInjectionCallback>();
    }

    /**
     * 创建 Log 引导模块，主要用于安装各种 {@link Module}，最后执行 {@link #startRuntime()}
     * 去创建 Log 运行时环境
     *
     * @param properties
     * @return
     */
    public static final DataBootstrap create(Properties properties) {
        return new DataBootstrap(properties);
    }

    /**
     * 创建 Log 引导模块，主要用于安装各种 {@link Module}，最后执行 {@link #startRuntime()}
     * 去创建 Log 运行时环境
     *
     * @param propertyFileName
     * @return
     */
    public static final DataBootstrap create(String propertyFileName) {
        return new DataBootstrap(readConfig(propertyFileName));
    }

    /**
     * 创建 Log 引导模块，主要用于安装各种 {@link Module}，最后执行 {@link #startRuntime()}
     * 去创建 Log 运行时环境
     *
     * @param propertyFileName
     * @return
     */
    public static final DataBootstrap create(String propertyFileName, String pointName) {
        Properties properties = readConfig(propertyFileName);
        properties.put("config.pointName", pointName);
        return create(properties);
    }

    /**
     * 安装模块，为方便管理, 模块都建议继承 ，
     * 并且包名为 com.pamirs.pradar.log.module
     *
     * @param module
     * @return
     */
    public DataBootstrap install(Module module) {
        modules.add(module);
        return this;
    }

    /**
     * 安装模块，为方便管理， 模块都建议继承BaseDataModule，
     * 并且包名为
     *
     * @param module
     * @param more
     * @return
     */
    public DataBootstrap install(Module module, Module... more) {
        install(module);
        if (more != null) {
            for (Module moreModule : more) {
                install(moreModule);
            }
        }
        return this;
    }

    /**
     * 安装模块，为方便管理，Log 模块都建议继承 BaseDataModule，
     *
     * @param modules
     * @return
     */
    public DataBootstrap install(Iterable<Module> modules) {
        for (Module module : modules) {
            install(module);
        }
        return this;
    }

    /**
     * 按照安装的模块去构建 Log 运行时环境
     *
     * @return
     */
    public DataRuntime startRuntime() {
        install(new BaseConfigModule(), new ZooKeeperClientModule(), new RemoteZkModule());
        for (Module module : modules) {
            if (module instanceof DataBootstrapAware) {
                DataBootstrapAware m = ((DataBootstrapAware) module);
                m.setDataBootstrap(this);
            }
        }
        Injector injector = Guice.createInjector(modules);
        DataRuntime runtime = injector.getInstance(DataRuntime.class);

        for (PostInjectionCallback callback : postInjectionCallbacks) {
            callback.afterInjection(runtime);
        }

        return runtime;
    }

    public Properties getProperties() {
        return properties;
    }


    public Map<String, Object> getModuleContext() {
        return moduleContext;
    }

    public void registPostInjectionCallback(PostInjectionCallback callback) {
        this.postInjectionCallbacks.add(callback);
    }


    private static Properties mergeProperties(Properties override) {
        // 加载默认的配置，然后把外部传入的配置覆盖上去
        Properties properties = loadDefaultConfig();
        properties.putAll(override);
        return properties;
    }

    private static Properties loadDefaultConfig() {
        String propertiesFileName = "runtime.properties";
        return readConfig(propertiesFileName);
    }

    public static Properties readConfig(String configFileName) {
        Properties prop = new Properties();
        InputStream is = getResourceAsStream(configFileName);
        if (is != null) {
            try {
                prop.load(is);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    // quiet
                }
            }
        }
        return prop;
    }

    private static InputStream getResourceAsStream(String name) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
    }
}
