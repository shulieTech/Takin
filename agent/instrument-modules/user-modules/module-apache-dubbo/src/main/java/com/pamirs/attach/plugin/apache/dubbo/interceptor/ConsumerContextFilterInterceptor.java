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

import com.pamirs.pradar.MiddlewareType;
import com.pamirs.pradar.interceptor.TraceInterceptorAdaptor;
import com.pamirs.pradar.pressurement.ClusterTestUtils;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcInvocation;

public class ConsumerContextFilterInterceptor extends TraceInterceptorAdaptor {

    @Override
    public String getPluginName() {
        return "apache-dubbo";
    }

    @Override
    public int getPluginType() {
        return MiddlewareType.TYPE_RPC;
    }

    @Override
    public void beforeFirst(Advice advice) {
        //被调用的客户端的targetClass name
        Invoker<?> invoker = (Invoker<?>) advice.getParameterArray()[0];
        final RpcInvocation invocation = (RpcInvocation) advice.getParameterArray()[1];
        Class targetClass = invoker.getInterface();
        String className = targetClass.getCanonicalName();
        RpcContext context = RpcContext.getContext();
        String methodName = getMethodName(invocation, context);
        ClusterTestUtils.validateRpcClusterTest(className, methodName);
    }

    private String getMethodName(RpcInvocation invocation, RpcContext context) {
        String methodName = context.getMethodName();
        if (methodName == null) {
            return invocation.getMethodName();
        }
        return methodName;
    }


}
