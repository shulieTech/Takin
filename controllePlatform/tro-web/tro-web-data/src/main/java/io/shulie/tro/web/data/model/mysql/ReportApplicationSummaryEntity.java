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
@TableName(value = "t_report_application_summary")
public class ReportApplicationSummaryEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "report_id")
    private Long reportId;

    @TableField(value = "application_name")
    private String applicationName;

    /**
     * 总机器数
     */
    @TableField(value = "machine_total_count")
    private Integer machineTotalCount;

    /**
     * 风险机器数
     */
    @TableField(value = "machine_risk_count")
    private Integer machineRiskCount;
}
