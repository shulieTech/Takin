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
package com.shulie.instrument.simulator.agent.core.zk.impl;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import com.shulie.instrument.simulator.agent.core.zk.ZkClient;
import org.apache.commons.lang.StringUtils;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ThreadFactory;

/**
 * zkClient 工厂
 *
 * @author xiaobin@shulie.io
 * @since 1.0.0
 */
public class NetflixCuratorZkClientFactory {

    private static final Logger logger = LoggerFactory.getLogger(NetflixCuratorZkClientFactory.class);

    private static Cache<String, ZkClient> cache = CacheBuilder.newBuilder().build();

    private static final class NetflixCuratorZkClientFactoryHolder {
        public final static NetflixCuratorZkClientFactory INSTANCE = new NetflixCuratorZkClientFactory();
    }

    public static NetflixCuratorZkClientFactory getInstance() {
        return NetflixCuratorZkClientFactoryHolder.INSTANCE;
    }

    private NetflixCuratorZkClientFactory() {
    }

    public ZkClient create(final ZkClientSpec spec) throws Exception {
        if (StringUtils.isBlank(spec.getZkServers())) {
            throw new RuntimeException("zookeeper servers is empty.");
        }
        return cache.get(spec.getZkServers(), new Callable<ZkClient>() {
            @Override
            public ZkClient call() throws Exception {
                String path = ZooKeeper.class.getProtectionDomain().getCodeSource().getLocation().toString();
                logger.info("Load ZooKeeper from {}", path);

                CuratorFramework client = CuratorFrameworkFactory.builder()
                        .connectString(spec.getZkServers())
                        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                        .connectionTimeoutMs(spec.getConnectionTimeoutMillis())
                        .sessionTimeoutMs(spec.getSessionTimeoutMillis())
                        .threadFactory(new ThreadFactory() {
                            @Override
                            public Thread newThread(Runnable r) {
                                return new Thread(r, spec.getThreadName());
                            }
                        })
                        .build();
                client.start();
                logger.info("ZkClient started: {}", spec.getZkServers());

                NetflixCuratorZkClient theClient = new NetflixCuratorZkClient(client, spec.getZkServers());
                return theClient;
            }
        });
    }
}
