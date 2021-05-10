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
package com.pamirs.attach.plugin.lettuce.shadowserver;

import com.pamirs.attach.plugin.common.datasource.redisserver.AbstractRedisServerFactory;
import com.pamirs.attach.plugin.common.datasource.redisserver.RedisClientMediator;
import com.pamirs.attach.plugin.lettuce.LettuceConstants;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.internal.config.ShadowRedisConfig;
import com.pamirs.pradar.pressurement.agent.event.IEvent;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import io.lettuce.core.AbstractRedisClient;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.codec.RedisCodec;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.lettuce.factory
 * @Date 2020/11/26 11:26 上午
 */
public class LettuceFactory extends AbstractRedisServerFactory<AbstractRedisClient> {

    private final static Logger LOGGER = LoggerFactory.getLogger(LettuceFactory.class);

    private static final LettuceFactory factory = new LettuceFactory();

    private LettuceFactory() {
        super(new LettuceNodesStrategy());
    }

    public static LettuceFactory getFactory() {
        return factory;
    }

    private final List<Object> lettucePools = new ArrayList<Object>();

    private final Map<Object, Object> connectionMap = new ConcurrentHashMap<Object, Object>();

    @Override
    public <T> T security(T client) {
        return client;
    }

    /**
     * 重写该方法是为了保证在影子库模式下必须初始化成功影子Client
     * 业务流量也必须初始化成功，最终以LettuceConnectionMediator返回的为准
     * 原因是：RedisClient、RedisCluster只会调用一次connect方法。并将该对象维护在Map<String, GenericObjectPool<Jedis>> pools池对象中。
     * Spring-Lettuce后续会从缓存池中使用该对象。返回的对象必须包裹一层出去
     *
     * @param client
     * @param <T>
     * @return
     */
    @Override
    public <T> T getClient(T client) {
        if (!GlobalConfig.getInstance().isShadowDbRedisServer()) {
            return client;
        }
        RedisClientMediator<T> mediator = getMediator(client);
        if (mediator == null) {
            // 抛出相关异常信息
            throw new PressureMeasureError(" get redis shadow server error.");

        }
        return security(mediator.getPerformanceRedisClient());
    }

    public Object getClient(Object target, String method, Object[] args, Object result) {
        AbstractRedisClient abstractRedisClient = (AbstractRedisClient) getClient(target);
        if (!RedisClientMediator.isShadowDb()) {
            return result;
        }
        if (abstractRedisClient instanceof RedisClusterClient) {
            RedisClusterClient client = (RedisClusterClient) abstractRedisClient;
            Object performanceClient = redisClusterConnect(client, method, args);
            if (performanceClient instanceof StatefulRedisClusterConnection) {
                LettuceClusterConnectionMediator mediator = new LettuceClusterConnectionMediator((StatefulRedisClusterConnection) performanceClient, (StatefulRedisClusterConnection) result);
                LettuceFactory.getFactory().add(mediator);
                return mediator;
            } else if (performanceClient instanceof StatefulRedisConnection) {
                LettuceConnectionMediator mediator = new LettuceConnectionMediator((StatefulRedisConnection) performanceClient, (StatefulRedisConnection) result);
                LettuceFactory.getFactory().add(mediator);
                return mediator;
            }
        } else if (abstractRedisClient instanceof RedisClient) {
            RedisClient client = (RedisClient) abstractRedisClient;
            Object performanceClient = redisClientConnect(client, method, args);
            if (performanceClient instanceof StatefulRedisConnection) {
                LettuceConnectionMediator mediator = new LettuceConnectionMediator((StatefulRedisConnection) redisClientConnect(client, method, args), (StatefulRedisConnection) result);
                LettuceFactory.getFactory().add(mediator);
                return mediator;
            }
        }
        return result;
    }

