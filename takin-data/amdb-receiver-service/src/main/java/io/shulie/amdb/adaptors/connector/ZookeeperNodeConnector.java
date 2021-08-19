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

package io.shulie.amdb.adaptors.connector;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import io.shulie.surge.data.common.pool.NamedThreadFactory;
import io.shulie.surge.data.common.utils.Bytes;
import io.shulie.surge.data.common.zk.ZkClient;
import io.shulie.surge.data.common.zk.ZkClientSpec;
import io.shulie.surge.data.common.zk.ZkNodeCache;
import io.shulie.surge.data.runtime.common.zk.NetflixCuratorZkClientFactory;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author vincent
 */
public class ZookeeperNodeConnector implements Connector {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperNodeConnector.class);

    private static final String ZK_SERVERS = System.getProperty("zookeeper.servers", "default.zookeeper:2181");
    private static final int CONNECTION_TIMEOUT = NumberUtils.toInt(System.getProperty("zookeeper.connection.timeout", "30000"));
    private static final int SESSION_TIMEOUT = NumberUtils.toInt(System.getProperty("zookeeper.session.timeout", "20000"));


    private ZkClient zkClient;

    private Map<String, ZkNodeCache> nodeCache = Maps.newHashMap();

    private Object lock= new Object();

    @Override
    public void init() throws Exception {
        init(ZK_SERVERS, CONNECTION_TIMEOUT, SESSION_TIMEOUT);
    }

    @Override
    public void init(int... ports) throws Exception {
        throw new UnsupportedOperationException("Method is not supported.");
    }

    @Override
    public void init(String zookeepers, int connectionTimeout, int sessionTimeout) throws Exception {
        ZkClientSpec spec = new ZkClientSpec(zookeepers);
        spec.setConnectionTimeoutMillis(connectionTimeout)
                .setSessionTimeoutMillis(sessionTimeout);
        try {
            this.zkClient = new NetflixCuratorZkClientFactory().create(spec);
        } catch (Exception e) {
            logger.error("zookeeper客户端初始化异常,请检查ZK集群连接是否正常", e);
            e.printStackTrace();
        }
        if (this.zkClient == null) {
            logger.error("zookeeper客户端初始化异常,请检查ZK集群连接是否正常");
        }
    }

    @Override
    public <T> void addPath(String path, Class<T> paramsClazz, Processor processor) throws Exception {
        try {
            // 只触发最新的一次 Update 更新
            final NamedThreadFactory threadFactory = new NamedThreadFactory("ZookeeperNodeConnector-" + path, true);
            ExecutorService executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                    Queues.<Runnable>newArrayBlockingQueue(1), threadFactory,
                    new ThreadPoolExecutor.DiscardOldestPolicy());
            // 已处理过的path进来后不再处理
            if (nodeCache.get(path) != null) {
                return;
            }
            ZkNodeCache zkNodeCache = zkClient.createZkNodeCache(path,false);
            zkNodeCache.setUpdateExecutor(executor);
            nodeCache.put(path, zkNodeCache);
            //初始化时先call线程，再发生变更后再call一次
            Thread callThread = new Thread() {
                @Override
                public void run() {
                    // FIXME CYF 锁优化
                    synchronized (lock) {
                        DataContext dataContext = processor.getContext();
                        dataContext.setPath(path);
                        try {
                            String body = Bytes.toString(zkClient.getData(path));
                            Object object = JSON.parseObject(body, paramsClazz);
                            dataContext.setModel(object);
                        } catch (Exception e) {
                            // 节点删除时执行
                            // 通知adaptor节点已删除，通过childPath+model同时为NULL即可确认
                            logger.error("解析ZK数据失败，path:{}，errorInfo:{}", path, e.getMessage());
                            if (nodeCache.get(path) != null) {
                                nodeCache.get(path).stop();
                                nodeCache.remove(path);
                            }
                        }
                        try {
                            processor.process(dataContext);
                        } catch (Exception e) {
                            logger.error("processor处理失败，path:{}，dataContext:{}", path, dataContext, e);
                        }
                    }
                }
            };
            callThread.start();
            zkNodeCache.setUpdateListener(callThread);
            zkNodeCache.startAndRefresh();
        } catch (Exception e) {
            throw new RuntimeException("fail to start heartbeat node for path:" + path, e);
        }
    }

    @Override
    public List<String> getChildrenPath(String path) throws Exception {
        createIfNotExistsDirectory(path);
        return zkClient.listChildren(path);
    }


    /**
     * @param path
     */
    private void createIfNotExistsDirectory(String path) {
        try {
            zkClient.ensureDirectoryExists(path);
        } catch (Exception e) {
            logger.error("createIfNotExistsDirectory err!", e);
        }
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public boolean close() throws Exception {
        for (ZkNodeCache zkNodeCache : nodeCache.values()) {
            try {
                zkNodeCache.stop();
            } catch (Exception e) {
                logger.error("instance cache start failed");
            }
        }
        return false;
    }

    @Override
    public ConnectorType getType() {
        return ConnectorType.ZOOKEEPER_NODE;
    }
}
