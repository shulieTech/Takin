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
