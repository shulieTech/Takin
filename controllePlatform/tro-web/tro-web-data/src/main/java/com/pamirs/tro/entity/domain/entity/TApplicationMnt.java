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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;

/**
 * 说明：应用管理实体类
 *
 * @author shulie
 * @version V1.0
 * @date: 2018年3月1日 下午12:49:55
 */
public class TApplicationMnt extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //@Field applicationId : 应用编号
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long applicationId;

    //@Field applicationName : 应用名称
    @NotBlank(message = "应用名称不能为空")
    private String applicationName;

    //@Field applicationName : 应用说明
    @NotBlank(message = "应用说明不能为空")
    private String applicationDesc;

    //@Field ddlScriptPath : 影子库表结构脚本路径`
    @NotBlank(message = "影子库表结构脚本路径不能为空")
    private String ddlScriptPath;

    //@Field cleanScriptPath : 数据清理脚本路径
    @NotBlank(message = "数据清理脚本路径不能为空")
    private String cleanScriptPath;

    //@Field readyScriptPath : 基础数据准备脚本路径
    @NotBlank(message = "基础数据准备脚本路径不能为空")
    private String readyScriptPath;

    //@Field basicScriptPath : 铺底数据脚本路径
    @NotBlank(message = "铺底数据脚本路径不能为空")
    private String basicScriptPath;

    //@Field cacheScriptPath : 缓存预热脚本地址
    @NotBlank(message = "缓存预热脚本地址不能为空")
    private String cacheScriptPath;

    // @Field useYn : 是否可用(0表示不可用;1表示可用)
    //@Range(min=0,max=1,message="有效值必须在0和1之间")
    private String useYn;

    //'接入状态； 0：正常 ； 1；待配置 ；2：待检测 ; 3：异常
    //@Range(min=0,max=3,message="应用接入状态")
    private Integer accessStatus;

    private String exceptionInfo;

    //节点数量
    private Integer nodeNum;

    private String switchStatus;
    //@Field cacheExpTime : 缓存失效时间
    @Min(value = 0, message = "缓存过期时间最小为0,0表示永不过期")
    private String cacheExpTime;
    //告警人，在链路探活中使用
    private String alarmPerson;
    private String pradarVersion;
    private Long customerId;
    private Long userId;

    public TApplicationMnt() {
        super();
    }

    public String getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(String switchStatus) {
        this.switchStatus = switchStatus;
    }

    public Integer getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(Integer accessStatus) {
        this.accessStatus = accessStatus;
    }

    public String getExceptionInfo() {
        return exceptionInfo;
    }

    public void setExceptionInfo(String exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
    }

    public Integer getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(Integer nodeNum) {
        this.nodeNum = nodeNum;
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
     * @return the applicationDesc
     * @author shulie
     * @version 1.0
     */
    public String getApplicationDesc() {
        return applicationDesc;
    }

    /**
     * 2018年5月17日
     *
     * @param applicationDesc the applicationDesc to set
     * @author shulie
     * @version 1.0
     */
    public void setApplicationDesc(String applicationDesc) {
        this.applicationDesc = applicationDesc;
    }

    /**
     * 2018年5月17日
     *
     * @return the ddlScriptPath
     * @author shulie
     * @version 1.0
     */
    public String getDdlScriptPath() {
        return ddlScriptPath;
    }

    /**
     * 2018年5月17日
     *
     * @param ddlScriptPath the ddlScriptPath to set
     * @author shulie
     * @version 1.0
     */
    public void setDdlScriptPath(String ddlScriptPath) {
        this.ddlScriptPath = ddlScriptPath;
    }

    /**
     * 2018年5月17日
     *
     * @return the cleanScriptPath
     * @author shulie
     * @version 1.0
     */
    public String getCleanScriptPath() {
        return cleanScriptPath;
    }

    /**
     * 2018年5月17日
     *
     * @param cleanScriptPath the cleanScriptPath to set
     * @author shulie
     * @version 1.0
     */
    public void setCleanScriptPath(String cleanScriptPath) {
        this.cleanScriptPath = cleanScriptPath;
    }

    /**
     * 2018年5月17日
     *
     * @return the readyScriptPath
     * @author shulie
     * @version 1.0
     */
    public String getReadyScriptPath() {
        return readyScriptPath;
    }

    /**
     * 2018年5月17日
     *
     * @param readyScriptPath the readyScriptPath to set
     * @author shulie
     * @version 1.0
     */
    public void setReadyScriptPath(String readyScriptPath) {
        this.readyScriptPath = readyScriptPath;
    }

    /**
     * 2018年5月17日
     *
     * @return the basicScriptPath
     * @author shulie
     * @version 1.0
     */
    public String getBasicScriptPath() {
        return basicScriptPath;
    }

    /**
     * 2018年5月17日
     *
     * @param basicScriptPath the basicScriptPath to set
     * @author shulie
     * @version 1.0
     */
    public void setBasicScriptPath(String basicScriptPath) {
        this.basicScriptPath = basicScriptPath;
    }

    /**
     * 2018年5月17日
     *
     * @return the cacheScriptPath
     * @author shulie
     * @version 1.0
     */
    public String getCacheScriptPath() {
        return cacheScriptPath;
    }

    /**
     * 2018年5月17日
     *
     * @param cacheScriptPath the cacheScriptPath to set
     * @author shulie
     * @version 1.0
     */
    public void setCacheScriptPath(String cacheScriptPath) {
        this.cacheScriptPath = cacheScriptPath;
    }

    /**
     * 2018年5月17日
     *
     * @return the useYn
     * @author shulie
     * @version 1.0
     */
    public String getUseYn() {
        return useYn;
    }

    /**
     * 2018年5月17日
     *
     * @param useYn the useYn to set
     * @author shulie
     * @version 1.0
     */
    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    /**
     * 2018年5月17日
     *
     * @return the cacheExpTime
     * @author shulie
     * @version 1.0
     */
    public String getCacheExpTime() {
        return cacheExpTime;
    }

    /**
     * 2018年5月17日
     *
     * @param cacheExpTime the cacheExpTime to set
     * @author shulie
     * @version 1.0
     */
    public void setCacheExpTime(String cacheExpTime) {
        this.cacheExpTime = cacheExpTime;
    }

    /**
     * Gets the value of alarmPerson.
     *
     * @return the value of alarmPerson
     * @author shulie
     * @version 1.0
     */
    public String getAlarmPerson() {
        return alarmPerson;
    }

    /**
     * Sets the alarmPerson.
     *
     * <p>You can use getAlarmPerson() to get the value of alarmPerson</p>
     *
     * @param alarmPerson alarmPerson
     * @author shulie
     * @version 1.0
     */
    public void setAlarmPerson(String alarmPerson) {
        this.alarmPerson = alarmPerson;
    }

    public String getPradarVersion() {
        return pradarVersion;
    }

    public TApplicationMnt setPradarVersion(String pradarVersion) {
        this.pradarVersion = pradarVersion;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        return "TApplicationMnt{" +
            "applicationId=" + applicationId +
            ", applicationName='" + applicationName + '\'' +
            ", applicationDesc='" + applicationDesc + '\'' +
            ", ddlScriptPath='" + ddlScriptPath + '\'' +
            ", cleanScriptPath='" + cleanScriptPath + '\'' +
            ", readyScriptPath='" + readyScriptPath + '\'' +
            ", basicScriptPath='" + basicScriptPath + '\'' +
            ", cacheScriptPath='" + cacheScriptPath + '\'' +
            ", alarmPerson='" + alarmPerson + '\'' +
            ", useYn='" + useYn + '\'' +
            ", cacheExpTime='" + cacheExpTime + '\'' +
            '}';
    }
    // add by 557092, in order to compare the object is equals to other or not.

    /**
     * 重写hashCode值，判断对象是否相等
     * 2018年5月17日
     *
     * @return hashCode
     * @author shulie
     * @version 1.0
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int)(applicationId ^ (applicationId >>> 32));
        result = prime * result + ((applicationName == null) ? 0 : applicationName.hashCode());
        return result;
    }

    // add by 557092, in order to compare the object is equals to other or not.

    /**
     * 重写equals()，判断对象是否相等
     * 2018年5月17日
     *
     * @return true相等 false不相等
     * @author shulie
     * @version 1.0
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TApplicationMnt other = (TApplicationMnt)obj;
        if (applicationId != other.applicationId) {
            return false;
        }
        if (applicationName == null) {
            if (other.applicationName != null) {
                return false;
            }
        } else if (!applicationName.equals(other.applicationName)) {
            return false;
        }
        return true;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
