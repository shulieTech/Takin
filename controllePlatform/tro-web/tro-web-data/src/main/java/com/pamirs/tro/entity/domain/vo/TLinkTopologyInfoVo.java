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

import com.pamirs.tro.entity.domain.entity.TLinkTopologyInfo;

/**
 * 说明： 增加数据字典的字段
 *
 * @author shulie
 * @create 2019-04-16 14:55:46
 */
public class TLinkTopologyInfoVo extends TLinkTopologyInfo {
    /**
     * 数据字典名称
     */
    private String name;
    /**
     * 数据字典顺序
     */
    private String order;

    /**
     * 应用agent版本
     */
    private String agentVersion;

    /**
     * pradarAgent版本
     */
    private String pradarVersion;

    public TLinkTopologyInfoVo() {
        super();
    }

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
    public TLinkTopologyInfoVo(Long tltiId, Long linkId, String linkName, String entranceType, String linkEntrance,
        String nameServer, String linkType, String linkGroup, String fromLinkIds, String toLinkIds,
        String showFromLinkId, String showToLinkId, String secondLinkId, String secondLinkName, String applicationName,
        String volumeCalcStatus, Date createTime, Date updateTime, String agentVersion, String pradarVersion) {
        super(tltiId, linkId, linkName, entranceType, linkEntrance, nameServer, linkType, linkGroup, fromLinkIds,
            toLinkIds, showFromLinkId, showToLinkId, secondLinkId, secondLinkName, applicationName, volumeCalcStatus,
            createTime, updateTime);
        this.agentVersion = agentVersion;
        this.pradarVersion = pradarVersion;
    }

    /**
     * Gets the value of name.
     *
     * @return the value of name
     * @author shulie
     * @version 1.0
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * <p>You can use getName() to get the value of name</p>
     *
     * @param name name
     * @author shulie
     * @version 1.0
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the value of order.
     *
     * @return the value of order
     * @author shulie
     * @version 1.0
     */
    public String getOrder() {
        return order;
    }

    /**
     * Sets the order.
     *
     * <p>You can use getOrder() to get the value of order</p>
     *
     * @param order order
     * @author shulie
     * @version 1.0
     */
    public void setOrder(String order) {
        this.order = order;
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

    @Override
    public String toString() {
        return "TLinkTopologyInfoVo{" +
            "name='" + name + '\'' +
            ", order='" + order + '\'' +
            ", agentVersion='" + agentVersion + '\'' +
            ", pradarVersion='" + pradarVersion + '\'' +
            '}';
    }
}
