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
package com.pamirs.attach.plugin.apache.dubbo.interceptor;

import com.pamirs.attach.plugin.apache.dubbo.DubboConstants;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarCoreUtils;
import com.pamirs.pradar.ResultCode;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.dubbo.rpc.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pamirs.attach.plugin.apache.dubbo.interceptor.DubboUtils.*;

/**
 * @author vincent
 */
public class DubboProviderInterceptor extends TraceInterceptorAdaptor {

    @Override
    public boolean isClient(Advice advice) {
        return false;
    }

    @Override
    public String getPluginName() {
        return DubboConstants.PLUGIN_NAME;
    }

    @Override
    public int getPluginType() {
        return DubboConstants.PLUGIN_TYPE;
    }

    private boolean isLocalHost(String address) {
        return "127.0.0.1".equals(address) || "localhost".equalsIgnoreCase(address);
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        SpanRecord record = new SpanRecord();
        RpcInvocation invocation = (RpcInvocation) advice.getParameterArray()[0];
        String version = getVersion(invocation);
        version = (version != null) ? (":" + version.trim()) : "";

        List<String> traceKeys = Pradar.getInvokeContextTransformKeys();
        final Map<String, String> attachments = invocation.getAttachments();
        Map<String, String> ctx = new HashMap<String, String>();
        for (String traceKey : traceKeys) {
            final String value = attachments.get(traceKey);
            if (value != null) {
                ctx.put(traceKey, value);
            }
        }
        record.setContext(ctx);

        RpcContext context = RpcContext.getContext();
        String remoteHost = context.getRemoteHost();
        if (isLocalHost(remoteHost)) {
            remoteHost = PradarCoreUtils.getLocalAddress();
        }

        record.setRemoteIp(remoteHost);
        final String interfaceName = getInterfaceName(invocation);
        record.setService(interfaceName + version);
        record.setMethod(context.getMethodName() + getParameterTypesString(context.getParameterTypes()));
        record.setRequest(invocation.getArguments());
        return record;
    }

    @Override
    public void beforeLast(Advice advice) {
        ClusterTestUtils.validateClusterTest();
    }

    private String getVersion(Invocation invocation) {
        Invoker invoker = invocation.getInvoker();
        if (invoker != null) {
            return invoker.getUrl().getParameter("version");
        } else {
            return invocation.getAttachment("interface");
        }
    }


    private String getInterfaceName(Invocation invocation) {
        Invoker invoker = invocation.getInvoker();
        if (invoker != null) {
            return invoker.getInterface().getCanonicalName();
        } else {
            return invocation.getAttachment("interface");
        }
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        SpanRecord record = new SpanRecord();
        RpcInvocation invocation = (RpcInvocation) advice.getParameterArray()[0];
        Result result = (Result) advice.getReturnObj();
        record.setResponseSize(getResponseSize(result));
        record.setRequest(invocation.getArguments());
        record.setResponse(getResponse(result));

        if (result == null) {
            record.setResultCode(ResultCode.INVOKE_RESULT_SUCCESS);
        } else if (result.hasException()) {
            record.setResultCode(getResultCode(result.getException()));
        } else {
            record.setResultCode(ResultCode.INVOKE_RESULT_SUCCESS);
        }
        return record;
    }


    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        SpanRecord record = new SpanRecord();
        RpcInvocation invocation = (RpcInvocation) advice.getParameterArray()[0];
        record.setRequest(invocation.getArguments());
        record.setResponse(advice.getThrowable());
        record.setResultCode(getResultCode(advice.getThrowable()));
        return record;
    }

}
