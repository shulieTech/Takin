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

import java.util.Date;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.DateToStringFormatSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @description:ESB/IBM生产消息数据表
 * @author:lk
 * @create:9-13-13:23
 */
public class TEbmProducer {
    /**
     * ESB/IBM生产者id
     */
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private Long tepId;
    /**
     * 消息类型(1表示ESB,2表示IBM)
     */
    private String msgType;
    /**
     * 数据字典类型（ID值）
     */
    private String dictType;
    /**
     * 消息地址
     */
    @Pattern(regexp = "((25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))\\.){3}(25[0-5]|2[0-4]\\d|((1\\d{2})|([1-9]?\\d)))",
        message = "IP格式错误,请填写正确的IP地址")
    private String msgHost;
    /**
     * 消息端口
     */
    //@Range(min = 1, max = 65535, message = "消息端口为1-65535之内!")
    private String msgPort;
    /**
     * 队列管理器
     */
    private String queueManager;
    /**
     * 系统队列通道
     */
    private String queueChannel;
    /**
     * 编码字符集标识符
     */
    private String ccsid;
    /**
     * ESBCODE
     */
    @Pattern(regexp = "(PT_\\w+\\|?)+", message = "esbcode必须以PT_开头,如果多个请以|分隔!")
    private String esbcode;

    /**
     * REQUESTCOMOUT
     */
    private String requestComout;

    /**
     * 消息休眠时间,默认为毫秒
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
     * Gets the value of tepId.
     *
     * @return the value of tepId
     * @author shulie
     * @version 1.0
     */
    public Long getTepId() {
        return tepId;
    }

    /**
     * Sets the tepId.
     *
     * <p>You can use getTepId() to get the value of tepId</p>
     *
     * @param tepId tepId
     * @author shulie
     * @version 1.0
     */
    public void setTepId(Long tepId) {
        this.tepId = tepId;
    }

    /**
     * Gets the value of msgType.
     *
     * @return the value of msgType
     * @author shulie
     * @version 1.0
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
     * @author shulie
     * @version 1.0
     */
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * Gets the value of dictType.
     *
     * @return the value of dictType
     * @author shulie
     * @version 1.0
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
     * @author shulie
     * @version 1.0
     */
    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    /**
     * Gets the value of msgHost.
     *
     * @return the value of msgHost
     * @author shulie
     * @version 1.0
     */
    public String getMsgHost() {
        return msgHost;
    }

    /**
     * Sets the msgHost.
     *
     * <p>You can use getMsgHost() to get the value of msgHost</p>
     *
     * @param msgHost msgHost
     * @author shulie
     * @version 1.0
     */
    public void setMsgHost(String msgHost) {
        this.msgHost = msgHost;
    }

    /**
     * Gets the value of msgPort.
     *
     * @return the value of msgPort
     * @author shulie
     * @version 1.0
     */
    public String getMsgPort() {
        return msgPort;
    }

    /**
     * Sets the msgPort.
     *
     * <p>You can use getMsgPort() to get the value of msgPort</p>
     *
     * @param msgPort msgPort
     * @author shulie
     * @version 1.0
     */
    public void setMsgPort(String msgPort) {
        this.msgPort = msgPort;
    }

    /**
     * Gets the value of queueManager.
     *
     * @return the value of queueManager
     * @author shulie
     * @version 1.0
     */
    public String getQueueManager() {
        return queueManager;
    }

    /**
     * Sets the queueManager.
     *
     * <p>You can use getQueueManager() to get the value of queueManager</p>
     *
     * @param queueManager queueManager
     * @author shulie
     * @version 1.0
     */
    public void setQueueManager(String queueManager) {
        this.queueManager = queueManager;
    }

    /**
     * Gets the value of queueChannel.
     *
     * @return the value of queueChannel
     * @author shulie
     * @version 1.0
     */
    public String getQueueChannel() {
        return queueChannel;
    }

    /**
     * Sets the queueChannel.
     *
     * <p>You can use getQueueChannel() to get the value of queueChannel</p>
     *
     * @param queueChannel queueChannel
     * @author shulie
     * @version 1.0
     */
    public void setQueueChannel(String queueChannel) {
        this.queueChannel = queueChannel;
    }

    /**
     * Gets the value of ccsid.
     *
     * @return the value of ccsid
     * @author shulie
     * @version 1.0
     */
    public String getCcsid() {
        return ccsid;
    }

    /**
     * Sets the ccsid.
     *
     * <p>You can use getCcsid() to get the value of ccsid</p>
     *
     * @param ccsid ccsid
     * @author shulie
     * @version 1.0
     */
    public void setCcsid(String ccsid) {
        this.ccsid = ccsid;
    }

    /**
     * Gets the value of esbcode.
     *
     * @return the value of esbcode
     * @author shulie
     * @version 1.0
     */
    public String getEsbcode() {
        return esbcode;
    }

    /**
     * Sets the esbcode.
     *
     * <p>You can use getEsbcode() to get the value of esbcode</p>
     *
     * @param esbcode esbcode
     * @author shulie
     * @version 1.0
     */
    public void setEsbcode(String esbcode) {
        this.esbcode = esbcode;
    }

    /**
     * Gets the value of requestComout.
     *
     * @return the value of requestComout
     * @author shulie
     * @version 1.0
     */
    public String getRequestComout() {
        return requestComout;
    }

    /**
     * Sets the requestComout.
     *
     * <p>You can use getRequestComout() to get the value of requestComout</p>
     *
     * @param requestComout requestComout
     * @author shulie
     * @version 1.0
     */
    public void setRequestComout(String requestComout) {
        this.requestComout = requestComout;
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
     * Gets the value of msgSuccessCount.
     *
     * @return the value of msgSuccessCount
     * @author shulie
     * @version 1.0
     */
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

    public String getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(String threadCount) {
        this.threadCount = threadCount;
    }

    public String getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(String messageSize) {
        this.messageSize = messageSize;
    }

    @Override
    public String toString() {
        return "TEbmProducer{" +
            "tepId=" + tepId +
            ", msgType='" + msgType + '\'' +
            ", dictType='" + dictType + '\'' +
            ", msgHost='" + msgHost + '\'' +
            ", msgPort='" + msgPort + '\'' +
            ", queueManager='" + queueManager + '\'' +
            ", queueChannel='" + queueChannel + '\'' +
            ", ccsid='" + ccsid + '\'' +
            ", esbcode='" + esbcode + '\'' +
            ", requestComout='" + requestComout + '\'' +
            ", sleepTime='" + sleepTime + '\'' +
            ", msgCount='" + msgCount + '\'' +
            ", msgSuccessCount='" + msgSuccessCount + '\'' +
            ", produceStatus='" + produceStatus + '\'' +
            ", produceStartTime=" + produceStartTime +
            ", produceEndTime=" + produceEndTime +
            ", lastProduceTime=" + lastProduceTime +
            ", createTime=" + createTime +
            ", updateTime=" + updateTime +
            '}';
    }
}
