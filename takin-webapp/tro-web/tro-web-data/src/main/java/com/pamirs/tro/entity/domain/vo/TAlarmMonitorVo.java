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

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.DateToStringFormatSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;

/**
 * 说明: 告警监控返回封装数据
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/6/28 16:20
 */
public class TAlarmMonitorVo implements Serializable {
    //序列号
    private static final long serialVersionUID = 1L;

    //@Field basicLinkId :基础链路id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long basicLinkId;

    private String secondLinkName;

    //@Field basicLinkName :基础链路名称
    private String basicLinkName;

    //@Field alarmId :告警列表id
    private Long alarmId;

    //@Field warPackages :告警列表应用名称(war包名称)
    private String warPackages;

    //@Field ip :告警服务器ip
    private String ip;

    //@Field alarmCollects :告警服务器异常信息
    private String alarmCollects;

    //@Field alarmDate :告警时间
    //    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    //    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date alarmDate;

    //@Field alarmDate :告警数据新增时间
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date createTime;

    //@Field alarmDate :告警数据变更时间
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date modifyTime;

    //@Field isDeleted :是否删除
    private int isDeleted;

    public TAlarmMonitorVo() {
    }

    /**
     * Gets the value of basicLinkId.
     *
     * @return the value of basicLinkId
     * @author shulie
     * @version 1.0
     */
    public long getBasicLinkId() {
        return basicLinkId;
    }

    /**
     * Sets the basicLinkId.
     *
     * <p>You can use getBasicLinkId() to get the value of basicLinkId</p>
     *
     * @param basicLinkId basicLinkId
     * @author shulie
     * @version 1.0
     */
    public void setBasicLinkId(long basicLinkId) {
        this.basicLinkId = basicLinkId;
    }

    /**
     * Gets the value of basicLinkName.
     *
     * @return the value of basicLinkName
     * @author shulie
     * @version 1.0
     */
    public String getBasicLinkName() {
        return basicLinkName;
    }

    /**
     * Sets the basicLinkName.
     *
     * <p>You can use getBasicLinkName() to get the value of basicLinkName</p>
     *
     * @param basicLinkName basicLinkName
     * @author shulie
     * @version 1.0
     */
    public void setBasicLinkName(String basicLinkName) {
        this.basicLinkName = basicLinkName;
    }

    /**
     * Gets the value of alarmId.
     *
     * @return the value of alarmId
     * @author shulie
     * @version 1.0
     */
    public Long getAlarmId() {
        return alarmId;
    }

    /**
     * Sets the alarmId.
     *
     * <p>You can use getAlarmId() to get the value of alarmId</p>
     *
     * @param alarmId alarmId
     * @author shulie
     * @version 1.0
     */
    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

    /**
     * Gets the value of warPackages.
     *
     * @return the value of warPackages
     * @author shulie
     * @version 1.0
     */
    public String getWarPackages() {
        return warPackages;
    }

    /**
     * Sets the warPackages.
     *
     * <p>You can use getWarPackages() to get the value of warPackages</p>
     *
     * @param warPackages warPackages
     * @author shulie
     * @version 1.0
     */
    public void setWarPackages(String warPackages) {
        this.warPackages = warPackages;
    }

    /**
     * Gets the value of ip.
     *
     * @return the value of ip
     * @author shulie
     * @version 1.0
     */
    public String getIp() {
        return ip;
    }

    /**
     * Sets the ip.
     *
     * <p>You can use getIp() to get the value of ip</p>
     *
     * @param ip ip
     * @author shulie
     * @version 1.0
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * Gets the value of alarmCollects.
     *
     * @return the value of alarmCollects
     * @author shulie
     * @version 1.0
     */
    public String getAlarmCollects() {
        return alarmCollects;
    }

    /**
     * Sets the alarmCollects.
     *
     * <p>You can use getAlarmCollects() to get the value of alarmCollects</p>
     *
     * @param alarmCollects alarmCollects
     * @author shulie
     * @version 1.0
     */
    public void setAlarmCollects(String alarmCollects) {
        this.alarmCollects = alarmCollects;
    }

    /**
     * Gets the value of alarmDate.
     *
     * @return the value of alarmDate
     * @author shulie
     * @version 1.0
     */
    public Date getAlarmDate() {
        return alarmDate;
    }

    /**
     * Sets the alarmDate.
     *
     * <p>You can use getAlarmDate() to get the value of alarmDate</p>
     *
     * @param alarmDate alarmDate
     * @author shulie
     * @version 1.0
     */
    public void setAlarmDate(Date alarmDate) {
        this.alarmDate = alarmDate;
    }

    /**
     * Gets the value of createTime.
     *
     * @return the value of createTime
     * @author shulie
     * @version 1.0
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * Sets the createTime.
     *
     * <p>You can use getCreateTime() to get the value of createTime</p>
     *
     * @param createTime createTime
     * @author shulie
     * @version 1.0
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * Gets the value of modifyTime.
     *
     * @return the value of modifyTime
     * @author shulie
     * @version 1.0
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * Sets the modifyTime.
     *
     * <p>You can use getModifyTime() to get the value of modifyTime</p>
     *
     * @param modifyTime modifyTime
     * @author shulie
     * @version 1.0
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * Gets the value of isDeleted.
     *
     * @return the value of isDeleted
     * @author shulie
     * @version 1.0
     */
    public int getIsDeleted() {
        return isDeleted;
    }

    /**
     * Sets the isDeleted.
     *
     * <p>You can use getIsDeleted() to get the value of isDeleted</p>
     *
     * @param isDeleted isDeleted
     * @author shulie
     * @version 1.0
     */
    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "TAlarmMonitor{" +
            "basicLinkId=" + basicLinkId +
            ", basicLinkName='" + basicLinkName + '\'' +
            ", alarmId=" + alarmId +
            ", warPackages='" + warPackages + '\'' +
            ", ip='" + ip + '\'' +
            ", alarmCollects='" + alarmCollects + '\'' +
            ", alarmDate=" + alarmDate +
            ", createTime=" + createTime +
            ", modifyTime=" + modifyTime +
            ", isDeleted=" + isDeleted +
            '}';
    }

    public String getSecondLinkName() {
        return secondLinkName;
    }

    public void setSecondLinkName(String secondLinkName) {
        this.secondLinkName = secondLinkName;
    }
}
