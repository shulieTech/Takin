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
     * tps
     */
    @TableField(value = "tps")
    private BigDecimal tps;

    /**
     * 响应时间
     */
    @TableField(value = "rt")
    private BigDecimal rt;

    /**
     * 成功率
     */
    @TableField(value = "success_rate")
    private BigDecimal successRate;

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

    @TableField(value = "features")
    private String features;

    @TableField(value = "is_deleted")
    private Integer isDeleted;

    @TableField(value = "gmt_create")
    private LocalDateTime gmtCreate;

    @TableField(value = "gmt_update")
    private LocalDateTime gmtUpdate;
}
