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
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import com.shulie.instrument.simulator.api.reflect.ReflectException;
import org.apache.commons.lang.StringUtils;
import sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection;

/**
 * @author fabing.zhaofb
 */

public class HttpsURLConnectionInterceptor extends TraceInterceptorAdaptor {

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
    protected ContextTransfer getContextTransfer(Advice advice) {
        Object target = advice.getTarget();
        final AbstractDelegateHttpsURLConnection request = (AbstractDelegateHttpsURLConnection) target;
        boolean connected = false;

        try {
            connected = Reflect.on(target).get(JdkHttpConstants.DYNAMIC_FIELD_CONNECTED);
        } catch (ReflectException e) {
        }

        boolean connecting = false;
        try {
            connecting = Reflect.on(target).get(JdkHttpConstants.DYNAMIC_FIELD_CONNECTING);
        } catch (ReflectException e) {
        }
        if (request.isConnected() || connected || connecting) {
            return null;
        }
        return new ContextTransfer() {
            @Override
            public void transfer(String key, String value) {
                request.addRequestProperty(key, value);
            }
        };
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object target = advice.getTarget();
        final AbstractDelegateHttpsURLConnection request = (AbstractDelegateHttpsURLConnection) target;
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
        if (request.isConnected() || connected || connecting) {
            return null;
        }
        //interceptorScope.getCurrentInvocation().setAttachment(JdkHttpConstants.TRACE_BLOCK_BEGIN_MARKER);
        String host = request.getURL().getHost();
        int port = request.getURL().getPort();
        String path = request.getURL().getPath();
        SpanRecord record = new SpanRecord();
        record.setService(path);
        record.setMethod(StringUtils.upperCase(request.getRequestMethod()));
        record.setRemoteIp(host);
        record.setPort(port);
        return record;
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        SpanRecord record = new SpanRecord();
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
