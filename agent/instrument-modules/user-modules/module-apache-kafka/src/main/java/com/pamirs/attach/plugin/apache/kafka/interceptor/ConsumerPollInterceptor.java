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

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarService;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.common.BytesUtils;
import com.pamirs.pradar.exception.PradarException;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.apache.kafka.interceptor
 * @Date 2019-08-05 19:32
 */
public class ConsumerPollInterceptor extends AroundInterceptor {
    private final static Logger LOGGER = LoggerFactory.getLogger(ConsumerPollInterceptor.class.getName());

    @Override
    public void doBefore(Advice advice) {
        try {
            if (!PradarSwitcher.isClusterTestEnabled()) {
                return;
            }
            if (advice.getReturnObj() == null) {
                return;
            }
            ConsumerRecords consumerRecords = (ConsumerRecords) advice.getReturnObj();
            if (consumerRecords.count() <= 0) {
                return;
            }
            Iterator iterator = consumerRecords.iterator();
            Object next = iterator.next();
            if (!(next instanceof ConsumerRecord)) {
                return;
            }
            ConsumerRecord record = (ConsumerRecord) next;
            String topic = record.topic();
            Pradar.setClusterTest(false);
            boolean isClusterTest = Pradar.isClusterTestPrefix(topic);
            if (PradarSwitcher.isKafkaMessageHeadersEnabled()) {
                Headers headers = record.headers();
                Header header = headers.lastHeader(PradarService.PRADAR_CLUSTER_TEST_KEY);
                if (header != null) {
                    isClusterTest = isClusterTest || ClusterTestUtils.isClusterTestRequest(BytesUtils.toString(header.value()));
                }
            }
            if (isClusterTest) {
                Pradar.setClusterTest(true);
            }
        } catch (PradarException e) {
            LOGGER.error("", e);
            if (Pradar.isClusterTest()) {
                throw e;
            }
        } catch (PressureMeasureError e) {
            LOGGER.error("", e);
            if (Pradar.isClusterTest()) {
                throw e;
            }
        } catch (Throwable e) {
            LOGGER.error("", e);
            if (Pradar.isClusterTest()) {
                throw new PressureMeasureError(e);
            }
        }
    }
}
