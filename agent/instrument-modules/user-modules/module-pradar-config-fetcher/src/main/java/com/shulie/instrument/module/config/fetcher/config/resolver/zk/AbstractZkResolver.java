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

import com.pamirs.pradar.ConfigNames;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.exception.PradarException;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.shulie.instrument.module.config.fetcher.config.AbstractConfig;
import com.shulie.instrument.module.config.fetcher.config.resolver.ConfigResolver;
import com.shulie.instrument.module.register.zk.ZkClient;
import com.shulie.instrument.module.register.zk.ZkNodeCache;
import com.shulie.instrument.module.register.zk.ZkPathChildrenCache;
import com.shulie.instrument.module.register.zk.impl.NetflixCuratorZkClientFactory;
import com.shulie.instrument.module.register.zk.impl.ZkClientSpec;
import com.shulie.instrument.simulator.api.executors.ExecutorServiceFactory;
import io.shulie.tro.web.config.sync.zk.constants.ZkConfigPathConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author shiyajian
 * create: 2020-08-13
 */
public abstract class AbstractZkResolver<T extends AbstractConfig<T>> implements ConfigResolver<T> {
    private final static Logger logger = LoggerFactory.getLogger(AbstractZkResolver.class.getName());

    private final static String NODE_PREFIX = "node|";
    private final static String PATH_PREFIX = "path|";
    protected final ZookeeperOptions options;
    protected ZkClient zkClient;
    protected AtomicBoolean isInited = new AtomicBoolean(false);
    protected Map<String, Object> nodeCaches;

    protected AbstractZkResolver(ZookeeperOptions options) {
        this.options = options;
        this.nodeCaches = new ConcurrentHashMap<String, Object>();
        init();
    }

    private void init() {
    }

