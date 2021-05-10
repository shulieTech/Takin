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
package com.pamirs.attach.plugin.es.interceptor;

import java.util.List;

import com.pamirs.attach.plugin.es.ElasticsearchConstants;
import com.pamirs.attach.plugin.es.common.RequestIndexRename;
import com.pamirs.attach.plugin.es.common.RequestIndexRenameProvider;
import com.pamirs.pradar.MiddlewareType;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.es6.interceptor
 * @Date 2020-07-02 17:03
 */
public class RestClientTraceInterceptor extends TraceInterceptorAdaptor {

    @Override
    public String getPluginName() {
        return "es";
    }

    @Override
    public int getPluginType() {
        return MiddlewareType.TYPE_SEARCH;
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object target = advice.getTarget();
        SpanRecord record = new SpanRecord();
        RequestIndexRename requestIndexRename = RequestIndexRenameProvider.get(args[0]);
        if (requestIndexRename == null) {
            LOGGER.error("elasticsearch {} is not supported", args[0].getClass().getName());
            return null;
        }
        final List<String> indexes = requestIndexRename.getIndex(args[0]);
        if (CollectionUtils.isEmpty(indexes)) {
            return null;
        }
        record.setService(toString(indexes));
        record.setMethod(advice.getBehavior().getName());
        String remoteIp = getRemoteIp(target);
        record.setRemoteIp(remoteIp);
        return record;

    }

    private String toString(List<String> list) {
        StringBuilder builder = new StringBuilder();
        for (String str : list) {
            builder.append(str).append(',');
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object result = advice.getReturnObj();
        SpanRecord record = new SpanRecord();
        record.setResultCode(ResultCode.INVOKE_RESULT_SUCCESS);
        record.setRequest(args[0]);
        record.setResponse(result);
        return record;

    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Throwable throwable = advice.getThrowable();
        SpanRecord record = new SpanRecord();
        record.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        record.setRequest(args[0]);
        record.setResponse(throwable);
        return record;
    }

    private String getRemoteIp(Object client) {
        if (!(client instanceof RestClient)) {
            return ElasticsearchConstants.ELASTICSEARCH_ADDRESS_UNKNOW;
        }
        try {
            RestClient restClient = (RestClient) client;
            List<Node> nodes = restClient.getNodes();
            if (CollectionUtils.isEmpty(nodes)) {
                return ElasticsearchConstants.ELASTICSEARCH_ADDRESS_UNKNOW;
            }
            StringBuilder builder = new StringBuilder();
            for (Node node : nodes) {
                builder.append(node.getHost().getHostName()).append(':').append(node.getHost().getPort()).append(';');
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            return builder.toString();
        } catch (Throwable e) {
            return ElasticsearchConstants.ELASTICSEARCH_ADDRESS_UNKNOW;
        }
    }
}
