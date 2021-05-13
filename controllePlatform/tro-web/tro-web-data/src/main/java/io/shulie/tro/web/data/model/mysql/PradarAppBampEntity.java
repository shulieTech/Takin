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
 * 出数系统
 */
@Data
@TableName(value = "pradar_app_bamp")
public class PradarAppBampEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 埋点ip
     */
    @TableField(value = "app_point_id")
    private Long appPointId;

    /**
     * 间隔，单位min
     */
    @TableField(value = "bamp_interval")
    private Integer bampInterval;

    /**
     * 指标编码
     */
    @TableField(value = "index_code")
    private String indexCode;

    /**
     * 响应耗时
     */
    @TableField(value = "rt_avg")
    private Integer rtAvg;

    @TableField(value = "deleted")
    private Boolean deleted;

    @TableField(value = "gmt_created")
    private LocalDateTime gmtCreated;

    @TableField(value = "gmt_modified")
    private LocalDateTime gmtModified;
}
