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
 * 二级链路管理
 */
@Data
@TableName(value = "t_second_link_mnt")
public class SecondLinkMntEntity {
    /**
     * 主键id
     */
    @TableId(value = "LINK_ID", type = IdType.INPUT)
    private String linkId;

    /**
     * 链路名称
     */
    @TableField(value = "LINK_NAME")
    private String linkName;

    /**
     * 基础链路列表
     */
    @TableField(value = "BASE_LINKS")
    private String baseLinks;

    /**
     * 阿斯旺id
     */
    @TableField(value = "ASWAN_ID")
    private String aswanId;

    /**
     * 链路TPS
     */
    @TableField(value = "LINK_TPS")
    private Long linkTps;

    /**
     * 目标TPS
     */
    @TableField(value = "TARGET_TPS")
    private Long targetTps;

    /**
     * 二级链路TPS计算规则
     */
    @TableField(value = "LINK_TPS_RULE")
    private String linkTpsRule;

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
     * 备注(预留)
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 测试状态 0没在测试 1正在测试
     */
    @TableField(value = "TEST_STATUS")
    private String testStatus;
}
