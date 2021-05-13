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

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 压测报告整体概况
 */
@Data
@TableName(value = "t_whole_report")
public class WholeReportEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 压测任务业务流程ID
     */
    @TableField(value = "pessure_process_id")
    private Long pessureProcessId;

    /**
     * 压测名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 0-通过、1-不通过
     */
    @TableField(value = "pessure_result")
    private Boolean pessureResult;

    /**
     * 压测开始时间
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /**
     * 压测结束时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /**
     * 请求总量
     */
    @TableField(value = "request_total")
    private String requestTotal;

    /**
     * 目标TPS
     */
    @TableField(value = "target_tps")
    private String targetTps;

    /**
     * 实际平均TPS
     */
    @TableField(value = "actual_avg_tps")
    private String actualAvgTps;

    /**
     * 实际最大TPS
     */
    @TableField(value = "actual_max_tps")
    private String actualMaxTps;

    /**
     * 目标RT
     */
    @TableField(value = "target_rt")
    private String targetRt;

    /**
     * 实际平均RT
     */
    @TableField(value = "actual_avg_rt")
    private String actualAvgRt;

    /**
     * 目标请求成功率
     */
    @TableField(value = "target_request_rate")
    private String targetRequestRate;

    /**
     * 实际请求成功率
     */
    @TableField(value = "actual_request_rate")
    private String actualRequestRate;

    /**
     * 峰值时刻
     */
    @TableField(value = "peak_time")
    private LocalDateTime peakTime;

    /**
     * 压测峰值TPS
     */
    @TableField(value = "peak_tps")
    private String peakTps;

    /**
     * 压测峰值请求量
     */
    @TableField(value = "peak_request_total")
    private String peakRequestTotal;

    /**
     * 压测峰值成功率
     */
    @TableField(value = "peak_success_rate")
    private String peakSuccessRate;

    /**
     * 压测峰值rt
     */
    @TableField(value = "peak_rt")
    private String peakRt;

    /**
     * 上次压测报告ID
     */
    @TableField(value = "last_report_id")
    private Long lastReportId;

    /**
     * 上次实际平均TPS
     */
    @TableField(value = "last_avg_tps")
    private String lastAvgTps;

    /**
     * 上次实际最大TPS
     */
    @TableField(value = "last_max_tps")
    private String lastMaxTps;

    /**
     * 上次实际平均R
     */
    @TableField(value = "last_avg_rt")
    private String lastAvgRt;

    /**
     * 上次实际请求成功率
     */
    @TableField(value = "last_request_total")
    private String lastRequestTotal;

    /**
     * 优化策略
     */
    @TableField(value = "optimization_strategy")
    private String optimizationStrategy;

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
