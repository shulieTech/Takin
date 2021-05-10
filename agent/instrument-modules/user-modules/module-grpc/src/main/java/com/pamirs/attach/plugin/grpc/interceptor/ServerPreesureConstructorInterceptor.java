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
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.resource.DynamicFieldManager;

import javax.annotation.Resource;

/**
 * io.grpc.stub.ServerCalls$naryServerCallHandler#startCall
 * 方法处理结束new io.grpc.stub.ServerCalls$UnaryServerCallHandler$UnaryServerCallListener
 * 此时会从startCall切换线程去处理该请求。所以做了以下处理
 * 1、pradar对UnaryServerCallListener该内部类添加了Boolean字段。用于识别压测流量
 * 2、startCall方法识别metadata中Grpc-PT 完成线程染色
 * 3、new UnaryServerCallListener的时候给Boolean赋值PressureMeasurement.ispressureMeaurement()
 * 4、onHalfClose方法执行时判断该实例Boolean是否为true，如果是则进行染色
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.grpc.interceptor
 * @Date 2020-05-15 00:32
 */
public class ServerPreesureConstructorInterceptor extends AroundInterceptor {
    @Resource
    protected DynamicFieldManager manager;

    @Override
    public void doBefore(Advice advice) {
        Object target = advice.getTarget();
        try {
            if (Pradar.isClusterTest()) {
                manager.setDynamicField(target, GrpcConstants.DYNAMIC_FIELD_IS_CLUSTER_TEST, Pradar.isClusterTest());
            }
        } catch (Throwable e) {
        }
    }

}
