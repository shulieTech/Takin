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
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.internal.HostAndPort;
import io.lettuce.core.masterslave.MasterSlave;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.lettuce.factory
 * @Date 2020/11/26 11:26 上午
 */
public class LettuceMasterSlaveFactory extends AbstractRedisServerFactory<Object> {

    private final static Logger LOGGER = LoggerFactory.getLogger(LettuceMasterSlaveFactory.class);

    private static final LettuceMasterSlaveFactory factory = new LettuceMasterSlaveFactory();

    private LettuceMasterSlaveFactory() {
        super(new LettuceMasterStrategy(manager));
    }

    public static LettuceMasterSlaveFactory getFactory() {
        return factory;
    }

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
        return security(mediator.getClient());
    }

    @Override
    public <T> T security(T client) {
        return client;
    }

    @Override
    public RedisClientMediator<Object> createMediator(Object obj, ShadowRedisConfig shadowRedisConfig) {
        RedisClientMediator<Object> mediator = null;
        try {
            String method = manager.getDynamicField(obj, LettuceConstants.DYNAMIC_FIELD_LETTUCE_METHOD);
            Object[] args = manager.getDynamicField(obj, LettuceConstants.DYNAMIC_FIELD_LETTUCE_ARGS);
            Object performanceConnection = null;

            List<RedisURI> performanceUris = new ArrayList<RedisURI>();
            RedisURI performanceUri = null;
            if (args[2] instanceof RedisURI) {
                RedisURI redisURI = (RedisURI) args[2];
                RedisURI.Builder builder = RedisURI.builder();
                builder.withSentinelMasterId(redisURI.getSentinelMasterId());
                HostAndPort master = convert(shadowRedisConfig.getMaster());
                builder.withSentinel(master.getHostText(), master.getPort());
                for (String node : shadowRedisConfig.getNodeNums()) {
                    HostAndPort nodeHost = convert(node);
                    builder.withSentinel(nodeHost.getHostText(), nodeHost.getPort());
                }
                performanceUri = builder.build();
            } else {
                performanceUris.add(RedisURI.create("redis://" + shadowRedisConfig.getMaster()));
                for (String nodeNum : shadowRedisConfig.getNodeNums()) {
                    performanceUris.add(RedisURI.create("redis://" + nodeNum));
                }
            }

            if ("connect".equals(method)) {
                if (isRedisUri(args[2], performanceUri)) {
                    performanceConnection = MasterSlave.connect((RedisClient) args[0], (RedisCodec) args[1], performanceUri);
                } else {
                    performanceConnection = MasterSlave.connect((RedisClient) args[0], (RedisCodec) args[1], performanceUris);
                }
            } else if ("connectAsync".equals(method)) {
                if (isRedisUri(args[2], performanceUri)) {
                    performanceConnection = MasterSlave.connectAsync((RedisClient) args[0], (RedisCodec) args[1], performanceUri);
                } else {
                    performanceConnection = MasterSlave.connectAsync((RedisClient) args[0], (RedisCodec) args[1], performanceUris);
                }
            }
            mediator = new RedisClientMediator<Object>(obj, performanceConnection);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.RedisServer)
                    .setErrorCode("redisServer-0001")
                    .setMessage("redis server lettuce master slave error！")
                    .setDetail(ExceptionUtils.getStackTrace(e))
                    .report();
        }
        return mediator;
    }

    @Override
    public void clearAll(IEvent event) {
        try {
            clear();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public boolean isRedisUri(Object uri, Object performanceUri) {
        if (uri instanceof RedisURI && null != performanceUri) {
            return true;
        }
        return false;
    }

    public HostAndPort convert(String hostAndPort) {
        int index = hostAndPort.indexOf(":");
        String host = hostAndPort.substring(0, index);
        int port = Integer.parseInt(hostAndPort.substring(index + 1));
        return HostAndPort.of(host, port);
    }
}
