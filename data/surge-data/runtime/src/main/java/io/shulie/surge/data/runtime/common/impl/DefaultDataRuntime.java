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

package io.shulie.surge.data.runtime.common.impl;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.inject.*;
import com.google.inject.name.Names;
import io.shulie.surge.data.common.factory.GenericFactory;
import io.shulie.surge.data.common.factory.GenericFactorySpec;
import io.shulie.surge.data.common.factory.GenericFactorySpecSerializer;
import io.shulie.surge.data.common.lifecycle.StopLevel;
import io.shulie.surge.data.common.lifecycle.Stoppable;
import io.shulie.surge.data.common.utils.FormatUtils;
import io.shulie.surge.data.runtime.common.DataRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author pamirs
 */
@Singleton
public class DefaultDataRuntime implements DataRuntime {

    private static final Logger logger = LoggerFactory.getLogger(DefaultDataRuntime.class);

    private static final DataRuntimeLock processLock = new DataRuntimeLock();
    private static final AtomicInteger instanceCounter = new AtomicInteger(0);
    private static final Map<StopLevel, List<Stoppable>> processShutdownStoppables;
    private Injector injector;

    private final Map<String, Object> innerContext = new HashMap<String, Object>();

    private static class DataRuntimeLock {
    }


    static {
        Map<StopLevel, List<Stoppable>> shutdownStoppables = Maps.newEnumMap(StopLevel.class);
        for (StopLevel stopLevel : StopLevel.values()) {
            if (stopLevel.isProcessLevel()) {
                shutdownStoppables.put(stopLevel, new ArrayList<Stoppable>());
            }
        }
        processShutdownStoppables = shutdownStoppables;
    }


    @Inject
    private GenericFactorySpecSerializer specSerializer;

    private final Map<StopLevel, List<Stoppable>> instanceShutdownStoppables;

    public DefaultDataRuntime() {
        Map<StopLevel, List<Stoppable>> shutdownStoppables = Maps.newEnumMap(StopLevel.class);
        for (StopLevel stopLevel : StopLevel.values()) {
            if (!stopLevel.isProcessLevel()) {
                shutdownStoppables.put(stopLevel, new ArrayList<Stoppable>());
            }
        }
        this.instanceShutdownStoppables = shutdownStoppables;
    }

    @Inject
    public void onInject(Injector injector) {
        this.injector = injector;
        int startedInstances = instanceCounter.incrementAndGet();
        // @formatter:off
        logger.warn("\n\n" +
                        "░██████╗██╗░░░██╗██████╗░░██████╗░███████╗  ██████╗░░█████╗░████████╗░█████╗░\n" +
                        "██╔════╝██║░░░██║██╔══██╗██╔════╝░██╔════╝  ██╔══██╗██╔══██╗╚══██╔══╝██╔══██╗\n" +
                        "╚█████╗░██║░░░██║██████╔╝██║░░██╗░█████╗░░  ██║░░██║███████║░░░██║░░░███████║\n" +
                        "░╚═══██╗██║░░░██║██╔══██╗██║░░╚██╗██╔══╝░░  ██║░░██║██╔══██║░░░██║░░░██╔══██║\n" +
                        "██████╔╝╚██████╔╝██║░░██║╚██████╔╝███████╗  ██████╔╝██║░░██║░░░██║░░░██║░░██║\n" +
                        "╚═════╝░░╚═════╝░╚═╝░░╚═╝░╚═════╝░╚══════╝  ╚═════╝░╚═╝░░╚═╝░░░╚═╝░░░╚═╝░░╚═╝" +
                        "LogRuntime {} started, counter={}",
                new Object[]{
                        FormatUtils.getIdentityCode(this),
                        startedInstances
                });
        // @formatter:on
    }

    @Override
    public <T> T getInstance(Class<T> type) {
        return injector.getInstance(type);
    }

    @Override
    public <T> T getInstance(Class<T> type, String name) {
        return injector.getInstance(Key.get(type, Names.named(name)));
    }

