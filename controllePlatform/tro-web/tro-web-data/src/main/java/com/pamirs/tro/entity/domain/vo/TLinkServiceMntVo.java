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

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pamirs.tro.common.util.LongToStringFormatSerialize;
import com.pamirs.tro.entity.domain.annotation.ExcelTag;
import com.pamirs.tro.entity.domain.entity.BaseEntity;
import com.pamirs.tro.entity.domain.entity.TLinkServiceMnt;

/**
 * 说明：基础链路服务封装类
 *
 * @author shulie
 * @version V1.0
 * @date: 2018年3月1日 下午12:49:55
 */
@JsonIgnoreProperties(value = {"handler"})
public class TLinkServiceMntVo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @JsonIgnoreProperties
    private String linkServiceIds;

    // @Field aswanId : 阿斯旺场景id
    @ExcelTag(name = "阿斯旺场景id", type = String.class)
    private String aswanId;

    //@Field tLinkServiceMntList : 链路服务管理实体类
    @ExcelTag(name = "链路服务", type = List.class)
    private List<TLinkServiceMnt> tLinkServiceMntList;
    // @Field linkDesc : 基础链路说明
    @ExcelTag(name = "链路描述", type = String.class)
    private String linkDesc;

    //@Field linkEntrence : 链路入口(http接口)
    @ExcelTag(name = "链路入口(http接口)", type = String.class)
    private String linkEntrence;

    //链路所属模块
    private String linkModule;

    // @Field linkName : 基础链路名称
    @ExcelTag(name = "链路名称", type = String.class)
    private String linkName;

    // @Field linkRank : 基础链路等级
    private String linkRank;

    // @Field techLinks : 技术链路集合
    private String techLinks;

    // @Field techLinks : 技术链路集合List
    private List<List<Map<String, Object>>> techLinksList;

    // @Field linkType : 1: 表示业务链路; 2: 表示技术链路; 3: 表示既是业务也是技术链路;
    private String linkType;

    // @Field principalNo : 负责人工号
    @ExcelTag(name = "负责人工号", type = String.class)
    private String principalNo;

    //@Field rt : 请求标准毫秒值
    @ExcelTag(name = "目标rt", type = String.class)
    private String rt;

    //@Field rtSa : 请求达标率(%)
    @ExcelTag(name = "目标rt-sa", type = String.class)
    private String rtSa;

    //二级链路id
    private String secondLinkId;

    //二级链路id
    @ExcelTag(name = "二级链路", type = String.class)
    private String secondLinkName;

    //@Field targetSuccessRate : 目标成功率(%)
    @ExcelTag(name = "目标成功率(%)", type = String.class)
    private String targetSuccessRate;

    //@Field tps : 目标TPS
    @ExcelTag(name = "目标TPS", type = String.class)
    private String targetTps;

    // @Field useYn : 是否可用(0表示不可用;1表示可用)
    @ExcelTag(name = "是否可用", type = String.class, convert = true, rule = "1=可用,2=不可用")
    private int useYn;

    //是否计算单量
    private String volumeCalcStatus;

    // @Field id : 基础链路id
    @JsonSerialize(using = LongToStringFormatSerialize.class)
    @ExcelTag(name = "基础链路id", type = String.class)
    private long linkId;
    //@Field dictType : 字典类型
    private String dictType;
    //@Field tps : 吞吐量(每秒完成事务数量) 当前TPS
    @ExcelTag(name = "当前TPS", type = String.class)
    private String tps;

    //@Field nodes : 节点
    private List<Map<String, Object>> nodes;

    //@Field nodes : 链路
    private List<Map<String, Object>> links;

    public TLinkServiceMntVo() {
        super();
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getTechLinks() {
        return techLinks;
    }

    public void setTechLinks(String techLinks) {
        this.techLinks = techLinks;
    }

    /**
     * 2018年5月17日
     *
     * @return the linkServiceIds
     * @author shulie
     * @version 1.0
     */
    public String getLinkServiceIds() {
        return linkServiceIds;
    }

    /**
     * 2018年5月17日
     *
     * @param linkServiceIds the linkServiceIds to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkServiceIds(String linkServiceIds) {
        this.linkServiceIds = linkServiceIds;
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
     * @return the linkEntrence
     * @author shulie
     * @version 1.0
     */
    public String getLinkEntrence() {
        return linkEntrence;
    }

    /**
     * 2018年5月17日
     *
     * @param linkEntrence the linkEntrence to set
     * @author shulie
     * @version 1.0
     */
    public void setLinkEntrence(String linkEntrence) {
        this.linkEntrence = linkEntrence;
    }

    /**
     * 2018年5月17日
     *
     * @return the rtSa
     * @author shulie
     * @version 1.0
     */
    public String getRtSa() {
        return rtSa;
    }

    /**
     * 2018年5月17日
     *
     * @param rtSa the rtSa to set
     * @author shulie
     * @version 1.0
     */
    public void setRtSa(String rtSa) {
        this.rtSa = rtSa;
    }

    /**
     * 2018年5月17日
     *
     * @return the rt
     * @author shulie
     * @version 1.0
     */
    public String getRt() {
        return rt;
    }

    /**
     * 2018年5月17日
     *
     * @param rt the rt to set
     * @author shulie
     * @version 1.0
     */
    public void setRt(String rt) {
        this.rt = rt;
    }

    /**
     * 2018年5月17日
     *
     * @return the tps
     * @author shulie
     * @version 1.0
     */
    public String getTps() {
        return tps;
    }

    /**
     * 2018年5月17日
     *
     * @param tps the tps to set
     * @author shulie
     * @version 1.0
     */
    public void setTps(String tps) {
        this.tps = tps;
    }

    /**
     * 2018年5月17日
     *
     * @return the targetSuccessRate
     * @author shulie
     * @version 1.0
     */
    public String getTargetSuccessRate() {
        return targetSuccessRate;
    }

    /**
     * 2018年5月17日
     *
     * @param targetSuccessRate the targetSuccessRate to set
     * @author shulie
     * @version 1.0
     */
    public void setTargetSuccessRate(String targetSuccessRate) {
        this.targetSuccessRate = targetSuccessRate;
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
     * @return the useYn
     * @author shulie
     * @version 1.0
     */
    public int getUseYn() {
        return useYn;
    }

    /**
     * 2018年5月17日
     *
     * @param useYn the useYn to set
     * @author shulie
     * @version 1.0
     */
    public void setUseYn(int useYn) {
        this.useYn = useYn;
    }

    /**
     * 2018年5月17日
     *
     * @return the tLinkServiceMntList
     * @author shulie
     * @version 1.0
     */
    public List<TLinkServiceMnt> gettLinkServiceMntList() {
        return tLinkServiceMntList;
    }

    /**
     * 2018年5月17日
     *
     * @param tLinkServiceMntList the tLinkServiceMntList to set
     * @author shulie
     * @version 1.0
     */
    public void settLinkServiceMntList(List<TLinkServiceMnt> tLinkServiceMntList) {
        this.tLinkServiceMntList = tLinkServiceMntList;
    }

    /**
     * 2019年1月11日
     *
     * @return the nodes
     * @author 298403
     * @version 1.0
     */
    public List<Map<String, Object>> getNodes() {
        return nodes;
    }

    /**
     * 2019年1月11日
     *
     * @return the nodes
     * @author 298403
     * @version 1.0
     */
    public void setNodes(List<Map<String, Object>> nodes) {
        this.nodes = nodes;
    }

    /**
     * 2019年1月11日
     *
     * @return the links
     * @author 298403
     * @version 1.0
     */
    public List<Map<String, Object>> getLinks() {
        return links;
    }

    /**
     * 2019年1月11日
     *
     * @return the links
     * @author 298403
     * @version 1.0
     */
    public void setLinks(List<Map<String, Object>> links) {
        this.links = links;
    }

    public List<List<Map<String, Object>>> getTechLinksList() {
        return techLinksList;
    }

    public void setTechLinksList(List<List<Map<String, Object>>> techLinksList) {
        this.techLinksList = techLinksList;
    }

    /**
     * Gets the value of secondLinkId.
     *
     * @return the value of secondLinkId
     * @author shulie
     * @version 1.0
     */
    public String getSecondLinkId() {
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
    public void setSecondLinkId(String secondLinkId) {
        this.secondLinkId = secondLinkId;
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
     * Gets the value of volumeCalcStatus.
     *
     * @return the value of volumeCalcStatus
     * @author shulie
     * @version 1.0
     */
    public String getVolumeCalcStatus() {
        return volumeCalcStatus;
    }

    /**
     * Sets the volumeCalcStatus.
     *
     * <p>You can use getVolumeCalcStatus() to get the value of volumeCalcStatus</p>
     *
     * @param volumeCalcStatus volumeCalcStatus
     * @author shulie
     * @version 1.0
     */
    public void setVolumeCalcStatus(String volumeCalcStatus) {
        this.volumeCalcStatus = volumeCalcStatus;
    }

    public String getTargetTps() {
        return null == targetTps ? "1" : targetTps;
    }

    public void setTargetTps(String targetTps) {
        this.targetTps = targetTps;
    }

    public String getSecondLinkName() {
        return secondLinkName;
    }

    public void setSecondLinkName(String secondLinkName) {
        this.secondLinkName = secondLinkName;
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
        return "TLinkServiceMntVo{" +
            "linkServiceIds='" + linkServiceIds + '\'' +
            ", linkId=" + linkId +
            ", linkName='" + linkName + '\'' +
            ", techLinks='" + techLinks + '\'' +
            ", techLinksList=" + techLinksList +
            ", linkDesc='" + linkDesc + '\'' +
            ", linkType='" + linkType + '\'' +
            ", aswanId='" + aswanId + '\'' +
            ", linkEntrence='" + linkEntrence + '\'' +
            ", rtSa='" + rtSa + '\'' +
            ", rt='" + rt + '\'' +
            ", tps='" + tps + '\'' +
            ", targetSuccessRate='" + targetSuccessRate + '\'' +
            ", linkRank='" + linkRank + '\'' +
            ", dictType='" + dictType + '\'' +
            ", principalNo='" + principalNo + '\'' +
            ", useYn=" + useYn +
            ", tLinkServiceMntList=" + tLinkServiceMntList +
            ", nodes=" + nodes +
            ", links=" + links +
            ", secondLinkId='" + secondLinkId + '\'' +
            ", linkModule='" + linkModule + '\'' +
            ", volumeCalcStatus='" + volumeCalcStatus + '\'' +
            ", targetTPS='" + targetTps + '\'' +
            '}';
    }
}
