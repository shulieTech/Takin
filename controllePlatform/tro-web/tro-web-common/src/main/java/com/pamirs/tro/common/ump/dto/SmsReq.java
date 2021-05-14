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

package com.pamirs.tro.common.ump.dto;

import java.io.Serializable;

/**
 * com.pamirs.ump.api.rest.entity
 *
 * SmsReq
 *
 * @auth zhh
 * @created 2018/8/27
 */
public class SmsReq implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1918560188932468998L;

    /**
     * 唯一标识
     */
    private String unionId;
    /**
     * 电话号码
     */
    private String mobile;
    /**
     * 内容
     */
    private String msgContent;
    /**
     * 发送部门
     */
    private String sendDept;
    /**
     * 发送人
     */
    private String sender;
    /**
     * 单号
     */
    private String waybillNo;
    /**
     * 服务类型
     */
    private String serviceType;
    /**
     * 最晚发送时间 （不填写将以策略时间填充）
     */
    private Long latestSendTime;
    /**
     * 发送时间
     */
    private Long sendTime;

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getSendDept() {
        return sendDept;
    }

    public void setSendDept(String sendDept) {
        this.sendDept = sendDept;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Long getLatestSendTime() {
        return latestSendTime;
    }

    public void setLatestSendTime(Long latestSendTime) {
        this.latestSendTime = latestSendTime;
    }

    public Long getSendTime() {
        return sendTime;
    }

    public void setSendTime(Long sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "Sms [unionId=" + unionId + ", mobile=" + mobile + ", msgContent="
            + msgContent + ", sendDept=" + sendDept + ", sender=" + sender + ", waybillNo=" + waybillNo
            + ", serviceType=" + serviceType + ", latestSendTime=" + latestSendTime + ", sendTime=" + sendTime
            + "]";
    }
}
