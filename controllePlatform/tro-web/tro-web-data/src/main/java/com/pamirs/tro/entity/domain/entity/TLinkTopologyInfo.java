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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 链路拓扑图实体类
 *
 * @author 298403
 */
public class TLinkTopologyInfo implements Serializable {

    private static final long serialVersionUID = 6197054606039331050L;
    /**
     * 主键id
     */
    private Long tltiId;

    /**
     * 链路id
     */
    private Long linkId;

    /**
     * 链路名称
     */
    private String linkName;

    /**
     * 入口类型
     */
    private String entranceType;

    /**
     * 链路入口
     */
    private String linkEntrance;

    /**
     * mq的 nameServer
     */
    private String nameServer;

    /**
     * 链路类型
     */
    private String linkType;

    /**
     * 链路分组
     */
    private String linkGroup;

    /**
     * 上级链路
     */
    private String fromLinkIds;

    /**
     * 下级链路
     */
    private String toLinkIds;

    /**
     * 只显示上级id 有逆向链路写 滞留id
     */
    private String showFromLinkId;

    /**
     * 只显示下级id
     */
    private String showToLinkId;

    /**
     * 二级链路id
     */
    private String secondLinkId;

    /**
     * 耳机链路名称
     */
    private String secondLinkName;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 计算规则
     */
    private String volumeCalcStatus;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private List<String> entranceTypes;

