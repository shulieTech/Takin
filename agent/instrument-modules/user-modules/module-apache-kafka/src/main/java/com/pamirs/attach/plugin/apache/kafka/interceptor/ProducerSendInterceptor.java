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

import com.pamirs.attach.plugin.apache.kafka.KafkaConstants;
import com.pamirs.attach.plugin.apache.kafka.header.HeaderProvider;
import com.pamirs.attach.plugin.apache.kafka.header.HeaderSetter;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarService;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.interceptor.ContextTransfer;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.internal.PradarInternalService;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Headers;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * send方法增强类
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.apache.kafka.interceptor
 * @Date 2019-08-05 19:25
 */
public class ProducerSendInterceptor extends TraceInterceptorAdaptor {
    private Field topicField;
    private Field producerConfigField;

    private void initTopicField(Object target) {
        if (topicField != null) {
            return;
        }
        try {
            topicField = target.getClass().getDeclaredField(KafkaConstants.REFLECT_FIELD_TOPIC);
            topicField.setAccessible(true);
        } catch (Throwable e) {
        }
    }

    private void initProducerConfigField(Object target) {
        if (producerConfigField != null) {
            return;
        }
        try {
            producerConfigField = target.getClass().getDeclaredField(KafkaConstants.REFLECT_FIELD_PRODUCER_CONFIG);
            producerConfigField.setAccessible(true);
        } catch (Throwable e) {
        }
    }

    private void setTopic(Object producerRecord, String topic) {
        if (topicField != null) {
            try {
                topicField.set(producerRecord, topic);
            } catch (Throwable e) {
                try {
                    Reflect.on(producerRecord).set(KafkaConstants.REFLECT_FIELD_TOPIC, topic);
                } catch (ReflectException ex) {
                }
            }
        } else {
            try {
                Reflect.on(producerRecord).set(KafkaConstants.REFLECT_FIELD_TOPIC, topic);
            } catch (ReflectException ex) {
            }
        }
    }

    @Override
    public String getPluginName() {
        return KafkaConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return KafkaConstants.PLUGIN_TYPE;
    }

    @Override
    public void beforeFirst(Advice advice) {
        Object[] args = advice.getParameterArray();
        ClusterTestUtils.validateClusterTest();
        try {
            final Callback callback = (Callback) advice.getParameterArray()[1];
            final Map<String, String> context = Pradar.getInvokeContextMap();
            if (callback != null) {
                advice.changeParameter(1, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata metadata, Exception exception) {
                        boolean clear = false;
                        if (PradarService.getInvokeContext().isEmpty()) {
                            PradarInternalService.setInvokeContext(context);
                            clear = true;
                        }
                        callback.onCompletion(metadata, exception);
                        if (clear) {
                            PradarInternalService.clearInvokeContext();
                        }
                    }
                });
            }

        } catch (Throwable e) {
            LOGGER.warn("SIMULATOR: kafka send message wrap callback failed.", e);
        }

        ProducerRecord producerRecord = (ProducerRecord) args[0];
        if (null != producerRecord) {
            if (Pradar.isClusterTest()) {
                String topic = Pradar.addClusterTestPrefix(producerRecord.topic());
                initTopicField(producerRecord);
                setTopic(producerRecord, topic);
                if (PradarSwitcher.isKafkaMessageHeadersEnabled()) {
                    try {
                        Headers headers = producerRecord.headers();
                        headers.add(PradarService.PRADAR_CLUSTER_TEST_KEY, Boolean.TRUE.toString().getBytes());
                    } catch (NoSuchMethodError e) {
                        //可能不存在该方法，如果不存在 headers 方法则直接忽略
                    }
                }
            }
        }
    }

    private String getListString(List<String> value) {
        if (value != null) {
            if (value.size() == 1) {
                return value.get(0);
            } else {
                StringBuilder builder = new StringBuilder();
                for (String str : value) {
                    builder.append(str).append(',');
                }
                if (builder.length() > 0) {
                    builder.deleteCharAt(builder.length() - 1);
                }
                return builder.toString();
            }
        }
        return "";
    }

    private String getValue(ProducerConfig producerConfig, String key) {
        try {
            List<String> value = producerConfig.getList(key);
            return getListString(value);
        } catch (Exception e) {
            return producerConfig.getString(key);
        }
    }

    private String getRemoteAddress(Object remoteAddressFieldAccessor) {
        initProducerConfigField(remoteAddressFieldAccessor);
        try {
            ProducerConfig producerConfig = Reflect.on(remoteAddressFieldAccessor).get(producerConfigField);
            String value = getValue(producerConfig, KafkaConstants.KEY_BOOTSTRAP_SERVERS);
            if (value == null) {
                value = getValue(producerConfig, KafkaConstants.KEY_ZOOKEEPER_CONNECT);
            }
            if (value == null) {
                return KafkaConstants.UNKNOWN;
            }
            return value;
        } catch (Throwable e) {
            return KafkaConstants.UNKNOWN;
        }
    }

    @Override
    protected ContextTransfer getContextTransfer(Advice advice) {
        Object[] args = advice.getParameterArray();
        final ProducerRecord producerRecord = (ProducerRecord) args[0];
        if (PradarSwitcher.isKafkaMessageHeadersEnabled()) {
            return new ContextTransfer() {
                @Override
                public void transfer(String key, String value) {
                    HeaderSetter headerSetter = HeaderProvider.getHeaderSetter(producerRecord);
                    headerSetter.setHeader(producerRecord);
                }
            };
        }
        return null;
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object target = advice.getTarget();
        String remoteAddress = getRemoteAddress(target);
        final ProducerRecord producerRecord = (ProducerRecord) args[0];
        SpanRecord spanRecord = new SpanRecord();
        spanRecord.setService(producerRecord.topic());
        spanRecord.setMethod("MQSend");
        spanRecord.setRemoteIp(remoteAddress);
        return spanRecord;
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object result = advice.getReturnObj();
        final ProducerRecord producerRecord = (ProducerRecord) args[0];
        SpanRecord spanRecord = new SpanRecord();
        spanRecord.setRequest(producerRecord);
        spanRecord.setResponse(result);
        spanRecord.setResultCode(ResultCode.INVOKE_RESULT_SUCCESS);
        return spanRecord;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Throwable throwable = advice.getThrowable();
        final ProducerRecord producerRecord = (ProducerRecord) args[0];
        SpanRecord spanRecord = new SpanRecord();
        spanRecord.setRequest(producerRecord);
        spanRecord.setResponse(throwable);
        spanRecord.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        return spanRecord;
    }
}
