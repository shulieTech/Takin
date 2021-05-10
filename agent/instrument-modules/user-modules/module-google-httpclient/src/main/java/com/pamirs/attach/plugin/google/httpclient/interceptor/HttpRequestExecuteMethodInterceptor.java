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
package com.pamirs.attach.plugin.google.httpclient.interceptor;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.pamirs.attach.plugin.google.httpclient.HttpClientConstants;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.interceptor.ContextTransfer;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;

/**
 * @author fabing.zhaofb
 */
public class HttpRequestExecuteMethodInterceptor extends TraceInterceptorAdaptor {

    @Override
    public String getPluginName() {
        return HttpClientConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return HttpClientConstants.PLUGIN_TYPE;
    }

    @Override
    public void afterFirst(Advice advice) {
        Object target = advice.getTarget();
        final HttpRequest request = (HttpRequest) target;
        String url = request.getUrl().build();

        ClusterTestUtils.validateHttpClusterTest(url);
    }

    @Override
    protected ContextTransfer getContextTransfer(Advice advice) {
        Object target = advice.getTarget();
        final HttpRequest request = (HttpRequest) target;
        final HttpHeaders headers = request.getHeaders();
        return new ContextTransfer() {
            @Override
            public void transfer(String key, String value) {
                headers.set(key, Collections.singletonList(value));
            }
        };
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object target = advice.getTarget();
        final HttpRequest request = (HttpRequest) target;
        SpanRecord record = new SpanRecord();

        record.setService(request.getUrl().getRawPath());
        record.setMethod(StringUtils.upperCase(request.getRequestMethod()));
        record.setRemoteIp(request.getUrl().getHost());
        record.setPort(request.getUrl().getPort());
        final HttpHeaders headers = request.getHeaders();

        record.setRequest(new Object[]{headers, request.getUrl().build()});
        Long contentLength = headers.getContentLength();
        contentLength = contentLength == null ? 0 : contentLength;
        record.setRequestSize(contentLength);
        record.setRemoteIp(request.getUrl().getHost());
        return record;
    }


    @Override
    public SpanRecord afterTrace(Advice advice) {
        Object result = advice.getReturnObj();
        SpanRecord record = new SpanRecord();
        HttpResponse response = (HttpResponse) result;
        record.setResponse(response.getStatusCode() + "->" + response.getStatusMessage());
        record.setResponseSize(response.getHeaders().getContentLength());
        record.setResultCode(String.valueOf(response.getStatusCode()));
        return record;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        Throwable throwable = advice.getThrowable();
        SpanRecord record = new SpanRecord();
        record.setResponse(throwable);
        record.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        return record;

    }
}
