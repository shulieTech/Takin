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
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_trace_manage_deploy")
public class TraceManageDeployEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 追踪对象id
     */
    @TableField(value = "trace_manage_id")
    private Long traceManageId;

    /**
     * 追踪对象实例名称
     */
    @TableField(value = "trace_deploy_object")
    private String traceDeployObject;

    /**
     * 追踪凭证
     */
    @TableField(value = "sample_id")
    private String sampleId;

    /**
     * 级别
     */
    @TableField(value = "level")
    private Integer level;

    /**
     * 父id
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 0:没有;1:有;2未知
     */
    @TableField(value = "has_children")
    private Integer hasChildren;

    /**
     * 行号
     */
    @TableField(value = "line_num")
    private Integer lineNum;

    /**
     * 平均耗时
     */
    @TableField(value = "avg_cost")
    private BigDecimal avgCost;
    /**
     * 中位数
     */
    @TableField(value = "p50")
    private BigDecimal p50;
    @TableField(value = "p90")
    private BigDecimal p90;
    @TableField(value = "p95")
    private BigDecimal p95;
    @TableField(value = "p99")
    private BigDecimal p99;
    @TableField(value = "min")
    private BigDecimal min;
    @TableField(value = "max")
    private BigDecimal max;
    /**
     * 状态0:待采集;1:采集中;2:采集结束
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 拓展字段
     */
    @TableField(value = "feature")
    private String feature;
}
