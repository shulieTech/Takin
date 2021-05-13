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
import com.pamirs.tro.entity.domain.annotation.ExcelTag;
import com.pamirs.tro.entity.domain.entity.BaseEntity;

/**
 * 说明：应用接口实体类
 *
 * @author shulie
 * @version V1.0
 * @date: 2018年3月1日 下午12:49:55
 */
public class TApplicationInterface extends BaseEntity {
    //序列号
    private static final long serialVersionUID = 1L;

    //@Field applicationId : 应用id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long applicationId;
    //名单id
    private Long id;

    // @Field principalNo : 负责人工号
    @ExcelTag(name = "负责人工号", type = String.class)
    private String principalNo;

    // @Field applicationName : 应用名称
    @ExcelTag(name = "应用名称", type = String.class)
    private String applicationName;

    // @Field applicationName : 应用说明
    private String applicationDesc;

    //@Field type : 白名单类型
    @ExcelTag(name = "白名单类型", type = String.class)
    private String typeName;

    /**
     * http类型：1页面 2接口
     */
    @ExcelTag(name = "http类型", type = String.class)
    private String httpType;

    /**
     * JOB调度间隔：1调度间隔≤1分钟 2调度间隔≤5分钟 3调度间隔≤15分钟 4调度间隔≤60分钟
     */
    @ExcelTag(name = "job调度间隔", type = String.class)
    private String jobInterval;

    //mq消息类型
    @ExcelTag(name = "mq类型", type = String.class)
    private String mqType;

    //@Field useYn : 是否可用
    @ExcelTag(name = "是否可用", type = String.class, convert = true, rule = "1=可用,2=不可用")
    private String useYn;

    // @Field interfaceName : 接口名称
    @ExcelTag(name = "接口名称", type = String.class)
    private String interfaceName;

    /**
     * 页面分类：1普通页面加载(3s) 2简单查询页面/复杂界面(5s) 3复杂查询页面(8s)
     */
    @ExcelTag(name = "http页面分类", type = String.class)
    private String pageLevel;

    //队列名称
    @ExcelTag(name = "队列名字", type = String.class)
    private String queueName;

    //@Field type : 白名单类型
    private String type;

    // @Field ddlScriptPath : 影子库表结构脚本路径
    private String ddlScriptPath;

    // @Field cleanScriptPath : 数据清理脚本路径
    private String cleanScriptPath;

    // @Field readyScriptPath : 基础数据准备脚本路径
    private String readyScriptPath;

    // @Field basicScriptPath : 铺底数据脚本路径
    private String basicScriptPath;

    // @Field cacheScriptPath : 缓存预热脚本地址
    private String cacheScriptPath;

    // @Field cacheExpTime : 缓存失效时间
    private String cacheExpTime;

    //ip端口
    private String ipPort;
    @ExcelTag(name = "白名单Id", type = String.class)
    //@Field wlistLd : 白名单id
    private String wlistId;

    /**
     * 接口类型：1简单操作/查询 2一般操作/查询 3复杂操作 4涉及级联嵌套调用多服务操作 5调用外网操作
     */
    private String interfaceLevel;

    public TApplicationInterface() {
        super();
    }

    /**
     * 2018年5月17日
     *
     * @return the wlistId
     * @author shulie
     * @version 1.0
     */
    public String getWlistId() {
        return wlistId;
    }

