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

package com.pamirs.tro.entity.domain.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.DateToStringFormatSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @description:rocketmq生产消息数据表
 * @author:lk
 * @create:2018-09-12:
 */

public class TRocketmqProducer implements Serializable {

    /**
     * ROCKETMQ生产者id
     */
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private Long trpId;
    /**
     * 集群ip(形式为host:port;host:port,ROCKETMQ使用)
     */
    private String msgIp;
    /**
     * 订阅主题
     */
    //    @Pattern(regexp = "PT_\\w+", message = "订阅主题topic必须以PT_开头,如果多个请以|分隔!")
    private String topic;
    /**
     * 组名
     */
    private String groupName;

    /**
     * 睡眠时间，单位ms
     */
    private String sleepTime;

    /**
     * 消息发送数量,默认为条
     */
    private String msgCount;

    /**
     * 消息成功发送数量,默认为条
     */
    private String msgSuccessCount;
    /**
     * 生产消息状态, 0未生产消息 1正在生产消息 2已停止生产消息 3生产消息异常
     */
    private String produceStatus;
    /**
     * 开始生产消息时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date produceStartTime;
    /**
     * 停止生产消息时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date produceEndTime;
    /**
     * 上次停止生产时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date lastProduceTime;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date createTime;

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date updateTime;
    /**
     * 线程数量
     */
    private String threadCount;
    /**
     * 消息大小
     */
    private String messageSize;
    /**
     * 消息类型(3表示ROCKETMQ,4表示DPBOOT_ROCKETMQ)
     */
    private String msgType;
    /**
     * 数据字典类型（ID值）
     */
    private String dictType;

    public String getMsgSuccessCount() {
        return msgSuccessCount;
    }

    /**
     * Sets the msgSuccessCount.
     *
     * <p>You can use getMsgSuccessCount() to get the value of msgSuccessCount</p>
     *
     * @param msgSuccessCount msgSuccessCount
     * @author shulie
     * @version 1.0
     */
    public void setMsgSuccessCount(String msgSuccessCount) {
        this.msgSuccessCount = msgSuccessCount;
    }

    /**
     * Gets the value of trpId.
     *
     * @return the value of trpId
     * @author shulie
     * @version 1.0
     */
    public Long getTrpId() {
        return trpId;
    }

    /**
     * Sets the trpId.
     *
     * <p>You can use getTrpId() to get the value of trpId</p>
     *
     * @param trpId trpId
     * @author shulie
     * @version 1.0
     */
    public void setTrpId(Long trpId) {
        this.trpId = trpId;
    }

    /**
     * Gets the value of msgIp.
     *
     * @return the value of msgIp
     * @author shulie
     * @version 1.0
     */
    public String getMsgIp() {
        return msgIp;
    }

    /**
     * Sets the msgIp.
     *
     * <p>You can use getMsgIp() to get the value of msgIp</p>
     *
     * @param msgIp msgIp
     * @author shulie
     * @version 1.0
     */
    public void setMsgIp(String msgIp) {
        this.msgIp = msgIp;
    }

    /**
     * Gets the value of topic.
     *
     * @return the value of topic
     * @author shulie
     * @version 1.0
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the topic.
     *
     * <p>You can use getTopic() to get the value of topic</p>
     *
     * @param topic topic
     * @author shulie
     * @version 1.0
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Gets the value of groupName.
     *
     * @return the value of groupName
     * @author shulie
     * @version 1.0
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * Sets the groupName.
     *
     * <p>You can use getGroupName() to get the value of groupName</p>
     *
     * @param groupName groupName
     * @author shulie
     * @version 1.0
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * Gets the value of sleepTime.
     *
     * @return the value of sleepTime
     * @author shulie
     * @version 1.0
     */
    public String getSleepTime() {
        return sleepTime;
    }

