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

/**
 * 说明: 基础链路应用检测封装类
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2018/6/18 18:14
 */
public class BasicLinkApplicationDetection implements Serializable {

    private static final long serialVersionUID = 1L;

    // @Field basicLinkId : 基础链路id
    private String basicLinkId;

    // @Field basicLinkName : 基础链路名称
    private String basicLinkName;

    // @Field applicationId : 应用id
    private String applicationId;

    // @Field applicationName : 应用名称
    private String applicationName;

    // @Field shadowlibCheck : 影子库整体同步检测状态
    private String shadowlibCheck;

    // @Field cacheCheck : 缓存预热校验状态
    private String cacheCheck;

    // @Field wlistCheck : 白名单校验状态
    private String wlistCheck;

    // @Field updateTime : 更新时间
    private Date updateTime;

    public BasicLinkApplicationDetection() {
    }

    /**
     * Gets the value of basicLinkId.
     *
     * @return the value of basicLinkId
     * @author shulie
     * @version 1.0
     */
    public String getBasicLinkId() {
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
    public void setBasicLinkId(String basicLinkId) {
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
     * Gets the value of applicationId.
     *
     * @return the value of applicationId
     * @author shulie
     * @version 1.0
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * Sets the applicationId.
     *
     * <p>You can use getApplicationId() to get the value of applicationId</p>
     *
     * @param applicationId applicationId
     * @author shulie
     * @version 1.0
     */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * Gets the value of applicationName.
     *
     * @return the value of applicationName
     * @author shulie
     * @version 1.0
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Sets the applicationName.
     *
     * <p>You can use getApplicationName() to get the value of applicationName</p>
     *
     * @param applicationName applicationName
     * @author shulie
     * @version 1.0
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * Gets the value of shadowlibCheck.
     *
     * @return the value of shadowlibCheck
     * @author shulie
     * @version 1.0
     */
    public String getShadowlibCheck() {
        return shadowlibCheck;
    }

    /**
     * Sets the shadowlibCheck.
     *
     * <p>You can use getShadowlibCheck() to get the value of shadowlibCheck</p>
     *
     * @param shadowlibCheck shadowlibCheck
     * @author shulie
     * @version 1.0
     */
    public void setShadowlibCheck(String shadowlibCheck) {
        this.shadowlibCheck = shadowlibCheck;
    }

    /**
     * Gets the value of cacheCheck.
     *
     * @return the value of cacheCheck
     * @author shulie
     * @version 1.0
     */
    public String getCacheCheck() {
        return cacheCheck;
    }

    /**
     * Sets the cacheCheck.
     *
     * <p>You can use getCacheCheck() to get the value of cacheCheck</p>
     *
     * @param cacheCheck cacheCheck
     * @author shulie
     * @version 1.0
     */
    public void setCacheCheck(String cacheCheck) {
        this.cacheCheck = cacheCheck;
    }

    /**
     * Gets the value of wlistCheck.
     *
     * @return the value of wlistCheck
     * @author shulie
     * @version 1.0
     */
    public String getWlistCheck() {
        return wlistCheck;
    }

    /**
     * Sets the wlistCheck.
     *
     * <p>You can use getWlistCheck() to get the value of wlistCheck</p>
     *
     * @param wlistCheck wlistCheck
     * @author shulie
     * @version 1.0
     */
    public void setWlistCheck(String wlistCheck) {
        this.wlistCheck = wlistCheck;
    }

    /**
     * Gets the value of updateTime.
     *
     * @return the value of updateTime
     * @author shulie
     * @version 1.0
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * Sets the updateTime.
     *
     * <p>You can use getUpdateTime() to get the value of updateTime</p>
     *
     * @param updateTime updateTime
     * @author shulie
     * @version 1.0
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "BasicLinkApplicationDetection{" +
            "basicLinkId='" + basicLinkId + '\'' +
            ", basicLinkName='" + basicLinkName + '\'' +
            ", applicationId='" + applicationId + '\'' +
            ", applicationName='" + applicationName + '\'' +
            ", shadowlibCheck='" + shadowlibCheck + '\'' +
            ", cacheCheck='" + cacheCheck + '\'' +
            ", wlistCheck='" + wlistCheck + '\'' +
            '}';
    }
}
