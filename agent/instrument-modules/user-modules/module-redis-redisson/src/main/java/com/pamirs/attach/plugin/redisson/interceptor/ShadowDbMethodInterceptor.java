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
package com.pamirs.attach.plugin.redisson.interceptor;

import com.pamirs.attach.plugin.common.datasource.redisserver.RedisClientMediator;
import com.pamirs.attach.plugin.redisson.factory.RedissonFactory;
import com.pamirs.attach.plugin.redisson.utils.CutOffSwitcher;
import com.pamirs.pradar.CutOffResult;
import com.pamirs.pradar.ErrorTypeEnum;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.Throwables;
import com.pamirs.pradar.interceptor.CutoffInterceptorAdaptor;
import com.pamirs.pradar.pressurement.agent.shared.service.ErrorReporter;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import com.shulie.instrument.simulator.api.reflect.Reflect;

/**
 * @Auther: vernon
 * @Date: 2020/11/27 02:46
 * @Description:
 */
public class ShadowDbMethodInterceptor extends CutoffInterceptorAdaptor {
    @Override
    public CutOffResult cutoff0(Advice advice) {
        Object[] args = advice.getParameterArray();
        String methodName = advice.getBehavior().getName();
        Object target = advice.getTarget();
        /*  ClusterTestUtils.validateClusterTest();*/

        if (!Pradar.isClusterTest()) {
            return CutOffResult.passed();
        }

        if (!RedisClientMediator.isShadowDb()) {
            return CutOffResult.passed();
        }

        if (CutOffSwitcher.status()) {
            return CutOffResult.passed();
        }

        try {
            CutOffSwitcher.turnOn();

            Object client = RedissonFactory.RedissonHolder.getFactory().getClient(target);
            return CutOffResult.cutoff(Reflect.on(client).call(methodName, args));
        } catch (Throwable e) {
            if (Pradar.isClusterTest()) {
                ErrorReporter.buildError()
                        .setErrorType(ErrorTypeEnum.RedisServer)
                        .setErrorCode("redisson error.")
                        .setDetail(Throwables.getStackTraceAsString(e))
                        .report();
            }
        } finally {
            CutOffSwitcher.turnOff();
        }
        return null;
    }
}
