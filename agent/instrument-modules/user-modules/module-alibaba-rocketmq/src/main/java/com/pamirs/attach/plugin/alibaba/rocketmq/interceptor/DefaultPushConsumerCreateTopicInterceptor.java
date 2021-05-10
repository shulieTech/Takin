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
import com.alibaba.rocketmq.client.exception.MQClientException;
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
public class DefaultPushConsumerCreateTopicInterceptor extends CutoffInterceptorAdaptor {
    protected final static Logger logger = LoggerFactory.getLogger(DefaultPushConsumerCreateTopicInterceptor.class);

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

        Object[] args = advice.getParameterArray();
        if (args.length == 3) {
            DefaultMQPushConsumer consumer = ConsumerRegistry.getConsumer(defaultMQPushConsumer);
            if (consumer != null) {
                try {
                    consumer.createTopic((String) args[0], (String) args[1], (Integer) args[2]);
                } catch (MQClientException e) {
                    logger.error("Alibaba-RocketMQ: create topic err, key: {} newTopic:{},queueNum:{}", args[0], args[1], args[2], e);
                    throw new PressureMeasureError(e);
                }
            }
        } else if (args.length == 4) {
            DefaultMQPushConsumer consumer = ConsumerRegistry.getConsumer(defaultMQPushConsumer);
            if (consumer != null) {
                try {
                    consumer.createTopic((String) args[0], (String) args[1], (Integer) args[2], (Integer) args[3]);
                } catch (Throwable e) {
                    logger.error("Alibaba-RocketMQ: create topic err, key: {} newTopic: {}, queueNum: {}, topicSysFlag: {}", args[0], args[1], args[2], args[3], e);
                    throw new PressureMeasureError(e);
                }
            }
        }
        return CutOffResult.cutoff(null);
    }

}
