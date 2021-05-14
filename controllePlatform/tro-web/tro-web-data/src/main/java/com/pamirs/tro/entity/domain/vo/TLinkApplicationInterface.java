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
 * 说明：链路应用接口实体类
 *
 * @author shulie
 * @version V1.0
 * @date: 2018年3月1日 下午12:49:55
 */
public class TLinkApplicationInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //@Field applicationId : 应用id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long applicationId;

    // @Field principalNo :
    @ExcelTag(name = "负责人工号", type = String.class)
    private String principalNo;

    //@Field linkId : 业务链路id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    private long linkId;

    // @Field linkName : 链路名称
    @ExcelTag(name = "链路名称", type = String.class)
    private String linkName;

    // @Field techLinks : 技术链路集合
    private String techLinks;

    // @Field linkDesc : 链路说明
    private String linkDesc;

    // @Field linkRank : 链路等级
    @ExcelTag(name = "链路等级", type = String.class)
    private String linkRank;

    // @Field linkRank : 链路类型
    @ExcelTag(name = "链路类型", type = String.class)
    private String linkType;

    //@Field aswanId : 阿斯旺链路id
    @ExcelTag(name = "阿斯旺链路id", type = String.class)
    private String aswanId;

    // @Field applicationName : 应用名称
    @ExcelTag(name = "应用名称", type = String.class)
    private String applicationName;

    // @Field applicationName : 应用说明
    private String applicationDesc;

    //@Field tWListId : 白名单id
    private long wlistId;

    // @Field interfaceName : 接口名称
    @ExcelTag(name = "接口名称", type = String.class)
    private String interfaceName;

    //@Field type : 白名单类型
    private String typeName;

    // @Field interfaceDesc : 接口说明
    private String interfaceDesc;

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
    @ExcelTag(name = "是否可用", type = String.class, convert = true, rule = "1=可用,2=不可用")
    private String useYn;

    private String linkServiceId;

    //二级链路名称
    @ExcelTag(name = "二级链路名称", type = String.class)
    private String secondLinkName;

    //链路所属模块
    private String linkModule;

    public TLinkApplicationInterface() {
        super();
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

    public String getTechLinks() {
        return techLinks;
    }

    public void setTechLinks(String techLinks) {
        this.techLinks = techLinks;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
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
     * @return the interfaceDesc
     * @author shulie
     * @version 1.0
     */
    public String getInterfaceDesc() {
        return interfaceDesc;
    }

    /**
     * 2018年5月17日
     *
     * @param interfaceDesc the interfaceDesc to set
     * @author shulie
     * @version 1.0
     */
    public void setInterfaceDesc(String interfaceDesc) {
        this.interfaceDesc = interfaceDesc;
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
     * @return
     * @author 298403
     */
    public String getLinkServiceId() {
        return linkServiceId;
    }

    /**
     * @return
     * @author 298403
     */
    public void setLinkServiceId(String linkServiceId) {
        this.linkServiceId = linkServiceId;
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
     * Gets the value of linkModule.
     *
     * @return the value of linkModule
     * @author shulie
     * @version 1.0
     */
    public String getLinkModule() {
        return linkModule;
    }

    /**
     * Sets the linkModule.
     *
     * <p>You can use getLinkModule() to get the value of linkModule</p>
     *
     * @param linkModule linkModule
     * @author shulie
     * @version 1.0
     */
    public void setLinkModule(String linkModule) {
        this.linkModule = linkModule;
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
        return "TLinkApplicationInterface{" +
            "applicationId=" + applicationId +
            ", principalNo='" + principalNo + '\'' +
            ", linkId=" + linkId +
            ", linkName='" + linkName + '\'' +
            ", techLinks='" + techLinks + '\'' +
            ", linkDesc='" + linkDesc + '\'' +
            ", linkRank='" + linkRank + '\'' +
            ", linkType='" + linkType + '\'' +
            ", aswanId='" + aswanId + '\'' +
            ", applicationName='" + applicationName + '\'' +
            ", applicationDesc='" + applicationDesc + '\'' +
            ", wlistId=" + wlistId +
            ", interfaceName='" + interfaceName + '\'' +
            ", typeName='" + typeName + '\'' +
            ", interfaceDesc='" + interfaceDesc + '\'' +
            ", ddlScriptPath='" + ddlScriptPath + '\'' +
            ", cleanScriptPath='" + cleanScriptPath + '\'' +
            ", readyScriptPath='" + readyScriptPath + '\'' +
            ", basicScriptPath='" + basicScriptPath + '\'' +
            ", cacheScriptPath='" + cacheScriptPath + '\'' +
            ", cacheExpTime='" + cacheExpTime + '\'' +
            ", useYn='" + useYn + '\'' +
            ", linkServiceId='" + linkServiceId + '\'' +
            ", secondLinkName='" + secondLinkName + '\'' +
            ", linkModule='" + linkModule + '\'' +
            '}';
    }
}
