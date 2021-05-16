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
package com.pamirs.attach.plugin.apache.kafka.bestKafka.interceptor;

import com.pamirs.attach.plugin.apache.kafka.KafkaConstants;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarService;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.common.BytesUtils;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

/**
 * @author angju
 * @date 2020/8/21 16:03
 */
public class BestConsumerMessageSingleAcknowledgingMessageListenerInterceptor extends TraceInterceptorAdaptor {
    @Override
    public String getPluginName() {
        return KafkaConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return KafkaConstants.PLUGIN_TYPE;
    }

    @Override
    public void beforeLast(Advice advice) {
        Object[] args = advice.getParameterArray();
        if (args == null) {
            return;
        }

        try {
            ConsumerRecord consumerRecord = (ConsumerRecord) args[0];
            String topic = consumerRecord.topic();
            if (null != topic && Pradar.isClusterTestPrefix(topic)) {
                Pradar.setClusterTest(true);
            }
            if (PradarSwitcher.isKafkaMessageHeadersEnabled()) {
                Headers headers = consumerRecord.headers();
                Header header = headers.lastHeader(PradarService.PRADAR_CLUSTER_TEST_KEY);
                if (null != header
                        && ClusterTestUtils.isClusterTestRequest(BytesUtils.toString(header.value()))) {
                    Pradar.setClusterTest(true);
                }
            }
        } catch (Throwable e) {

        }
    }
}
