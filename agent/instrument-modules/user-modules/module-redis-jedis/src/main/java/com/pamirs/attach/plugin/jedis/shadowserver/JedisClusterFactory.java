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
import redis.clients.jedis.JedisSlotBasedConnectionHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * JedisCluster 工厂
 *
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.jedis.redisserver
 * @Date 2020/11/27 4:34 下午
 */
public class JedisClusterFactory extends AbstractRedisServerFactory<JedisSlotBasedConnectionHandler> {

    private static final JedisClusterFactory jedisFactory = new JedisClusterFactory();

    private JedisClusterFactory() {
        super(new JedisClusterNodesStrategy());
    }

    public static JedisClusterFactory getFactory() {
        return jedisFactory;
    }

    @Override
    public <T> T security(T client) {
        return client;
    }

    @Override
    public RedisClientMediator<JedisSlotBasedConnectionHandler> createMediator(Object connection, ShadowRedisConfig shadowConfig) {
        RedisClientMediator<JedisSlotBasedConnectionHandler> mediator = null;
        if (connection instanceof JedisSlotBasedConnectionHandler) {
            JedisConstructorConfig jedisConfig = RedisConstants.jedisInstance.get(connection);
            if (jedisConfig == null) {
                return null;
            }


            JedisSlotBasedConnectionHandler pressureJedisPool = null;
            if (200 == jedisConfig.getConstructorType()) {
                pressureJedisPool = new JedisSlotBasedConnectionHandler(convert(shadowConfig.getNodeNums()),
                        jedisConfig.getPoolConfig(), jedisConfig.getConnectionTimeout(), jedisConfig.getSoTimeout(),
                        shadowConfig.getPassword(), jedisConfig.getClientName(), jedisConfig.isSsl(),
                        jedisConfig.getSslSocketFactory(), jedisConfig.getSslParameters(), jedisConfig.getHostnameVerifier(),
                        jedisConfig.getJedisMap());
            } else if (201 == jedisConfig.getConstructorType()) {
                pressureJedisPool = new JedisSlotBasedConnectionHandler(convert(shadowConfig.getNodeNums()),
                        jedisConfig.getPoolConfig(), jedisConfig.getConnectionTimeout(), jedisConfig.getSoTimeout(),
                        shadowConfig.getPassword());
            }
            if (null != pressureJedisPool) {
                mediator = new RedisClientMediator<JedisSlotBasedConnectionHandler>(pressureJedisPool, (JedisSlotBasedConnectionHandler) connection, true);
            }
        }
        return mediator;
    }


    @Override
    public void clearAll(IEvent event) {
        clear();
        RedisConstants.registerShadowNodes.clear();
    }

    public Set<HostAndPort> convert(List<String> nodes) {
        Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
        for (String node : nodes) {
            String host = node.substring(0, node.indexOf(":"));
            int port = Integer.parseInt(node.substring(node.indexOf(":") + 1));
            HostAndPort hostAndPort = new HostAndPort(host, port);
            hostAndPorts.add(hostAndPort);
        }
        return hostAndPorts;
    }
}
