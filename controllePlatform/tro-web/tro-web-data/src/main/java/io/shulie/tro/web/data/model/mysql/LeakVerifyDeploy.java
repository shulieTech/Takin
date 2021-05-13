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

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
    * 漏数实例表
    */
@Data
@TableName(value = "t_leak_verify_deploy")
public class LeakVerifyDeploy {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 漏数记录id
     */
    @TableField(value = "leak_verify_id")
    private Long leakVerifyId;

    /**
     * 应用名
     */
    @TableField(value = "application_name")
    private String applicationName;

    /**
     * 链路入口名称
     */
    @TableField(value = "entry_name")
    private String entryName;

    /**
     * 压测请求数量
     */
    @TableField(value = "pressure_request_count")
    private Long pressureRequestCount;

    /**
     * 压测漏数数量
     */
    @TableField(value = "pressure_leak_count")
    private Long pressureLeakCount;

    /**
     * 业务请求数量
     */
    @TableField(value = "biz_request_count")
    private Long bizRequestCount;

    /**
     * 业务漏数数量
     */
    @TableField(value = "biz_leak_count")
    private Long bizLeakCount;

    @TableField(value = "gmt_create")
    private Date gmtCreate;

    @TableField(value = "gmt_update")
    private Date gmtUpdate;

    @TableField(value = "is_deleted")
    private Boolean isDeleted;
}
