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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;

/**
 * 说明：业务链路管理实体类
 *
 * @author shulie
 * @version V1.0
 * @date: 2018年3月1日 下午12:49:55
 */
public class TLinkMnt extends BaseEntity {

    private static final long serialVersionUID = 1L;

    // @Field id : 业务链路管理id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long linkId;

    // @Field linkName : 业务链路名称
    private String linkName;

    // @Field linkDesc : 链路说明
    private String linkDesc;

    //@Field aswanId : 阿斯旺链路id
    private String aswanId;

    // @Field linkRank : 链路等级
    private String linkRank;

    //@Field dictType : 字典类型
    private String dictType;

    // @Field principalNo : 负责人工号
    private String principalNo;

    // @Field bLinkOrder : 基础链路顺序(扩展字段)
    private int bLinkOrder;

    // @Field bLinkCheckStatus : 基础链路检测状态(扩展字段)(0代表失败红灯,1代表成功绿灯)
    private String bLinkCheckStatus;

    // @Field blinkBank : 基础链路等级(二级链路下有基础链路1，基础链路2等)
    private int bLinkBank;

    public TLinkMnt() {
        super();
    }

    public int getbLinkOrder() {
        return bLinkOrder;
    }

    public void setbLinkOrder(int bLinkOrder) {
        this.bLinkOrder = bLinkOrder;
    }

    public String getbLinkCheckStatus() {
        return bLinkCheckStatus;
    }

    public void setbLinkCheckStatus(String bLinkCheckStatus) {
        this.bLinkCheckStatus = bLinkCheckStatus;
    }

    public int getBLinkBank() {
        return bLinkBank;
    }

    public void setBLinkBank(int bLinkBank) {
        this.bLinkBank = bLinkBank;
    }

    /**
     * 2018年5月17日
     *
     * @return the linkId
     * @author shulie
     * @version 1.0
     */
    public long getLinkId() {
        return linkId;
    }

    /**
     * 2018年5月17日
     *
     * @param linkId the linkId to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    /**
     * 2018年5月17日
     *
     * @return the linkName
     * @author shulie
     * @version 1.0
     */
    public String getLinkName() {
        return linkName;
    }

    /**
     * 2018年5月17日
     *
     * @param linkName the linkName to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    /**
     * 2018年5月17日
     *
     * @return the linkDesc
     * @author shulie
     * @version 1.0
     */
    public String getLinkDesc() {
        return linkDesc;
    }

    /**
     * 2018年5月17日
     *
     * @param linkDesc the linkDesc to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkDesc(String linkDesc) {
        this.linkDesc = linkDesc;
    }

    /**
     * 2018年5月17日
     *
     * @return the aswanId
     * @author shulie
     * @version 1.0
     */
    public String getAswanId() {
        return aswanId;
    }

    /**
     * 2018年5月17日
     *
     * @param aswanId the aswanId to set
     * @author shulie
     * @version 1.0
     */
    public void setAswanId(String aswanId) {
        this.aswanId = aswanId;
    }

    /**
     * 2018年5月17日
     *
     * @return the linkRank
     * @author shulie
     * @version 1.0
     */
    public String getLinkRank() {
        return linkRank;
    }

    /**
     * 2018年5月17日
     *
     * @param linkRank the linkRank to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkRank(String linkRank) {
        this.linkRank = linkRank;
    }

    /**
     * 2018年5月17日
     *
     * @return the dictType
     * @author shulie
     * @version 1.0
     */
    public String getDictType() {
        return dictType;
    }

    /**
     * 2018年5月17日
     *
     * @param dictType the dictType to set
     * @author shulie
     * @version 1.0
     */
    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    /**
     * 2018年5月17日
     *
     * @return the principalNo
     * @author shulie
     * @version 1.0
     */
    public String getPrincipalNo() {
        return principalNo;
    }

    /**
     * 2018年5月17日
     *
     * @param principalNo the principalNo to set
     * @author shulie
     * @version 1.0
     */
    public void setPrincipalNo(String principalNo) {
        this.principalNo = principalNo;
    }

    /**
     * 2018年5月17日
     *
     * @return 返回实体字符串
     * @author shulie
     * @version 1.0
     */
    @Override
    public String toString() {
        return "TLinkMnt{" +
            "linkId=" + linkId +
            ", linkName='" + linkName + '\'' +
            ", linkDesc='" + linkDesc + '\'' +
            ", aswanId='" + aswanId + '\'' +
            ", linkRank='" + linkRank + '\'' +
            ", dictType='" + dictType + '\'' +
            ", principalNo='" + principalNo + '\'' +
            ", bLinkOrder=" + bLinkOrder +
            ", bLinkCheckStatus='" + bLinkCheckStatus + '\'' +
            ", bLinkBank=" + bLinkBank +
            '}';
    }
}
