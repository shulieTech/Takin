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
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;
import io.grpc.MethodDescriptor;

import javax.annotation.Resource;

/**
 * Grpc获取调用IP、方法名
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.grpc.interceptor
 * @Date 2020-03-13 15:49
 */
public class ChannelNewCallInterceptor extends AroundInterceptor {
    @Resource
    protected DynamicFieldManager manager;

    @Override
    public void doAfter(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object target = advice.getTarget();
        Object result = advice.getReturnObj();
        if (null == args || args.length <= 0) {
            return;
        }
        setMethodName(result, args[0]);
        setRemoteAddress(result, target);
    }


    private void setMethodName(Object result, Object methodDescriptor) {
        try {
            if (methodDescriptor instanceof io.grpc.MethodDescriptor) {
                String fullMethodName = ((MethodDescriptor) methodDescriptor).getFullMethodName();
                manager.setDynamicField(result, GrpcConstants.DYNAMIC_FIELD_METHOD_NAME, fullMethodName);
            } else {
            }
        } catch (Throwable e) {

        }
    }

    private void setRemoteAddress(Object result, Object channel) {
        try {
            if (channel instanceof io.grpc.Channel) {
                String remoteAddress = ((io.grpc.Channel) channel).authority();
                manager.setDynamicField(result, GrpcConstants.DYNAMIC_FIELD_REMOTE_ADDRESS, remoteAddress);
            }
        } catch (Throwable e) {

        }
    }
}
