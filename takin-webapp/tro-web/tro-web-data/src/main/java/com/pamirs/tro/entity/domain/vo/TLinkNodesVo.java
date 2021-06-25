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

package com.pamirs.tro.entity.domain.vo;

/**
 * 说明：链路检测实体类
 *
 * @author shoaqi
 * @version V1.0
 * @date: 2019年1月11日
 */
public class TLinkNodesVo {
    //@Field tLinkId : 链路id
    private String tLinkId;

    //@Field tLinkId : 链路顺序
    private Integer tLinkOrder;

    //@Field tLinkId : 链路级别
    private Integer tLinkBank;

    //@Field tLinkId : 链路名称
    private String linkName;

    public String gettLinkId() {
        return tLinkId;
    }

    public void settLinkId(String tLinkId) {
        this.tLinkId = tLinkId;
    }

    public Integer gettLinkOrder() {
        return tLinkOrder;
    }

    public void settLinkOrder(Integer tLinkOrder) {
        this.tLinkOrder = tLinkOrder;
    }

    public Integer gettLinkBank() {
        return tLinkBank;
    }

    public void settLinkBank(Integer tLinkBank) {
        this.tLinkBank = tLinkBank;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    @Override
    public String toString() {
        return "TLinkNodesVo{" +
            "tLinkId='" + tLinkId + '\'' +
            ", tLinkOrder=" + tLinkOrder +
            ", tLinkBank=" + tLinkBank +
            ", linkName='" + linkName + '\'' +
            '}';
    }

}
