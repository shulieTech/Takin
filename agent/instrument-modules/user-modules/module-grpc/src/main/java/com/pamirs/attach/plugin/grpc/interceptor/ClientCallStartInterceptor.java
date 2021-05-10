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
package com.pamirs.attach.plugin.grpc.interceptor;

import com.pamirs.attach.plugin.grpc.GrpcConstants;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.interceptor.ContextTransfer;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import io.grpc.Metadata;

import javax.annotation.Resource;

/**
 * GRPC调用服务端
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.grpc.interceptor
 * @Date 2020-03-13 15:36
 */
public class ClientCallStartInterceptor extends TraceInterceptorAdaptor {
    @Resource
    protected DynamicFieldManager manager;

    @Override
    public String getPluginName() {
        return GrpcConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return GrpcConstants.PLUGIN_TYPE;
    }

    @Override
    protected ContextTransfer getContextTransfer(Advice advice) {
        Object[] args = advice.getParameterArray();
        if (args == null || args.length != 2) {
            return null;
        }
        if (!(args[1] instanceof Metadata)) {
            return null;
        }
        final Metadata metadata = (Metadata) args[1];
        return new ContextTransfer() {
            @Override
            public void transfer(String keyName, String value) {
                Metadata.Key<String> key = Metadata.Key.of(keyName, Metadata.ASCII_STRING_MARSHALLER);
                if (null != value) {
                    metadata.put(key, value);
                }
            }
        };
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object target = advice.getTarget();
        if (args == null || args.length != 2) {
            return null;
        }
        if (!(args[1] instanceof Metadata)) {
            return null;
        }
        SpanRecord record = new SpanRecord();

        String host = getEndPoint(target);
        String port = "";
        if (host.indexOf(":") != -1) {
            port = host.substring(host.lastIndexOf(":") + 1);
            host = host.substring(0, host.lastIndexOf(":"));
        }
        record.setRemoteIp(host);
        record.setPort(port);
        final String fullMethodName = getMethodName(target);
        String method = fullMethodName;
        String service = "";
        if (fullMethodName.indexOf(".") != -1) {
            service = fullMethodName.substring(0, fullMethodName.lastIndexOf("."));
            method = fullMethodName.substring(fullMethodName.lastIndexOf(".") + 1);
        }

        record.setService(service);
        record.setMethod(method);
        final Metadata metadata = (Metadata) args[1];
        record.setRequest(metadata);
        return record;
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object result = advice.getReturnObj();
        if (args == null || args.length != 2) {
            return null;
        }
        if (!(args[1] instanceof Metadata)) {
            return null;
        }
        SpanRecord record = new SpanRecord();
        record.setResponse(result == null ? 0 : result);
        record.setResultCode(ResultCode.INVOKE_RESULT_SUCCESS);
        return record;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Throwable throwable = advice.getThrowable();
        if (args == null || args.length != 2) {
            return null;
        }
        SpanRecord record = new SpanRecord();
        record.setResponse(throwable);
        record.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        return record;
    }

    private String getEndPoint(Object target) {
        String remoteAddress = manager.getDynamicField(target, GrpcConstants.DYNAMIC_FIELD_REMOTE_ADDRESS);
        if (remoteAddress != null) {
            return remoteAddress;
        }
        return "Unknown";
    }

    private String getMethodName(Object target) {
        String methodName = manager.getDynamicField(target, GrpcConstants.DYNAMIC_FIELD_METHOD_NAME);
        if (methodName != null) {
            return methodName;
        }
        return "UnknownMethod";
    }
}