    public Object dynamic(Object object) {
        Object o = connectionMap.get(object);
        if (o != null) {
            return o;
        }

        Object target = manager.removeField(object, LettuceConstants.DYNAMIC_FIELD_LETTUCE_TARGET);
        String method = manager.removeField(object, LettuceConstants.DYNAMIC_FIELD_LETTUCE_METHOD);
        Object[] args = manager.removeField(object, LettuceConstants.DYNAMIC_FIELD_LETTUCE_ARGS);
        Object result = manager.removeField(object, LettuceConstants.DYNAMIC_FIELD_LETTUCE_RESULT);
        RedisClientMediator mediator = (RedisClientMediator) getClient(target, method, args, result);
        Object performanceRedisClient = mediator.getPerformanceRedisClient();
        connectionMap.put(target, performanceRedisClient);
        return performanceRedisClient;
    }

    @Override
    public RedisClientMediator<AbstractRedisClient> createMediator(Object obj, ShadowRedisConfig shadowRedisConfig) {

        AbstractRedisClient client = (AbstractRedisClient) obj;
        RedisClientMediator<AbstractRedisClient> mediator = null;
        try {

            if (client instanceof RedisClusterClient) {
                RedisClusterClient redisClient = (RedisClusterClient) client;
                Iterable<RedisURI> redisURIS = Reflect.on(redisClient).call("getInitialUris").get();
                Iterable<RedisURI> performanceRedisUris = performanceRedisUris(redisURIS, shadowRedisConfig);
                // redisURIS需要修改ip:port等信息
                RedisClusterClient performanceRedisClient = RedisClusterClient.create(redisClient.getResources(), performanceRedisUris);
                mediator = new RedisClientMediator<AbstractRedisClient>(performanceRedisClient, client, true);

            } else if (client instanceof RedisClient) {
                RedisClient redisClient = (RedisClient) client;
                RedisURI redisURI = Reflect.on(redisClient).get("redisURI");


                // 单机redisUri，如果控制台配置多个，只取第一个node节点
                RedisURI performanceRedisUri = performanceRedisUri(redisURI, shadowRedisConfig.getNodeNums(), 0, shadowRedisConfig.getDatabase(), shadowRedisConfig.getPassword());
                RedisClient performanceRedisClient = RedisClient.create(redisClient.getResources(), performanceRedisUri);
                mediator = new RedisClientMediator<AbstractRedisClient>(performanceRedisClient, client, true);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.RedisServer)
                    .setErrorCode("redisServer-0001")
                    .setMessage("redis server lettuce error！")
                    .setDetail(ExceptionUtils.getStackTrace(e))
                    .report();
        }

        return mediator;
    }

    public void add(Object value) {
        lettucePools.add(value);
    }