    /**
     * Sets the sleepTime.
     *
     * <p>You can use getSleepTime() to get the value of sleepTime</p>
     *
     * @param sleepTime sleepTime
     * @author shulie
     * @version 1.0
     */
    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }

    /**
     * Gets the value of msgCount.
     *
     * @return the value of msgCount
     * @author shulie
     * @version 1.0
     */
    public String getMsgCount() {
        return msgCount;
    }

    /**
     * Sets the msgCount.
     *
     * <p>You can use getMsgCount() to get the value of msgCount</p>
     *
     * @param msgCount msgCount
     * @author shulie
     * @version 1.0
     */
    public void setMsgCount(String msgCount) {
        this.msgCount = msgCount;
    }

    /**
     * Gets the value of produceStatus.
     *
     * @return the value of produceStatus
     * @author shulie
     * @version 1.0
     */
    public String getProduceStatus() {
        return produceStatus;
    }

    /**
     * Sets the produceStatus.
     *
     * <p>You can use getProduceStatus() to get the value of produceStatus</p>
     *
     * @param produceStatus produceStatus
     * @author shulie
     * @version 1.0
     */
    public void setProduceStatus(String produceStatus) {
        this.produceStatus = produceStatus;
    }

    /**
     * Gets the value of produceStartTime.
     *
     * @return the value of produceStartTime
     * @author shulie
     * @version 1.0
     */
    public Date getProduceStartTime() {
        return produceStartTime;
    }

    /**
     * Sets the produceStartTime.
     *
     * <p>You can use getProduceStartTime() to get the value of produceStartTime</p>
     *
     * @param produceStartTime produceStartTime
     * @author shulie
     * @version 1.0
     */
    public void setProduceStartTime(Date produceStartTime) {
        this.produceStartTime = produceStartTime;
    }

    /**
     * Gets the value of produceEndTime.
     *
     * @return the value of produceEndTime
     * @author shulie
     * @version 1.0
     */
    public Date getProduceEndTime() {
        return produceEndTime;
    }

    /**
     * Sets the produceEndTime.
     *
     * <p>You can use getProduceEndTime() to get the value of produceEndTime</p>
     *
     * @param produceEndTime produceEndTime
     * @author shulie
     * @version 1.0
     */
    public void setProduceEndTime(Date produceEndTime) {
        this.produceEndTime = produceEndTime;
    }

    /**
     * Gets the value of lastProduceTime.
     *
     * @return the value of lastProduceTime
     * @author shulie
     * @version 1.0
     */
    public Date getLastProduceTime() {
        return lastProduceTime;
    }

    /**
     * Sets the lastProduceTime.
     *
     * <p>You can use getLastProduceTime() to get the value of lastProduceTime</p>
     *
     * @param lastProduceTime lastProduceTime
     * @author shulie
     * @version 1.0
     */
    public void setLastProduceTime(Date lastProduceTime) {
        this.lastProduceTime = lastProduceTime;
    }

    /**
     * Gets the value of createTime.
     *
     * @return the value of createTime
     * @author shulie
     * @version 1.0
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Sets the createTime.
     *
     * <p>You can use getCreateTime() to get the value of createTime</p>
     *
     * @param createTime createTime
     * @author shulie
     * @version 1.0
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets the value of updateTime.
     *
     * @return the value of updateTime
     * @author shulie
     * @version 1.0
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * Sets the updateTime.
     *
     * <p>You can use getUpdateTime() to get the value of updateTime</p>
     *
     * @param updateTime updateTime
     * @author shulie
     * @version 1.0
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * Gets the value of threadCount.
     *
     * @return the value of threadCount
     */
    public String getThreadCount() {
        return threadCount;
    }

    /**
     * Sets the threadCount.
     *
     * <p>You can use getThreadCount() to get the value of threadCount</p>
     *
     * @param threadCount threadCount
     */
    public void setThreadCount(String threadCount) {
        this.threadCount = threadCount;
    }

    /**
     * Gets the value of messageSize.
     *
     * @return the value of messageSize
     */
    public String getMessageSize() {
        return messageSize;
    }

    /**
     * Sets the messageSize.
     *
     * <p>You can use getMessageSize() to get the value of messageSize</p>
     *
     * @param messageSize messageSize
     */
    public void setMessageSize(String messageSize) {
        this.messageSize = messageSize;
    }

    /**
     * Gets the value of msgType.
     *
     * @return the value of msgType
     */
    public String getMsgType() {
        return msgType;
    }

    /**
     * Sets the msgType.
     *
     * <p>You can use getMsgType() to get the value of msgType</p>
     *
     * @param msgType msgType
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * Gets the value of dictType.
     *
     * @return the value of dictType
     */
    public String getDictType() {
        return dictType;
    }

    /**
     * Sets the dictType.
     *
     * <p>You can use getDictType() to get the value of dictType</p>
     *
     * @param dictType dictType
     */
    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    @Override
    public String toString() {
        return "TRocketmqProducer{" +
            "trpId=" + trpId +
            ", msgIp='" + msgIp + '\'' +
            ", topic='" + topic + '\'' +
            ", groupName='" + groupName + '\'' +
            ", sleepTime='" + sleepTime + '\'' +
            ", msgCount='" + msgCount + '\'' +
            ", msgSuccessCount='" + msgSuccessCount + '\'' +
            ", produceStatus='" + produceStatus + '\'' +
            ", produceStartTime=" + produceStartTime +
            ", produceEndTime=" + produceEndTime +
            ", lastProduceTime=" + lastProduceTime +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            ", threadCount='" + threadCount + '\'' +
            ", messageSize='" + messageSize + '\'' +
            ", msgType='" + msgType + '\'' +
            ", dictType='" + dictType + '\'' +
            '}';
    }
}
