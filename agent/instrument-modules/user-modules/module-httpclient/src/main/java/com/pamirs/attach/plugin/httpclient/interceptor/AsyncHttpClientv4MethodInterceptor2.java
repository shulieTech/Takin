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
package com.pamirs.attach.plugin.httpclient.interceptor;

import com.pamirs.attach.plugin.httpclient.HttpClientConstants;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.common.HeaderMark;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;

import java.util.Map;

/**
 * Created by xiaobin on 2016/12/15.
 */
public class AsyncHttpClientv4MethodInterceptor2 extends AroundInterceptor {

    private static String getService(String schema, String host, int port, String path) {
        String url = schema + "://" + host;
        if (port != -1 && port != 80) {
            url = url + ':' + port;
        }
        return url + path;
    }

    @Override
    public void doBefore(final Advice advice) {
        Object[] args = advice.getParameterArray();
        HttpAsyncRequestProducer httpAsyncRequestProducer = (HttpAsyncRequestProducer) args[0];

        HttpHost httpHost = httpAsyncRequestProducer.getTarget();
        HttpRequest request = null;
        try {
            request = httpAsyncRequestProducer.generateRequest();
        } catch (Throwable e) {
            LOGGER.error("AsyncHttpClient org.apache.http.impl.nio.client.CloseableHttpAsyncClient.execute(org.apache.http.nio.protocol.HttpAsyncRequestProducer, org.apache.http.nio.protocol.HttpAsyncResponseConsumer<T>, org.apache.http.concurrent.FutureCallback<T>) generateRequest error. ignore it", e);
        }

        if (httpHost == null) {
            return;
        }

        String host = httpHost.getHostName();
        int port = httpHost.getPort();
        String path = httpHost.getHostName();
        String reqStr = request.toString();
        String method = StringUtils.upperCase(reqStr.substring(0, reqStr.indexOf(" ")));
        if (request instanceof HttpUriRequest) {
            path = ((HttpUriRequest) request).getURI().getPath();
            method = ((HttpUriRequest) request).getMethod();
        }

        //判断是否在白名单中
        String url = getService(httpHost.getSchemeName(), host, port, path);
        ClusterTestUtils.validateHttpClusterTest(url);


        Pradar.startClientInvoke(path, method);
        Pradar.remoteIp(host);
        Pradar.remotePort(port);
        Pradar.middlewareName(HttpClientConstants.HTTP_CLIENT_NAME_4X);
        Header[] headers = request.getHeaders("content-length");
        if (headers != null && headers.length != 0) {
            try {
                Header header = headers[0];
                Pradar.requestSize(Integer.valueOf(header.getValue()));
            } catch (NumberFormatException e) {
            }
        }
        final Map<String, String> context = Pradar.getInvokeContextMap();
        for (Map.Entry<String, String> entry : context.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (request.getHeaders(HeaderMark.DONT_MODIFY_HEADER) == null ||
                    request.getHeaders(HeaderMark.DONT_MODIFY_HEADER).length == 0) {
                request.setHeader(key, value);
            }
        }
        Pradar.popInvokeContext();

        Object future = args[args.length - 1];
        if (!(future instanceof FutureCallback)) {
            return;
        }
        advice.changeParameter(args.length - 1, new FutureCallback() {
            @Override
            public void completed(Object result) {
                Pradar.setInvokeContext(context);
                try {
                    if (result instanceof HttpResponse) {
                        afterTrace(advice, (HttpResponse) result);
                    } else {
                        afterTrace(advice, null);
                    }
                } catch (Throwable e) {
                    LOGGER.error("AsyncHttpClient execute future endTrace error.", e);
                    Pradar.endClientInvoke("200", HttpClientConstants.PLUGIN_TYPE);
                }
            }

            @Override
            public void failed(Exception ex) {
                Pradar.setInvokeContext(context);
                try {
                    exceptionTrace(advice, ex);
                } catch (Throwable e) {
                    LOGGER.error("AsyncHttpClient execute future endTrace error.", e);
                    Pradar.endClientInvoke("200", HttpClientConstants.PLUGIN_TYPE);
                }
            }

            @Override
            public void cancelled() {
                Pradar.setInvokeContext(context);
                try {
                    exceptionTrace(advice, null);
                } catch (Throwable e) {
                    LOGGER.error("AsyncHttpClient execute future endTrace error.", e);
                    Pradar.endClientInvoke("200", HttpClientConstants.PLUGIN_TYPE);
                }
            }
        });

    }

    public void afterTrace(Advice advice, HttpResponse response) {
        Object[] args = advice.getParameterArray();
        HttpRequest request = (HttpRequest) args[1];
        try {
            Pradar.responseSize(response == null ? 0 : response.getEntity().getContentLength());
        } catch (Throwable e) {
            Pradar.responseSize(0);
        }
        Pradar.request(request.getParams());
        int code = response == null ? 200 : response.getStatusLine().getStatusCode();
        Pradar.endClientInvoke(code + "", HttpClientConstants.PLUGIN_TYPE);

    }


    public void exceptionTrace(Advice advice, Throwable throwable) {
        Object[] args = advice.getParameterArray();
        HttpRequest request = (HttpRequest) args[1];

        Pradar.request(request.getParams());
        Pradar.response(throwable);
        Pradar.endClientInvoke(ResultCode.INVOKE_RESULT_FAILED, HttpClientConstants.PLUGIN_TYPE);
    }

}
