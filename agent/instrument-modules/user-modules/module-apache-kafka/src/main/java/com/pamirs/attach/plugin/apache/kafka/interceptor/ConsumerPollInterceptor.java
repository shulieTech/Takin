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

import java.time.Duration;
import java.util.Iterator;

import com.pamirs.attach.plugin.apache.kafka.origin.ConsumerHolder;
import com.pamirs.attach.plugin.apache.kafka.origin.ConsumerProxy;
import com.pamirs.attach.plugin.apache.kafka.origin.ConsumerMetaData;
import com.pamirs.pradar.CutOffResult;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarService;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.common.BytesUtils;
import com.pamirs.pradar.exception.PradarException;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.CutoffInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.utils.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.apache.kafka.interceptor
 * @Date 2019-08-05 19:32
 */
@SuppressWarnings("rawtypes")
public class ConsumerPollInterceptor extends CutoffInterceptorAdaptor {
    private final static Logger LOGGER = LoggerFactory.getLogger(ConsumerPollInterceptor.class.getName());

    @Override
    public CutOffResult cutoff0(Advice advice) throws Throwable {
        if (!PradarSwitcher.isClusterTestEnabled()) {
            return CutOffResult.passed();
        }
        if (ConsumerHolder.isWorkWithOtherFramework((Consumer<?, ?>)advice.getTarget())) {
            doWithSpringIntercept(advice);
            return CutOffResult.passed();
        } else {
            return doOriginIntercept(advice);
        }
    }

    private CutOffResult doOriginIntercept(Advice advice) {
        KafkaConsumer consumer = (KafkaConsumer)advice.getTarget();
        ConsumerMetaData consumerMetaData = ConsumerHolder.getConsumerMetaData(consumer);
        if (consumerMetaData == null) {
            return CutOffResult.passed();
        }
        if (consumerMetaData.isHasShadow()) {
            Object[] args = advice.getParameterArray();
            ConsumerProxy consumerProxy = ConsumerHolder.getProxyOrCreate(consumer);
            long timeout = 100L;
            if (args[0] instanceof Long) {
                timeout = (long)args[0];
            } else if (args[0] instanceof Duration) {
                timeout = ((Duration)args[0]).toMillis();
            } else if (args[0] instanceof Timer) {
                timeout = ((Timer)args[0]).remainingMs();
            }
            return CutOffResult.cutoff(consumerProxy.poll(timeout));
        } else {
            return CutOffResult.passed();
        }
    }

    public void doWithSpringIntercept(Advice advice) {
        try {
            if (!PradarSwitcher.isClusterTestEnabled()) {
                return;
            }
            if (advice.getReturnObj() == null) {
                return;
            }
            ConsumerRecords consumerRecords = (ConsumerRecords)advice.getReturnObj();
            if (consumerRecords.count() <= 0) {
                return;
            }
            Iterator iterator = consumerRecords.iterator();
            Object next = iterator.next();
            if (!(next instanceof ConsumerRecord)) {
                return;
            }
            ConsumerRecord record = (ConsumerRecord)next;
            String topic = record.topic();
            Pradar.setClusterTest(false);
            boolean isClusterTest = Pradar.isClusterTestPrefix(topic);
            if (PradarSwitcher.isKafkaMessageHeadersEnabled()) {
                Headers headers = record.headers();
                Header header = headers.lastHeader(PradarService.PRADAR_CLUSTER_TEST_KEY);
                if (header != null) {
                    isClusterTest = isClusterTest || ClusterTestUtils.isClusterTestRequest(
                        BytesUtils.toString(header.value()));
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
