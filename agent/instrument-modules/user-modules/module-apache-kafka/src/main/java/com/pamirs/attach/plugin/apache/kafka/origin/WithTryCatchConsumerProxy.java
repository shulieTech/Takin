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
package com.pamirs.attach.plugin.apache.kafka.origin;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/05/17 2:36 下午
 */
public class WithTryCatchConsumerProxy implements Consumer {

    private final KafkaConsumer consumer;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public WithTryCatchConsumerProxy(KafkaConsumer consumer) {this.consumer = consumer;}

    @Override
    public Set<TopicPartition> assignment() {
        try {
            return consumer.assignment();
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "assignment", e);
            return Collections.emptySet();
        }
    }

    @Override
    public Set<String> subscription() {
        try {
            return consumer.subscription();
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "subscription", e);
            return Collections.emptySet();
        }
    }

    @Override
    public void subscribe(Pattern pattern, ConsumerRebalanceListener callback) {
        try {
            consumer.subscribe(pattern, callback);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "subscribe", e);
        }
    }

    @Override
    public void unsubscribe() {
        try {
            consumer.unsubscribe();
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "unsubscribe", e);
        }
    }

    @Override
    public ConsumerRecords poll(long timeout) {
        try {
            return consumer.poll(timeout);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "poll", e);
            return ConsumerRecords.empty();
        }
    }

    @Override
    public void commitSync() {
        try {
            consumer.commitSync();
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "commitSync", e);
        }
    }

    @Override
    public void commitAsync() {
        try {
            consumer.commitAsync();
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "commitAsync", e);
        }
    }

    @Override
    public void commitAsync(OffsetCommitCallback callback) {
        try {
            consumer.commitAsync(callback);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "commitAsync", e);
        }
    }

    @Override
    public void seek(TopicPartition partition, long offset) {
        try {
            consumer.seek(partition, offset);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "seek", e);
        }
    }

    @Override
    public long position(TopicPartition partition) {
        try {
            return consumer.position(partition);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "position", e);
        }
        return 0;
    }

    @Override
    public OffsetAndMetadata committed(TopicPartition partition) {
        try {
            return consumer.committed(partition);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "committed", e);
        }
        return new OffsetAndMetadata(0);
    }

    @Override
    public Map<MetricName, ? extends Metric> metrics() {
        try {
            return consumer.metrics();
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "metrics", e);
        }
        return Collections.emptyMap();
    }

    @Override
    public List<PartitionInfo> partitionsFor(String topic) {
        try {
            return consumer.partitionsFor(topic);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "partitionsFor", e);
        }
        return Collections.emptyList();
    }

    @Override
    public Map<String, List<PartitionInfo>> listTopics() {
        try {
            return consumer.listTopics();
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "listTopics", e);
        }
        return Collections.emptyMap();
    }

    @Override
    public Set<TopicPartition> paused() {
        try {
            return consumer.paused();
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "paused", e);
        }
        return Collections.emptySet();
    }

    @Override
    public void close() {
        try {
            consumer.close();
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "close", e);
        }
    }

    @Override
    public void close(long timeout, TimeUnit unit) {
        try {
            consumer.close(timeout, unit);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "close", e);
        }
    }

    @Override
    public void wakeup() {
        try {
            consumer.wakeup();
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "wakeup", e);
        }
    }

    @Override
    public Map<TopicPartition, Long> endOffsets(Collection collection) {
        try {
            return consumer.endOffsets(collection);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "endOffsets", e);
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<TopicPartition, Long> beginningOffsets(Collection collection) {
        try {
            return consumer.beginningOffsets(collection);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "beginningOffsets", e);
        }
        return Collections.emptyMap();
    }

    @Override
    public Map<TopicPartition, OffsetAndTimestamp> offsetsForTimes(Map timestampsToSearch) {
        try {
            return consumer.offsetsForTimes(timestampsToSearch);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "offsetsForTimes", e);
        }
        return Collections.emptyMap();
    }

    @Override
    public void resume(Collection collection) {
        try {
            consumer.resume(collection);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "resume", e);
        }
    }

    @Override
    public void pause(Collection collection) {
        try {
            consumer.pause(collection);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "pause", e);
        }
    }

    @Override
    public void seekToEnd(Collection collection) {
        try {
            consumer.seekToEnd(collection);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "seekToEnd", e);
        }
    }

    @Override
    public void seekToBeginning(Collection collection) {
        try {
            consumer.seekToBeginning(collection);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "seekToBeginning", e);
        }
    }

    @Override
    public void commitAsync(Map offsets, OffsetCommitCallback callback) {
        try {
            consumer.commitAsync(offsets, callback);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "commitAsync", e);
        }
    }

    @Override
    public void commitSync(Map offsets) {
        try {
            consumer.commitSync(offsets);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "commitSync", e);
        }
    }

    @Override
    public void assign(Collection collection) {
        try {
            consumer.assign(collection);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "assign", e);
        }
    }

    @Override
    public void subscribe(Collection topics, ConsumerRebalanceListener callback) {
        try {
            consumer.subscribe(topics, callback);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "subscribe", e);
        }
    }

    @Override
    public void subscribe(Collection topics) {
        try {
            consumer.subscribe(topics);
        } catch (Exception e) {
            log.error("shadow consumer {} invoke fail!", "subscribe", e);
        }
    }
}
