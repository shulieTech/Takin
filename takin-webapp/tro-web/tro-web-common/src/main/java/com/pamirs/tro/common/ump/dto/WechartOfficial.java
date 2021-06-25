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

//企业微信消息体
public class WechartOfficial implements Serializable {

    private static final long serialVersionUID = -5436180970669446636L;
    // 发送方系统
    private String sendDept;
    // 发送人
    private String sender;
    // 消息ID
    private String unionId;
    // 企业微信栏目ID
    private String wxAgentid;
    // 发送内容
    private String wxContent;
    // 消息类型
    private String wxMegtype;
    // 发送目的人工号
    private String wxTouser;

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

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getWxAgentid() {
        return wxAgentid;
    }

    public void setWxAgentid(String wxAgentid) {
        this.wxAgentid = wxAgentid;
    }

    public String getWxContent() {
        return wxContent;
    }

    public void setWxContent(String wxContent) {
        this.wxContent = wxContent;
    }

    public String getWxMegtype() {
        return wxMegtype;
    }

    public void setWxMegtype(String wxMegtype) {
        this.wxMegtype = wxMegtype;
    }

    public String getWxTouser() {
        return wxTouser;
    }

    public void setWxTouser(String wxTouser) {
        this.wxTouser = wxTouser;
    }

    @Override
    public String toString() {
        return "WechartOfficial [ sendDept=" + sendDept + ", sender=" + sender + ", unionId="
            + unionId + ", wxAgentid=" + wxAgentid + ", wxContent=" + wxContent + ", wxMegtype=" + wxMegtype
            + ", wxTouser=" + wxTouser + "]";
    }

}
