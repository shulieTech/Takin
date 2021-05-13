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

/**
 * 压测报告业务活动
 */
@Data
@TableName(value = "t_business_activities")
public class BusinessActivitiesEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 压测报告整体概况ID
     */
    @TableField(value = "whole_report_id")
    private Long wholeReportId;

    /**
     * 业务流程ID
     */
    @TableField(value = "pessure_process_id")
    private Long pessureProcessId;

    /**
     * 业务活动名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * tps
     */
    @TableField(value = "tps")
    private String tps;

    /**
     * 平均rt
     */
    @TableField(value = "avg_rt")
    private String avgRt;

    /**
     * 请求成功率
     */
    @TableField(value = "success_rate")
    private String successRate;

    /**
     * 压测峰值时刻
     */
    @TableField(value = "peak_time")
    private String peakTime;

    /**
     * 压测峰值TPS
     */
    @TableField(value = "peak_tps")
    private String peakTps;

    /**
     * 压测峰值rt
     */
    @TableField(value = "peak_avg_rt")
    private String peakAvgRt;

    /**
     * 压测峰值请求量
     */
    @TableField(value = "peak_request")
    private String peakRequest;

    /**
     * 压测峰值请求成功率
     */
    @TableField(value = "peak_request_rate")
    private String peakRequestRate;

    /**
     * 是否删除 0-未删除、1-已删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME")
    private LocalDateTime updateTime;
}
