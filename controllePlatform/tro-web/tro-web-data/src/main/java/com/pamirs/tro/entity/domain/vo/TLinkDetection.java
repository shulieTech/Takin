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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;
import com.pamirs.tro.entity.domain.entity.BaseEntity;

/**
 * 说明：链路检测实体类
 *
 * @author shulie
 * @version V1.0
 * @date: 2018年3月1日 下午12:49:55
 */
public class TLinkDetection extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //@Field secondLinkName : 二级链路名称
    private String secondLinkName;

    //@Field secondLinkId :二级链路id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long secondLinkId;

    // @Field id : 链路检测id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long linkDetectionId;

    // @Field linkId :基础链路id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long linkId;

    // @Field linkName : 基础链路名称
    private String linkName;

    // @Field principalNo : 负责人工号
    private String principalNo;

    // @Field applicationId : 应用id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long applicationId;

    // @Field applicationName : 应用名称
    private String applicationName;

    // @Field applicationName : 应用描述
    private String applicationDesc;

    // @Field shadowlibCheck : 影子库整体同步检测状态
    private String shadowlibCheck;

    //@Field shadowlibError : 影子库检测失败内容
    private String shadowlibError;

    // @Field cacheCheck : 缓存预热校验状态
    private String cacheCheck;

    //@Field cacheError : 缓存预热实时检测失败内容
    private String cacheError;

    // @Field wlistCheck : 白名单校验状态
    private String wlistCheck;

    //@Field wlistError : 白名单错误信息
    private String wlistError;

    public TLinkDetection() {
        super();
    }

    /**
     * Gets the value of secondLinkName.
     *
     * @return the value of secondLinkName
     * @author shulie
     * @version 1.0
     */
    public String getSecondLinkName() {
        return secondLinkName;
    }

    /**
     * Sets the secondLinkName.
     *
     * <p>You can use getSecondLinkName() to get the value of secondLinkName</p>
     *
     * @param secondLinkName secondLinkName
     * @author shulie
     * @version 1.0
     */
    public void setSecondLinkName(String secondLinkName) {
        this.secondLinkName = secondLinkName;
    }

    /**
     * Gets the value of secondLinkId.
     *
     * @return the value of secondLinkId
     * @author shulie
     * @version 1.0
     */
    public long getSecondLinkId() {
        return secondLinkId;
    }

    /**
     * Sets the secondLinkId.
     *
     * <p>You can use getSecondLinkId() to get the value of secondLinkId</p>
     *
     * @param secondLinkId secondLinkId
     * @author shulie
     * @version 1.0
     */
    public void setSecondLinkId(long secondLinkId) {
        this.secondLinkId = secondLinkId;
    }

    /**
     * Gets the value of applicationDesc.
     *
     * @return the value of applicationDesc
     * @author shulie
     * @version 1.0
     */
    public String getApplicationDesc() {
        return applicationDesc;
    }

    /**
     * Sets the applicationDesc.
     *
     * <p>You can use getApplicationDesc() to get the value of applicationDesc</p>
     *
     * @param applicationDesc applicationDesc
     * @author shulie
     * @version 1.0
     */
    public void setApplicationDesc(String applicationDesc) {
        this.applicationDesc = applicationDesc;
    }

    /**
     * Gets the value of shadowlibError.
     *
     * @return the value of shadowlibError
     * @author shulie
     * @version 1.0
     */
    public String getShadowlibError() {
        return shadowlibError;
    }

    /**
     * Sets the shadowlibError.
     *
     * <p>You can use getShadowlibError() to get the value of shadowlibError</p>
     *
     * @param shadowlibError shadowlibError
     * @author shulie
     * @version 1.0
     */
    public void setShadowlibError(String shadowlibError) {
        this.shadowlibError = shadowlibError;
    }

    /**
     * Gets the value of cacheError.
     *
     * @return the value of cacheError
     * @author shulie
     * @version 1.0
     */
    public String getCacheError() {
        return cacheError;
    }

    /**
     * Sets the cacheError.
     *
     * <p>You can use getCacheError() to get the value of cacheError</p>
     *
     * @param cacheError cacheError
     * @author shulie
     * @version 1.0
     */
    public void setCacheError(String cacheError) {
        this.cacheError = cacheError;
    }

    /**
     * 2018年5月17日
     *
     * @return the linkDetectionId
     * @author shulie
     * @version 1.0
     */
    public long getLinkDetectionId() {
        return linkDetectionId;
    }

    /**
     * 2018年5月17日
     *
     * @param linkDetectionId the linkDetectionId to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkDetectionId(long linkDetectionId) {
        this.linkDetectionId = linkDetectionId;
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
     * @return the shadowlibCheck
     * @author shulie
     * @version 1.0
     */
    public String getShadowlibCheck() {
        return shadowlibCheck;
    }

    /**
     * 2018年5月17日
     *
     * @param shadowlibCheck the shadowlibCheck to set
     * @author shulie
     * @version 1.0
     */
    public void setShadowlibCheck(String shadowlibCheck) {
        this.shadowlibCheck = shadowlibCheck;
    }

    /**
     * 2018年5月17日
     *
     * @return the cacheCheck
     * @author shulie
     * @version 1.0
     */
    public String getCacheCheck() {
        return cacheCheck;
    }

    /**
     * 2018年5月17日
     *
     * @param cacheCheck the cacheCheck to set
     * @author shulie
     * @version 1.0
     */
    public void setCacheCheck(String cacheCheck) {
        this.cacheCheck = cacheCheck;
    }

    /**
     * 2018年5月17日
     *
     * @return the wlistCheck
     * @author shulie
     * @version 1.0
     */
    public String getWlistCheck() {
        return wlistCheck;
    }

    /**
     * 2018年5月17日
     *
     * @param wlistCheck the wlistCheck to set
     * @author shulie
     * @version 1.0
     */
    public void setWlistCheck(String wlistCheck) {
        this.wlistCheck = wlistCheck;
    }

    /**
     * 2018年5月17日
     *
     * @return the wlistError
     * @author shulie
     * @version 1.0
     */
    public String getWlistError() {
        return wlistError;
    }

    /**
     * 2018年5月17日
     *
     * @param wlistError the wlistError to set
     * @author shulie
     * @version 1.0
     */
    public void setWlistError(String wlistError) {
        this.wlistError = wlistError;
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
        return "TLinkDetection{" +
            "secondLinkName='" + secondLinkName + '\'' +
            ", secondLinkId=" + secondLinkId +
            ", linkDetectionId=" + linkDetectionId +
            ", linkId=" + linkId +
            ", linkName='" + linkName + '\'' +
            ", principalNo='" + principalNo + '\'' +
            ", applicationId=" + applicationId +
            ", applicationName='" + applicationName + '\'' +
            ", applicationDesc='" + applicationDesc + '\'' +
            ", shadowlibCheck='" + shadowlibCheck + '\'' +
            ", shadowlibError='" + shadowlibError + '\'' +
            ", cacheCheck='" + cacheCheck + '\'' +
            ", cacheError='" + cacheError + '\'' +
            ", wlistCheck='" + wlistCheck + '\'' +
            ", wlistError='" + wlistError + '\'' +
            '}';
    }
}
