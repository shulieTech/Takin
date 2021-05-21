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
package com.shulie.instrument.simulator.core.manager.impl;

import com.shulie.instrument.simulator.api.listener.EventListener;
import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.core.manager.AffectStatistic;
import com.shulie.instrument.simulator.core.manager.SimulatorClassFileTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by xiaobin on 2017/1/19.
 */
public class CostDumpTransformer extends SimulatorClassFileTransformer {

    public final static String ENABLED_COST_DUMP = "simulator.cost.dump";
    private final static Logger logger = LoggerFactory.getLogger(CostDumpTransformer.class.getName());
    private final static long refreshInterval = 10000L;
    private final SimulatorClassFileTransformer delegate;

    private final AtomicLong cost;
    private volatile long lastLogTime;
    private final boolean costDumpEnabled;

    public static CostDumpTransformer wrap(SimulatorClassFileTransformer classFileTransformer, SimulatorConfig simulatorConfig) {
        return new CostDumpTransformer(classFileTransformer, simulatorConfig);
    }

    private CostDumpTransformer(SimulatorClassFileTransformer delegate, SimulatorConfig simulatorConfig) {
        if (delegate == null) {
            throw new NullPointerException("delegate must not be null");
        }
        if (simulatorConfig == null) {
            throw new NullPointerException("profilerConfig must not be null");
        }

        this.delegate = delegate;
        this.cost = new AtomicLong(0);
        this.costDumpEnabled = simulatorConfig.getBooleanProperty(ENABLED_COST_DUMP, false);
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!costDumpEnabled) {
            return delegate.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
        } else {
            long start = System.currentTimeMillis();
            try {
                return delegate.transform(loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
            } finally {
                long end = System.currentTimeMillis();
                long value = cost.addAndGet(end - start);
                if (end - start > 100) {
                    logger.warn("SIMULATOR: simulator attach cost {}ms by enhanced {},it should optimize it!", (end - start), className);
                }

                if (System.currentTimeMillis() - lastLogTime > refreshInterval) {
                    logger.warn("SIMULATOR: simulator attach total cost {}ms", value);
                    lastLogTime = System.currentTimeMillis();
                }
            }
        }
    }

    @Override
    public int getWatchId() {
        return delegate.getWatchId();
    }

    @Override
    public Map<Integer, EventListener> getEventListeners() {
        return delegate.getEventListeners();
    }

    @Override
    public List<BuildingForListeners> getAllListeners() {
        return delegate.getAllListeners();
    }


    @Override
    public Object getMatcher() {
        return delegate.getMatcher();
    }


    @Override
    public AffectStatistic getAffectStatistic() {
        return delegate.getAffectStatistic();
    }
}
