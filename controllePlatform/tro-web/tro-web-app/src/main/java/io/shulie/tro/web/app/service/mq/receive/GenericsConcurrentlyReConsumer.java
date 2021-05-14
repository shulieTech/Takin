/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.tro.web.app.service.mq.receive;

import java.util.Iterator;
import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Auther: vernon
 * @Date: 2019/12/8 17:05
 * @Description:GenericsReConsumer
 */
public class GenericsConcurrentlyReConsumer<T> extends BaseConsumer implements GenericsReConsumer<T> {
    static final Logger logger = LoggerFactory.getLogger(GenericsConcurrentlyReConsumer.class);
    private static final long serialVersionUID = 1L;
    protected GenericsSerializer<T> serializer = new GenericsSerializerFactory().getSerializer();
    protected IComsumer<T> messageReConsumer;
    protected DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();

    public DefaultMQPushConsumer getConsumer() {
        return this.consumer;
    }

    public void setConsumer(DefaultMQPushConsumer consumer) {
        this.consumer = consumer;
    }

    public GenericsSerializer<T> getSerializer() {
        return this.serializer;
    }

    public void setSerializer(GenericsSerializer<T> serializer) {
        this.serializer = serializer;
    }

    public IComsumer<T> getMessageReConsumer() {
        return this.messageReConsumer;
    }

    @Override
    public void setMessageReConsumer(IComsumer<T> messageReConsumer) {
        this.messageReConsumer = messageReConsumer;
    }

    @Override
    public void startup() throws Exception {
        this.consumer.setInstanceName(this.instanceName);
        this.consumer.setConsumerGroup(this.consumerGroup);
        this.consumer.setNamesrvAddr(this.namesrvAddr);
        this.consumer.setConsumeFromWhere(this.consumeFromWhere);
        this.consumer.subscribe(this.topic, this.tag);
        this.consumer.setPullInterval(this.pullInterval);
        this.consumer.setPullBatchSize(this.pullBatchSize);
        this.consumer.setConsumeMessageBatchMaxSize(this.consumeMessageBatchMaxSize);
        this.consumer.setConsumeThreadMax(this.consumeThreadMax);
        this.consumer.setConsumeThreadMin(this.consumeThreadMin);
        this.consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
                try {
                    Iterator var3 = list.iterator();

                    while (var3.hasNext()) {
                        MessageExt msg = (MessageExt)var3.next();
                        T object = GenericsConcurrentlyReConsumer.this.serializer.unserialize(msg.getBody());
                        GenericsConcurrentlyReConsumer.this.onProcessing(object, msg);
                    }
                } catch (Exception e) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        this.consumer.setMessageModel(this.messageModel);
        if (this.messageReConsumer == null) {
            throw new Exception("messageConsumer is null,please set it!");
        } else {
            if (ConsumeFromWhere.CONSUME_FROM_TIMESTAMP.equals(this.consumeFromWhere)) {
                this.consumer.setConsumeTimestamp(this.sConsumeTimestamp);
            }

            this.consumer.start();
        }
    }

    @Override
    public void shutdown() {
        this.consumer.shutdown();
    }

    public void onProcessing(T task, MessageExt msg) {
        Boolean result = this.messageReConsumer.onConsumer(task, msg);
        if (!Boolean.TRUE.equals(result)) {
            throw new RuntimeException("GenericsMessageReConsumer onReConsume return false then revert");
        }
    }
}
