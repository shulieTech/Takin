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

import io.shulie.surge.data.common.aggregation.metrics.CallStat;
import io.shulie.surge.data.common.aggregation.metrics.Metric;
import io.shulie.surge.data.common.lifecycle.Lifecycle;

/**
 * @author vincent
 */
public interface Aggregator extends Lifecycle {

    /**
     * 长度
     *
     * @return
     */
    int size();

    /**
     * 间隔
     * @return
     */
    int getInterval();

    /**
     * 时延
     * @return
     */
    int getLowerLimit();

    /**
     * 下一个槽位
     * @return
     */
    long getLowerBound();

    /**
     * 槽位到时间戳，slotKey 到 timestamp 的逆转换，已经丢失精度
     * @param key
     * @return
     */
    long slotKeyToTimestamp(long key);

    /**
     * 时间戳到槽位
     * @param timestamp
     * @return
     */
    long timestampToSlotKey(long timestamp);

    /**
     * 获取时间戳的槽位
     * @param timestamp
     * @return
     */
    AggregateSlot<Metric, CallStat> getSlotByTimestamp(long timestamp);


    /**
     * 获取时间戳的槽位
     * @param timestamp
     * @return
     */
    AggregateSlot<Metric, CallStat> getSlotByTimestamp(String timestamp);
}
