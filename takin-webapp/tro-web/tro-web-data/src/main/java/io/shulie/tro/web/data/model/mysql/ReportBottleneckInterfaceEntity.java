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
@TableName(value = "t_report_bottleneck_interface")
public class ReportBottleneckInterfaceEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "report_id")
    private Long reportId;

    @TableField(value = "application_name")
    private String applicationName;

    @TableField(value = "sort_no")
    private Integer sortNo;

    /**
     * 接口类型
     */
    @TableField(value = "interface_type")
    private String interfaceType;

    @TableField(value = "interface_name")
    private String interfaceName;

    @TableField(value = "tps")
    private BigDecimal tps;

    @TableField(value = "rt")
    private BigDecimal rt;

    @TableField(value = "node_count")
    private Integer nodeCount;

    @TableField(value = "error_reqs")
    private Integer errorReqs;

    @TableField(value = "bottleneck_weight")
    private BigDecimal bottleneckWeight;
}
