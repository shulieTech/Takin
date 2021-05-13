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
 * 基础链路服务关联表
 */
@Data
@TableName(value = "t_link_service_mnt")
public class LinkServiceMntEntity {
    /**
     * 主键id
     */
    @TableId(value = "LINK_SERVICE_ID", type = IdType.INPUT)
    private Long linkServiceId;

    /**
     * 链路id
     */
    @TableField(value = "LINK_ID")
    private Long linkId;

    /**
     * 接口名称
     */
    @TableField(value = "INTERFACE_NAME")
    private String interfaceName;

    /**
     * 接口说明
     */
    @TableField(value = "INTERFACE_DESC")
    private String interfaceDesc;

    /**
     * 插入时间
     */
    @TableField(value = "CREATE_TIME")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME")
    private LocalDateTime updateTime;
}
