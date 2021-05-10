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
package com.pamirs.attach.plugin.apache.rocketmq.interceptor;

import com.pamirs.attach.plugin.apache.rocketmq.common.ConsumerRegistry;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.apache.rocketmq.client.impl.consumer.DefaultMQPushConsumerImpl;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/26 10:18 下午
 */
public class DefaultMQPushConsumerImplHasHookListener extends AroundInterceptor {

    @Override
    public void doBefore(Advice advice) throws Throwable {
        /**
         * 主要负责Consumer 注册，每一批的消息消费都会经过此方法
         * 如果是已经注册过的，则忽略
         */
        DefaultMQPushConsumerImpl target = (DefaultMQPushConsumerImpl) advice.getTarget();
        if (ConsumerRegistry.hasRegistered(target.getDefaultMQPushConsumer())) {
            return;
        }

        /**
         * 如果影子消费者，也忽略
         */
        if (ConsumerRegistry.isShadowConsumer(target.getDefaultMQPushConsumer())) {
            return;
        }
        /**
         * 注册影子消费者
         */
        ConsumerRegistry.registerConsumer(target.getDefaultMQPushConsumer());
    }
}
