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
package com.pamirs.attach.plugin.apache.kafka.interceptor;

import com.pamirs.attach.plugin.apache.kafka.origin.ConsumerHolder;
import com.pamirs.attach.plugin.apache.kafka.origin.ConsumerProxy;
import com.pamirs.pradar.CutOffResult;
import com.pamirs.pradar.interceptor.CutoffInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/05/13 2:31 下午
 */
public abstract class AbstractProxiedConsumerInterceptor extends CutoffInterceptorAdaptor {

    @Override
    public CutOffResult cutoff0(Advice advice) throws Throwable {
        ConsumerProxy consumerProxy = ConsumerHolder.getProxy(advice.getTarget());
        if (consumerProxy == null) {
            return CutOffResult.passed();
        }
        Object result = doCutoff(consumerProxy, advice);
        if (result instanceof CutOffResult) {
            return (CutOffResult)result;
        } else {
            return CutOffResult.cutoff(result);
        }
    }

    protected abstract Object doCutoff(ConsumerProxy consumerProxy, Advice advice);
}
