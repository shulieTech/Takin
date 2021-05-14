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

/**
 * @author 何仲奇
 * @Package io.shulie.tro.web.data.model.mysql
 * @date 2020/12/28 11:59 上午
 */
@Data
@TableName(value = "t_fast_debug_machine_performance")
public class FastDebugMachinePerformanceEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * traceId
     */
    @TableField(value = "trace_id")
    private String traceId;

    /**
     * rpcid
     */
    @TableField(value = "rpc_id")
    private String rpcId;

    /**
     * 服务端、客户端
     */
    @TableField(value = "log_type")
    private Integer logType;


    /**
     * 性能类型，beforeFirst/beforeLast/afterFirst/afterLast/exceptionFirst/exceptionLast
     */
    @TableField(value = "`index`")
    private String index;

    /**
     * cpu利用率
     */
    @TableField(value = "cpu_usage")
    private BigDecimal cpuUsage;

    /**
     * cpu load
     */
    @TableField(value = "cpu_load")
    private BigDecimal cpuLoad;

    /**
     * 没存利用率
     */
    @TableField(value = "memory_usage")
    private BigDecimal memoryUsage;

    /**
     * 堆内存总和
     */
    @TableField(value = "memory_total")
    private BigDecimal memoryTotal;

    /**
     * io 等待率
     */
    @TableField(value = "io_wait")
    private BigDecimal ioWait;

    /**
     * younggc 次数
     */
    @TableField(value = "young_gc_count")
    private Long youngGcCount;

    /**
     * young gc 耗时
     */
    @TableField(value = "young_gc_time")
    private Long youngGcTime;

    /**
     * old gc 次数
     */
    @TableField(value = "old_gc_count")
    private Long oldGcCount;

    /**
     * old gc 耗时
     */
    @TableField(value = "old_gc_time")
    private Long oldGcTime;

    /**
     * 状态 0: 正常 1： 删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @TableField(value = "gmt_update")
    private Date gmtUpdate;
}