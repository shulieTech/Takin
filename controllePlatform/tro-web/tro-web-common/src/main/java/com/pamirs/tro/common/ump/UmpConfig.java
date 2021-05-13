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

package com.pamirs.tro.common.ump;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author shulie
 * @description
 * @create 2019-05-25 10:09:02
 */
@Component
public class UmpConfig {
    @Value("${ump.appKey}")
    private String appKey;

    @Value("${ump.appSecret}")
    private String appSecret;

    @Value("${ump.sendMsgUrl}")
    private String sendMsgUrl;

    @Value("${ump.msgSource}")
    private String msgSource;

    @Value("${ump.msgType}")
    private String msgType;

    @Value("${ump.sendDept}")
    private String sendDept;

    @Value("${ump.sender}")
    private String sender;

    @Value("${ump.wxAgentid}")
    private String wxAgentid;

    @Value("${ump.wxMegType}")
    private String wxMegType;

    @Value("${ump.serviceType}")
    private String serviceType;

    @Value("${ump.defaultAlarmPerson}")
    private String defaultAlarmPerson;

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getSendMsgUrl() {
        return sendMsgUrl;
    }

    public void setSendMsgUrl(String sendMsgUrl) {
        this.sendMsgUrl = sendMsgUrl;
    }

    public String getMsgSource() {
        return msgSource;
    }

    public void setMsgSource(String msgSource) {
        this.msgSource = msgSource;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
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

    public String getWxAgentid() {
        return wxAgentid;
    }

    public void setWxAgentid(String wxAgentid) {
        this.wxAgentid = wxAgentid;
    }

    public String getWxMegType() {
        return wxMegType;
    }

    public void setWxMegType(String wxMegType) {
        this.wxMegType = wxMegType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * Gets the value of defaultAlarmPerson.
     *
     * @return the value of defaultAlarmPerson
     * @author shulie
     * @version 1.0
     */
    public String getDefaultAlarmPerson() {
        return defaultAlarmPerson;
    }

    /**
     * Sets the defaultAlarmPerson.
     *
     * <p>You can use getDefaultAlarmPerson() to get the value of defaultAlarmPerson</p>
     *
     * @param defaultAlarmPerson defaultAlarmPerson
     * @author shulie
     * @version 1.0
     */
    public void setDefaultAlarmPerson(String defaultAlarmPerson) {
        this.defaultAlarmPerson = defaultAlarmPerson;
    }
}
