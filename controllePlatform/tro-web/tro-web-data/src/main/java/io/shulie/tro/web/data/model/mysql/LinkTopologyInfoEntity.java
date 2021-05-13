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

package io.shulie.tro.web.data.model.mysql;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 透明流量链路拓扑
 */
@Data
@TableName(value = "t_link_topology_info")
public class LinkTopologyInfoEntity {
    /**
     * 链路拓扑图主键ID
     */
    @TableId(value = "TLTI_ID", type = IdType.AUTO)
    private Long tltiId;

    /**
     * 基础链路id
     */
    @TableField(value = "LINK_ID")
    private Long linkId;

    /**
     * 链路名称
     */
    @TableField(value = "LINK_NAME")
    private String linkName;

    /**
     * 入口类型 job dubbo mq http
     */
    @TableField(value = "ENTRANCE_TYPE")
    private String entranceType;

    /**
     * 链路入口
     */
    @TableField(value = "LINK_ENTRANCE")
    private String linkEntrance;

    /**
     * 队列的 nameserver
     */
    @TableField(value = "NAME_SERVER")
    private String nameServer;

    /**
     * 链路类型
     */
    @TableField(value = "LINK_TYPE")
    private String linkType;

    /**
     * 链路分组
     */
    @TableField(value = "LINK_GROUP")
    private String linkGroup;

    /**
     * 上级链路id
     */
    @TableField(value = "FROM_LINK_IDS")
    private String fromLinkIds;

    /**
     * 下级链路id
     */
    @TableField(value = "TO_LINK_IDS")
    private String toLinkIds;

    /**
     * 只显示上级ID
     */
    @TableField(value = "SHOW_FROM_LINK_ID")
    private String showFromLinkId;

    /**
     * 只显示下级ID
     */
    @TableField(value = "SHOW_TO_LINK_ID")
    private String showToLinkId;

    /**
     * 二级链路Id
     */
    @TableField(value = "SECOND_LINK_ID")
    private String secondLinkId;

    /**
     * 二级链路名称
     */
    @TableField(value = "SECOND_LINK_NAME")
    private String secondLinkName;

    /**
     * 应用名称
     */
    @TableField(value = "APPLICATION_NAME")
    private String applicationName;

    /**
     * 1 计算订单量  2 计算运单量 3 不计算单量
     */
    @TableField(value = "VOLUME_CALC_STATUS")
    private String volumeCalcStatus;

    /**
     * 插入时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 变更时间
     */
    @TableField(value = "UPDATE_TIME")
    private LocalDateTime updateTime;
}
