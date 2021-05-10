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

import com.pamirs.attach.plugin.common.datasource.redisserver.AbstractRedisServerFactory;
import com.pamirs.attach.plugin.common.datasource.redisserver.RedisClientMediator;
import com.pamirs.attach.plugin.redisson.RedissonConstants;
import com.pamirs.attach.plugin.redisson.RedissonVersion;
import com.pamirs.attach.plugin.redisson.utils.RedissonUtils;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.Throwables;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.internal.config.ShadowRedisConfig;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Auther: vernon
 * @Date: 2020/11/26 14:29
 * @Description:
 */
public class RedissonFactory extends AbstractRedisServerFactory {


    static private Logger logger = LoggerFactory.getLogger(RedissonFactory.class);

    static private AbstractRedisServerFactory factory = new RedissonFactory();


    public RedissonFactory() {
        super(new RedissonNodesStrategy());
    }


    public static class RedissonHolder {
        public static AbstractRedisServerFactory getFactory() {
            return factory;
        }
    }


    @Override
    public Object security(Object o) {
        return o;
    }

    @Override
    protected RedisClientMediator createMediator(Object obj, ShadowRedisConfig shadowRedisConfig) {
        try {
            Config oldConfig = null;
            Config newConfig = null;
            Object client = null;
            Object shadowClient = null;

            if (RedissonClient.class.isAssignableFrom(obj.getClass())) {
                client = (RedissonClient) obj;
                oldConfig = ((RedissonClient) client).getConfig();
                newConfig = generate(oldConfig, shadowRedisConfig);
                shadowClient = Redisson.create(newConfig);

            } else if (RedissonReactiveClient.class.isAssignableFrom(obj.getClass())) {
                client = (RedissonReactiveClient) obj;
                oldConfig = ((RedissonReactiveClient) client).getConfig();
                newConfig = generate(oldConfig, shadowRedisConfig);
                shadowClient = Redisson.createReactive(newConfig);

            }
            RedisClientMediator mediator = new RedisClientMediator(obj, shadowClient);
            putMediator(obj, mediator);
            return mediator;
        } catch (Throwable e) {
            logger.error(Throwables.getStackTraceAsString(e));

            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.RedisServer)
                    .setDetail("build redisson shadow db error.")
                    .setMessage(Throwables.getStackTraceAsString(e))
                    .report();
        }
        return null;
    }

    @Override
    public void clean(ShadowRedisConfig config) {
        Map<Object, RedisClientMediator<?>> mediators = getMediators();
        String master = config.getMaster();
        List<String> nodes = config.getNodes().contains(",")
                ? Arrays.asList(StringUtils.split(config.getNodes(), ",")) : Arrays.asList(config.getNodes());
        nodes = new ArrayList<String>(nodes);
        if (StringUtils.isNotBlank(master)) {
            nodes.add(master);
        }
        if (mediators == null) {
            return;
        }
        for (Map.Entry entry : mediators.entrySet()) {
            RedisClientMediator value = (RedisClientMediator) entry.getValue();
            if (value == null) {
                continue;
            }
            Object performanceClient = value.getPerformanceRedisClient();
            if (performanceClient == null) {
                continue;
            }

            List<String> addrs = null;
            if (performanceClient instanceof RedissonClient) {
                RedissonClient client = (RedissonClient) performanceClient;
                addrs = getAddr(client.getConfig());
                if (equals(nodes, addrs)) {
                    remove(entry.getKey());
                    if (!client.isShutdown() && !client.isShuttingDown()) {
                        client.shutdown();
                    }
                    break;
                }
            } else if (performanceClient instanceof RedissonReactiveClient) {
                RedissonReactiveClient client = (RedissonReactiveClient) performanceClient;
                addrs = getAddr(client.getConfig());
                if (equals(nodes, addrs)) {
                    remove(entry.getKey());
                    if (!client.isShutdown() && !client.isShuttingDown()) {
                        client.shutdown();
                    }
                    break;
                }
            } else {
                logger.error("unsupported redisson client ,disable all registerd shadow server..");
                //  clear();
            }
        }
    }

    public boolean equals(List<String> var1, List<String> var2) {
        if (CollectionUtils.isEmpty(var1) || CollectionUtils.isEmpty(var2)) {
            return false;
        }
        return var1.containsAll(var2) && var2.containsAll(var1);
    }

    public List<String> getAddr(Config config) {
        SingleServerConfig singleServerConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_SINGLE_SERVER_CONFIG);
        ClusterServersConfig clusterServersConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_CLUSTER_SERVERS_CONFIG);
        SentinelServersConfig sentinelServersConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_SENTINEL_SERVERS_CONFIG);
        ReplicatedServersConfig replicatedServersConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_REPLICATED_SERVERS_CONFIG);
        MasterSlaveServersConfig masterSlaveServersConfig = Reflect.on(config).get(RedissonConstants.DYNAMIC_FIELD_MASTER_SLAVE_SERVERS_CONFIG);
        if (singleServerConfig != null) {
            return RedissonUtils.removePre(singleServerConfig.getAddress());
        } else if (clusterServersConfig != null) {
            return RedissonUtils.removePre(clusterServersConfig.getNodeAddresses());
        } else if (replicatedServersConfig != null) {
            return RedissonUtils.removePre(replicatedServersConfig.getNodeAddresses());
        } else if (masterSlaveServersConfig != null) {
            Set slave = masterSlaveServersConfig.getSlaveAddresses();
            List<String> result = new ArrayList(RedissonUtils.removePre(slave));
            Object master = masterSlaveServersConfig.getMasterAddress();
            result.addAll(RedissonUtils.removePre(master));
            return result;
        }
        return null;
    }

    /**
     * generate new Config
     *
     * @param oldConfig
     * @return
     */
    public Config generate(Config oldConfig, ShadowRedisConfig shadowRedisConfig) {

        String master = RedissonUtils.addPre(shadowRedisConfig.getMaster());
        String nodeStr = shadowRedisConfig.getNodes();
        Integer database = shadowRedisConfig.getDatabase();
        List<String> nodes = nodeStr.contains(",")
                ? Arrays.asList(nodeStr.split(","))
                : Arrays.asList(nodeStr);

        nodes = RedissonUtils.addPre(nodes);
        String[] nodesArrays = nodes.toArray(new String[nodes.size()]);
        String passwd = shadowRedisConfig.getPassword() != null
                && !"null".equals(shadowRedisConfig.getPassword()) ? shadowRedisConfig.getPassword() : null;

        Config newConfig = initBaseConfig(oldConfig);
        SingleServerConfig singleServerConfig = Reflect.on(oldConfig).get(RedissonConstants.DYNAMIC_FIELD_SINGLE_SERVER_CONFIG);
        ClusterServersConfig clusterServersConfig = Reflect.on(oldConfig).get(RedissonConstants.DYNAMIC_FIELD_CLUSTER_SERVERS_CONFIG);
        SentinelServersConfig sentinelServersConfig = Reflect.on(oldConfig).get(RedissonConstants.DYNAMIC_FIELD_SENTINEL_SERVERS_CONFIG);
        ReplicatedServersConfig replicatedServersConfig = Reflect.on(oldConfig).get(RedissonConstants.DYNAMIC_FIELD_REPLICATED_SERVERS_CONFIG);
        MasterSlaveServersConfig masterSlaveServersConfig = Reflect.on(oldConfig).get(RedissonConstants.DYNAMIC_FIELD_MASTER_SLAVE_SERVERS_CONFIG);
        if (singleServerConfig != null) {
            newConfig.useSingleServer()
                    .setAddress(nodes.get(0))
                    .setIdleConnectionTimeout(oldConfig.useSingleServer().getIdleConnectionTimeout())
                    .setConnectTimeout(oldConfig.useSingleServer().getConnectTimeout())
                    .setConnectionPoolSize(oldConfig.useSingleServer().getConnectionPoolSize())
                    .setTimeout(oldConfig.useSingleServer().getTimeout())
                    .setClientName(Pradar.CLUSTER_TEST_PREFIX + oldConfig.useSingleServer().getClientName())
                    .setDnsMonitoringInterval(oldConfig.useSingleServer().getDnsMonitoringInterval());
            if (passwd != null) {
                newConfig.useSingleServer().setPassword(passwd);
            }
            if (database != null) {
                newConfig.useSingleServer().setDatabase(database);
            }
        } else if (clusterServersConfig != null) {
            newConfig.useClusterServers()
                    .addNodeAddress(nodesArrays)
                    .setClientName(Pradar.CLUSTER_TEST_PREFIX + oldConfig.useClusterServers().getClientName())
            ;
            if (passwd != null) {
                newConfig.useClusterServers().setPassword(passwd);
            }
        } else if (sentinelServersConfig != null) {
            newConfig.useSentinelServers()
                    .addSentinelAddress(nodesArrays)
                    .setMasterName(sentinelServersConfig.getMasterName())
                    .setClientName(Pradar.CLUSTER_TEST_PREFIX + sentinelServersConfig.getClientName())
                    .setConnectTimeout(sentinelServersConfig.getConnectTimeout())
                    .setIdleConnectionTimeout(sentinelServersConfig.getIdleConnectionTimeout())
                    .setLoadBalancer(sentinelServersConfig.getLoadBalancer())
                    .setTimeout(sentinelServersConfig.getTimeout())
                    .setSlaveConnectionMinimumIdleSize(sentinelServersConfig.getSlaveConnectionMinimumIdleSize())
                    .setRetryInterval(sentinelServersConfig.getRetryInterval())
                    .setMasterConnectionMinimumIdleSize(sentinelServersConfig.getMasterConnectionMinimumIdleSize())
                    .setMasterConnectionPoolSize(sentinelServersConfig.getMasterConnectionPoolSize())
                    .setSlaveConnectionMinimumIdleSize(sentinelServersConfig.getSlaveConnectionMinimumIdleSize())
                    .setSlaveConnectionPoolSize(sentinelServersConfig.getSlaveConnectionPoolSize());
            //过滤检查
            setCheckSentinelsList(newConfig.useSentinelServers());
            if (master != null && !"null".equals(master)) {
                newConfig.useSentinelServers().addSentinelAddress(master);
            }
            if (passwd != null) {
                newConfig.useSentinelServers().setPassword(passwd);
            }
            if (database != null) {
                newConfig.useSentinelServers().setDatabase(database);
            }
        } else if (replicatedServersConfig != null) {
            newConfig.useReplicatedServers()
                    .addNodeAddress(nodesArrays);
            if (passwd != null) {
                newConfig.useReplicatedServers().setPassword(passwd);
            }
            if (database != null) {
                newConfig.useReplicatedServers().setDatabase(database);
            }
        } else if (masterSlaveServersConfig != null) {
            newConfig.useMasterSlaveServers()
                    .setMasterAddress(master);
            newConfig.useMasterSlaveServers().addSlaveAddress(nodesArrays);
            if (database != null) {
                newConfig.useMasterSlaveServers().setDatabase(database);
            }
        }
        return newConfig;

    }


    private Config initBaseConfig(Config oldConf) {
        Config newBaseConfig = new Config();
        newBaseConfig.setExecutor(oldConf.getExecutor());
        newBaseConfig.setCodec(oldConf.getCodec());
        newBaseConfig.setKeepPubSubOrder(oldConf.isKeepPubSubOrder());
        newBaseConfig.setLockWatchdogTimeout(oldConf.getLockWatchdogTimeout());
        newBaseConfig.setNettyThreads(oldConf.getNettyThreads());
        newBaseConfig.setThreads(oldConf.getThreads());
        newBaseConfig.setCodec(oldConf.getCodec());
        newBaseConfig.setEventLoopGroup(oldConf.getEventLoopGroup());

        return newBaseConfig;
    }

    private void setCheckSentinelsList(SentinelServersConfig config) {
        Method method = null;
        try {
            method = config.getClass().getDeclaredMethod("setCheckSentinelsList", boolean.class);
            if (method != null) {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                    try {
                        RedissonVersion.setLowVersion(false);
                        method.invoke(config, false);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (NoSuchMethodException e) {
            //ignore
            return;
        }
    }
}
