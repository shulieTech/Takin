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

package io.shulie.surge.data.runtime.common.zk;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.ExponentialBackoffRetry;
import io.shulie.surge.data.common.pool.NamedThreadFactory;
import io.shulie.surge.data.common.zk.ZkClient;
import io.shulie.surge.data.common.zk.impl.NetflixCuratorZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author fabing.zhaofb
 */
@Singleton
public class CuratorClientProvider implements Provider<ZkClient> {
    private static final Logger logger = LoggerFactory.getLogger(CuratorClientProvider.class);
    private ZkClient singleton;

    @Inject
    public CuratorClientProvider(@Named("config.data.zk.servers") String zkServers,
                                 @Named("config.data.zk.sessionTimeoutMillis") int sessionTimeout,
                                 @Named("config.data.zk.connTimeoutMillis") int connectionTimeout) {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(zkServers)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .connectionTimeoutMs(sessionTimeout)
                .sessionTimeoutMs(connectionTimeout)
                .threadFactory(new NamedThreadFactory("curator", true))
                .build();
        client.start();
        logger.info("ZkClient started: {}", zkServers);

        this.singleton = new NetflixCuratorZkClient(client, zkServers);
    }

    @Override
    public ZkClient get() {
        return singleton;
    }
}
