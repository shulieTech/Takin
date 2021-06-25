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
 * 告警
 */
@Data
@TableName(value = "pradar_app_warn")
public class PradarAppWarnEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 埋点ip
     */
    @TableField(value = "app_point_id")
    private Long appPointId;

    /**
     * 告警间隔，单位min
     */
    @TableField(value = "warn_interval")
    private Integer warnInterval;

    /**
     * 阈值：触发告警,单位ms
     */
    @TableField(value = "span_threshold")
    private Integer spanThreshold;

    /**
     * 耗时触发频率，单位次
     */
    @TableField(value = "span_frequency")
    private Integer spanFrequency;

    /**
     * 错误触发频率,单位次
     */
    @TableField(value = "error_frequency")
    private Integer errorFrequency;

    /**
     * 告警级别，1：error， 2：warning
     */
    @TableField(value = "warn_level")
    private Boolean warnLevel;

    @TableField(value = "deleted")
    private Boolean deleted;

    @TableField(value = "gmt_created")
    private LocalDateTime gmtCreated;

    @TableField(value = "gmt_modified")
    private LocalDateTime gmtModified;

    /**
     * 应用名
     */
    @TableField(value = "app_name")
    private String appName;

    /**
     * 埋点方法
     */
    @TableField(value = "method")
    private String method;
}
