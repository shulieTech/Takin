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
@TableName(value = "t_report_business_activity_detail")
public class ReportBusinessActivityDetailEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 报告ID
     */
    @TableField(value = "report_id")
    private Long reportId;

    /**
     * 场景ID
     */
    @TableField(value = "scene_id")
    private Long sceneId;

    /**
     * 业务活动ID
     */
    @TableField(value = "business_activity_id")
    private Long businessActivityId;

    /**
     * 业务活动名称
     */
    @TableField(value = "business_activity_name")
    private String businessActivityName;

    /**
     * 应用ID
     */
    @TableField(value = "application_ids")
    private String applicationIds;

    /**
     * 绑定关系
     */
    @TableField(value = "bind_ref")
    private String bindRef;

    /**
     * 请求数
     */
    @TableField(value = "request")
    private Long request;

    /**
     * sa
     */
    @TableField(value = "sa")
    private BigDecimal sa;

    /**
     * 目标sa
     */
    @TableField(value = "target_sa")
    private BigDecimal targetSa;

    /**
     * tps
     */
    @TableField(value = "tps")
    private BigDecimal tps;

    /**
     * 目标tps
     */
    @TableField(value = "target_tps")
    private BigDecimal targetTps;

    /**
     * 响应时间
     */
    @TableField(value = "rt")
    private BigDecimal rt;

    /**
     * 目标rt
     */
    @TableField(value = "target_rt")
    private BigDecimal targetRt;

    /**
     * 分布范围，格式json
     */
    @TableField(value = "rt_distribute")
    private String rtDistribute;

    /**
     * 成功率
     */
    @TableField(value = "success_rate")
    private BigDecimal successRate;

    /**
     * 目标成功率
     */
    @TableField(value = "target_success_rate")
    private BigDecimal targetSuccessRate;

    /**
     * 最大tps
     */
    @TableField(value = "max_tps")
    private BigDecimal maxTps;

    /**
     * 最大响应时间
     */
    @TableField(value = "max_rt")
    private BigDecimal maxRt;

    /**
     * 最小响应时间
     */
    @TableField(value = "min_rt")
    private BigDecimal minRt;

    /**
     * 是否通过
     */
    @TableField(value = "pass_flag")
    private Integer passFlag;

    @TableField(value = "features")
    private String features;

    @TableField(value = "is_deleted")
    private Integer isDeleted;

    @TableField(value = "gmt_create")
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_update")
    private LocalDateTime gmtUpdate;
}
