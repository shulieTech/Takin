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
 * prada获取http接口表
 */
@Data
@TableName(value = "t_prada_http_data")
public class PradaHttpDataEntity {
    /**
     * prada获取http接口表id
     */
    @TableId(value = "TPHD_ID", type = IdType.INPUT)
    private Long tphdId;

    /**
     * 应用名称
     */
    @TableField(value = "APP_NAME")
    private String appName;

    /**
     * 接口名称
     */
    @TableField(value = "INTERFACE_NAME")
    private String interfaceName;

    /**
     * 接口类型(1.http 2.dubbo 3.禁止压测 4.job)
     */
    @TableField(value = "INTERFACE_TYPE")
    private Integer interfaceType;

    /**
     * 同步数据时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;
}
