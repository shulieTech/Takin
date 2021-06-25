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
import java.util.LinkedList;
import java.util.List;

public class ClientMessEntity implements Serializable {

    private static final long serialVersionUID = -5705689196206154568L;

    /**
     * 发送类型 1，短信(001) 2，企业微信(010) 4，微信公众号(100) 3，(短信+企业微信)(011) 5，(短信+微信公众号)(101)
     */
    private Integer sendType;
    /**
     * 系统来源
     */
    private String msgSource;

    /**
     * 业务类型
     */
    private String msgType;

    /**
     * 密文
     */
    private String dataDigest;

    /**
     * 消息体
     */
    private List<UmpDetail> umpDetails = new LinkedList<UmpDetail>();

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
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

    public String getDataDigest() {
        return dataDigest;
    }

    public void setDataDigest(String dataDigest) {
        this.dataDigest = dataDigest;
    }

    public List<UmpDetail> getUmpDetails() {
        return umpDetails;
    }

    public void setUmpDetails(List<UmpDetail> umpDetails) {
        this.umpDetails = umpDetails;
    }

    @Override
    public String toString() {
        return "ClientMessEntity [sendType=" + sendType + ", msgSource=" + msgSource + ", msgType=" + msgType
            + ", dataDigest=" + dataDigest + ", umpDetails=" + umpDetails + "]";
    }

}
