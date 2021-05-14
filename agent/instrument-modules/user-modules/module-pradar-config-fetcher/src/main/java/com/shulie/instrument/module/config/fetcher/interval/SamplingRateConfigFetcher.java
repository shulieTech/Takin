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
package com.shulie.instrument.module.config.fetcher.interval;


import com.pamirs.pradar.AppNameUtils;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.shulie.instrument.module.config.fetcher.config.resolver.zk.AbstractZkResolver;
import com.shulie.instrument.module.config.fetcher.config.resolver.zk.ApplicationConfigZkResolver;
import com.shulie.instrument.module.config.fetcher.config.resolver.zk.ZookeeperOptions;
import com.shulie.instrument.module.register.zk.ZkClient;
import com.shulie.instrument.module.register.zk.ZkNodeCache;
import com.shulie.instrument.module.register.zk.ZkPathChildrenCache;
import com.shulie.instrument.module.register.zk.impl.NetflixCuratorZkClientFactory;
import com.shulie.instrument.module.register.zk.impl.ZkClientSpec;
import com.shulie.instrument.simulator.api.executors.ExecutorServiceFactory;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @ClassName: ConfigUtils
 * @author: wangjian
 * @Date: 2020/12/8 16:07
 * @Description: 采样率
 */
public class SamplingRateConfigFetcher implements ISamplingRateConfigFetcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(SamplingRateConfigFetcher.class.getName());

    private static final String GLOBAL_CONFIG_PATH = "/config/log/trace/simpling";
    private static final String APP_CONFIG_PATH = "/config/log/trace/%s/simpling";

    private ZkClient zkClient;
    private ZkClientSpec zkClientSpec;

    private boolean useLocal = false;
    private boolean useGlobal = true;
    private SimulatorConfig simulatorConfig;
    private String globalSamplingPath;
    private String appSamplingPath;

    public SamplingRateConfigFetcher(ZookeeperOptions zookeeperOptions, SimulatorConfig simulatorConfig) {
        zkClientSpec = new ZkClientSpec();
        this.simulatorConfig = simulatorConfig;
        this.globalSamplingPath = simulatorConfig.getProperty("pradar.trace.sampling.path", GLOBAL_CONFIG_PATH);
        if (this.globalSamplingPath.endsWith("/")) {
            this.globalSamplingPath = this.globalSamplingPath.substring(0, this.globalSamplingPath.length() - 1);
        }
        this.appSamplingPath = simulatorConfig.getProperty("pradar.trace.app.sampling.path", String.format(APP_CONFIG_PATH, AppNameUtils.appName()));
        if (this.appSamplingPath.endsWith("/")) {
            this.appSamplingPath = this.appSamplingPath.substring(0, this.appSamplingPath.length() - 1);
        }
        zkClientSpec.setZkServers(zookeeperOptions.getZkServers());
        zkClientSpec.setConnectionTimeoutMillis(zookeeperOptions.getConnectionTimeoutMillis());
        zkClientSpec.setSessionTimeoutMillis(zookeeperOptions.getSessionTimeoutMillis());
        zkClientSpec.setThreadName("SamplingRateConfigPuller");
    }

    @Override
    public void start() {
        try {
            this.zkClient = NetflixCuratorZkClientFactory.getInstance().create(zkClientSpec);
        } catch (Throwable e) {
            LOGGER.error("init zk client error:", e);
            ErrorReporter.buildError()
                    .setErrorCode("agent-0007")
                    .setErrorType(ErrorTypeEnum.AgentError)
                    .setMessage("init zk client error.")
                    .setDetail(e.getMessage())
                    .report();
        }
        if (null == zkClient) {
            return;
        }
//        /config/log/trace/simpling
//        /config/log/trace/{应用名}/simpling
        try {
            boolean exists = this.zkClient.exists(globalSamplingPath);
            boolean appExists = this.zkClient.exists(appSamplingPath);
            PradarSwitcher.setSamplingZkConfig(exists || appExists);
            updateSamplingInterval(ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE, globalSamplingPath);
            updateSamplingInterval(ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE, appSamplingPath);
            listenPathChange(ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE, globalSamplingPath);
            listenPathChange(ExecutorServiceFactory.GLOBAL_EXECUTOR_SERVICE, appSamplingPath);
        } catch (Throwable e) {
            LOGGER.error("register zk listener error:", e);
            ErrorReporter.buildError()
                    .setErrorCode("agent-0007")
                    .setErrorType(ErrorTypeEnum.AgentError)
                    .setMessage("register zk listener error.")
                    .setDetail(e.getMessage())
                    .report();
        }


    }

    @Override
    public void stop() {
        if (this.zkClient != null) {
            try {
                this.zkClient.stop();
            } catch (Exception e) {
                LOGGER.error("Sampling rate config fetcher zkclient stop error.", e);
            }
        }
    }

    private void listenPathChange(ExecutorService executor, final String path) {
        final ZkPathChildrenCache pathChildrenCache = zkClient.createPathChildrenCache(path.substring(0, path.lastIndexOf("/")));
        final String simpleSamplingNodeName = path.substring(path.lastIndexOf("/") + 1);
        pathChildrenCache.setUpdateExecutor(executor);
        pathChildrenCache.setUpdateListener(new Runnable() {
            @Override
            public void run() {
                AbstractZkResolver.PathListener pathListener = new ApplicationConfigZkResolver.PathListener() {
                    @Override
                    public void onListen(String path, List<String> allList, List<String> addList, List<String> deleteList) {
                        // 节点删除
                        if (path.equals(globalSamplingPath) && !addList.contains(simpleSamplingNodeName)) {
                            // 全局配置节点删除
                            // 将使用本地配置文件配置
                            useLocal = true;
                            LOGGER.warn(path + "global trace sampling is changed. trigger by zookeeper node deleted. use local trace sampling.");
                            if (useGlobal && useLocal) {
                                PradarSwitcher.setSamplingZkConfig(false);
                            }
                        } else if (!addList.contains(simpleSamplingNodeName)) {
                            // 应用配置节点删除
                            // 将使用全局配置节点数据
                            useGlobal = true;
                            LOGGER.info(path + " deleted.");
                            if (useGlobal && useLocal) {
                                PradarSwitcher.setSamplingZkConfig(false);
                            }
                        }
                    }
                };
                pathListener.onListen(path, pathChildrenCache.getChildren(), pathChildrenCache.getAddChildren(), pathChildrenCache.getDeleteChildren());
            }
        });
        try {
            pathChildrenCache.startAndRefresh();
        } catch (Throwable e) {
            LOGGER.error("start zk path listener error:", e);
            ErrorReporter.buildError()
                    .setErrorCode("agent-0007")
                    .setErrorType(ErrorTypeEnum.AgentError)
                    .setMessage("start zk listener error.")
                    .setDetail(e.getMessage())
                    .report();
        }
    }

    private void updateSamplingInterval(ExecutorService executor, final String path) {
        final ZkNodeCache zkNodeCache = this.zkClient.createZkNodeCache(path, false);
        zkNodeCache.setUpdateExecutor(executor);
        zkNodeCache.setUpdateListener(new Runnable() {

            @Override
            public void run() {
                AbstractZkResolver.NodeListener nodeListener = new AbstractZkResolver.NodeListener() {
                    @Override
                    public void onListener(String path, String content) {
                        if (StringUtils.isNotBlank(content)) {
                            if (path.equals(GLOBAL_CONFIG_PATH) && !useGlobal) {
                                // 应用配置节点存在，因此不使用全局配置
                                return;
                            }
                            if (!path.equals(GLOBAL_CONFIG_PATH)) {
                                // 应用配置节点存在，因此不使用全局配置
                                useGlobal = false;
                            }
                            // zk配置节点存在并且有数据，则不使用本地配置文件配置
                            useLocal = false;
                            PradarSwitcher.setSamplingZkConfig(true);
                            try {
                                PradarSwitcher.setSamplingInterval(Integer.parseInt(content));
                            } catch (Throwable e) {
                                LOGGER.error("parse zk config error:", e);
                                ErrorReporter.buildError()
                                        .setErrorCode("agent-0007")
                                        .setErrorType(ErrorTypeEnum.AgentError)
                                        .setMessage("parse zk config error.:" + content)
                                        .setDetail(e.getMessage())
                                        .report();
                            }
                        } else {
                            LOGGER.info(path + " data deleted.");
                            // 节点删除or数据清除
                            if (path.equals(GLOBAL_CONFIG_PATH)) {
                                // 全局配置节点数据清空
                                // 将使用本地配置文件配置
                                useLocal = true;
                            } else {
                                // 应用配置节点数据清空
                                // 将使用全局配置节点数据
                                useGlobal = true;
                            }
                            if (useGlobal && useLocal) {
                                PradarSwitcher.setSamplingZkConfig(false);
                            }
                        }
                    }
                };
                try {
                    nodeListener.onListener(path, zkNodeCache.getData() == null ? null : new String(zkNodeCache.getData(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    nodeListener.onListener(path, zkNodeCache.getData() == null ? null : new String(zkNodeCache.getData()));
                }
            }
        });
        try {
            zkNodeCache.startAndRefresh();
        } catch (Throwable e) {
            LOGGER.error("start zk node listener error:", e);
            ErrorReporter.buildError()
                    .setErrorCode("agent-0007")
                    .setErrorType(ErrorTypeEnum.AgentError)
                    .setMessage("start zk listener error.")
                    .setDetail(e.getMessage())
                    .report();
        }
    }
}
