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
package com.shulie.instrument.module.config.fetcher.config.resolver.zk;


import com.pamirs.pradar.Pradar;
import com.shulie.instrument.module.config.fetcher.config.AbstractConfig;
import com.shulie.instrument.module.config.fetcher.config.event.FIELDS;
import com.shulie.instrument.module.config.fetcher.config.impl.ClusterTestConfig;
import com.shulie.instrument.simulator.api.executors.ExecutorServiceFactory;
import io.shulie.tro.web.config.sync.zk.constants.ZkConfigPathConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author shiyajian
 * @since 2020-08-11
 */
public class ClusterTestConfigZkResolver extends AbstractZkResolver<ClusterTestConfig> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterTestConfigZkResolver.class.getName());

    public ClusterTestConfigZkResolver(ZookeeperOptions options) {
        super(options);
    }

    private boolean isAllowListSwitchOn = false;
    private boolean isClusterTestSwitchOn = false;
    private AbstractConfig config;

    @Override
    protected void init(AbstractConfig config) {
        this.config = config;

        addAllowListSwitchListener(ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE);
        addClusterTestSwitchListener(ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE);
    }

    @Override
    public void triggerFetch(ClusterTestConfig clusterTestConfig) {

    }

    @Override
    public ClusterTestConfig fetch() {
        ClusterTestConfig clusterTestConfig = new ClusterTestConfig(this);
        /**
         * 初始化全局开关配置
         * premain初始化时 close Listener、open Listener都是0
         * 如果全局开关拉取不到则默认开关为打开，会向全局发开关开启事件
         */
        clusterTestConfig.setGlobalSwitchOn(isClusterTestSwitchOn);
        // 白名单开关配置
        clusterTestConfig.setWhiteListSwitchOn(isAllowListSwitchOn);
        return clusterTestConfig;
    }

    @Override
    public ClusterTestConfig fetch(FIELDS... fields) {
        ClusterTestConfig clusterTestConfig = new ClusterTestConfig(this);
        if (fields == null || fields.length == 0) {
            return null;
        }
        for (FIELDS field : fields) {
            switch (field) {
                case GLOBAL_SWITCHON:
                    /**
                     * 初始化全局开关配置
                     * premain初始化时 close Listener、open Listener都是0
                     * 如果全局开关拉取不到则默认开关为打开，会向全局发开关开启事件
                     */
                    clusterTestConfig.setGlobalSwitchOn(isClusterTestSwitchOn);
                    break;
                case WHITE_LIST_SWITCHON:
                    // 白名单开关配置
                    clusterTestConfig.setWhiteListSwitchOn(isAllowListSwitchOn);
                    break;
            }
        }

        return clusterTestConfig;
    }

    /**
     * 添加白名单开关监听器
     *
     * @param executor
     */
    private void addAllowListSwitchListener(final ExecutorService executor) {
        String value = addNodeListener(executor, ZkConfigPathConstants.NAME_SPACE + '/' + Pradar.PRADAR_USER_KEY + ZkConfigPathConstants.ALLOW_LIST_SWITCH_PATH, new NodeListener() {
            @Override
            public void onListener(String path, String content) {
                isAllowListSwitchOn = Boolean.valueOf(content);
                ClusterTestConfig.getWhiteListSwitcher = true;
                config.trigger(FIELDS.WHITE_LIST_SWITCHON);
            }
        });
        ClusterTestConfig.getWhiteListSwitcher = true;
        isAllowListSwitchOn = Boolean.valueOf(value);
        config.trigger(FIELDS.WHITE_LIST_SWITCHON);
    }

    /**
     * 添加全局开关监听器
     *
     * @param executor
     */
    private void addClusterTestSwitchListener(final ExecutorService executor) {
        String value = addNodeListener(executor, ZkConfigPathConstants.NAME_SPACE + '/' + Pradar.PRADAR_USER_KEY + ZkConfigPathConstants.CLUSTER_TEST_SWITCH_PATH, new NodeListener() {
            @Override
            public void onListener(String path, String content) {
                isClusterTestSwitchOn = Boolean.valueOf(content);
                ClusterTestConfig.getApplicationSwitcher = true;
                config.trigger(FIELDS.GLOBAL_SWITCHON);
            }
        });
        isClusterTestSwitchOn = Boolean.valueOf(value);
        ClusterTestConfig.getApplicationSwitcher = true;
        config.trigger(FIELDS.GLOBAL_SWITCHON);
    }
}