    @Override
    public <T> GenericFactory<T, GenericFactorySpec<T>> getGenericFactory(Class<T> type, String name) {
        TypeLiteral<GenericFactory<T, GenericFactorySpec<T>>> typeLiteral = getGenericFactoryTypeLiteral(type);
        return injector.getInstance(Key.get(typeLiteral, Names.named(name)));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> GenericFactorySpec<T> getGenericFactorySpec(Class<T> type, String name) {
        return injector.getInstance(Key.get(GenericFactorySpec.class,
                Names.named(type.getName() + "@" + name)));
    }

    @Override
    public <T> T createGenericInstance(Class<T> type, String name, String json) throws Exception {
        GenericFactory<T, GenericFactorySpec<T>> factory = getGenericFactory(type, name);
        GenericFactorySpec<T> spec = specSerializer.fromJSONString(type, name, json);
        return factory.create(spec);
    }

    @Override
    public <T> T createGenericInstance(GenericFactorySpec<T> spec) throws Exception {
        GenericFactory<T, GenericFactorySpec<T>> factory = getGenericFactory(
                spec.productClass(), spec.factoryName());
        return factory.create(spec);
    }

    /**
     * 产生一个 Guice 可以识别的，范型 T 被回填的 TypeLiteral。
     * 为了避免 api 包依赖 guava，这个方法会被 api 中的 反射调用，请不要修改。
     *
     * @param productClass
     * @return
     */
    @SuppressWarnings({"serial", "unchecked"})
    static final <T> TypeLiteral<GenericFactory<T, GenericFactorySpec<T>>> getGenericFactoryTypeLiteral(
            Class<T> productClass) {
        // 绑定了 GenericFactory<ProductInterface, GenericFactorySpec<ProductInterface>>
        TypeToken<GenericFactory<T, GenericFactorySpec<T>>> typeToken =
                new TypeToken<GenericFactory<T, GenericFactorySpec<T>>>() {
                }
                        .where(new TypeParameter<T>() {
                        }, productClass);
        return (TypeLiteral<GenericFactory<T, GenericFactorySpec<T>>>) TypeLiteral.get(
                typeToken.getType());
    }

    @Override
    public void inject(Object instance) {
        injector.injectMembers(instance);
    }

    @Override
    public void shutdown() {
        logger.warn("stopping LogRuntime {}...", FormatUtils.getIdentityCode(this));
        synchronized (this) {
            logger.warn("in shutdown.synchronized ");
            for (StopLevel stopLevel : StopLevel.values()) {
                if (!stopLevel.isProcessLevel()) {
                    doStop(instanceShutdownStoppables.get(stopLevel));
                }
            }
        }
        int remainingInstances;
        logger.warn("entering shutdown.synchronized.processLock ");
        synchronized (processLock) {
            logger.warn("in shutdown.synchronized.processLock ");
            remainingInstances = instanceCounter.decrementAndGet();
            logger.warn("remainingInstances {}", remainingInstances);
            if (remainingInstances == 0) {
                for (StopLevel stopLevel : StopLevel.values()) {
                    if (stopLevel.isProcessLevel() && !stopLevel.equals(StopLevel.RESOURCE)) {
                        doStop(processShutdownStoppables.get(stopLevel));
                    }
                }
            }
            doStop(processShutdownStoppables.get(StopLevel.RESOURCE));
        }
        logger.warn("LogRuntime {} shutdown successfully, counter={}",
                FormatUtils.getIdentityCode(this), remainingInstances);

    }

    private void doStop(List<Stoppable> stoppables) {
        if (stoppables == null || stoppables.isEmpty()) {
            return;
        }
        for (Stoppable stoppable : stoppables) {
            logger.warn("stopping {} {}...", stoppable.getClass().getSimpleName(),
                    FormatUtils.getIdentityCode(stoppable));
            try {
                stoppable.stop();
            } catch (Exception e) {
                logger.warn("exception when shutting down {}", stoppable.getClass().getSimpleName(), e);
            }
        }
        stoppables.clear();
    }

    @Override
    public void registShutdownCall(Stoppable stoppable, StopLevel stopLevel) {
        checkNotNull(stoppable);
        logger.info("regist shutdown hook of {} {}, stopLevel={}",
                new Object[]{
                        stoppable.getClass().getSimpleName(), FormatUtils.getIdentityCode(stoppable), stopLevel
                });
        if (!stopLevel.isProcessLevel()) {
            synchronized (this) {
                List<Stoppable> stoppables = instanceShutdownStoppables.get(stopLevel);
                if (!stoppables.contains(stoppable)) {
                    stoppables.add(stoppable);
                }
            }
        } else {
            synchronized (processLock) {
                List<Stoppable> stoppables = processShutdownStoppables.get(stopLevel);
                if (!stoppables.contains(stoppable)) {
                    stoppables.add(stoppable);
                }
            }
        }
    }

    @Override
    public boolean unregistShutdownCall(Stoppable stoppable, StopLevel stopLevel) {
        if (!stopLevel.isProcessLevel()) {
            synchronized (this) {
                return instanceShutdownStoppables.get(stopLevel).remove(stoppable);
            }
        } else {
            synchronized (processLock) {
                return processShutdownStoppables.get(stopLevel).remove(stoppable);
            }
        }
    }

    @Override
    public Object getValue(String key) {
        return innerContext.get(key);
    }

    @Override
    public void putValue(String key, Object value) {
        innerContext.put(key, value);
    }
}
