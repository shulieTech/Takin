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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * trace入口列表
 */
@Data
@TableName(value = "t_tro_trace_entry")
public class TroTraceEntryEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 应用
     */
    @TableField(value = "app_name")
    private String appName;

    /**
     * 入口
     */
    @TableField(value = "entry")
    private String entry;

    /**
     * 方法
     */
    @TableField(value = "method")
    private String method;

    /**
     * 状态
     */
    @TableField(value = "status")
    private String status;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private Long startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private Long endTime;

    /**
     * 耗时
     */
    @TableField(value = "process_time")
    private Long processTime;

    /**
     * traceId
     */
    @TableField(value = "trace_id")
    private String traceId;
}
