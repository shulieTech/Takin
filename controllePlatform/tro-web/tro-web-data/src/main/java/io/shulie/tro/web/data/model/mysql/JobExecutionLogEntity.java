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

@Data
@TableName(value = "JOB_EXECUTION_LOG")
public class JobExecutionLogEntity {
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @TableField(value = "job_name")
    private String jobName;

    @TableField(value = "task_id")
    private String taskId;

    @TableField(value = "hostname")
    private String hostname;

    @TableField(value = "ip")
    private String ip;

    @TableField(value = "sharding_item")
    private Integer shardingItem;

    @TableField(value = "execution_source")
    private String executionSource;

    @TableField(value = "failure_cause")
    private String failureCause;

    @TableField(value = "is_success")
    private Integer isSuccess;

    @TableField(value = "start_time")
    private LocalDateTime startTime;

    @TableField(value = "complete_time")
    private LocalDateTime completeTime;
}
