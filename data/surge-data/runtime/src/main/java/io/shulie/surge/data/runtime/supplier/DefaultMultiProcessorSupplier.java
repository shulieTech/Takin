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

package io.shulie.surge.data.runtime.supplier;

import com.google.common.collect.Lists;
import io.shulie.surge.data.common.lifecycle.LifecycleObserver;
import io.shulie.surge.data.runtime.processor.DataQueue;

import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.Preconditions.checkState;


/**
 * @author vincent
 */
public class DefaultMultiProcessorSupplier implements MultiProcessorSupplier {

    private final CopyOnWriteArrayList<LifecycleObserver<Supplier>> observers =
            Lists.newCopyOnWriteArrayList();

    private final AtomicBoolean running = new AtomicBoolean(false);
    protected Map<String, DataQueue> queueMap;

    /**
     * 开始获取数据
     *
     * @throws IllegalStateException Queue 尚未设置时抛出此异常
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        if (!running.compareAndSet(false, true)) {
            return;
        }
        for (LifecycleObserver<Supplier> observer : observers) {
            observer.beforeStart(this);
        }
        if (queueMap != null && !queueMap.isEmpty()) {
            for (DataQueue queue : queueMap.values()) {
                queue.start();
            }
        }

        for (LifecycleObserver<Supplier> observer : observers) {
            observer.afterStart(this);
        }
    }

    /**
     * 停止获取数据
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        for (DataQueue dataQueue : queueMap.values()) {
            dataQueue.stop();
        }
    }

    /**
     * 设置使用的队列
     *
     * @param queue
     * @throws IllegalStateException 已经执行了 {@link #start()} 时抛出此异常
     */
    @Override
    public void setQueue(DataQueue queue) throws Exception {
        checkState(!running.get(), "supplier has been started");
        throw new UnsupportedOperationException("MultiProcessorSupplier unsupport set single queue ");
    }

    /**
     * 设置使用的队列
     *
     * @param queueMap
     * @throws IllegalStateException 已经执行了 {@link #start()} 时抛出此异常
     */
    @Override
    public void setQueue(Map<String, DataQueue> queueMap) throws IllegalStateException {
        checkState(!running.get(), "supplier has been started");
        this.queueMap = queueMap;
    }

    public Map<String, DataQueue> getQueueMap() {
        return queueMap;
    }

    /**
     * 检查当前是否在运行状态
     */
    @Override
    public boolean isRunning() {
        return running.get();
    }


    @Override
    public void addObserver(LifecycleObserver<Supplier> observer) {
        observers.add(observer);
    }
}
