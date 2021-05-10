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
package com.pamirs.attach.plugin.alibaba.rocketmq.common;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarService;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 消息轨迹数据实体
 *
 * @author: wangxian<tangdahu @ pamirs.top>
 * @date:2016/09/29
 */
public class MQTraceBean {
    private static String LOCAL_ADDRESS = MixUtils.getLocalAddress();
    private Map<String, String> context;

    private String requestId;
    private String topic = "";
    private String msgId = "";
    private String originMsgId = "";
    private String buyerId = "";
    private String transferFlag = "";
    private String correctionFlag = "";
    private String tags = "";
    private String keys = "";
    private String bornHost = LOCAL_ADDRESS;
    private String storeHost = LOCAL_ADDRESS;
    private String port = "";
    private String clientHost = LOCAL_ADDRESS;
    private String brokerName = "";
    private int queueId;
    private long storeTime = System.currentTimeMillis();
    private long offset;
    private int retryTimes;
    private int bodyLength;

    //TODO：系统属性还没有记录，转换成字符串记录
    private Map<String, String> props = new TreeMap<String, String>();

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public String getBornHost() {
        return bornHost;
    }

    public void setBornHost(String bornHost) {
        this.bornHost = bornHost;
    }

    public String getStoreHost() {
        return storeHost;
    }

    public void setStoreHost(String storeHost) {
        this.storeHost = storeHost;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public String getBrokerName() {
        return brokerName;
    }

    public void setBrokerName(String brokerName) {
        this.brokerName = brokerName;
    }

    public long getStoreTime() {
        return storeTime;
    }

    public void setStoreTime(long storeTime) {
        this.storeTime = storeTime;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    public void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }

    public String getClientHost() {
        return clientHost;
    }

    public void setClientHost(String clientHost) {
        this.clientHost = clientHost;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getOriginMsgId() {
        return originMsgId;
    }

    public void setOriginMsgId(String originMsgId) {
        this.originMsgId = originMsgId;
    }

    public String getTransferFlag() {
        return transferFlag;
    }

    public void setTransferFlag(String transferFlag) {
        this.transferFlag = transferFlag;
    }

    public String getCorrectionFlag() {
        return correctionFlag;
    }

    public void setCorrectionFlag(String correctionFlag) {
        this.correctionFlag = correctionFlag;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setClusterTest(String clusterTest) {
        if (context == null) {
            context = new HashMap<String, String>();
        }
        context.put(PradarService.PRADAR_CLUSTER_TEST_KEY, clusterTest);
    }
}
