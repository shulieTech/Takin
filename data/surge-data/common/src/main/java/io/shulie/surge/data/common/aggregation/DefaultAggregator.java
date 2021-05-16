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
import io.shulie.surge.data.common.aggregation.metrics.CallStat;
import io.shulie.surge.data.common.aggregation.metrics.Metric;
import org.apache.commons.lang3.math.NumberUtils;
import org.cliffc.high_scale_lib.NonBlockingHashMapLong;

import java.util.Map;

/**
 * 默认的统计类
 */
public class DefaultAggregator implements Aggregator {
    /**
     * 实时数据统计的间隔，单位：秒
     */
    public static final int AGGREGATE_SECONDS_INTERVAL = 60;
    /**
     * 实时数据统计的延时，单位：秒
     */
    public static final int AGGREGATE_SECONDS_LOWER_LIMIT = 80;

    private Aggregation<Metric, CallStat> aggregation;

    private Scheduler scheduler;

    private Map<String, ResultListener> resultListeners = Maps.newHashMap();

    /**
     * @param intervalInSecond 统计间隔
     * @param delayInSecond    延迟间隔
     */
    public DefaultAggregator(int intervalInSecond, int delayInSecond, Scheduler scheduler) {
        aggregation = new Aggregation<Metric, CallStat>(intervalInSecond, delayInSecond);
        this.scheduler = scheduler;
    }

    /**
     * @param intervalInSecond 统计间隔
     * @param delayInSecond    延迟间隔
     */
    public DefaultAggregator(int intervalInSecond, int delayInSecond) {
        aggregation = new Aggregation<Metric, CallStat>(intervalInSecond, delayInSecond);
        this.scheduler = new Scheduler(1);
    }

    /**
     * @param intervalInSecond 统计间隔
     * @param delayInSecond    延迟间隔
     */
    public DefaultAggregator(int intervalInSecond, int delayInSecond, Map<String, ResultListener> resultListeners) {
        aggregation = new Aggregation<Metric, CallStat>(intervalInSecond, delayInSecond);
        this.scheduler = new Scheduler(1);
        this.resultListeners = resultListeners;
    }

    /**
     * 添加监听器
     *
     * @param metricsId
     * @param resultListener
     */
    public void addListener(String metricsId, ResultListener resultListener) {
        resultListeners.putIfAbsent(metricsId, resultListener);
    }

    public AggregateSlot<Metric, CallStat> getSlotBySlotKey(long key) {
        return aggregation.getSlotBySlotKey(key);
    }

    /**
     * 默认统计实现
     */
    public DefaultAggregator(Scheduler scheduler) {
        this(AGGREGATE_SECONDS_INTERVAL, AGGREGATE_SECONDS_LOWER_LIMIT, scheduler);
    }

    public DefaultAggregator() {
        this(AGGREGATE_SECONDS_INTERVAL, AGGREGATE_SECONDS_LOWER_LIMIT);
    }

    /**
     * 长度
     *
     * @return
     */
    @Override
    public int size() {
        return aggregation.size();
    }

    /**
     * 间隔
     *
     * @return
     */
    @Override
    public int getInterval() {
        return aggregation.getInterval();
    }

    /**
     * 时延
     *
     * @return
     */
    @Override
    public int getLowerLimit() {
        return aggregation.getDelay();
    }

    /**
     * 下一个槽位
     *
     * @return
     */
    @Override
    public long getLowerBound() {
        return aggregation.getSlotKeyToCommit();
    }

    /**
     * 槽位到时间戳，slotKey 到 timestamp 的逆转换，已经丢失精度
     *
     * @param key
     * @return
     */
    @Override
    public long slotKeyToTimestamp(long key) {
        return aggregation.slotKeyToTimestamp(key);
    }

    /**
     * 时间戳到槽位
     *
     * @param timestamp
     * @return
     */
    @Override
    public long timestampToSlotKey(long timestamp) {
        return aggregation.timestampToSlotKey(timestamp);
    }

    /**
     * 获取时间戳的槽位
     *
     * @param timestamp
     * @return
     */
    @Override
    public AggregateSlot<Metric, CallStat> getSlotByTimestamp(long timestamp) {
        return aggregation.getSlotByTimestamp(timestamp);
    }

    /**
     * 获取时间戳的槽位
     *
     * @param timestamp
     * @return
     */
    @Override
    public AggregateSlot<Metric, CallStat> getSlotByTimestamp(String timestamp) {
        return aggregation.getSlotByTimestamp(NumberUtils.toLong(timestamp));
    }

    /**
     * 开始运行
     */
    @Override
    public void start() throws Exception {
        aggregation.start(scheduler, new Aggregation.CommitAction<Metric, CallStat>() {
            @Override
            public void commit(long slotKey, AggregateSlot<Metric, CallStat> slot) {
                Map<Metric, CallStat> slotMap = slot.toMap();
                slotMap.entrySet().forEach(metricCallStatEntry -> {
                    resultListeners.get(metricCallStatEntry.getKey().getMetricId()).fire(slotKey, metricCallStatEntry.getKey(), metricCallStatEntry.getValue());
                });
            }
        });
    }

    /**
     * 停止运行。如果已经停止，则应该不会有任何效果。
     * 建议实现使用同步方式执行。
     */
    @Override
    public void stop() throws Exception {
        aggregation.stop();
    }

    /**
     * 检查当前是否在运行状态
     */
    @Override
    public boolean isRunning() {
        return false;
    }

    /**
     * 结果监听器
     */
    public interface ResultListener {
        /**
         * 获取metricsId
         */
        String metricId();

        /**
         * 统计结果监听器
         *
         * @param slotKey
         * @param metric
         * @param callStat
         * @return
         */
        boolean fire(Long slotKey, Metric metric, CallStat callStat);
    }
}
