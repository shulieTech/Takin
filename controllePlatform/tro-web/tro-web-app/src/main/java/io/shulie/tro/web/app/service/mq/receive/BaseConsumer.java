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

import java.io.Serializable;
import java.util.UUID;

import org.apache.rocketmq.common.UtilAll;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @Auther: vernon
 * @Date: 2019/12/8 17:08
 * @Description:
 */
public abstract class BaseConsumer implements Serializable {
    private static final long serialVersionUID = 2888922039679988356L;
    protected String namesrvAddr;
    protected String instanceName;
    protected String consumerGroup;
    protected Boolean vipChannelEnabled = false;
    protected ConsumeFromWhere consumeFromWhere;
    protected String topic;
    protected String tag;
    protected long pullInterval;
    protected int pullBatchSize;
    protected int consumeMessageBatchMaxSize;
    protected int consumeThreadMin;
    protected int consumeThreadMax;
    protected long consumeTimeout;
    protected MessageModel messageModel;
    protected long consumeTimestamp;
    protected String sConsumeTimestamp;

    public BaseConsumer() {
        this.consumeFromWhere = ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET;
        this.tag = "*";
        this.pullInterval = 0L;
        this.pullBatchSize = 32;
        this.consumeMessageBatchMaxSize = 1;
        this.consumeThreadMin = 20;
        this.consumeThreadMax = 64;
        this.consumeTimeout = 5000L;
        this.messageModel = MessageModel.CLUSTERING;
        this.consumeTimestamp = 1800L;
        this.sConsumeTimestamp = UtilAll.timeMillisToHumanString3(
            System.currentTimeMillis() - 1000L * this.consumeTimestamp);
        String time = UUID.randomUUID().toString().replace("-", "");
        this.instanceName = "consumer_" + time + "";
        this.consumerGroup = "consumerG_" + time + "";
    }

    public String getNamesrvAddr() {
        return this.namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getInstanceName() {
        return this.instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getConsumerGroup() {
        return this.consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public Boolean getVipChannelEnabled() {
        return this.vipChannelEnabled;
    }

    public void setVipChannelEnabled(Boolean vipChannelEnabled) {
        this.vipChannelEnabled = vipChannelEnabled;
    }

    public ConsumeFromWhere getConsumeFromWhere() {
        return this.consumeFromWhere;
    }

    public void setConsumeFromWhere(ConsumeFromWhere consumeFromWhere) {
        this.consumeFromWhere = consumeFromWhere;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getPullBatchSize() {
        return this.pullBatchSize;
    }

    public void setPullBatchSize(int pullBatchSize) {
        this.pullBatchSize = pullBatchSize;
    }

    public int getConsumeMessageBatchMaxSize() {
        return this.consumeMessageBatchMaxSize;
    }

    public void setConsumeMessageBatchMaxSize(int consumeMessageBatchMaxSize) {
        this.consumeMessageBatchMaxSize = consumeMessageBatchMaxSize;
    }

    public long getPullInterval() {
        return this.pullInterval;
    }

    public void setPullInterval(long pullInterval) {
        this.pullInterval = pullInterval;
    }

    public int getConsumeThreadMin() {
        return this.consumeThreadMin;
    }

    public void setConsumeThreadMin(int consumeThreadMin) {
        this.consumeThreadMin = consumeThreadMin;
    }

    public int getConsumeThreadMax() {
        return this.consumeThreadMax;
    }

    public void setConsumeThreadMax(int consumeThreadMax) {
        this.consumeThreadMax = consumeThreadMax;
    }

    public long getConsumeTimeout() {
        return this.consumeTimeout;
    }

    public void setConsumeTimeout(long consumeTimeout) {
        this.consumeTimeout = consumeTimeout;
    }

    public MessageModel getMessageModel() {
        return this.messageModel;
    }

    public void setMessageModel(MessageModel messageModel) {
        this.messageModel = messageModel;
    }

    public long getConsumeTimestamp() {
        return this.consumeTimestamp;
    }

    public void setConsumeTimestamp(long second) {
        this.consumeTimestamp = second;
        this.sConsumeTimestamp = UtilAll.timeMillisToHumanString3(System.currentTimeMillis() - 1000L * second);
    }
}
