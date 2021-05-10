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

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.ConsumerRegistry;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/26 11:13 下午
 */
public class DefaultPushConsumerShutdownInterceptor extends AroundInterceptor {
    @Override
    public void doAfter(Advice advice) throws Throwable {
        DefaultMQPushConsumer target = (DefaultMQPushConsumer) advice.getTarget();
        ConsumerRegistry.shutdownShadowConsumerByBusinessConsumer(target);
    }
}
