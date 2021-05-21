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
package com.pamirs.attach.plugin.apache.kafka.origin.selector;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/05/12 5:30 下午
 */
@Deprecated
public class LagDetectionSelector implements PollConsumerSelector {

    private final Logger log = LoggerFactory.getLogger(LagDetectionSelector.class);

    private final long maxLagMillSecond;

    private final PollConsumerSelector consumerSelector;

    private final KafkaConsumer bizConsumer;

    public LagDetectionSelector(PollConsumerSelector selector,
        KafkaConsumer bizConsumer,
        long maxLag, TimeUnit timeUnit) {
        this.maxLagMillSecond = timeUnit.toMillis(maxLag);
        this.bizConsumer = bizConsumer;
        this.consumerSelector = selector;
    }

    @Override
    public ConsumerType select() {
        //todo 需要每次检测？可不可以当延迟较少的时候不检测？
        if (isLagTooMuch()) {
            log.warn("检测到业务consumer延迟过高，已经设置为业务消费优先");
            return ConsumerType.BIZ;
        }
        return consumerSelector.select();
    }

    /**
     * 目前通过kafka提供的api不能得到具体延迟多少秒，只能得出是否延迟了多少秒
     * 判断已经提交的偏移量是否小于过去maxLagMillSecond之前的偏移量，就可以得出是否延迟超过了maxLagMillSecond
     */
    private boolean isLagTooMuch() {
        Set<TopicPartition> assignedTopicPartition = bizConsumer.assignment();
        Map<TopicPartition, Long> committedOffsets = new HashMap<>();
        Map<TopicPartition, Long> timeMap = new HashMap<>();
        long checkTime = System.currentTimeMillis() - maxLagMillSecond;
        for (TopicPartition topicPartition : assignedTopicPartition) {
            OffsetAndMetadata offsetAndMetadata = bizConsumer.committed(topicPartition);
            timeMap.put(topicPartition, checkTime);
            committedOffsets.put(topicPartition, offsetAndMetadata == null ? 0 : offsetAndMetadata.offset());
        }
        Map<TopicPartition, OffsetAndTimestamp> offsetsForTimes = bizConsumer.offsetsForTimes(timeMap);
        boolean result = false;
        for (Entry<TopicPartition, Long> entry : committedOffsets.entrySet()) {
            long committed = entry.getValue();
            OffsetAndTimestamp offsetAndTimestamp = offsetsForTimes.get(entry.getKey());
            if (offsetAndTimestamp != null) {
                long timeOffset = offsetAndTimestamp.offset();
                if (committed < timeOffset) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