    @Override
    public void resolve(final T existsConfig) {
        /**
         * 使用定时线程去初始化，如果初始化失败则定时重试
         */
        ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    /**
                     * 如果没有初始化则初始化
                     */
                    if (!isInited.get()) {
                        initZkClient();
                        isInited.set(true);
                    }

                    /**
                     * 初始化成功后则订阅各个节点的数据
                     */
                    if (isInited.get()) {
                        init(existsConfig);
                    }

                    if (!isInited.get()) {
                        ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.schedule(this, 5, TimeUnit.SECONDS);
                    }
                } catch (PradarException e) {
                    logger.error("[config-subscriber] start config-subscriber zookeeper err!", e);
                    ErrorReporter.buildError()
                            .setErrorType(ErrorTypeEnum.AgentError)
                            .setErrorCode("agent-1001")
                            .setMessage("初始化ZK/初始化配置监听失败")
                            .setDetail("初始化ZK/初始化配置监听失败")
                            .closePradar(ConfigNames.CLUSTER_TEST_READY_CONFIG)
                            .report();
                    ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.schedule(this, 5, TimeUnit.SECONDS);
                } catch (Throwable e) {
                    ErrorReporter.buildError()
                            .setErrorType(ErrorTypeEnum.AgentError)
                            .setErrorCode("agent-1001")
                            .setMessage("初始化ZK/初始化配置监听失败")
                            .setDetail("初始化ZK/初始化配置监听失败")
                            .closePradar(ConfigNames.CLUSTER_TEST_READY_CONFIG)
                            .report();
                    logger.error("[config-subscriber] start config-subscriber zookeeper err!", e);
                    ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.schedule(this, 5, TimeUnit.SECONDS);
                }
            }
        }, 0, TimeUnit.SECONDS);

    }

    /**
     * 初始化各个配置节点的监听器
     */
    protected void init(AbstractConfig config) {

    }

    private void initZkClient() throws Exception {
        ZkClientSpec zkClientSpec = new ZkClientSpec();
        zkClientSpec.setZkServers(options.getZkServers());
        zkClientSpec.setConnectionTimeoutMillis(options.getConnectionTimeoutMillis());
        zkClientSpec.setSessionTimeoutMillis(options.getSessionTimeoutMillis());
        zkClientSpec.setThreadName("Config-Fetcher-Zk-Thread");
        AbstractZkResolver.this.zkClient = NetflixCuratorZkClientFactory.getInstance().create(zkClientSpec);
        AbstractZkResolver.this.zkClient.ensureDirectoryExists(ZkConfigPathConstants.NAME_SPACE);
    }

    protected List<String> addPathListener(ExecutorService executor, final String path, final ApplicationConfigZkResolver.PathListener pathListener) {
        //处理一下重复执行的问题
        if (!this.nodeCaches.containsKey(PATH_PREFIX + path)) {
            final ZkPathChildrenCache pathChildrenCache = zkClient.createPathChildrenCache(path);
            pathChildrenCache.setUpdateExecutor(executor);
            pathChildrenCache.setUpdateListener(new Runnable() {
                @Override
                public void run() {
                    if (pathListener != null) {
                        pathListener.onListen(path, pathChildrenCache.getChildren(), pathChildrenCache.getAddChildren(), pathChildrenCache.getDeleteChildren());
                    }
                }
            });
            try {
                pathChildrenCache.startAndRefresh();
                this.nodeCaches.put(PATH_PREFIX + path, pathChildrenCache);
                List<String> children = pathChildrenCache.getChildren();
                return children;
            } catch (Throwable e) {
                try {
                    pathChildrenCache.stop();
                } catch (Throwable exception) {
                }
                pathChildrenCache.setUpdateListener(null);
                pathChildrenCache.setUpdateExecutor(null);
                throw new PradarException(e);
            }
        } else {
            ZkPathChildrenCache zkPathChildrenCache = (ZkPathChildrenCache) this.nodeCaches.get(NODE_PREFIX + path);
            return zkPathChildrenCache.getChildren();
        }
    }


    protected String addNodeListener(ExecutorService executor, final String path, final ApplicationConfigZkResolver.NodeListener nodeListener) {
        //处理一下重复执行的问题
        if (!nodeCaches.containsKey(NODE_PREFIX + path)) {
            final ZkNodeCache zkNodeCache = zkClient.createZkNodeCache(path, false);
            zkNodeCache.setUpdateExecutor(executor);
            zkNodeCache.setUpdateListener(new Runnable() {
                @Override
                public void run() {
                    if (nodeListener != null) {
                        try {
                            nodeListener.onListener(path, zkNodeCache.getData() == null ? null : new String(zkNodeCache.getData(), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            nodeListener.onListener(path, zkNodeCache.getData() == null ? null : new String(zkNodeCache.getData()));
                        }
                    }
                }
            });
            try {
                zkNodeCache.startAndRefresh();
                this.nodeCaches.put(NODE_PREFIX + path, zkNodeCache);
                try {
                    if (zkNodeCache.getData() == null) {
                        return null;
                    }
                    return new String(zkNodeCache.getData(), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    return new String(zkNodeCache.getData());
                }
            } catch (Throwable e) {
                try {
                    zkNodeCache.stop();
                } catch (Throwable exception) {
                }
                zkNodeCache.setUpdateListener(null);
                zkNodeCache.setUpdateExecutor(null);
                throw new PradarException(e);
            }
        } else {
            ZkNodeCache zkNodeCache = (ZkNodeCache) this.nodeCaches.get(NODE_PREFIX + path);
            try {
                if (zkNodeCache.getData() == null) {
                    return null;
                }
                return new String(zkNodeCache.getData(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return new String(zkNodeCache.getData());
            }
        }
    }

    @Override
    public void destroy() {
        try {
            this.zkClient.stop();
        } catch (Throwable e) {
            logger.warn("zk-config-fetcher stop zkclient err!", e);
        }

        for (Map.Entry<String, Object> entry : this.nodeCaches.entrySet()) {
            if (entry.getValue() instanceof ZkNodeCache) {
                try {
                    ((ZkNodeCache) entry.getValue()).stop();
                } catch (Throwable e) {
                    logger.warn("zk-config-fetcher stop zk node err!{}", ((ZkNodeCache) entry.getValue()).getPath(), e);
                }
            } else if (entry.getValue() instanceof ZkPathChildrenCache) {
                try {
                    ((ZkPathChildrenCache) entry.getValue()).stop();
                } catch (Throwable e) {
                    logger.warn("zk-config-fetcher stop zk path err!{}", ((ZkPathChildrenCache) entry.getValue()).getPath(), e);
                }
            }
        }
        this.nodeCaches.clear();
        this.nodeCaches = null;
    }

    public interface PathListener {
        void onListen(String path, List<String> allList, List<String> addList, List<String> deleteList);
    }

    public interface NodeListener {
        void onListener(String path, String content);
    }

}
