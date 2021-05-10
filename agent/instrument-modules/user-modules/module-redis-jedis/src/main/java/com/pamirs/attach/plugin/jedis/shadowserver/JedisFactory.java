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
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.pressurement.agent.event.IEvent;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.pamirs.pradar.internal.config.ShadowRedisConfig;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Jedis 工厂
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.jedis.redisserver
 * @Date 2020/11/27 4:34 下午
 */
public class JedisFactory extends AbstractRedisServerFactory<JedisPool> {

    private static final JedisFactory jedisFactory = new JedisFactory();

    private JedisFactory(){
        super(new JedisMatchStrategy(new JedisNodesStrategy()));
    }

    private final Map<String, String> configMaps = new HashMap<String, String>();

    public static JedisFactory getFactory() {
        return jedisFactory;
    }

    @Override
    public boolean doBefore() {
        return Pradar.isClusterTest();
    }

    @Override
    public <T> T security(T client) {
        return client;
    }

    @Override
    public RedisClientMediator<JedisPool> createMediator(Object client, ShadowRedisConfig shadowConfig) {
        RedisClientMediator<JedisPool> mediator = null;
        if (client instanceof JedisPool) {
            JedisConstructorConfig jedisConfig = RedisConstants.jedisInstance.get(client);
            if (jedisConfig == null) {
                return null;
            }

            // 兼容主从模式
            HostAndPort next = null;
            if (StringUtils.isNotBlank(shadowConfig.getMaster())) {
                next = electionMaster(jedisConfig, shadowConfig);
                if (next == null) {
                    next = next(shadowConfig.getNodeNums());
                }
            } else {
                next = next(shadowConfig.getNodeNums());
            }

            if (next == null) {
                return null;
            }

            
            JedisPool pressureJedisPool = null;
            if (1 == jedisConfig.getConstructorType()) {
                //JedisPool(final String host) 1
                pressureJedisPool = new JedisPool(next.getHost());
            } else if (2 == jedisConfig.getConstructorType()) {
                // 4
                //JedisPool(final String host, final SSLSocketFactory sslSocketFactory,final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier)
                pressureJedisPool = new JedisPool(next.getHost(), jedisConfig.getSslSocketFactory(), jedisConfig.getSslParameters(),jedisConfig.getHostnameVerifier());
            } else if (3 == jedisConfig.getConstructorType()) {
                // 8
                //JedisPool(final GenericObjectPoolConfig poolConfig,
                //final String host, int port,final int connectionTimeout,
                //final int soTimeout, final String password, final int database,final String clientName)
                pressureJedisPool = new JedisPool(jedisConfig.getPoolConfig(), next.getHost(), next.getPort(),jedisConfig.getConnectionTimeout(), jedisConfig.getSoTimeout(), shadowConfig.getPassword(), jedisConfig.getDatabase(), jedisConfig.getClientName());
            } else if (4 == jedisConfig.getConstructorType()) {
                // 12
                //JedisPool(final GenericObjectPoolConfig poolConfig, final String host,
                //int port,final int connectionTimeout, final int soTimeout,
                //final String password, final int database,final String clientName,
                //final boolean ssl, final SSLSocketFactory sslSocketFactory,
                //final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier)
                pressureJedisPool = new JedisPool(jedisConfig.getPoolConfig(), next.getHost(), next.getPort(), jedisConfig.getConnectionTimeout(), jedisConfig.getSoTimeout(), shadowConfig.getPassword(), jedisConfig.getDatabase(), jedisConfig.getClientName(), jedisConfig.isSsl(), jedisConfig.getSslSocketFactory(), jedisConfig.getSslParameters(), jedisConfig.getHostnameVerifier());
            } else if (5 == jedisConfig.getConstructorType()) {
                // 4
                //JedisPool(final GenericObjectPoolConfig poolConfig, final URI uri,final int connectionTimeout, final int soTimeout)
                pressureJedisPool = new JedisPool(jedisConfig.getPoolConfig(), jedisConfig.getUri(), jedisConfig.getConnectionTimeout(), jedisConfig.getSoTimeout());
            } else if (6 == jedisConfig.getConstructorType()) {
                // 7
                //JedisPool(final GenericObjectPoolConfig poolConfig, final URI uri,
                //final int connectionTimeout, final int soTimeout, final SSLSocketFactory sslSocketFactory,
                //final SSLParameters sslParameters, final HostnameVerifier hostnameVerifier)
                pressureJedisPool = new JedisPool(jedisConfig.getPoolConfig(), jedisConfig.getUri(), jedisConfig.getConnectionTimeout(), jedisConfig.getSoTimeout(), jedisConfig.getSslSocketFactory(), jedisConfig.getSslParameters(), jedisConfig.getHostnameVerifier());
            }
            if (null != pressureJedisPool) {
                if (StringUtils.isNotBlank(shadowConfig.getMaster())) {
                    for (String nodeNum : shadowConfig.getNodeNums()) {
                        configMaps.put(nodeNum, shadowConfig.getMaster());
                    }
                }
                mediator = new RedisClientMediator<JedisPool>(pressureJedisPool, (JedisPool) client, true);
            }
        }
        return mediator;
    }

    public HostAndPort electionMaster(JedisConstructorConfig jedisConfig, ShadowRedisConfig shadowConfig){
        for (Map.Entry<String, ShadowRedisConfig> entry : GlobalConfig.getInstance().getShadowRedisConfigs().entrySet()) {
            if (entry.getValue() == shadowConfig) {
                String configKey = entry.getKey();
                List<String> configKeys = configKey.contains(",")
                        ? Arrays.asList(StringUtils.split(configKey,','))
                        : Collections.singletonList(configKey);
                if (configKeys.get(0).equals(getKey(jedisConfig))) {
                    return createNode(shadowConfig.getMaster());
                }
            }
        }
        return null;
    }

    public HostAndPort getMaster(String key){
        String value = configMaps.get(key);
        if (value == null) {
            return null;
        }
        return createNode(value);
    }


    @Override
    public void clearAll(IEvent event) {
        clear();
        RedisConstants.registerShadowNodes.clear();
    }

    private AtomicInteger index = new AtomicInteger(0);

    private HostAndPort next(List<String> nodes){
        for (String node : nodes) {
            if (!RedisConstants.registerShadowNodes.contains(node)) {
                return createNode(node);
            }
        }
        // TODO 该怎么做？有可能会遇到业务IP * 3,影子IP * 2的情况
        // 目前轮训选取
        return createNode(nodes.get(index.getAndIncrement() % nodes.size()));
    }

    public HostAndPort createNode(String node){
        String host = node.substring(0, node.indexOf(":"));
        int port = Integer.parseInt(node.substring(node.indexOf(":") + 1));
        RedisConstants.registerShadowNodes.add(node);
        return new HostAndPort(host, port);
    }

    public String getKey(JedisConstructorConfig jedisConfig){
        return jedisConfig.getHost() + ":" + jedisConfig.getPort();
    }

}
