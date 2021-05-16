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
import com.pamirs.attach.plugin.apache.kafka.header.HeaderGetter;
import com.pamirs.attach.plugin.apache.kafka.header.HeaderProvider;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarService;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.common.BytesUtils;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.apache.kafka.interceptor
 * @Date 2019-08-05 19:34
 */
public class ConsumerRecordEntryPointInterceptor extends TraceInterceptorAdaptor {
    @Resource
    protected DynamicFieldManager manager;

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
        Object[] args = advice.getParameterArray();
        ConsumerRecord consumerRecord = (ConsumerRecord) args[0];
        long consumerCell = System.currentTimeMillis() - consumerRecord.timestamp();
        String group = null;
        String remoteAddress = null;
        if (args.length >= 3) {
            group = manager.removeField(args[2], KafkaConstants.DYNAMIC_FIELD_GROUP);
            remoteAddress = getRemoteAddress(args[2]);
        }
        SpanRecord spanRecord = new SpanRecord();
        spanRecord.setRemoteIp(remoteAddress);
        if (PradarSwitcher.isKafkaMessageHeadersEnabled()) {
            HeaderGetter headerGetter = HeaderProvider.getHeaderGetter(consumerRecord);
            Map<String, String> ctx = headerGetter.getHeaders(consumerRecord);
            spanRecord.setContext(ctx);
        }
        spanRecord.setRequest(consumerRecord);
        spanRecord.setService(consumerRecord.topic());
        spanRecord.setMethod(group == null ? "" : group);
        spanRecord.setCallbackMsg(consumerCell + "");
        return spanRecord;
    }

    private String getRemoteAddress(Object remoteAddressFieldAccessor) {
        String remoteAddress = manager.removeField(remoteAddressFieldAccessor, KafkaConstants.DYNAMIC_FIELD_REMOTE_ADDRESS);

        if (StringUtils.isEmpty(remoteAddress)) {
            return KafkaConstants.UNKNOWN;
        } else {
            return remoteAddress;
        }
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object result = advice.getReturnObj();
        ConsumerRecord consumerRecord = (ConsumerRecord) args[0];
        SpanRecord spanRecord = new SpanRecord();
        spanRecord.setRequest(consumerRecord);
        spanRecord.setResponse(result);
        spanRecord.setResultCode(ResultCode.INVOKE_RESULT_SUCCESS);
        return spanRecord;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Throwable throwable = advice.getThrowable();
        ConsumerRecord consumerRecord = (ConsumerRecord) args[0];
        SpanRecord spanRecord = new SpanRecord();
        spanRecord.setRequest(consumerRecord);
        spanRecord.setResponse(throwable);
        spanRecord.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        return spanRecord;
    }

    @Override
    public void beforeLast(Advice advice) {
        Object[] args = advice.getParameterArray();
        ClusterTestUtils.validateClusterTest();
        ConsumerRecord consumerRecord = (ConsumerRecord) args[0];
        String topic = consumerRecord.topic();
        boolean isClusterTest = Pradar.isClusterTestPrefix(topic);
        if (PradarSwitcher.isKafkaMessageHeadersEnabled()) {
            Headers headers = consumerRecord.headers();
            Header header = headers.lastHeader(PradarService.PRADAR_CLUSTER_TEST_KEY);
            if (header != null) {
                isClusterTest = isClusterTest || ClusterTestUtils.isClusterTestRequest(BytesUtils.toString(header.value()));
            }
        }
        if (isClusterTest) {
            Pradar.setClusterTest(true);
        }
    }
}
