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
 * AlipayLogisticsSend
 */
public class AlipayLogisticsSend implements Serializable {

    private String uuid;

    /**
     * 运单号
     */
    private String waybillNo;

    /**
     * 物流状态
     * RECEIVE_SUCCESS 揽件成功
     * DELIVER_SUCCESS 快件发出
     * TRANSIT 中转
     * DELIVERY_REMIND 派件前提醒
     * RECEIVE_SIGN 签收
     * TO_PICK_UP 待提货
     * SIGN_FAILED 签收失败
     */
    private String logisticsStatus;

    /**
     * 物流更新时间: 格式 : YYYY-MM-DD HH:MM:SS
     */
    private String updateTime;

    /**
     * 寄件人手机号
     */
    private String senderMobile;

    /**
     * 快递员手机号
     */
    private String courierMobile;

    /**
     * 收件人手机号
     */
    private String receiverMobile;

    /**
     * 最新物流流转信息	 如：到西安
     * （可空）
     */
    private String desc;

    /**
     * 物品名称 如：衣服
     * （可空）
     */
    private String goodsName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public AlipayLogisticsSend setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
        return this;
    }

    public String getLogisticsStatus() {
        return logisticsStatus;
    }

    public AlipayLogisticsSend setLogisticsStatus(String logisticsStatus) {
        this.logisticsStatus = logisticsStatus;
        return this;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public AlipayLogisticsSend setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public String getSenderMobile() {
        return senderMobile;
    }

    public AlipayLogisticsSend setSenderMobile(String senderMobile) {
        this.senderMobile = senderMobile;
        return this;
    }

    public String getCourierMobile() {
        return courierMobile;
    }

    public AlipayLogisticsSend setCourierMobile(String courierMobile) {
        this.courierMobile = courierMobile;
        return this;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public AlipayLogisticsSend setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public AlipayLogisticsSend setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public AlipayLogisticsSend setGoodsName(String goodsName) {
        this.goodsName = goodsName;
        return this;
    }
}
