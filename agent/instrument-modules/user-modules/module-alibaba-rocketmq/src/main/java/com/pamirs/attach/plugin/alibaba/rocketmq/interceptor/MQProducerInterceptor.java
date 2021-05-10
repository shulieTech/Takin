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
package com.pamirs.attach.plugin.alibaba.rocketmq.interceptor;

import com.alibaba.rocketmq.client.impl.producer.DefaultMQProducerImpl;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.pamirs.attach.plugin.alibaba.rocketmq.hook.SendMessageHookImpl;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

/**
 * Created by xiaobin on 2017/3/10.
 */
public class MQProducerInterceptor extends AroundInterceptor {
    @Override
    public void doBefore(Advice advice) {
        DefaultMQProducer producer = (DefaultMQProducer) advice.getTarget();
        DefaultMQProducerImpl consumerImpl = producer.getDefaultMQProducerImpl();
        if (consumerImpl != null) {
            consumerImpl.registerSendMessageHook(new SendMessageHookImpl());
        }
    }

    @Override
    public void doAfter(Advice advice) {

    }

    @Override
    public void doException(Advice advice) {

    }
}
