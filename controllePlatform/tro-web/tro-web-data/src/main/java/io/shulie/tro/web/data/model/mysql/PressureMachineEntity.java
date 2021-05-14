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
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "t_pressure_machine")
public class PressureMachineEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 压力机名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 压力机IP
     */
    @TableField(value = "ip")
    private String ip;

    /**
     * 标签
     */
    @TableField(value = "flag")
    private String flag;

    /**
     * cpu核数
     */
    @TableField(value = "cpu")
    private Integer cpu;

    /**
     * 内存，单位字节
     */
    @TableField(value = "memory")
    private Long memory;


    @TableField(value = "machine_usage")
    private BigDecimal machineUsage;
    /**
     * 磁盘，单位字节
     */
    @TableField(value = "disk")
    private Long disk;

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
     * 内存利用率
     */
    @TableField(value = "memory_used")
    private BigDecimal memoryUsed;

    /**
     * 磁盘 IO 等待率
     */
    @TableField(value = "disk_io_wait")
    private BigDecimal diskIoWait;

    /**
     * 网络带宽总大小
     */
    @TableField(value = "transmitted_total")
    private Long transmittedTotal;
    /**
     * 网络带宽入大小
     */
    @TableField(value = "transmitted_in")
    private Long transmittedIn;

    /**
     * 网络带宽入利用率
     */
    @TableField(value = "transmitted_in_usage")
    private BigDecimal transmittedInUsage;

    /**
     * 网络带宽出大小
     */
    @TableField(value = "transmitted_out")
    private Long transmittedOut;

    /**
     * 网络带宽出利用率
     */
    @TableField(value = "transmitted_out_usage")
    private BigDecimal transmittedOutUsage;

    /**
     * 网络带宽利用率
     */
    @TableField(value = "transmitted_usage")
    private BigDecimal transmittedUsage;

    /**
     * 压测场景id
     */
    @TableField(value = "scene_names")
    private String sceneNames;

    /**
     * 状态 0：空闲 ；1：压测中  -1:离线
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 状态 0: 正常 1： 删除
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private String gmtCreate;

    /**
     * 修改时间
     */
    @TableField(value = "gmt_update")
    private String gmtUpdate;
}
