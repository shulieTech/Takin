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
 * @ClassName PerformanceBaseDataEntiry
 * @Description 性能基础数据表
 * @Author qianshui
 * @Date 2020/11/4 下午1:53
 */
@Data
@TableName(value = "t_performance_base_data")
public class PerformanceBaseDataEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "agent_id")
    private String agentId;

    @TableField(value = "app_name")
    private String appName;

    @TableField(value = "app_ip")
    private String appIp;

    @TableField(value = "process_id")
    private Long processId;

    @TableField(value = "process_name")
    private String processName;

    @TableField(value = "timestamp")
    private String timestamp;

    @TableField(value = "total_memory")
    private Double totalMemory;

    @TableField(value = "perm_memory")
    private Double permMemory;

    @TableField(value = "young_memory")
    private Double youngMemory;

    @TableField(value = "old_memory")
    private Double oldMemory;

    @TableField(value = "young_gc_count")
    private Integer youngGcCount;

    @TableField(value = "full_gc_count")
    private Integer fullGcCount;

    @TableField(value = "young_gc_cost")
    private Long youngGcCost;

    @TableField(value = "full_gc_cost")
    private Long fullGcCost;

}
