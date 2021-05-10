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
package com.pamirs.attach.plugin.redisson.factory;

import com.pamirs.attach.plugin.common.datasource.redisserver.RedisServerNodesStrategy;
import com.pamirs.attach.plugin.redisson.RedissonConstants;
import com.pamirs.attach.plugin.redisson.utils.RedissonUtils;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.api.RedissonRxClient;
import org.redisson.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Auther: vernon
 * @Date: 2020/11/26 14:32
 * @Description:
 */
public class RedissonNodesStrategy implements RedisServerNodesStrategy {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<String> match(Object client) {
        if (client == null) {
            return null;
        }

        Config config = null;
        if (client instanceof RedissonClient) {
            RedissonClient redissonClient = (RedissonClient) client;
            config = redissonClient.getConfig();

        } else if (client instanceof RedissonRxClient) {
            RedissonRxClient redissonRxClient = (RedissonRxClient) client;
            config = redissonRxClient.getConfig();

        } else if (client instanceof RedissonReactiveClient) {
            RedissonReactiveClient redissonReactiveClient = (RedissonReactiveClient) client;
            config = redissonReactiveClient.getConfig();
        }

        List<String> addrs = getAddress(config);

        return addrs;

    }

    public static List getAddress(Config config) {

        SingleServerConfig singleServerConfig = null;
        try {
            singleServerConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_SINGLE_SERVER_CONFIG);
        } catch (ReflectException e) {
        }
        ClusterServersConfig clusterServersConfig = null;
        try {
            clusterServersConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_CLUSTER_SERVERS_CONFIG);
        } catch (ReflectException e) {
        }
        SentinelServersConfig sentinelServersConfig = null;
        try {
            sentinelServersConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_SENTINEL_SERVERS_CONFIG);
        } catch (ReflectException e) {
        }
        ReplicatedServersConfig replicatedServersConfig = null;
        try {
            replicatedServersConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_REPLICATED_SERVERS_CONFIG);
        } catch (ReflectException e) {
        }
        MasterSlaveServersConfig masterSlaveServersConfig = null;
        try {
            masterSlaveServersConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_MASTER_SLAVE_SERVERS_CONFIG);
        } catch (ReflectException e) {
        }
        if (singleServerConfig != null) {
            return RedissonUtils.removePre(singleServerConfig.getAddress());
        } else if (clusterServersConfig != null) {
            return RedissonUtils.removePre(clusterServersConfig.getNodeAddresses());
        } else if (sentinelServersConfig != null) {
            return RedissonUtils.removePre(sentinelServersConfig.getSentinelAddresses());
        } else if (replicatedServersConfig != null) {
            return RedissonUtils.removePre(replicatedServersConfig.getNodeAddresses());
        } else if (masterSlaveServersConfig != null) {
            String master = masterSlaveServersConfig.getMasterAddress();
            Set<String> slave = masterSlaveServersConfig.getSlaveAddresses();
            List<String> result = new ArrayList(RedissonUtils.removePre(slave));
            result.addAll(RedissonUtils.removePre(master));
            return result;

        }
        return null;
    }
}
