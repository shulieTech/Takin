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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/05/12 4:15 下午
 */
public class ConsumerHolder {

    public static boolean isZTO = false;

    static {
        try {
            Class.forName("com.zto.consumer.KafkaConsumerProxy");
            isZTO = true;
        } catch (ClassNotFoundException e) {
            isZTO = false;
        }
    }

    private static final Set<Consumer<?, ?>> WORK_WITH_SPRING = Collections.synchronizedSet(
        new HashSet<Consumer<?, ?>>());

    private static final Map<KafkaConsumer, ConsumerProxy> PROXY_MAPPING = new HashMap<KafkaConsumer, ConsumerProxy>();

    private final static Map<KafkaConsumer, ConsumerMetaData> cache = new ConcurrentHashMap<>();

    public static void addWorkWithSpring(Consumer<?, ?> consumer) {
        ConsumerHolder.WORK_WITH_SPRING.add((Consumer<?, ?>)consumer);
    }

    public static boolean isWorkWithOtherFramework(Consumer<?, ?> consumer) {
        return WORK_WITH_SPRING.contains(consumer) && !ConsumerHolder.isZTO;
    }

    public static ConsumerProxy getProxy(Object target) {
        return PROXY_MAPPING.get(target);
    }

    public static ConsumerMetaData getConsumerMetaData(KafkaConsumer consumer) {
        ConsumerMetaData consumerMetaData = cache.get(consumer);
        if (consumerMetaData == null) {
            synchronized (cache) {
                consumerMetaData = cache.get(consumer);
                if (consumerMetaData == null) {
                    consumerMetaData = ConsumerMetaData.build(consumer);
                    cache.put(consumer, consumerMetaData);
                }
            }
        }
        return consumerMetaData;
    }

    public static ConsumerProxy getProxyOrCreate(KafkaConsumer consumer) {
        ConsumerMetaData consumerMetaData = getConsumerMetaData(consumer);
        ConsumerProxy consumerProxy = ConsumerHolder.PROXY_MAPPING.get(consumer);
        if (consumerProxy == null) {
            synchronized (ConsumerHolder.PROXY_MAPPING) {
                consumerProxy = ConsumerHolder.PROXY_MAPPING.get(consumer);
                if (consumerProxy == null) {
                    consumerProxy = new ConsumerProxy(consumer, consumerMetaData, getAllowMaxLag());
                    ConsumerHolder.PROXY_MAPPING.put(consumer, consumerProxy);
                }
            }
        }
        return consumerProxy;
    }

    private static long getAllowMaxLag() {
        long maxLagMillSecond = TimeUnit.SECONDS.toMillis(3);
        String maxLagMillSecondStr = System.getProperty("shadow.kafka.maxLagMillSecond");
        if (!StringUtils.isEmpty(maxLagMillSecondStr)) {
            try {
                maxLagMillSecond = Long.parseLong(maxLagMillSecondStr);
            } catch (NumberFormatException ignore) {
            }
        }
        return maxLagMillSecond;
    }
}
