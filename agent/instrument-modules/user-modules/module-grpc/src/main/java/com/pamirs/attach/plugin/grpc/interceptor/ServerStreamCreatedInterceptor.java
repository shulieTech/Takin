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
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarCoreUtils;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import io.grpc.Metadata;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GRPC服务端接收处理请求
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.grpc.interceptor
 * @Date 2020-03-13 15:44
 */
public class ServerStreamCreatedInterceptor extends TraceInterceptorAdaptor {

    @Override
    public String getPluginName() {
        return GrpcConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return GrpcConstants.PLUGIN_TYPE;
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        if (!validate(args)) {
            return null;
        }
        SpanRecord record = new SpanRecord();

        Metadata metadata = (Metadata) args[2];

        List<String> traceKeys = Pradar.getInvokeContextTransformKeys();
        Map<String, String> context = new HashMap<String, String>(traceKeys.size());
        for (String traceKey : traceKeys) {
            final String value = metadata.get(Metadata.Key.of(traceKey, Metadata.ASCII_STRING_MARSHALLER));
            if (StringUtils.isNotBlank(value)) {
                context.put(traceKey, value);
            }
        }
        record.setContext(context);

        final String fullMethodName = String.valueOf(args[1]);
        String method = fullMethodName;
        String service = "";
        if (fullMethodName.indexOf(".") != -1) {
            service = fullMethodName.substring(0, fullMethodName.lastIndexOf("."));
            method = fullMethodName.substring(fullMethodName.lastIndexOf(".") + 1);
        }
        record.setMethod(method);
        record.setService(service);
        record.setRequest(metadata);
        String remoteIp = Pradar.getRemoteIp();
        if (remoteIp == null) {
            remoteIp = PradarCoreUtils.getLocalAddress();
        }

        record.setRemoteIp(remoteIp);
        return record;
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object result = advice.getReturnObj();
        if (!validate(args)) {
            return null;
        }
        SpanRecord record = new SpanRecord();
        record.setResponse(result);
        record.setResultCode(ResultCode.INVOKE_RESULT_SUCCESS);
        return record;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        Object[] args = advice.getParameterArray();
        if (!validate(args)) {
            return null;
        }
        SpanRecord record = new SpanRecord();
        record.setResponse(advice.getThrowable());
        record.setResultCode(ResultCode.INVOKE_RESULT_FAILED);
        return record;
    }

    boolean validate(Object[] args) {
        if (args.length == 3) {
            if (!(args[0] instanceof io.grpc.internal.ServerStream)) {
                return false;
            }
            if (!(args[1] instanceof String)) {
                return false;
            }
            if (!(args[2] instanceof io.grpc.Metadata)) {
                return false;
            }
            return true;
        }
        return false;
    }
}
