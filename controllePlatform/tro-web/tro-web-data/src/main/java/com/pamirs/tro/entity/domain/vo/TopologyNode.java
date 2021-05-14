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

/**
 * 逻辑拓扑图 节点数据
 *
 * @author 298403
 */
public class TopologyNode {

    /**
     * 链路id
     */
    private String linkId;

    /**
     * 链路名称
     */
    private String linkName;

    /**
     * 链路类型
     */
    private String linkType;

    /**
     * 链路入口
     */
    private String linkEntrance;

    /**
     * 入口类型
     */
    private String entranceType;

    /**
     * 只显示上级id
     */
    private String showFromLinkId;

    /**
     * 只显示下级id
     */
    private String showToLinkId;

    /**
     * mq的 nameServer
     *
     * @return
     */
    private String nameServer;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 计算方式
     */
    private String volumeCalcStatus;

    /**
     * 应用agent版本
     */
    private String agentVersion;

    /**
     * pradarAgent版本
     */
    private String pradarVersion;

    /**
     * 严重 serious 普通 error 正常 normal
     */
    private String bottleLevel;

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }

    public String getLinkEntrance() {
        return linkEntrance;
    }

    public void setLinkEntrance(String linkEntrance) {
        this.linkEntrance = linkEntrance;
    }

    public String getEntranceType() {
        return entranceType;
    }

    public void setEntranceType(String entranceType) {
        this.entranceType = entranceType;
    }

    public String getShowFromLinkId() {
        return showFromLinkId;
    }

    public void setShowFromLinkId(String showFromLinkId) {
        this.showFromLinkId = showFromLinkId;
    }

    public String getShowToLinkId() {
        return showToLinkId;
    }

    public void setShowToLinkId(String showToLinkId) {
        this.showToLinkId = showToLinkId;
    }

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
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

    /**
     * Gets the value of agentVersion.
     *
     * @return the value of agentVersion
     * @author shulie
     * @version 1.0
     */
    public String getAgentVersion() {
        return agentVersion;
    }

    /**
     * Sets the agentVersion.
     *
     * <p>You can use getAgentVersion() to get the value of agentVersion</p>
     *
     * @param agentVersion agentVersion
     * @author shulie
     * @version 1.0
     */
    public void setAgentVersion(String agentVersion) {
        this.agentVersion = agentVersion;
    }

    /**
     * Gets the value of pradarVersion.
     *
     * @return the value of pradarVersion
     * @author shulie
     * @version 1.0
     */
    public String getPradarVersion() {
        return pradarVersion;
    }

    /**
     * Sets the pradarVersion.
     *
     * <p>You can use getPradarVersion() to get the value of pradarVersion</p>
     *
     * @param pradarVersion pradarVersion
     * @author shulie
     * @version 1.0
     */
    public void setPradarVersion(String pradarVersion) {
        this.pradarVersion = pradarVersion;
    }

    /**
     * Gets the value of bottleLevel.
     *
     * @return the value of bottleLevel
     * @author shulie
     * @version 1.0
     */
    public String getBottleLevel() {
        return bottleLevel;
    }

    /**
     * Sets the bottleLevel.
     *
     * <p>You can use getBottleLevel() to get the value of bottleLevel</p>
     *
     * @param bottleLevel bottleLevel
     * @author shulie
     * @version 1.0
     */
    public void setBottleLevel(String bottleLevel) {
        this.bottleLevel = bottleLevel;
    }

    @Override
    public String toString() {
        return "TopologyNode{" +
            "linkId='" + linkId + '\'' +
            ", linkName='" + linkName + '\'' +
            ", linkType='" + linkType + '\'' +
            ", linkEntrance='" + linkEntrance + '\'' +
            ", entranceType='" + entranceType + '\'' +
            ", showFromLinkId='" + showFromLinkId + '\'' +
            ", showToLinkId='" + showToLinkId + '\'' +
            ", nameServer='" + nameServer + '\'' +
            ", applicationName='" + applicationName + '\'' +
            ", volumeCalcStatus='" + volumeCalcStatus + '\'' +
            ", agentVersion='" + agentVersion + '\'' +
            ", pradarVersion='" + pradarVersion + '\'' +
            ", bottleLevel='" + bottleLevel + '\'' +
            '}';
    }
}
