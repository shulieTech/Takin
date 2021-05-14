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

package io.shulie.tro.cloud.data.model.mysql;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 压测报告容量水位
 */
@Data
@TableName(value = "t_capacity_water_level_info")
public class CapacityWaterLevelInfoEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 压测报告整体概况ID
     */
    @TableField(value = "whole_report_id")
    private Long wholeReportId;

    /**
     * 应用ID
     */
    @TableField(value = "app_id")
    private Long appId;

    /**
     * 应用名称
     */
    @TableField(value = "app_name")
    private String appName;

    /**
     * 主机总量
     */
    @TableField(value = "total_count")
    private String totalCount;

    /**
     * 达标主机
     */
    @TableField(value = "not_standard_count")
    private String notStandardCount;

    /**
     * 未达标主要原因
     */
    @TableField(value = "cause")
    private String cause;

    /**
     * 对比上次报错信息ID
     */
    @TableField(value = "last_cwli_id")
    private Long lastCwliId;

    /**
     * 上次未达标主机数量
     */
    @TableField(value = "last_not_standard_count")
    private String lastNotStandardCount;

    /**
     * 是否删除 0-未删除、1-已删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME")
    private LocalDateTime updateTime;
}
