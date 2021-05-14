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
 * 压测引擎配置
 */
@Data
@TableName(value = "t_pressure_test_engine_config")
public class PressureTestEngineConfigEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 压测引擎名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 压测引擎类型 PTS，SPT，JMETER
     */
    @TableField(value = "type")
    private String type;

    /**
     * access_key
     */
    @TableField(value = "access_key")
    private String accessKey;

    /**
     * secret_key
     */
    @TableField(value = "secret_key")
    private String secretKey;

    /**
     * 地域ID
     */
    @TableField(value = "region_id")
    private String regionId;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 0-可用 1-不可用
     */
    @TableField(value = "status")
    private Boolean status;

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
