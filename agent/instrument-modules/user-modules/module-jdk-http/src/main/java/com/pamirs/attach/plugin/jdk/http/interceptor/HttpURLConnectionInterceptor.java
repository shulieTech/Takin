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
package com.pamirs.attach.plugin.jdk.http.interceptor;


import com.pamirs.attach.plugin.jdk.http.JdkHttpConstants;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.interceptor.ContextTransfer;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import org.apache.commons.lang.StringUtils;

import java.net.HttpURLConnection;

public class HttpURLConnectionInterceptor extends TraceInterceptorAdaptor {

    private static String getService(String schema, String host, int port, String path) {
        String url = schema + "://" + host;
        if (port != -1 && port != 80) {
            url = url + ':' + port;
        }
        return url + path;
    }

    @Override
    public String getPluginName() {
        return JdkHttpConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return JdkHttpConstants.PLUGIN_TYPE;
    }

    @Override
    public void beforeLast(Advice advice) {
        Object target = advice.getTarget();
        final HttpURLConnection request = (HttpURLConnection) target;
        String url = getService(request.getURL().getProtocol(),
                request.getURL().getHost(),
                request.getURL().getPort(),
                request.getURL().getPath());
        ClusterTestUtils.validateHttpClusterTest(url);
    }

    @Override
    protected ContextTransfer getContextTransfer(Advice advice) {
        Object target = advice.getTarget();
        final HttpURLConnection request = (HttpURLConnection) target;

        boolean connected = false;
        try {
            connected = Reflect.on(target).get(JdkHttpConstants.DYNAMIC_FIELD_CONNECTED);
        } catch (ReflectException e) {
            LOGGER.warn("{} has not field {}", target.getClass().getName(), JdkHttpConstants.DYNAMIC_FIELD_CONNECTED);
        }

        boolean connecting = false;
        try {
            connecting = Reflect.on(target).get(JdkHttpConstants.DYNAMIC_FIELD_CONNECTING);
        } catch (ReflectException e) {
            LOGGER.warn("{} has not field {}", target.getClass().getName(), JdkHttpConstants.DYNAMIC_FIELD_CONNECTING);
        }

        if (connected || connecting) {
            return null;
        }
        return new ContextTransfer() {
            @Override
            public void transfer(String key, String value) {
                request.setRequestProperty(key, value);
            }
        };
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object target = advice.getTarget();
        final HttpURLConnection request = (HttpURLConnection) target;

        boolean connected = false;
        try {
            connected = Reflect.on(target).get(JdkHttpConstants.DYNAMIC_FIELD_CONNECTED);
        } catch (ReflectException e) {
            LOGGER.warn("{} has not field {}", target.getClass().getName(), JdkHttpConstants.DYNAMIC_FIELD_CONNECTED);
        }

        boolean connecting = false;
        try {
            connecting = Reflect.on(target).get(JdkHttpConstants.DYNAMIC_FIELD_CONNECTING);
        } catch (ReflectException e) {
            LOGGER.warn("{} has not field {}", target.getClass().getName(), JdkHttpConstants.DYNAMIC_FIELD_CONNECTING);
        }

        if (connected || connecting) {
            return null;
        }

        SpanRecord record = new SpanRecord();
        String host = request.getURL().getHost();
        int port = request.getURL().getPort();
        String path = request.getURL().getPath();
        record.setService(path);
        record.setMethod(StringUtils.upperCase(request.getRequestMethod()));
        record.setRemoteIp(host);
        record.setPort(port);
        return record;
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        SpanRecord record = new SpanRecord();
        record.setResultCode(ResultCode.INVOKE_RESULT_SUCCESS);
        return record;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        SpanRecord record = new SpanRecord();
        record.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        record.setResponse(advice.getThrowable());
        return record;
    }

}
