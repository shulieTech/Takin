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
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_report")
public class ReportEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户id
     */
    @TableField(value = "custom_id")
    private Long customId;

    /**
     * 流量消耗
     */
    @TableField(value = "amount")
    private BigDecimal amount;

    /**
     * 场景ID
     */
    @TableField(value = "scene_id")
    private Long sceneId;

    /**
     * 场景名称
     */
    @TableField(value = "scene_name")
    private String sceneName;

    /**
     * 开始时间
     */
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 报表生成状态:0/就绪状态，1/生成中,2/完成生成
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 报告类型；0普通场景，1流量调试
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 压测结论: 0/不通过，1/通过
     */
    @TableField(value = "conclusion")
    private Integer conclusion;

    /**
     * 请求总数
     */
    @TableField(value = "total_request")
    private Long totalRequest;

    /**
     * 施压类型,0:并发,1:tps,2:自定义;不填默认为0
     */
    @TableField(value = "pressure_type")
    private Integer pressureType;

    /**
     * 平均并发数
     */
    @TableField(value = "avg_concurrent")
    private BigDecimal avgConcurrent;

    /**
     * 目标TPS
     */
    @TableField(value = "tps")
    private Integer tps;


    /**
     * 平均tps
     */
    @TableField(value = "avg_tps")
    private BigDecimal avgTps;

    /**
     * 平均响应时间
     */
    @TableField(value = "avg_rt")
    private BigDecimal avgRt;

    /**
     * 最大并发
     */
    @TableField(value = "concurrent")
    private Integer concurrent;

    /**
     * 成功率
     */
    @TableField(value = "success_rate")
    private BigDecimal successRate;

    /**
     * sa
     */
    @TableField(value = "sa")
    private BigDecimal sa;

    /**
     * 操作用户ID
     */
    @TableField(value = "operate_id")
    private Long operateId;

    /**
     * 扩展字段，JSON数据格式
     */
    @TableField(value = "features")
    private String features;

    /**
     * 是否删除:0/正常，1、已删除
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    @TableField(value = "gmt_create")
    private Date gmtCreate;

    @TableField(value = "gmt_update")
    private Date gmtUpdate;

    @TableField(value = "dept_id")
    private Long deptId;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "script_id")
    private Long scriptId;

    /**
     * 锁报告
     */
    @TableField(value = "`lock`")
    private Integer lock;
}
