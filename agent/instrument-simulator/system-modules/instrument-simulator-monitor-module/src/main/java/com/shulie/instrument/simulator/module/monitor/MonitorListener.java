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
package com.shulie.instrument.simulator.module.monitor;

import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.listener.ext.AdviceListener;
import com.shulie.instrument.simulator.module.model.monitor.MonitorView;
import com.shulie.instrument.simulator.module.util.ThreadLocalWatch;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.apache.commons.lang.ArrayUtils.isEquals;

public class MonitorListener extends AdviceListener {
    // 监控数据
    private ConcurrentHashMap<Key, AtomicReference<MonitorView>> monitorData = new ConcurrentHashMap<Key, AtomicReference<MonitorView>>();
    private final ThreadLocalWatch threadLocalWatch = new ThreadLocalWatch();
    private int limits;
    private CountDownLatch latch;
    private Queue<MonitorView> views;

    public MonitorListener(CountDownLatch latch, Queue<MonitorView> views, int limits) {
        this.latch = latch;
        this.views = views;
        this.limits = limits;
    }

    @Override
    public void before(Advice advice)
            throws Throwable {
        threadLocalWatch.start();
    }

    @Override
    public void afterReturning(Advice advice) throws Throwable {
        finishing(advice);
    }

    @Override
    public void afterThrowing(Advice advice) {
        finishing(advice);
    }

    private void finishing(Advice advice) {
        long cost = threadLocalWatch.cost();
        final Key key = new Key(advice.getTargetClass().getName(), advice.getBehavior().getName());

        while (true) {
            AtomicReference<MonitorView> value = monitorData.get(key);
            if (null == value) {
                MonitorView monitorView = new MonitorView();
                monitorView.setClassName(advice.getTargetClass().getName());
                monitorView.setMethodName(advice.getBehavior().getName());
                monitorData.putIfAbsent(key, new AtomicReference<MonitorView>(monitorView));
                continue;
            }

            while (true) {
                MonitorView oData = value.get();
                MonitorView nData = new MonitorView();
                if (advice.getThrowable() != null) {
                    nData.fail(oData, cost);
                } else {
                    nData.success(oData, cost);
                }
                if (value.compareAndSet(oData, nData)) {
                    break;
                }
            }
            break;
        }
        long total = 0;
        for (Map.Entry<Key, AtomicReference<MonitorView>> entry : monitorData.entrySet()) {
            AtomicReference<MonitorView> reference = entry.getValue();
            if (reference.get() == null) {
                continue;
            }
            MonitorView monitorView = reference.get();
            total += monitorView.getCount();
        }
        if (limits != -1 && total >= limits) {
            for (Map.Entry<Key, AtomicReference<MonitorView>> entry : monitorData.entrySet()) {
                AtomicReference<MonitorView> reference = entry.getValue();
                if (reference.get() == null) {
                    continue;
                }
                MonitorView monitorView = reference.get();
                views.add(monitorView);
            }
            if (latch != null) {
                latch.countDown();
                monitorData.clear();
            }
        }
    }

    /**
     * 数据监控用的Key
     *
     * @author vlinux
     */
    private static class Key {
        private final String className;
        private final String methodName;

        Key(String className, String behaviorName) {
            this.className = className;
            this.methodName = behaviorName;
        }

        public String getClassName() {
            return className;
        }

        public String getMethodName() {
            return methodName;
        }

        @Override
        public int hashCode() {
            return className.hashCode() + methodName.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (null == obj
                    || !(obj instanceof Key)) {
                return false;
            }
            Key okey = (Key) obj;
            return isEquals(okey.className, className) && isEquals(okey.methodName, methodName);
        }

    }
}
