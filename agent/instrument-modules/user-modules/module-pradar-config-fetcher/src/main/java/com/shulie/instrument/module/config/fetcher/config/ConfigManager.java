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
package com.shulie.instrument.module.config.fetcher.config;


import com.shulie.instrument.module.config.fetcher.config.event.ConfigEvent;
import com.shulie.instrument.module.config.fetcher.config.event.ConfigListener;
import com.shulie.instrument.module.config.fetcher.config.impl.ApplicationConfig;
import com.shulie.instrument.module.config.fetcher.config.impl.ClusterTestConfig;
import com.shulie.instrument.module.config.fetcher.config.resolver.ConfigResolver;
import com.shulie.instrument.module.config.fetcher.config.resolver.http.ApplicationConfigHttpResolver;
import com.shulie.instrument.module.config.fetcher.config.resolver.http.ClusterTestConfigHttpResolver;
import com.shulie.instrument.module.config.fetcher.config.resolver.zk.ApplicationConfigZkResolver;
import com.shulie.instrument.module.config.fetcher.config.resolver.zk.ClusterTestConfigZkResolver;
import com.shulie.instrument.module.config.fetcher.config.resolver.zk.ZookeeperOptions;
import com.shulie.instrument.simulator.api.guard.SimulatorGuard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author shiyajian
 * @since 2020-08-11
 */
public final class ConfigManager {

    private final ApplicationConfig applicationConfig;
    private final ClusterTestConfig clusterTestConfig;

    private final ConcurrentHashMap<String, List<ConfigListener>> listenerHolder = new ConcurrentHashMap<String, List<ConfigListener>>();
    private static ConfigManager INSTANCE;
    private ZookeeperOptions zookeeperOptions;

    private ConfigManager(int interval, TimeUnit timeUnit) {
        /**
         * 只需要你保护 ConfigResolver 下所有的操作不被其他插件增强即可
         */
        this.applicationConfig = new ApplicationConfig(SimulatorGuard.getInstance().doGuard(ConfigResolver.class, new ApplicationConfigHttpResolver(interval, timeUnit)));
        this.clusterTestConfig = new ClusterTestConfig(SimulatorGuard.getInstance().doGuard(ConfigResolver.class, new ClusterTestConfigHttpResolver(interval, timeUnit)));
        initAll();
    }

    private ConfigManager(ZookeeperOptions zookeeperOptions) {
        this.zookeeperOptions = zookeeperOptions;
        this.applicationConfig = new ApplicationConfig(new ApplicationConfigZkResolver(zookeeperOptions));
        this.clusterTestConfig = new ClusterTestConfig(new ClusterTestConfigZkResolver(zookeeperOptions));
        initAll();
    }

    public void destroy() {
        if (this.applicationConfig != null) {
            this.applicationConfig.destroy();
        }
        if (this.clusterTestConfig != null) {
            this.clusterTestConfig.destroy();
        }
    }

    /**
     * 获取 ConfigManager 实例
     *
     * @param interval 拉取配置的间隔时间
     * @param timeUnit 拉取配置的间隔时间单位
     * @return
     */
    public static ConfigManager getInstance(int interval, TimeUnit timeUnit) {
        if (INSTANCE == null) {
            synchronized (ConfigManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ConfigManager(interval, timeUnit);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 获取 ConfigManager 实例,需要注意的是这个方法不会触发初始化
     *
     * @return
     */
    static ConfigManager getInstance() {
        return INSTANCE;
    }

    /**
     * 获取 ConfigManager 实例
     *
     * @param zookeeperOptions zk 配置
     * @return
     */
    public static ConfigManager getInstance(ZookeeperOptions zookeeperOptions) {
        if (INSTANCE == null) {
            synchronized (ConfigManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ConfigManager(zookeeperOptions);
                }
            }
        }
        return INSTANCE;
    }


    public ConcurrentHashMap<String, List<ConfigListener>> getListenerHolder() {
        return listenerHolder;
    }

    public synchronized <T extends AbstractConfig<T>> void subscribeConfigEvent(ConfigEvent<T> configEvent, ConfigListener listener) {
        String listenerKey = getListenerKey(configEvent.getType().getName(), configEvent.getField().getFileName(), configEvent.getEvent().name());
        List<ConfigListener> configListeners = listenerHolder.get(listenerKey);
        if (configListeners == null) {
            configListeners = new ArrayList<ConfigListener>();
        }
        configListeners.add(listener);
        listenerHolder.put(listenerKey, configListeners);
    }

    public static String getListenerKey(String typeName, String fieldName, String eventName) {
        return typeName + "@" + fieldName + "#" + eventName;
    }

    public void triggerConfigFetch() {
        if (applicationConfig.isInit()) {
            applicationConfig.triggerFetch();
        }
        if (clusterTestConfig.isInit()) {
            clusterTestConfig.triggerFetch();
        }
    }

    public void initAll() {
        if (!applicationConfig.isInit()) {
            synchronized (applicationConfig) {
                if (!applicationConfig.isInit()) {
                    applicationConfig.init();
                }
            }
        }
        if (!clusterTestConfig.isInit()) {
            synchronized (clusterTestConfig) {
                if (!clusterTestConfig.isInit()) {
                    clusterTestConfig.init();
                }
            }
        }
    }

}
