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

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_report_machine")
public class ReportMachineEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "report_id")
    private Long reportId;

    /**
     * 应用名称
     */
    @TableField(value = "application_name")
    private String applicationName;

    /**
     * 机器ip
     */
    @TableField(value = "machine_ip")
    private String machineIp;

    /**
     * 机器ip
     */
    @TableField(value = "agent_id")
    private String agentId;

    /**
     * 机器基本信息
     */
    @TableField(value = "machine_base_config")
    private String machineBaseConfig;

    /**
     * 机器tps对应指标信息
     */
    @TableField(value = "machine_tps_target_config")
    private String machineTpsTargetConfig;

    /**
     * 风险计算值
     */
    @TableField(value = "risk_value")
    private BigDecimal riskValue;

    /**
     * 是否风险机器(0-否，1-是)
     */
    @TableField(value = "risk_flag")
    private Integer riskFlag;

    /**
     * 风险提示内容
     */
    @TableField(value = "risk_content")
    private String riskContent;
}
