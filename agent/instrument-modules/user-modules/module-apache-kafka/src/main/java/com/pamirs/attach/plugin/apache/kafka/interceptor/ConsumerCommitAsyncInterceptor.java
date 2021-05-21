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
package com.pamirs.attach.plugin.apache.kafka.interceptor;

import java.util.Arrays;
import java.util.Map;

import com.pamirs.attach.plugin.apache.kafka.origin.ConsumerProxy;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.common.TopicPartition;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/05/13 10:30 上午
 */
public class ConsumerCommitAsyncInterceptor extends AbstractProxiedConsumerInterceptor {

    @Override
    protected Object doCutoff(ConsumerProxy consumerProxy, Advice advice) {
        Object[] args = advice.getParameterArray();
        if (args.length == 1 &&
            (args[0] instanceof OffsetCommitCallback || args[0] == null)) {
            if (args[0] == null) {
                consumerProxy.commitAsync();
            } else {
                consumerProxy.commitAsync((OffsetCommitCallback)args[0]);
            }
            return null;
        }
        if (args.length == 2 &&
            args[0] instanceof Map
            && (args[1] instanceof OffsetCommitCallback || args[1] == null)) {
            if (args[1] == null) {
                consumerProxy.commitAsync((Map<TopicPartition, OffsetAndMetadata>)args[0], null);
            } else {
                consumerProxy.commitAsync((Map<TopicPartition, OffsetAndMetadata>)args[0],
                    (OffsetCommitCallback)args[1]);
            }
            return null;
        }
        throw new PressureMeasureError(String.format("未支持的kafka版本！ method : %s(%s)",
            advice.getBehavior().getName(), Arrays.toString(advice.getBehavior().getParameterTypes())));
    }
}
