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

package io.shulie.surge.data.common.aggregation;

import com.google.common.collect.Maps;
import io.shulie.surge.data.common.lifecycle.Stoppable;
import io.shulie.surge.data.common.utils.FormatUtils;
import org.apache.log4j.Logger;
import org.cliffc.high_scale_lib.NonBlockingHashMapLong;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static io.shulie.surge.data.common.utils.CommonUtils.isNullEmpty;


/**
 * 进行数据聚集
 *
 * @author pamirs
 */
@SuppressWarnings({"unchecked"})
public class Aggregation<K, A extends AggregateSupport<A>> implements Stoppable {

    private static final int MILLIS_PER_SECOND = 1000;

    private static final Logger logger = Logger.getLogger(Aggregation.class);

    /**
     * 聚集间隔，也是聚集的数据的时间粒度，最小粒度为秒，也就是最小每秒提交一次聚集结果
     */
    private final int interval;

    /**
     * 聚集数据期间，缓存数据的 slot 个数，大小为 2 的 n 次方倍
     */
    private final int bufferSize;
    /**
     * 用于快速定位 slot 的掩码
     */
    private final int bufferMask;
    /**
     * 聚集在同一个时间段的数据都放在一个 slot 里面，
     * 一个 slotMap 放入了多个 slotKey，它们映射成 index 是一样的
     */
    private final NonBlockingHashMapLong<AggregateSlot<K, A>>[] slotMaps;

    /**
     * 提交延时，单位：秒
     */
    private final int delay;

    /**
     * 下一个 commit 的 slotIndex
     */
    private transient long slotKeyToCommit;

    /**
     * 定时触发的提交动作
     */
    private CommitAction<K, A> commitAction;

    /**
     * 指定需要聚集的数据范围
     *
     * @param intervalInSecond 数据聚集的时间粒度
     * @param delayInSecond    数据聚集的时间下界（如延时 30 秒提交，则为 30）
     */
    public Aggregation(int intervalInSecond, int delayInSecond) {
        this.interval = intervalInSecond;

        int requireSize = delayInSecond / intervalInSecond + 1;
        this.bufferSize = 1 << (32 - Integer.numberOfLeadingZeros(requireSize));
        this.bufferMask = bufferSize - 1;
        this.delay = delayInSecond;

        NonBlockingHashMapLong<AggregateSlot<K, A>>[] slotMaps = new NonBlockingHashMapLong[bufferSize];
        for (int i = 0; i < bufferSize; i++) {
            slotMaps[i] = new NonBlockingHashMapLong<AggregateSlot<K, A>>(32);
        }
        this.slotMaps = slotMaps;

        logger.info("new Aggregation, " +
                "bufferSize=" + bufferSize + ", interval=" + interval + ", delayInSecond=" + delayInSecond);
    }

    public int size() {
        return bufferSize;
    }

    public int getInterval() {
        return interval;
    }

    public int getDelay() {
        return delay;
    }

    public long getSlotKeyToCommit() {
        return slotKeyToCommit;
    }

    /**
     * slotKey 到 index 的转换
     *
     * @param key
     * @return
     */
    private int slotKeyToIndex(long key) {
        return (int) ((key / interval) & bufferMask);
    }

    /**
     * slotKey 到 timestamp 的逆转换，已经丢失精度
     *
     * @param key
     * @return
     */
    public long slotKeyToTimestamp(long key) {
        return key * MILLIS_PER_SECOND;
    }

    /**
     * timestamp 到 slotKey 的转换
     *
     * @param timestamp
     * @return
     */
    public long timestampToSlotKey(long timestamp) {
        long ts = timestamp / MILLIS_PER_SECOND; // -> second
        return ts - ts % interval;
    }

    /**
     * @param key
     * @return
     */
    public AggregateSlot<K, A> getSlotBySlotKey(long key) {
        NonBlockingHashMapLong<AggregateSlot<K, A>> slotMap = this.slotMaps[slotKeyToIndex(key)];
        AggregateSlot<K, A> slot = slotMap.get(key);
        if (slot == null) {
            AggregateSlot<K, A> newSlot = new AggregateSlot<K, A>();
            slot = slotMap.putIfAbsent(key, newSlot);
            if (slot == null) {
                slot = newSlot;
            }
        }
        return slot;
    }

