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
import java.sql.Timestamp;
import java.util.Map;

/**
 * ClassName: WeChatPubInfo <br/>
 * Function: 微信公众号实例类 <br/>
 * date: 2018年8月25日 下午2:14:33 <br/>
 *
 * @author 260773
 * @version 1.0
 * @since JDK 1.7
 */
public class WeChatPubInfo implements Serializable {

    private static final long serialVersionUID = 1603745821855771444L;
    // 唯一标识
    private String unionId;
    // 手机号
    private String mobile;
    // 发送系统
    private String sender;
    // 发送环节    	1.DISPATCH(调度)  2.COURIER_BACK(快递/司机退回)  3.GOT(开单) 4.SEND_SCAN(派送)  5.SEND_BACK(滞留)   
    //			6.SIGN(正常签收) 7.SIGN_ERROR(异常签收)  8.INVOICE(发票) 9.PARKING(司机停靠)  10.REST(其他)  
    //	 		11.INTERACT(反馈结果) 12.DELAY_PACKAGE(收件变更)13.PDA_ACCEPT(PDA受理) 14.ACCEPT(订单受理 )
    private String sendStates;
    //用户方类型  1.CONSIGNOR（发货人） 2.CONSIGNEE（收货人）
    private String userType;
    // 运输类型 1.DBWL（零担）  2.DBKD（快递）  3.OTHER（其他）
    private String transportType;
    // 官网用户名
    private String userName;
    //微信id
    private String openId;
    // 状态时间
    private Timestamp stateTime;
    // 公众号消息内容
    private Map<String, String> msgData;

    /**
     * unionId.
     *
     * @return the unionId
     */
    public String getUnionId() {
        return unionId;
    }

    /**
     * unionId.
     *
     * @param unionId the unionId to set
     */
    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    /**
     * mobile.
     *
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * mobile.
     *
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * sender.
     *
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * sender.
     *
     * @param sender the sender to set
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * sendStates.
     *
     * @return the sendStates
     */
    public String getSendStates() {
        return sendStates;
    }

    /**
     * sendStates.
     *
     * @param sendStates the sendStates to set
     */
    public void setSendStates(String sendStates) {
        this.sendStates = sendStates;
    }

    /**
     * userType.
     *
     * @return the userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     * userType.
     *
     * @param userType the userType to set
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * transportType.
     *
     * @return the transportType
     */
    public String getTransportType() {
        return transportType;
    }

    /**
     * transportType.
     *
     * @param transportType the transportType to set
     */
    public void setTransportType(String transportType) {
        this.transportType = transportType;
    }

    /**
     * userName.
     *
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * userName.
     *
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * openId.
     *
     * @return the openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * openId.
     *
     * @param openId the openId to set
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * stateTime.
     *
     * @return the stateTime
     */
    public Timestamp getStateTime() {
        return stateTime;
    }

    /**
     * stateTime.
     *
     * @param stateTime the stateTime to set
     */
    public void setStateTime(Timestamp stateTime) {
        this.stateTime = stateTime;
    }

    /**
     * msgData.
     *
     * @return the msgData
     */
    public Map<String, String> getMsgData() {
        return msgData;
    }

    /**
     * msgData.
     *
     * @param msgData the msgData to set
     */
    public void setMsgData(Map<String, String> msgData) {
        this.msgData = msgData;
    }

}
