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

import com.alibaba.rocketmq.client.consumer.PullResult;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.hook.ConsumeMessageContext;
import com.alibaba.rocketmq.client.impl.consumer.DefaultMQPullConsumerImpl;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.pamirs.attach.plugin.alibaba.rocketmq.hook.PullConsumeMessageHookImpl;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;

/**
 * Created by xiaobin on 2017/3/10.
 */
public class PullConsumerInterceptor extends AroundInterceptor {

    private static PullConsumeMessageHookImpl hook = new PullConsumeMessageHookImpl();

    private boolean checkArgs(Object[] args, Object result) {
        if (args == null || args.length == 0) {
            return false;
        }
        if (result == null) {
            return false;
        }
        return true;
    }

    @Override
    public void doAfter(Advice advice) {
        Object[] args = advice.getParameterArray();
        Object result = advice.getReturnObj();
        if (!checkArgs(args, result)) {
            return;
        }
        DefaultMQPullConsumerImpl consumer = (DefaultMQPullConsumerImpl) advice.getTarget();
        MessageQueue mq = (MessageQueue) args[0];
        PullResult pullResult = (PullResult) result;
        ConsumeMessageContext consumeMessageContext = new ConsumeMessageContext();
        consumeMessageContext.setConsumerGroup(consumer.groupName());
        consumeMessageContext.setMq(mq);
        consumeMessageContext.setMsgList(pullResult.getMsgFoundList());
        consumeMessageContext.setSuccess(false);
        hook.consumeMessageBefore(consumeMessageContext);
        consumeMessageContext.setStatus(ConsumeConcurrentlyStatus.CONSUME_SUCCESS.toString());
        consumeMessageContext.setSuccess(true);
        hook.consumeMessageAfter(consumeMessageContext);

    }
}
