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
package com.pamirs.attach.plugin.jedis.shadowserver;

import com.pamirs.attach.plugin.common.datasource.redisserver.AbstractRedisServerFactory;
import com.pamirs.attach.plugin.common.datasource.redisserver.RedisClientMediator;
import com.pamirs.attach.plugin.jedis.RedisConstants;
import com.pamirs.attach.plugin.jedis.util.JedisConstructorConfig;
import com.pamirs.pradar.pressurement.agent.event.IEvent;
import com.pamirs.pradar.internal.config.ShadowRedisConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Jedis 工厂
 *
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.jedis.redisserver
 * @Date 2020/11/27 4:34 下午
 */
public class JedisSentinelFactory extends AbstractRedisServerFactory<JedisSentinelPool> {

    private static final JedisSentinelFactory jedisFactory = new JedisSentinelFactory();

    private JedisSentinelFactory() {
        super(new JedisSentinelNodesStrategy());
    }

    public static JedisSentinelFactory getFactory() {
        return jedisFactory;
    }

    @Override
    public <T> T security(T client) {
        return client;
    }

    @Override
    public RedisClientMediator<JedisSentinelPool> createMediator(Object client, ShadowRedisConfig shadowConfig) {
        RedisClientMediator<JedisSentinelPool> mediator = null;
        if (client instanceof JedisSentinelPool) {
            JedisConstructorConfig jedisConfig = RedisConstants.jedisInstance.get(client);
            if (jedisConfig == null) {
                return null;
            }

            HostAndPort next = next(shadowConfig.getNodeNums());
            if (next == null) {
                return null;
            }

            JedisSentinelPool pressureJedisPool = null;
            if (100 == jedisConfig.getConstructorType()) {
                pressureJedisPool = new JedisSentinelPool(jedisConfig.getMaster(),
                        new HashSet<String>(shadowConfig.getNodeNums()), jedisConfig.getPoolConfig(),
                        jedisConfig.getConnectionTimeout(), jedisConfig.getSoTimeout(),
                        shadowConfig.getPassword(), jedisConfig.getDatabase(), jedisConfig.getClientName());
            }
            if (null != pressureJedisPool) {
                mediator = new RedisClientMediator<JedisSentinelPool>(pressureJedisPool, (JedisSentinelPool) client, true);
            }
        }
        return mediator;
    }

    @Override
    public void clearAll(IEvent event) {
        clear();
        RedisConstants.registerShadowNodes.clear();
    }

    private AtomicInteger index = new AtomicInteger(0);

    private HostAndPort next(List<String> nodes) {
        for (String node : nodes) {
            if (!RedisConstants.registerShadowNodes.contains(node)) {
                return createNode(node);
            }
        }
        // TODO 该怎么做？有可能会遇到业务IP * 3,影子IP * 2的情况
        // 目前轮训选取
        return createNode(nodes.get(index.getAndIncrement() % nodes.size()));
    }

    public HostAndPort createNode(String node) {
        String host = node.substring(0, node.indexOf(":"));
        int port = Integer.parseInt(node.substring(node.indexOf(":") + 1));
        RedisConstants.registerShadowNodes.add(node);
        return new HostAndPort(host, port);
    }
}
