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

@Data
@TableName(value = "t_report_summary")
public class ReportSummaryEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "report_id")
    private Long reportId;

    /**
     * 瓶颈接口
     */
    @TableField(value = "bottleneck_interface_count")
    private Integer bottleneckInterfaceCount;

    /**
     * 风险机器数
     */
    @TableField(value = "risk_machine_count")
    private Integer riskMachineCount;

    /**
     * 业务活动数
     */
    @TableField(value = "business_activity_count")
    private Integer businessActivityCount;

    /**
     * 未达标业务活动数
     */
    @TableField(value = "unachieve_business_activity_count")
    private Integer unachieveBusinessActivityCount;

    /**
     * 应用数
     */
    @TableField(value = "application_count")
    private Integer applicationCount;

    /**
     * 机器数
     */
    @TableField(value = "machine_count")
    private Integer machineCount;

    /**
     * 告警次数
     */
    @TableField(value = "warn_count")
    private Integer warnCount;
}
