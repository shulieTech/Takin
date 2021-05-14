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

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.DateToStringFormatSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;
import com.pamirs.tro.entity.domain.entity.BaseEntity;

/**
 * 说明：数据构建实体类
 *
 * @author shulie
 * @version V1.0
 * @date: 2018年3月1日 下午12:49:55
 */
public class TDataBuild extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //@Field secondLinkName : 二级链路名称
    private String secondLinkName;

    //@Field secondLinkId :二级链路id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long secondLinkId;

    //@Field id : 构建id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long dataBuildId;

    //@Field linkId :链路id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long linkId;

    //@Field linkName : 链路名称
    private String linkName;

    // @Field principalNo : 负责人工号
    private String principalNo;

    //@Field applicationId : 应用id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long applicationId;

    //@Field applicationName : 应用名称
    private String applicationName;

    //@Field ddlBuildStatus : 影子库表结构脚本构建状态(0未启动 1正在执行 2执行成功 3执行失败)
    private int ddlBuildStatus;

    //@Field ddlLastSuccessTime : 影子库表结构脚本上一次执行成功时间
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date ddlLastSuccessTime;

    //@Field cacheBuildStatus : 缓存预热脚本执行状态
    private int cacheBuildStatus;

    //@Field cacheLastSuccessTime : 缓存预热脚本上一次执行成功时间
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date cacheLastSuccessTime;

    //@Field readyBuildStatus : 基础数据准备脚本执行状态
    private int readyBuildStatus;

    //@Field readyLastSuccessTime : 基础数据准备脚本上一次执行成功时间
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date readyLastSuccessTime;

    //@Field basicBuildStatus : 铺底数据脚本执行状态
    private int basicBuildStatus;

    //@Field basicLastSuccessTime : 铺底数据脚本上一次执行成功时间
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date basicLastSuccessTime;

    //@Field cleanBuildStatus : 数据清理脚本执行状态
    private int cleanBuildStatus;

    //@Field cleanLastSuccessTime : 数据清理脚本上一次执行成功时间
    @JsonSerialize(using = DateToStringFormatSerialize.class)
    private Date cleanLastSuccessTime;

    public TDataBuild() {
        super();
    }

    public String getSecondLinkName() {
        return secondLinkName;
    }

    public void setSecondLinkName(String secondLinkName) {
        this.secondLinkName = secondLinkName;
    }

    public long getSecondLinkId() {
        return secondLinkId;
    }

    public void setSecondLinkId(long secondLinkId) {
        this.secondLinkId = secondLinkId;
    }

    /**
     * 2018年5月17日
     *
     * @return the dataBuildId
     * @author shulie
     * @version 1.0
     */
    public long getDataBuildId() {
        return dataBuildId;
    }

    /**
     * 2018年5月17日
     *
     * @param dataBuildId the dataBuildId to set
     * @author shulie
     * @version 1.0
     */
    public void setDataBuildId(long dataBuildId) {
        this.dataBuildId = dataBuildId;
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
     * @return the applicationId
     * @author shulie
     * @version 1.0
     */
    public long getApplicationId() {
        return applicationId;
    }

    /**
     * 2018年5月17日
     *
     * @param applicationId the applicationId to set
     * @author shulie
     * @version 1.0
     */
    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * 2018年5月17日
     *
     * @return the applicationName
     * @author shulie
     * @version 1.0
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * 2018年5月17日
     *
     * @param applicationName the applicationName to set
     * @author shulie
     * @version 1.0
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * 2018年5月17日
     *
     * @return the ddlBuildStatus
     * @author shulie
     * @version 1.0
     */
    public int getDdlBuildStatus() {
        return ddlBuildStatus;
    }

    /**
     * 2018年5月17日
     *
     * @param ddlBuildStatus the ddlBuildStatus to set
     * @author shulie
     * @version 1.0
     */
    public void setDdlBuildStatus(int ddlBuildStatus) {
        this.ddlBuildStatus = ddlBuildStatus;
    }

    /**
     * 2018年5月17日
     *
     * @return the ddlLastSuccessTime
     * @author shulie
     * @version 1.0
     */
    public Date getDdlLastSuccessTime() {
        return ddlLastSuccessTime;
    }

    /**
     * 2018年5月17日
     *
     * @param ddlLastSuccessTime the ddlLastSuccessTime to set
     * @author shulie
     * @version 1.0
     */
    public void setDdlLastSuccessTime(Date ddlLastSuccessTime) {
        this.ddlLastSuccessTime = ddlLastSuccessTime;
    }

    /**
     * 2018年5月17日
     *
     * @return the cacheBuildStatus
     * @author shulie
     * @version 1.0
     */
    public int getCacheBuildStatus() {
        return cacheBuildStatus;
    }

    /**
     * 2018年5月17日
     *
     * @param cacheBuildStatus the cacheBuildStatus to set
     * @author shulie
     * @version 1.0
     */
    public void setCacheBuildStatus(int cacheBuildStatus) {
        this.cacheBuildStatus = cacheBuildStatus;
    }

    /**
     * 2018年5月17日
     *
     * @return the cacheLastSuccessTime
     * @author shulie
     * @version 1.0
     */
    public Date getCacheLastSuccessTime() {
        return cacheLastSuccessTime;
    }

    /**
     * 2018年5月17日
     *
     * @param cacheLastSuccessTime the cacheLastSuccessTime to set
     * @author shulie
     * @version 1.0
     */
    public void setCacheLastSuccessTime(Date cacheLastSuccessTime) {
        this.cacheLastSuccessTime = cacheLastSuccessTime;
    }

    /**
     * 2018年5月17日
     *
     * @return the readyBuildStatus
     * @author shulie
     * @version 1.0
     */
    public int getReadyBuildStatus() {
        return readyBuildStatus;
    }

    /**
     * 2018年5月17日
     *
     * @param readyBuildStatus the readyBuildStatus to set
     * @author shulie
     * @version 1.0
     */
    public void setReadyBuildStatus(int readyBuildStatus) {
        this.readyBuildStatus = readyBuildStatus;
    }

    /**
     * 2018年5月17日
     *
     * @return the readyLastSuccessTime
     * @author shulie
     * @version 1.0
     */
    public Date getReadyLastSuccessTime() {
        return readyLastSuccessTime;
    }

    /**
     * 2018年5月17日
     *
     * @param readyLastSuccessTime the readyLastSuccessTime to set
     * @author shulie
     * @version 1.0
     */
    public void setReadyLastSuccessTime(Date readyLastSuccessTime) {
        this.readyLastSuccessTime = readyLastSuccessTime;
    }

    /**
     * 2018年5月17日
     *
     * @return the basicBuildStatus
     * @author shulie
     * @version 1.0
     */
    public int getBasicBuildStatus() {
        return basicBuildStatus;
    }

    /**
     * 2018年5月17日
     *
     * @param basicBuildStatus the basicBuildStatus to set
     * @author shulie
     * @version 1.0
     */
    public void setBasicBuildStatus(int basicBuildStatus) {
        this.basicBuildStatus = basicBuildStatus;
    }

    /**
     * 2018年5月17日
     *
     * @return the basicLastSuccessTime
     * @author shulie
     * @version 1.0
     */
    public Date getBasicLastSuccessTime() {
        return basicLastSuccessTime;
    }

    /**
     * 2018年5月17日
     *
     * @param basicLastSuccessTime the basicLastSuccessTime to set
     * @author shulie
     * @version 1.0
     */
    public void setBasicLastSuccessTime(Date basicLastSuccessTime) {
        this.basicLastSuccessTime = basicLastSuccessTime;
    }

    /**
     * 2018年5月17日
     *
     * @return the cleanBuildStatus
     * @author shulie
     * @version 1.0
     */
    public int getCleanBuildStatus() {
        return cleanBuildStatus;
    }

    /**
     * 2018年5月17日
     *
     * @param cleanBuildStatus the cleanBuildStatus to set
     * @author shulie
     * @version 1.0
     */
    public void setCleanBuildStatus(int cleanBuildStatus) {
        this.cleanBuildStatus = cleanBuildStatus;
    }

    /**
     * 2018年5月17日
     *
     * @return the cleanLastSuccessTime
     * @author shulie
     * @version 1.0
     */
    public Date getCleanLastSuccessTime() {
        return cleanLastSuccessTime;
    }

    /**
     * 2018年5月17日
     *
     * @param cleanLastSuccessTime the cleanLastSuccessTime to set
     * @author shulie
     * @version 1.0
     */
    public void setCleanLastSuccessTime(Date cleanLastSuccessTime) {
        this.cleanLastSuccessTime = cleanLastSuccessTime;
    }

    /**
     * 2018年5月17日
     *
     * @return 实体字符串
     * @author shulie
     * @version 1.0
     */
    @Override
    public String toString() {
        return "TDataBuild{" +
            "secondLinkName='" + secondLinkName + '\'' +
            ", secondLinkId=" + secondLinkId +
            ", dataBuildId=" + dataBuildId +
            ", linkId=" + linkId +
            ", linkName='" + linkName + '\'' +
            ", principalNo='" + principalNo + '\'' +
            ", applicationId=" + applicationId +
            ", applicationName='" + applicationName + '\'' +
            ", ddlBuildStatus=" + ddlBuildStatus +
            ", ddlLastSuccessTime=" + ddlLastSuccessTime +
            ", cacheBuildStatus=" + cacheBuildStatus +
            ", cacheLastSuccessTime=" + cacheLastSuccessTime +
            ", readyBuildStatus=" + readyBuildStatus +
            ", readyLastSuccessTime=" + readyLastSuccessTime +
            ", basicBuildStatus=" + basicBuildStatus +
            ", basicLastSuccessTime=" + basicLastSuccessTime +
            ", cleanBuildStatus=" + cleanBuildStatus +
            ", cleanLastSuccessTime=" + cleanLastSuccessTime +
            '}';
    }
}