    public AggregateSlot<K, A> getSlotByTimestamp(long timestamp) {
        return getSlotBySlotKey(timestampToSlotKey(timestamp));
    }

    private Map<Long, AggregateSlot<K, A>> popSlots(final long slotKey) {
        return popSlots(slotKeyToIndex(slotKey), slotKey);
    }

    private Map<Long, AggregateSlot<K, A>> popSlots(final int idx, final long maxCommitSlotKey) {
        NonBlockingHashMapLong<AggregateSlot<K, A>> slotMap = slotMaps[idx];
        Map<Long, AggregateSlot<K, A>> results = Maps.newHashMap();
        for (Long key : slotMap.keySet()) {
            // 需要提交的是老数据的 slotKey 和当前这批需要提交的 slotKey，
            // 所有小于提交时间 maxCommitSlotKey 的 slot 不提交
            long currentSlotKey = key.longValue();
            if (currentSlotKey <= maxCommitSlotKey) {
                AggregateSlot<K, A> removedSlot = slotMap.remove(currentSlotKey);
                if (removedSlot != null) {
                    results.put(key, removedSlot);
                }
            }
        }
        return results;
    }

    /**
     * 提交聚合得到的数据
     */
    public static interface CommitAction<K, A extends AggregateSupport<A>> {
        void commit(long slotKey, AggregateSlot<K, A> slot);
    }

    /**
     * 开始进行聚集，每隔 interval 秒会调用一次 commitAction
     *
     * @param scheduler
     * @param commitAction
     */
    public void start(final ScheduledExecutorService scheduler, final CommitAction<K, A> commitAction) {
        if (this.commitAction != null) {
            throw new IllegalStateException("Aggregation has been started");
        }
        this.commitAction = commitAction;
        long now = System.currentTimeMillis();
        this.slotKeyToCommit = timestampToSlotKey(now);
        long startTime = (slotKeyToCommit + delay) * MILLIS_PER_SECOND;
        long initialDelay = startTime - now;
        scheduler.scheduleAtFixedRate(new CommitRunnable(),
                initialDelay, interval * MILLIS_PER_SECOND, TimeUnit.MILLISECONDS);
        logger.info("initial delay: " + initialDelay + "ms, slotKeyToCommit=" +
                FormatUtils.toSecondTimeString(slotKeyToCommit * MILLIS_PER_SECOND) + " at " +
                FormatUtils.toSecondTimeString(startTime) +
                ", interval=" + interval + "s" + ", delay=" + delay + "s");
    }

    private class CommitRunnable implements Runnable {
        private AtomicBoolean running = new AtomicBoolean(false);

        @Override
        public void run() {
            if (running.compareAndSet(false, true)) {
                try {
                    final long slotKeyToCommit = Aggregation.this.slotKeyToCommit;
                    Aggregation.this.slotKeyToCommit = slotKeyToCommit + interval;
                    final Map<Long, AggregateSlot<K, A>> slots = popSlots(slotKeyToCommit);
                    if (!isNullEmpty(slots)) {
                        for (Map.Entry<Long, AggregateSlot<K, A>> entry : slots.entrySet()) {
                            commitAction.commit(entry.getKey(), entry.getValue());
                        }
                    }
                } catch (Exception e) {
                    logger.warn("fail to commit slot", e);
                }
                running.set(false);
            }
        }
    }

    @Override
    public void stop() {
        logger.info("[End Of World] stopping aggregation...");
        try {
            for (int i = 0; i < bufferSize; ++i) {
                final Map<Long, AggregateSlot<K, A>> slots = popSlots(0, Long.MAX_VALUE);
                if (!isNullEmpty(slots)) {
                    for (Map.Entry<Long, AggregateSlot<K, A>> entry : slots.entrySet()) {
                        logger.info("[End Of World] committing slot[" + i + "] " +
                                FormatUtils.toSecondTimeString(entry.getKey()) +
                                ", size=" + entry.getValue().size());
                        commitAction.commit(entry.getKey(), entry.getValue());
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("fail to commit slot", e);
        }
        logger.info("[End Of World] stopped aggregation");
    }
}