    /**
     * 全参数构造函数
     *
     * @param tltiId
     * @param linkId
     * @param linkName
     * @param entranceType
     * @param linkEntrance
     * @param nameServer
     * @param linkType
     * @param linkGroup
     * @param fromLinkIds
     * @param toLinkIds
     * @param showFromLinkId
     * @param showToLinkId
     * @param secondLinkId
     * @param secondLinkName
     * @param applicationName
     * @param volumeCalcStatus
     * @param createTime
     * @param updateTime
     */
    public TLinkTopologyInfo(Long tltiId, Long linkId, String linkName, String entranceType, String linkEntrance,
        String nameServer, String linkType, String linkGroup, String fromLinkIds, String toLinkIds,
        String showFromLinkId, String showToLinkId, String secondLinkId, String secondLinkName, String applicationName,
        String volumeCalcStatus, Date createTime, Date updateTime) {
        this.tltiId = tltiId;
        this.linkId = linkId;
        this.linkName = linkName;
        this.entranceType = entranceType;
        this.linkEntrance = linkEntrance;
        this.nameServer = nameServer;
        this.linkType = linkType;
        this.linkGroup = linkGroup;
        this.fromLinkIds = fromLinkIds;
        this.toLinkIds = toLinkIds;
        this.showFromLinkId = showFromLinkId;
        this.showToLinkId = showToLinkId;
        this.secondLinkId = secondLinkId;
        this.secondLinkName = secondLinkName;
        this.applicationName = applicationName;
        this.volumeCalcStatus = volumeCalcStatus;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    /**
     * 默认构造器
     */
    public TLinkTopologyInfo() {
        super();
    }

    public List<String> getEntranceTypes() {
        return entranceTypes;
    }

    public void setEntranceTypes(List<String> entranceTypes) {
        this.entranceTypes = entranceTypes;
    }

    /**
     * get 方法
     *
     * @return
     */
    public Long getTltiId() {
        return tltiId;
    }

    /**
     * set 方法
     *
     * @param tltiId
     */
    public void setTltiId(Long tltiId) {
        this.tltiId = tltiId;
    }

    /**
     * get 方法
     *
     * @return
     */
    public Long getLinkId() {
        return linkId;
    }

    /**
     * set 方法
     *
     * @param linkId
     */
    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    /**
     * set 方法
     *
     * @return
     */
    public String getLinkName() {
        return linkName;
    }

    /**
     * set 方法
     *
     * @param linkName
     */
    public void setLinkName(String linkName) {
        this.linkName = linkName == null ? null : linkName.trim();
    }

    /**
     * get 方法
     *
     * @return
     */
    public String getEntranceType() {
        return entranceType;
    }

    /**
     * set 方法
     *
     * @param entranceType
     */
    public void setEntranceType(String entranceType) {
        this.entranceType = entranceType == null ? null : entranceType.trim();
    }

    /**
     * get 方法
     *
     * @return
     */
    public String getLinkEntrance() {
        return linkEntrance;
    }

    /**
     * set 方法
     *
     * @param linkEntrance
     */
    public void setLinkEntrance(String linkEntrance) {
        this.linkEntrance = linkEntrance == null ? null : linkEntrance.trim();
    }

    /**
     * get 方法
     *
     * @return
     */
    public String getNameServer() {
        return nameServer;
    }

    /**
     * set 方法
     *
     * @param nameServer
     */
    public void setNameServer(String nameServer) {
        this.nameServer = nameServer == null ? null : nameServer.trim();
    }

    /**
     * get 方法
     *
     * @return
     */
    public String getLinkType() {
        return linkType;
    }

    /**
     * set 方法
     *
     * @param linkType
     */
    public void setLinkType(String linkType) {
        this.linkType = linkType == null ? null : linkType.trim();
    }

    /**
     * get 方法
     *
     * @return
     */
    public String getLinkGroup() {
        return linkGroup;
    }

    /**
     * set 方法
     *
     * @param linkGroup
     */
    public void setLinkGroup(String linkGroup) {
        this.linkGroup = linkGroup == null ? null : linkGroup.trim();
    }

    /**
     * get 方法
     *
     * @return
     */
    public String getFromLinkIds() {
        return fromLinkIds;
    }

    /**
     * set 方法
     *
     * @param fromLinkIds
     */
    public void setFromLinkIds(String fromLinkIds) {
        this.fromLinkIds = fromLinkIds == null ? null : fromLinkIds.trim();
    }

    /**
     * get 方法
     *
     * @return
     */
    public String getToLinkIds() {
        return toLinkIds;
    }

    /**
     * set 方法
     *
     * @param toLinkIds
     */
    public void setToLinkIds(String toLinkIds) {
        this.toLinkIds = toLinkIds == null ? null : toLinkIds.trim();
    }

    /**
     * get 方法
     *
     * @return
     */
    public String getShowFromLinkId() {
        return showFromLinkId;
    }

    /**
     * set 方法
     *
     * @param showFromLinkId
     */
    public void setShowFromLinkId(String showFromLinkId) {
        this.showFromLinkId = showFromLinkId == null ? null : showFromLinkId.trim();
    }

    /**
     * get 方法
     *
     * @return
     */
    public String getShowToLinkId() {
        return showToLinkId;
    }

    /**
     * set 方法
     *
     * @param showToLinkId
     */
    public void setShowToLinkId(String showToLinkId) {
        this.showToLinkId = showToLinkId == null ? null : showToLinkId.trim();
    }

    /**
     * get 方法
     *
     * @return
     */
    public String getSecondLinkId() {
        return secondLinkId;
    }

    /**
     * set 方法
     *
     * @param secondLinkId
     */
    public void setSecondLinkId(String secondLinkId) {
        this.secondLinkId = secondLinkId == null ? null : secondLinkId.trim();
    }

    /**
     * get 方法
     *
     * @return
     */
    public String getSecondLinkName() {
        return secondLinkName;
    }

    /**
     * set 方法
     *
     * @param secondLinkName
     */
    public void setSecondLinkName(String secondLinkName) {
        this.secondLinkName = secondLinkName == null ? null : secondLinkName.trim();
    }

    /**
     * get 方法
     *
     * @return
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * set 方法
     *
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * get 方法
     *
     * @return
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * set 方法
     *
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getVolumeCalcStatus() {
        return volumeCalcStatus;
    }

    public void setVolumeCalcStatus(String volumeCalcStatus) {
        this.volumeCalcStatus = volumeCalcStatus;
    }

}
