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

package io.shulie.surge.data.common.batch;

import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author vincent
 */
public class RotationBatch<T extends Serializable> {

    private Logger logger = LoggerFactory.getLogger(RotationBatch.class);
    private int maxRetries = 3;
    private LinkedBlockingQueue<T> batchObjs = new LinkedBlockingQueue<>();
    private List<RotationPolicy> rotationPolicies = Lists.newLinkedList();
    private ScheduledExecutorService executor;
    private BatchSaver batchSaver;
    private AtomicBoolean started = new AtomicBoolean(false);

    public RotationBatch(RotationPolicy... rotationPolicy) {
        rotationPolicy(rotationPolicy);
    }

    public AtomicBoolean getStarted() {
        return started;
    }

    /**
     * 滚动策略
     *
     * @param rotationPolicy
     * @return
     */
    public RotationBatch rotationPolicy(RotationPolicy... rotationPolicy) {
        rotationPolicies.addAll(Arrays.asList(rotationPolicy));
        return this;
    }

    /**
     * 最大重试次数
     *
     * @param maxRetries
     * @return
     */
    public RotationBatch maxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    /**
     * 批保存
     *
     * @param batchSaver
     * @return
     */
    public RotationBatch batchSaver(BatchSaver batchSaver) {
        this.batchSaver = batchSaver;
        return this;
    }

    /**
     * 添加对象到批中
     *
     * @param object
     * @return
     */
    public RotationBatch addBatch(T object, long offset) {
        if (started.compareAndSet(false, true)) {
            start();
        }
        batchObjs.add(object);
        /**
         * 检查mark
         */
        if (checkMark(offset)) {
            saveBatch();
        }
        return this;
    }

    /**
     * 添加对象到批中
     *
     * @param object
     * @return
     */
    public RotationBatch addBatch(T object) {
        addBatch(object, 1);
        return this;
    }

    /**
     *
     */
    private void saveBatch() {
        LinkedBlockingQueue<T> batch = null;
        synchronized (this) {
            batch = batchObjs;
            batchObjs = new LinkedBlockingQueue<T>();
            reset();
        }
        if (CollectionUtils.isEmpty(batch)) {
            return;
        }
        int count = 0;
        while (count < maxRetries) {
            try {
                if (batchSaver.saveBatch(batch)) {
                    break;
                }
            } catch (Throwable e) {
                logger.error("Save batch error.{}", ExceptionUtils.getStackTrace(e));
            }
        }
    }

    /**
     * 检查是需要rotate
     *
     * @return
     */
    private boolean checkMark(long offset) {
        boolean rotate = false;
        for (RotationPolicy rotationPolicy : rotationPolicies) {
            if (rotationPolicy.mark(offset)) {
                rotate = true;
                continue;
            }
        }
        return rotate;
    }

    /**
     * 重置
     */
    private void reset() {
        for (RotationPolicy rotationPolicy : rotationPolicies) {
            rotationPolicy.reset();
        }
    }

    /**
     * 启动
     *
     * @return
     */
    public RotationBatch start() {
        Iterator<RotationPolicy> iterator = rotationPolicies.iterator();
        while (iterator.hasNext()) {
            RotationPolicy rotationPolicy = iterator.next();
            if (rotationPolicy instanceof TimedRotationPolicy) {
                if (batchSaver == null) {
                    throw new IllegalStateException("Please add batchSaver first!");
                }
                if (null == executor) {
                    executor = Executors.newSingleThreadScheduledExecutor();
                }
                long interval = ((TimedRotationPolicy) rotationPolicy).getInterval();
                executor.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        saveBatch();
                    }
                }, interval, interval, TimeUnit.MILLISECONDS);
                iterator.remove();
            }
        }
        return this;
    }


    /**
     * 批处理保存器
     *
     * @param <T>
     */
    public interface BatchSaver<T extends Serializable> {
        boolean saveBatch(LinkedBlockingQueue<T> ObjectBatch);
    }

    /**
     * flush数据
     */
    public synchronized void flush() {
        batchSaver.saveBatch(batchObjs);
    }
}