    /**
     * 2018年5月17日
     *
     * @param wlistId the wlistId to set
     * @author shulie
     * @version 1.0
     */
    public void setWlistId(String wlistId) {
        this.wlistId = wlistId;
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
     * @return the interfaceName
     * @author shulie
     * @version 1.0
     */
    public String getInterfaceName() {
        return interfaceName;
    }

    /**
     * 2018年5月17日
     *
     * @param interfaceName the interfaceName to set
     * @author shulie
     * @version 1.0
     */
    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    /**
     * 2018年5月17日
     *
     * @return the type
     * @author shulie
     * @version 1.0
     */
    public String getType() {
        return type;
    }

    /**
     * 2018年5月17日
     *
     * @param type the type to set
     * @author shulie
     * @version 1.0
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 2018年5月17日
     *
     * @return the typeName
     * @author shulie
     * @version 1.0
     */
    public String getTypeName() {
        return typeName;
    }

    /**
     * 2018年5月17日
     *
     * @param typeName the typeName to set
     * @author shulie
     * @version 1.0
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
     * Gets the value of mqType.
     *
     * @return the value of mqType
     * @author shulie
     * @version 1.0
     */
    public String getMqType() {
        return mqType;
    }

    /**
     * Sets the mqType.
     *
     * <p>You can use getMqType() to get the value of mqType</p>
     *
     * @param mqType mqType
     * @author shulie
     * @version 1.0
     */
    public void setMqType(String mqType) {
        this.mqType = mqType;
    }

    /**
     * Gets the value of queueName.
     *
     * @return the value of queueName
     * @author shulie
     * @version 1.0
     */
    public String getQueueName() {
        return queueName;
    }

    /**
     * Sets the queueName.
     *
     * <p>You can use getQueueName() to get the value of queueName</p>
     *
     * @param queueName queueName
     * @author shulie
     * @version 1.0
     */
    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    /**
     * Gets the value of ipPort.
     *
     * @return the value of ipPort
     * @author shulie
     * @version 1.0
     */
    public String getIpPort() {
        return ipPort;
    }

    /**
     * Sets the ipPort.
     *
     * <p>You can use getIpPort() to get the value of ipPort</p>
     *
     * @param ipPort ipPort
     * @author shulie
     * @version 1.0
     */
    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    /**
     * Gets the value of httpType.
     *
     * @return the value of httpType
     * @author shulie
     * @version 1.0
     */
    public String getHttpType() {
        return httpType;
    }

    /**
     * Sets the httpType.
     *
     * <p>You can use getHttpType() to get the value of httpType</p>
     *
     * @param httpType httpType
     * @author shulie
     * @version 1.0
     */
    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }

    /**
     * Gets the value of pageLevel.
     *
     * @return the value of pageLevel
     * @author shulie
     * @version 1.0
     */
    public String getPageLevel() {
        return pageLevel;
    }

    /**
     * Sets the pageLevel.
     *
     * <p>You can use getPageLevel() to get the value of pageLevel</p>
     *
     * @param pageLevel pageLevel
     * @author shulie
     * @version 1.0
     */
    public void setPageLevel(String pageLevel) {
        this.pageLevel = pageLevel;
    }

    /**
     * Gets the value of interfaceLevel.
     *
     * @return the value of interfaceLevel
     * @author shulie
     * @version 1.0
     */
    public String getInterfaceLevel() {
        return interfaceLevel;
    }

    /**
     * Sets the interfaceLevel.
     *
     * <p>You can use getInterfaceLevel() to get the value of interfaceLevel</p>
     *
     * @param interfaceLevel interfaceLevel
     * @author shulie
     * @version 1.0
     */
    public void setInterfaceLevel(String interfaceLevel) {
        this.interfaceLevel = interfaceLevel;
    }

    /**
     * Gets the value of jobInterval.
     *
     * @return the value of jobInterval
     * @author shulie
     * @version 1.0
     */
    public String getJobInterval() {
        return jobInterval;
    }

    /**
     * Sets the jobInterval.
     *
     * <p>You can use getJobInterval() to get the value of jobInterval</p>
     *
     * @param jobInterval jobInterval
     * @author shulie
     * @version 1.0
     */
    public void setJobInterval(String jobInterval) {
        this.jobInterval = jobInterval;
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
        return "TApplicationInterface{" +
            "wlistId='" + wlistId + '\'' +
            ", applicationId=" + applicationId +
            ", principalNo='" + principalNo + '\'' +
            ", applicationName='" + applicationName + '\'' +
            ", applicationDesc='" + applicationDesc + '\'' +
            ", interfaceName='" + interfaceName + '\'' +
            ", type='" + type + '\'' +
            ", typeName='" + typeName + '\'' +
            ", useYn='" + useYn + '\'' +
            ", ddlScriptPath='" + ddlScriptPath + '\'' +
            ", cleanScriptPath='" + cleanScriptPath + '\'' +
            ", readyScriptPath='" + readyScriptPath + '\'' +
            ", basicScriptPath='" + basicScriptPath + '\'' +
            ", cacheScriptPath='" + cacheScriptPath + '\'' +
            ", cacheExpTime='" + cacheExpTime + '\'' +
            ", mqType='" + mqType + '\'' +
            ", queueName='" + queueName + '\'' +
            ", ipPort='" + ipPort + '\'' +
            ", httpType='" + httpType + '\'' +
            ", pageLevel='" + pageLevel + '\'' +
            ", interfaceLevel='" + interfaceLevel + '\'' +
            ", jobInterval='" + jobInterval + '\'' +
            '}';
    }
}
