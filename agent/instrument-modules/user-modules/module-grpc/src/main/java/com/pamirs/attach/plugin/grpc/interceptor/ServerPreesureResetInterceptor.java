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
 * onHalfClose 方法
 * 1、线程染色
 * 2、重置线程染色
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.grpc.interceptor
 * @Date 2020-05-14 23:22
 */
public class ServerPreesureResetInterceptor extends AroundInterceptor {
    @Resource
    protected DynamicFieldManager manager;

    @Override
    public void doBefore(Advice advice) {
        Object target = advice.getTarget();
        try {
            boolean isClusterTest = manager.getDynamicField(target, GrpcConstants.DYNAMIC_FIELD_IS_CLUSTER_TEST, false);
            if (isClusterTest) {
                Pradar.setClusterTest(true);
            }
        } catch (Throwable e) {
        }
    }

    @Override
    public void doAfter(Advice advice) {
        Object target = advice.getTarget();
        try {
            manager.setDynamicField(target, GrpcConstants.DYNAMIC_FIELD_IS_CLUSTER_TEST, false);
            Pradar.setClusterTest(false);
        } catch (Throwable e) {
        }
    }
}