    @Override
    public void clearAll(IEvent event) {
        try {
            synchronized (monitLock) {
                for (Object lettucePool : lettucePools) {
                    Reflect.on(lettucePool).call("closeAll");
                }
                clear();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private Iterable<RedisURI> performanceRedisUris(Iterable<RedisURI> uris, ShadowRedisConfig config) {
        List<String> nodes = config.getNodeNums();
        List<RedisURI> shadowNodes = new ArrayList<RedisURI>();
        Iterator<RedisURI> iterator = uris.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            RedisURI redisURI = performanceRedisUri(iterator.next(), nodes, index, config.getDatabase(), config.getPassword());
            shadowNodes.add(redisURI);
            index++;
        }
        return shadowNodes;
    }

    /**
     * @param redisURI
     * @param nodes
     * @param index
     * @return
     */
    private RedisURI performanceRedisUri(RedisURI redisURI, List<String> nodes, int index, Integer database, String password) {
        RedisURI newRedisUri = new RedisURI();
        if (null != redisURI && null != nodes) {
            String node = nodes.get(index);
            String host = node.substring(0, node.indexOf(":"));
            int port = Integer.parseInt(node.substring(node.indexOf(":") + 1));
            newRedisUri.setHost(host);
            newRedisUri.setPort(port);
            newRedisUri.setPassword(password == null ? "" : password);
            newRedisUri.setDatabase(redisURI.getDatabase());
            newRedisUri.setClientName(redisURI.getClientName());
            newRedisUri.setSentinelMasterId(redisURI.getSentinelMasterId());
            newRedisUri.setSocket(redisURI.getSocket());
            newRedisUri.setSsl(redisURI.isSsl());
            newRedisUri.setStartTls(redisURI.isStartTls());
            newRedisUri.setTimeout(redisURI.getTimeout());
            newRedisUri.setVerifyPeer(redisURI.isVerifyPeer());
            if (null != database) {
                newRedisUri.setDatabase(database);
            }

        }
        return newRedisUri;
    }


    public Object redisClusterConnect(RedisClusterClient client, String methodName, Object[] args) {
        if (LettuceConstants.CONNECT.equals(methodName)) {
            if (isRedisCodec(args)) {
                return client.connect((RedisCodec) args[0]);
            } else {
                return client.connect();
            }
        } else if (LettuceConstants.CONNECT_ASYNC.equals(methodName)) {
            return client.connectAsync((RedisCodec) args[0]);
        } else if (LettuceConstants.CONNECT_PUB_SUB.equals(methodName)) {
            if (isRedisCodec(args)) {
                return client.connectPubSub((RedisCodec) args[0]);
            } else {
                return client.connectPubSub();
            }
        } else if (LettuceConstants.CONNECT_PUB_SUB_ASYNC.equals(methodName)) {
            if (isRedisCodec(args)) {
                return client.connectPubSubAsync((RedisCodec) args[0]);
            }
        } else if (LettuceConstants.CONNECT_TO_NODE.equals(methodName)) {

        } else if (LettuceConstants.CONNECT_TO_NODE_ASYNC.equals(methodName)) {

        } else if (LettuceConstants.CONNECT_PUB_SUB_TO_NODE_ASYNC.equals(methodName)) {

        } else if (LettuceConstants.CONNECT_CLUSTER_ASYNC.equals(methodName)) {

        }
        return null;
    }

    public Object redisClientConnect(RedisClient client, String methodName, Object[] args) {
        if (LettuceConstants.CONNECT.equals(methodName)) {
            if (isRedisCodec(args)) {
                return client.connect((RedisCodec) args[0]);
            } else {
                return client.connect();
            }
        } else if (LettuceConstants.CONNECT_ASYNC.equals(methodName)) {
            return client.connectAsync((RedisCodec) args[0], (RedisURI) args[1]);
        } else if (LettuceConstants.CONNECT_PUB_SUB.equals(methodName)) {
            if (isRedisCodec(args)) {
                return client.connectPubSub((RedisCodec) args[0]);
            } else {
                return client.connectPubSub();
            }
        } else if (LettuceConstants.CONNECT_PUB_SUB_ASYNC.equals(methodName)) {
            if (isRedisCodec(args)) {
                return client.connectPubSubAsync((RedisCodec) args[0], (RedisURI) args[1]);
            }
        } else if (LettuceConstants.CONNECT_SENTINEL.equals(methodName)) {
            if (isRedisCodec(args)) {
                return client.connectSentinel((RedisCodec) args[0], (RedisURI) args[1]);
            } else if (args[0] instanceof RedisURI) {
                return client.connectSentinel((RedisURI) args[0]);
            } else {
                return client.connectSentinel();
            }
        } else if (LettuceConstants.CONNECT_SENTINEL_ASYNC.equals(methodName)) {
            return client.connectSentinelAsync((RedisCodec) args[0], (RedisURI) args[1]);
        }
        return null;
    }

    public boolean isRedisCodec(Object[] args) {
        if (null != args && args.length > 0 && args[0] instanceof RedisCodec) {
            return true;
        }
        return false;
    }
}
