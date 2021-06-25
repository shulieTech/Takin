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
 * 网络隔离配置表
 */
@Data
@TableName(value = "t_network_isolate_config")
public class NetworkIsolateConfigEntity {
    /**
     * 主键id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 应用名称
     */
    @TableField(value = "APP_NAME")
    private String appName;

    /**
     * VIP地址
     */
    @TableField(value = "VIP_ADDR")
    private String vipAddr;

    /**
     * 负载均衡类型
     */
    @TableField(value = "BALANCE_TYPE")
    private String balanceType;

    /**
     * 地址池名称，多个用逗号分隔
     */
    @TableField(value = "POOL_NAME")
    private String poolName;

    /**
     * 隔离状态(0:未隔离，1:预隔离，2:隔离)
     */
    @TableField(value = "ISOLATED_STATUS")
    private Integer isolatedStatus;

    /**
     * 是否有效: Y:有效 N:无效
     */
    @TableField(value = "ACTIVE")
    private String active;

    /**
     * 隔离出错信息
     */
    @TableField(value = "FAILED_DETAIL")
    private String failedDetail;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 变更时间
     */
    @TableField(value = "UPDATE_TIME")
    private LocalDateTime updateTime;

    /**
     * 隔离成功IP，多个用逗号分隔
     */
    @TableField(value = "ISOLATED_IP")
    private String isolatedIp;
}
