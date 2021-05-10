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
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.pamirs.pradar.pressurement.agent.shared.service.GlobalConfig;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.elasticsearch.client.support.AbstractClient;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.pamirs.attach.plugin.es.common.ElasticSearchParser.parseAddressAndPort;

/**
 * @author vincent
 * @version v0.1 2016/12/29 11:10
 */
public class TransportClientTraceInterceptor extends TraceInterceptorAdaptor {
    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public String getPluginName() {
        return ElasticsearchConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return ElasticsearchConstants.PLUGIN_TYPE;
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object target = advice.getTarget();
        SpanRecord record = new SpanRecord();
        RequestIndexRename requestIndexRename = RequestIndexRenameProvider.get(args[1]);
        if (requestIndexRename == null) {
            LOGGER.error("elasticsearch {} is not supported", args[1].getClass().getName());
            return null;
        }
        AbstractClient client = (AbstractClient)target;
        record.setService(toString(requestIndexRename.getIndex(args[1])));
        record.setMethod(advice.getBehavior().getName());
        record.setRemoteIp(parseAddressAndPort(client));
        return record;

    }

    @Override
    public void beforeLast(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object target = advice.getTarget();
        check(target, advice.getBehavior().getName(), args);
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
        record.setRequest(args[1]);
        record.setResponse(result);
        return record;

    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Throwable throwable = advice.getThrowable();
        SpanRecord record = new SpanRecord();
        record.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        record.setRequest(args[1]);
        record.setResponse(throwable);
        return record;
    }

    private void check(Object target, String methodName, Object[] args) {
        if (GlobalConfig.getInstance().isShadowEsServer()) {
            return;
        }
        RequestIndexRename requestIndexRename = RequestIndexRenameProvider.get(args[1]);
        List<String> indexs = requestIndexRename.getIndex(args[1]);
        if (Pradar.isClusterTest()) {
            for (String index : indexs) {
                if (!Pradar.isClusterTestPrefix(index)) {
                    /**
                     * 如果索引在白名单中，则不需要走
                     */
                    if (GlobalConfig.getInstance().getSearchWhiteList().contains(index)) {
                        continue;
                    }
                    throw new PressureMeasureError("[error] forbidden pressurement into biz index. index = " + index);
                }
            }
        } else {
            for (String index : indexs) {
                if (Pradar.isClusterTestPrefix(index)) {
                    logger.error("[error]  biz data into pressurement index.");
                }
            }
        }
    }
}
