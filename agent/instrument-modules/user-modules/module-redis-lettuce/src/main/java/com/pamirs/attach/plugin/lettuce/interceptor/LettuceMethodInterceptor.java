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
package com.pamirs.attach.plugin.lettuce.interceptor;

import com.pamirs.attach.plugin.lettuce.LettuceConstants;
import com.pamirs.attach.plugin.lettuce.utils.ParameterUtils;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.reflect.Reflect;
import io.lettuce.core.protocol.DefaultEndpoint;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class LettuceMethodInterceptor extends TraceInterceptorAdaptor {

    @Override
    public String getPluginName() {
        return LettuceConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return LettuceConstants.PLUGIN_TYPE;
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        String methodName = advice.getBehavior().getName();
        Object target = advice.getTarget();
        SpanRecord spanRecord = new SpanRecord();
        appendEndPoint(target, spanRecord);
        spanRecord.setService(methodName);
        spanRecord.setMethod(getMethodNameExt(args));
        spanRecord.setRequest(toArgs(args));
        spanRecord.setMiddlewareName(LettuceConstants.MIDDLEWARE_NAME);
        return spanRecord;
    }

    private Object[] toArgs(Object[] args) {
        if (args == null || args.length == 0) {
            return args;
        }
        Object[] ret = new Object[args.length];
        for (int i = 0, len = args.length; i < len; i++) {
            Object arg = args[i];
            if (arg instanceof byte[]) {
                ret[i] = new String((byte[]) arg);
            } else if (arg instanceof char[]) {
                ret[i] = new String((char[]) arg);
            } else {
                ret[i] = arg;
            }
        }
        return ret;
    }

    public static String getMethodNameExt(Object... args) {
        if (args == null || args.length == 0) {
            return "";
        }
        return ParameterUtils.toString(200, args[0]);
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        SpanRecord spanRecord = new SpanRecord();
        spanRecord.setMiddlewareName(LettuceConstants.MIDDLEWARE_NAME);
        spanRecord.setCallbackMsg(LettuceConstants.PLUGIN_NAME);
        return spanRecord;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        SpanRecord spanRecord = new SpanRecord();
        spanRecord.setResponse(advice.getThrowable());
        spanRecord.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        spanRecord.setMiddlewareName(LettuceConstants.MIDDLEWARE_NAME);
        spanRecord.setCallbackMsg(LettuceConstants.PLUGIN_NAME);
        return spanRecord;
    }

    private String toEndPoint(Object target) {
        try {
            final Object connection = Reflect.on(target).get(LettuceConstants.REFLECT_FIELD_CONNECTION);
            final DefaultEndpoint endpoint = Reflect.on(connection).get(LettuceConstants.REFLECT_FIELD_CHANNEL_WRITER);
            if (endpoint == null) {
                return LettuceConstants.ADDRESS_UNKNOW;
            }
            Channel channel = Reflect.on(endpoint).get(LettuceConstants.REFLECT_FIELD_CHANNEL);
            if (channel == null) {
                return LettuceConstants.ADDRESS_UNKNOW;
            }
            SocketAddress socketAddress = channel.remoteAddress();
            if (socketAddress == null) {
                return LettuceConstants.ADDRESS_UNKNOW;
            }
            if (!(socketAddress instanceof InetSocketAddress)) {
                return LettuceConstants.ADDRESS_UNKNOW;
            }
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            return inetSocketAddress.getAddress().getHostAddress() + ":" + inetSocketAddress.getPort();
        } catch (Throwable e) {
            return LettuceConstants.ADDRESS_UNKNOW;
        }
    }

    private void appendEndPoint(Object target, final SpanRecord spanRecord) {
        try {
            final Object connection = Reflect.on(target).get(LettuceConstants.REFLECT_FIELD_CONNECTION);
            final DefaultEndpoint endpoint = Reflect.on(connection).get(LettuceConstants.REFLECT_FIELD_CHANNEL_WRITER);
            if (endpoint == null) {
                spanRecord.setRemoteIp(LettuceConstants.ADDRESS_UNKNOW);
                return;
            }
            Channel channel = Reflect.on(endpoint).get(LettuceConstants.REFLECT_FIELD_CHANNEL);
            if (channel == null) {
                spanRecord.setRemoteIp(LettuceConstants.ADDRESS_UNKNOW);
                return;
            }
            SocketAddress socketAddress = channel.remoteAddress();
            if (socketAddress == null) {
                spanRecord.setRemoteIp(LettuceConstants.ADDRESS_UNKNOW);
                return;
            }
            if (!(socketAddress instanceof InetSocketAddress)) {
                spanRecord.setRemoteIp(LettuceConstants.ADDRESS_UNKNOW);
                return;
            }
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
            spanRecord.setRemoteIp(inetSocketAddress.getAddress().getHostAddress());
            spanRecord.setPort(inetSocketAddress.getPort());
        } catch (Throwable e) {
            spanRecord.setRemoteIp(LettuceConstants.ADDRESS_UNKNOW);
        }
    }
}
