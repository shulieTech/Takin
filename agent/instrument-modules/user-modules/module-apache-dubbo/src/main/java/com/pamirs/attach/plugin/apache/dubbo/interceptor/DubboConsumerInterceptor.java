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
import com.pamirs.pradar.PradarCoreUtils;
import com.pamirs.pradar.interceptor.ContextTransfer;
import com.pamirs.pradar.interceptor.SpanRecord;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.rpc.*;

/**
 * @author vincent
 */
public class DubboConsumerInterceptor extends TraceInterceptorAdaptor {
    public void initConsumer(Invoker<?> invoker, Invocation invocation) {
        RpcContext.getContext()
                .setInvoker(invoker)
                .setInvocation(invocation)
                .setLocalAddress(NetUtils.getLocalHost(), 0)
                .setRemoteAddress(invoker.getUrl().getHost(),
                        invoker.getUrl().getPort());
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

    private boolean isMonitorService(Invoker invoker) {
        if (invoker != null) {
            return "com.alibaba.dubbo.monitor.MonitorService".equals(invoker.getInterface().getName());
        }
        return false;
    }

    @Override
    public void beforeFirst(Advice advice) {
        final RpcInvocation invocation = (RpcInvocation) advice.getParameterArray()[0];
        String interfaceName = getInterfaceName(invocation);
        RpcContext context = RpcContext.getContext();
        String methodName = context.getMethodName();
        ClusterTestUtils.validateRpcClusterTest(interfaceName, methodName);

    }

    private String getInterfaceName(Invocation invocation) {
        Invoker invoker = invocation.getInvoker();
        if (invoker != null) {
            return invoker.getInterface().getCanonicalName();
        } else {
            return invocation.getAttachment("interface");
        }
    }

    private String buildServiceName(String className, String version) {
        if (StringUtils.isBlank(version)) {
            return className;
        }
        return className + ':' + version;
    }

    private String getVersion(Invocation invocation) {
        Invoker invoker = invocation.getInvoker();
        if (invoker != null) {
            return invoker.getUrl().getParameter("version");
        } else {
            return invocation.getAttachment("version");
        }
    }

    @Override
    protected ContextTransfer getContextTransfer(Advice advice) {
        final RpcInvocation invocation = (RpcInvocation) advice.getParameterArray()[0];
        Invoker<?> invoker = (Invoker<?>) advice.getTarget();
        if (isMonitorService(invoker)) {
            return null;
        }
        if (invocation instanceof RpcInvocation) {
            return new ContextTransfer() {
                @Override
                public void transfer(String key, String value) {
                    (invocation).setAttachment(key, value);
                }
            };
        }
        return null;
    }

    @Override
    public SpanRecord beforeTrace(Advice advice) {
        final RpcInvocation invocation = (RpcInvocation) advice.getParameterArray()[0];
        Invoker<?> invoker = (Invoker<?>) advice.getTarget();
        if (isMonitorService(invoker)) {
            return null;
        }

        initConsumer((Invoker<?>) advice.getTarget(), invocation);
        RpcContext context = RpcContext.getContext();
        String remoteHost = context.getRemoteHost();
        if (isLocalHost(remoteHost)) {
            remoteHost = PradarCoreUtils.getLocalAddress();
        }

        SpanRecord record = new SpanRecord();
        record.setRemoteIp(remoteHost);
        record.setPort(context.getRemotePort());
        String version = getVersion(invocation);
        final String name = getInterfaceName(invocation);
        record.setService(buildServiceName(name, version));
        record.setMethod(context.getMethodName()
                + DubboUtils.getParameterTypesString(context.getParameterTypes()));
        record.setRequestSize(DubboUtils.getRequestSize());
        record.setRequest(invocation.getArguments());
        return record;
    }

    @Override
    public SpanRecord afterTrace(Advice advice) {
        Result result = (Result) advice.getReturnObj();
        SpanRecord record = new SpanRecord();
        record.setResponse(DubboUtils.getResponse(result));
        record.setResponseSize(result == null ? 0 : DubboUtils.getResponseSize(result));
        if (result.hasException()) {
            record.setResultCode(DubboUtils.getResultCode(result.getException()));
            record.setResponse(result.getException());
        }
        return record;
    }

    @Override
    public SpanRecord exceptionTrace(Advice advice) {
        SpanRecord record = new SpanRecord();
        record.setResponseSize(0);
        record.setResponse(advice.getThrowable());
        record.setResultCode(DubboUtils.getResultCode(advice.getThrowable()));
        return record;
    }
}
