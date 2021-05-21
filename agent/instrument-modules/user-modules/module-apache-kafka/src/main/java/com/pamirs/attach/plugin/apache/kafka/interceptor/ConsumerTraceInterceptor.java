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

import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import com.pamirs.attach.plugin.apache.kafka.KafkaConstants;
import com.pamirs.attach.plugin.apache.kafka.header.HeaderGetter;
import com.pamirs.attach.plugin.apache.kafka.header.HeaderProvider;
import com.pamirs.attach.plugin.apache.kafka.origin.ConsumerHolder;
import com.pamirs.attach.plugin.apache.kafka.origin.ConsumerMetaData;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.interceptor.ReversedTraceInterceptorAdaptor;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/05/14 11:37 上午
 */
public class ConsumerTraceInterceptor extends ReversedTraceInterceptorAdaptor {

    @Resource
    protected DynamicFieldManager manager;

    private final ThreadLocal<Boolean> lastPollHasRecordsThreadLocal = new ThreadLocal<>();

    @Override
    public String getPluginName() {
        return KafkaConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return KafkaConstants.PLUGIN_TYPE;
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object result = advice.getReturnObj();
        KafkaConsumer kafkaConsumer = (KafkaConsumer)advice.getTarget();
        if (ConsumerHolder.isWorkWithOtherFramework(kafkaConsumer)) {
            return null;
        }
        ConsumerMetaData consumerMetaData = ConsumerHolder.getConsumerMetaData(kafkaConsumer);
        ConsumerRecords consumerRecords = (ConsumerRecords)result;
        if (consumerRecords.isEmpty()) {
            lastPollHasRecordsThreadLocal.set(false);
            return null;
        }
        Iterator iterator = consumerRecords.iterator();
        Object next = iterator.next();
        if (!(next instanceof ConsumerRecord)) {
            return null;
        }
        lastPollHasRecordsThreadLocal.set(true);
        ConsumerRecord consumerRecord = (ConsumerRecord)next;
        SpanRecord spanRecord = new SpanRecord();
        if (PradarSwitcher.isKafkaMessageHeadersEnabled()) {
            HeaderGetter headerGetter = HeaderProvider.getHeaderGetter(consumerRecord);
            Map<String, String> ctx = headerGetter.getHeaders(consumerRecord);
            spanRecord.setContext(ctx);
        }
        String topic = consumerRecord.topic();
        spanRecord.setRemoteIp(consumerMetaData.getBootstrapServers());
        spanRecord.setRequest(consumerRecords.count());
        spanRecord.setService(consumerRecord.topic());
        spanRecord.setMethod(
            Pradar.isClusterTestPrefix(topic) ? consumerMetaData.getPtGroupId() : consumerMetaData.getGroupId());
        return spanRecord;
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        if (ConsumerHolder.isWorkWithOtherFramework((Consumer)advice.getTarget())) {
            return null;
        }
        Boolean lastPollHasRecords = lastPollHasRecordsThreadLocal.get();
        if (lastPollHasRecords == null || !lastPollHasRecords) {
            return null;
        }
        SpanRecord spanRecord = new SpanRecord();
        spanRecord.setResponse("next poll");
        spanRecord.setResultCode(ResultCode.INVOKE_RESULT_SUCCESS);
        return spanRecord;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        if (ConsumerHolder.isWorkWithOtherFramework((Consumer)advice.getTarget())) {
            return null;
        }
        Throwable throwable = advice.getThrowable();
        SpanRecord spanRecord = new SpanRecord();
        spanRecord.setRequest("poll");
        spanRecord.setResponse(throwable);
        spanRecord.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        return spanRecord;
    }
}
