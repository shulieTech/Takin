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
package com.pamirs.attach.plugin.common.datasource.redisserver;

import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.internal.config.ShadowRedisConfig;
import com.pamirs.pradar.pressurement.agent.event.IEvent;
import com.pamirs.pradar.pressurement.agent.event.impl.ShadowRedisServerDisableEvent;
import com.pamirs.pradar.pressurement.agent.listener.EventResult;
import com.pamirs.pradar.pressurement.agent.listener.PradarEventListener;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.pamirs.pradar.pressurement.agent.shared.service.EventRouter;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author qianfan
 * @package: com.pamirs.attach.plugin.lettuce.factory
 * @Date 2020/11/26 11:50 上午
 */
public abstract class AbstractRedisServerFactory<T> {

    protected final static Logger logger = LoggerFactory.getLogger(AbstractRedisServerFactory.class);

    protected static DynamicFieldManager manager;

    protected RedisServerMatchStrategy serverMatch;

    protected final Object monitLock = new Object();

    public static void setDynamicFieldManager(DynamicFieldManager dynamicFieldManager) {
        manager = dynamicFieldManager;
    }

    public AbstractRedisServerFactory(RedisServerNodesStrategy strategy) {
        this.serverMatch = new DefaultRedisServerMatch(strategy);
        addListener();
    }

    public AbstractRedisServerFactory(RedisServerMatchStrategy matchStrategy) {
        this.serverMatch = matchStrategy;
        addListener();
    }


    private static final Map<Object, RedisClientMediator<?>> mediators = new ConcurrentHashMap<Object, RedisClientMediator<?>>();

    public static Map<Object, RedisClientMediator<?>> getMediators() {
        return mediators;
    }

    public static void destroy() {
        for (Map.Entry<Object, RedisClientMediator<?>> entry : mediators.entrySet()) {
            try {
                entry.getValue().destroy();
            } catch (Throwable e) {
            }
        }
        mediators.clear();
    }

    public boolean doBefore() {
        return true;
    }

    public <T> void shutdown(T client) {
        RedisClientMediator<?> redisClientMediator = mediators.remove(client);
        if (redisClientMediator != null) {
            redisClientMediator.destroy();
        }
    }

    public <T> T getClient(T client) {

        // 根据当前的ip:port在容器中进行判断是否存在，存在不做任何操作
        // 在初始化阶段不需要判断是否为压测流量，只需要在connec方法中判断

        // 获取控制台影子Redis Server配置

        // 根据当前的ip:port进行匹配，匹配成功进行影子Redis Server初始化

        // 初始化成功交给Redis Server容器进行管理
        // 将容器中对应的key赋值到当前类中，以便在connec方法中进行判断，取出协调者对象
        // 后续在connec|getResouces方法中获取

        // 默认情况下判断压测流量，如有特殊情况，重写该方法
        if (!doBefore()) {
            return client;
        }

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

    public abstract <T> T security(T t);

    protected RedisClientMediator getMediator(Object key) {
        RedisClientMediator<?> redisClientMediator = mediators.get(key);
        if (redisClientMediator == null) {
            synchronized (monitLock) {
                redisClientMediator = mediators.get(key);
                if (redisClientMediator == null) {
                    return create(key);
                }
            }
        }
        return redisClientMediator;
    }

    private RedisClientMediator<?> create(Object obj) {
        ShadowRedisConfig shadowRedisConfig = serverMatch.getConfig(obj);
        if (null == shadowRedisConfig) {
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.RedisServer)
                    .setErrorCode("redisServer-0001")
                    .setMessage("没有配置影子Redis Server")
                    .setDetail("没有配置影子Redis Server")
                    .report();
            // 抛出相关异常信息
            throw new PressureMeasureError("not found redis shadow server config error.");
        }


        validationConfig(shadowRedisConfig);

        RedisClientMediator<T> mediator = createMediator(obj, shadowRedisConfig);
        putMediator(obj, mediator);
        return mediator;
    }

    private void validationConfig(ShadowRedisConfig shadowRedisConfig) {
        if (null == shadowRedisConfig.getNodes()) {
            throw new PressureMeasureError("shadow redis nodes is null.");
        }

        String[] nodes = StringUtils.split(shadowRedisConfig.getNodes(), ',');

        if (nodes.length < 1) {
            throw new PressureMeasureError("shadow redis nodes size Less than 1.");
        }

        StringBuilder builder = new StringBuilder();
        for (String node : nodes) {
            if (node.contains("redis://")) {
                node = node.substring(node.indexOf("redis://") + 8);
            }
            if (!node.contains(":")) {
                builder.append(node).append("格式错误，不包含\":\"\\n");
            } else {
                if (node.length() < 3) {
                    builder.append(node).append("格式错误长度小于3\n");
                } else {
                    String host = node.substring(0, node.indexOf(":"));
                    String[] split = StringUtils.split(host, '.');
                    if (split.length != 4) {
                        builder.append(node).append("ip格式错误\n");
                    }
                    try {
                        int port = Integer.parseInt(node.substring(node.indexOf(":") + 1));
                        if (port < 0) {
                            builder.append(node).append("port格式错误, 小于0\n");
                        }
                    } catch (Throwable e) {
                        builder.append(node).append("port格式错误\n");
                    }
                }
            }
        }

        if (builder.length() > 4) {
            ErrorReporter.buildError()
                    .setErrorType(ErrorTypeEnum.RedisServer)
                    .setErrorCode("redisServer-0002")
                    .setMessage("影子Redis Server配置格式错误")
                    .setDetail(builder.toString())
                    .report();
            // 抛出相关异常信息
            if (GlobalConfig.getInstance().isShadowDbRedisServer()) {
                throw new PressureMeasureError(builder.toString());
            }
        }

        shadowRedisConfig.setNodeNums(Arrays.asList(nodes));
    }

    protected abstract RedisClientMediator<T> createMediator(Object obj, ShadowRedisConfig config);

    protected void putMediator(Object key, RedisClientMediator<?> client) {
        mediators.put(key, client);
    }

    public static void remove(Object key) {
        mediators.remove(key);
    }

    public void clean(ShadowRedisConfig config) {
    }

    public void clearAll(IEvent event) {
        List<ShadowRedisConfig> events = (List<ShadowRedisConfig>) event.getTarget();
        for (ShadowRedisConfig config : events) {
            clean(config);
        }
    }

    public void clear() {
        mediators.clear();
    }

    private AtomicBoolean init = new AtomicBoolean(false);

    void addListener() {
        if (!init.compareAndSet(false, true)) {
            return;
        }
        EventRouter.router().addListener(new PradarEventListener() {
            @Override
            public EventResult onEvent(IEvent event) {
                if (!ShadowRedisServerDisableEvent.class.isAssignableFrom(event.getClass())) {
                    return EventResult.IGNORE;
                }
                if (!GlobalConfig.getInstance().isShadowDbRedisServer()) {
                    return EventResult.IGNORE;
                }
                try {
                    clearAll(event);
                } catch (Throwable t) {
                    logger.error("redis remove pressure client error-->{}", t.getStackTrace());
                    return EventResult.error(event.getTarget(), t.getCause().toString());
                }
                return EventResult.success(event.getTarget());
            }

            @Override
            public int order() {
                return 10;
            }
        });
    }
}
