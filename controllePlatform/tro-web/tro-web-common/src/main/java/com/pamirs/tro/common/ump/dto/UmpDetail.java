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
 * 消息body
 *
 * @author 616580
 */
public class UmpDetail implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4613945968013607174L;
    /**
     * 统一UUID
     */
    private String uuid;
    // 发送环节  1.only(只发微信) 2.fail(失败后转发短信)
    private Integer pushType;
    /**
     * 短信实体
     */
    private SmsReq sms;
    /**
     * 微信公众号实体
     */
    private WeChatPubInfo weChatPubInfo;
    /**
     * 企业微信实体
     */
    private WechartOfficial wechartOfficial;
    /**
     * 支付宝物流轨迹推送
     */
    private AlipayLogisticsSend alipayLogistics;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getPushType() {
        return pushType;
    }

    public void setPushType(Integer pushType) {
        this.pushType = pushType;
    }

    public WeChatPubInfo getWeChatPubInfo() {
        return weChatPubInfo;
    }

    public void setWeChatPubInfo(WeChatPubInfo weChatPubInfo) {
        this.weChatPubInfo = weChatPubInfo;
    }

    public WechartOfficial getWechartOfficial() {
        return wechartOfficial;
    }

    public void setWechartOfficial(WechartOfficial wechartOfficial) {
        this.wechartOfficial = wechartOfficial;
    }

    public SmsReq getSms() {
        return sms;
    }

    public void setSms(SmsReq sms) {
        this.sms = sms;
    }

    public AlipayLogisticsSend getAlipayLogistics() {
        return alipayLogistics;
    }

    public void setAlipayLogistics(AlipayLogisticsSend alipayLogistics) {
        this.alipayLogistics = alipayLogistics;
    }
}
