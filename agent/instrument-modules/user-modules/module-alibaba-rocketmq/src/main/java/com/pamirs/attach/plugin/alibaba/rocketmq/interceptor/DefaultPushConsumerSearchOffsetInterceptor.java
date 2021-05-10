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
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.pamirs.attach.plugin.alibaba.rocketmq.common.ConsumerRegistry;
import com.pamirs.pradar.CutOffResult;
import com.pamirs.pradar.exception.PressureMeasureError;
import com.pamirs.pradar.interceptor.CutoffInterceptorAdaptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/30 3:53 下午
 */
public class DefaultPushConsumerSearchOffsetInterceptor extends CutoffInterceptorAdaptor {
    protected final static Logger logger = LoggerFactory.getLogger(DefaultPushConsumerSearchOffsetInterceptor.class);

    @Override
    public CutOffResult cutoff0(Advice advice) {
        /**
         * 主要负责Consumer 注册，每一批的消息消费都会经过此方法
         * 如果是已经注册过的，则忽略
         */
        DefaultMQPushConsumer defaultMQPushConsumer = (DefaultMQPushConsumer) advice.getTarget();
        if (ConsumerRegistry.hasRegistered(defaultMQPushConsumer)) {
            return CutOffResult.passed();
        }

        /**
         * 如果影子消费者，也忽略
         */
        if (ConsumerRegistry.isShadowConsumer(defaultMQPushConsumer)) {
            return CutOffResult.passed();
        }

        DefaultMQPushConsumer businessConsumer = (DefaultMQPushConsumer) advice.getTarget();
        DefaultMQPushConsumer consumer = ConsumerRegistry.getConsumer(businessConsumer);
        Object[] args = advice.getParameterArray();
        try {
            return CutOffResult.cutoff(consumer.searchOffset((MessageQueue) args[0], (Long) args[1]));
        } catch (Throwable e) {
            logger.error("Alibaba-RocketMQ: search offset err, queue: {} timestamp:{}", args[0], args[1], e);
            throw new PressureMeasureError(e);
        }
    }

}
