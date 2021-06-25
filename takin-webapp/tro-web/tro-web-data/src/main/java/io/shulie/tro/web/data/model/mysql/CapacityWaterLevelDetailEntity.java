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
 * 压测报告容量水位详情
 */
@Data
@TableName(value = "t_capacity_water_level_detail")
public class CapacityWaterLevelDetailEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 压测报告容量水位ID
     */
    @TableField(value = "cwli_id")
    private Long cwliId;

    /**
     * 应用ID
     */
    @TableField(value = "app_id")
    private String appId;

    /**
     * 应用名称
     */
    @TableField(value = "app_name")
    private String appName;

    /**
     * 服务器IP
     */
    @TableField(value = "server_ip")
    private String serverIp;

    /**
     * 是否达标 0-达标、1-不达标
     */
    @TableField(value = "standard")
    private Boolean standard;

    /**
     * CPU利用率
     */
    @TableField(value = "cpu_use_ratio")
    private String cpuUseRatio;

    /**
     * CPU LOAD
     */
    @TableField(value = "cpu_load")
    private String cpuLoad;

    /**
     * 内存利用率
     */
    @TableField(value = "memory_use_ratio")
    private String memoryUseRatio;

    /**
     * 磁盘利用率
     */
    @TableField(value = "disk_use_ratio")
    private String diskUseRatio;

    /**
     * ygc
     */
    @TableField(value = "ygc")
    private String ygc;

    /**
     * fgc
     */
    @TableField(value = "fgc")
    private String fgc;

    /**
     * 缓存读RT
     */
    @TableField(value = "cache_read_rt")
    private String cacheReadRt;

    /**
     * 缓存写RT
     */
    @TableField(value = "cache_write_rt")
    private String cacheWriteRt;

    /**
     * db读RT
     */
    @TableField(value = "db_read_rt")
    private String dbReadRt;

    /**
     * db写RT
     */
    @TableField(value = "db_write_rt")
    private String dbWriteRt;

    /**
     * 远程调用rt
     */
    @TableField(value = "remote_call_rt")
    private String remoteCallRt;

    /**
     * 缓存读TPS
     */
    @TableField(value = "cache_read_tps")
    private String cacheReadTps;

    /**
     * 缓存写TPS
     */
    @TableField(value = "cache_write_tpst")
    private String cacheWriteTpst;

    /**
     * 数据库TPS
     */
    @TableField(value = "db_tps")
    private String dbTps;

    /**
     * 远程调用TPS
     */
    @TableField(value = "remote_call_tps")
    private String remoteCallTps;

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
