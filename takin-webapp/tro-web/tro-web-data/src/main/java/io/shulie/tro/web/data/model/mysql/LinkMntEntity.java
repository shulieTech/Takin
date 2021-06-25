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
 * 链路管理表
 */
@Data
@TableName(value = "t_link_mnt")
public class LinkMntEntity {
    /**
     * 主键id
     */
    @TableId(value = "LINK_ID", type = IdType.INPUT)
    private Long linkId;

    /**
     * 链路名称
     */
    @TableField(value = "LINK_NAME")
    private String linkName;

    /**
     * 业务链路下属技术链路ids
     */
    @TableField(value = "TECH_LINKS")
    private String techLinks;

    /**
     * 链路说明
     */
    @TableField(value = "LINK_DESC")
    private String linkDesc;

    /**
     * 1: 表示业务链路; 2: 表示技术链路; 3: 表示既是业务也是技术链路;
     */
    @TableField(value = "LINK_TYPE")
    private Integer linkType;

    /**
     * 阿斯旺链路id
     */
    @TableField(value = "ASWAN_ID")
    private String aswanId;

    /**
     * 链路入口(http接口)
     */
    @TableField(value = "LINK_ENTRENCE")
    private String linkEntrence;

    /**
     * 请求达标率(%)
     */
    @TableField(value = "RT_SA")
    private String rtSa;

    /**
     * 请求标准毫秒值
     */
    @TableField(value = "RT")
    private String rt;

    /**
     * 吞吐量(每秒完成事务数量)
     */
    @TableField(value = "TPS")
    private String tps;

    /**
     * 目标成功率(%)
     */
    @TableField(value = "TARGET_SUCCESS_RATE")
    private String targetSuccessRate;

    /**
     * 字典分类
     */
    @TableField(value = "DICT_TYPE")
    private String dictType;

    /**
     * 链路等级
     */
    @TableField(value = "LINK_RANK")
    private String linkRank;

    /**
     * 链路负责人工号
     */
    @TableField(value = "PRINCIPAL_NO")
    private String principalNo;

    /**
     * 是否可用(0表示未启用,1表示启用)
     */
    @TableField(value = "USE_YN")
    private Integer useYn;

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

    /**
     * 链路模块 1下单 2订单 3开单 4中转 5派送 6签收 7CUBC结算 10非主流程链路
     */
    @TableField(value = "LINK_MODULE")
    private String linkModule;

    /**
     * 是否计算单量
     */
    @TableField(value = "VOLUME_CALC_STATUS")
    private String volumeCalcStatus;

    /**
     * 目标TPS
     */
    @TableField(value = "TARGET_TPS")
    private String targetTps;
}
