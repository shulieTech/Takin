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

import java.util.Date;

/**
 * 说明：白名单实体类
 *
 * @author shulie
 * @version V1.0
 * @date: 2018年3月1日 下午12:49:55
 */
public class TWList extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 白名单id
     */
    private long wlistId;

    /**
     * 接口名称
     */
    //    @NotBlank(message = "接口名称不能为空")
    private String interfaceName;

    /**
     * 白名单类型
     */
    private String type;

    /**
     * 字典分类
     */
    private String dictType;

    /**
     * 应用id
     */
    private String applicationId;

    /**
     * 负责人工号
     */
    private String principalNo;

    /**
     * 是否可用
     */
    private String useYn;

    /**
     * mq类型： 1ESB 2IBM 3ROCKETMQ 4DPBOOT_ROCKETMQ
     * 当且仅当白名单类型是MQ(即type=5)时才有值
     */
    private String mqType;

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * IP端口号, 即nameServer
     * 当且仅当mq是ROCKETMQ或DPBOOT_ROCKETMQ时有值
     */
    private String ipPort;

    /**
     * http类型：1页面 2接口
     */
    private String httpType;

    /**
     * 页面分类：1普通页面加载(3s) 2简单查询页面/复杂界面(5s) 3复杂查询页面(8s)
     */
    private String pageLevel;

    /**
     * 接口类型：1简单操作/查询 2一般操作/查询 3复杂操作 4涉及级联嵌套调用多服务操作 5调用外网操作
     */
    private String interfaceLevel;

    /**
     * JOB调度间隔：1调度间隔≤1分钟 2调度间隔≤5分钟 3调度间隔≤15分钟 4调度间隔≤60分钟
     */
    private String jobInterval;

    /**
     * 租户id
     */
    private Long customerId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 全局
     */
    private Boolean isGlobal;

    /**
     * 手工添加
     */
    private Boolean isHandwork;

    private Date gmtCreate;

    private Date gmtModified;



    public TWList() {
        super();
    }

    public static TWList build(String applicationId, String interfaceType, String interfaceName, String useYn,
                               String dictType) {
        TWList tWList = new TWList();
        tWList.setApplicationId(applicationId);
        tWList.setType(interfaceType);
        tWList.setInterfaceName(interfaceName);
        tWList.setUseYn(useYn);
        tWList.setDictType(dictType);
        return tWList;
    }

    /**
     * 2018年5月17日
     *
     * @return the wlistId
     * @author shulie
     * @version 1.0
     */
    public long getWlistId() {
        return wlistId;
    }

    /**
     * 2018年5月17日
     *
     * @param wlistId the wlistId to set
     * @author shulie
     * @version 1.0
     */
    public void setWlistId(long wlistId) {
        this.wlistId = wlistId;
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
     * @return the applicationId
     * @author shulie
     * @version 1.0
     */
    public String getApplicationId() {
        return applicationId;
    }

    /**
     * 2018年5月17日
     *
     * @param applicationId the applicationId to set
     * @author shulie
     * @version 1.0
     */
    public void setApplicationId(String applicationId) {
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getGlobal() {
        return isGlobal;
    }

    public void setGlobal(Boolean global) {
        isGlobal = global;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Boolean getHandwork() {
        return isHandwork;
    }

    public void setHandwork(Boolean handwork) {
        isHandwork = handwork;
    }

    /**
     * 2018年5月17日
     *
     * @return 实体类字符串
     * @author shulie
     * @version 1.0
     */
    @Override
    public String toString() {
        return "TWList{" +
            "wlistId=" + wlistId +
            ", interfaceName='" + interfaceName + '\'' +
            ", type='" + type + '\'' +
            ", dictType='" + dictType + '\'' +
            ", applicationId='" + applicationId + '\'' +
            ", principalNo='" + principalNo + '\'' +
            ", useYn='" + useYn + '\'' +
            ", mqType='" + mqType + '\'' +
            ", queueName='" + queueName + '\'' +
            ", ipPort='" + ipPort + '\'' +
            ", httpType='" + httpType + '\'' +
            ", pageLevel='" + pageLevel + '\'' +
            ", interfaceLevel='" + interfaceLevel + '\'' +
            ", jobInterval='" + jobInterval + '\'' +
            ", isGlobal='" + isGlobal + '\'' +
            '}';
    }
}
