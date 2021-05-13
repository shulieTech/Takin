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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_schedule_record")
public class ScheduleRecordEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 场景ID
     */
    @TableField(value = "scene_id")
    private Long sceneId;

    /**
     * 任务ID
     */
    @TableField(value = "task_id")
    private Long taskId;

    /**
     * pod数量
     */
    @TableField(value = "pod_num")
    private Integer podNum;

    /**
     * pod种类
     */
    @TableField(value = "pod_class")
    private String podClass;

    /**
     * 调度结果 0-失败 1-成功
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * CPU核数
     */
    @TableField(value = "cpu_core_num")
    private BigDecimal cpuCoreNum;

    /**
     * 内存G
     */
    @TableField(value = "memory_size")
    private BigDecimal memorySize;

    /**
     * 调度时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;
}
