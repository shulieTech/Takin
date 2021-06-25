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

@Data
@TableName(value = "t_quick_access")
public class QuickAccessEntity {
    /**
     * 数据库唯一主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户id
     */
    @TableField(value = "custom_id")
    private Long customId;

    /**
     * 快捷键名称
     */
    @TableField(value = "quick_name")
    private String quickName;

    /**
     * 快捷键logo
     */
    @TableField(value = "quick_logo")
    private String quickLogo;

    /**
     * 实际地址
     */
    @TableField(value = "url_address")
    private String urlAddress;

    /**
     * 顺序
     */
    @TableField(value = "order")
    private Integer order;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 是否删除 0:未删除;1:已删除
     */
    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 0:未启用；1:启用
     */
    @TableField(value = "is_enable")
    private Integer isEnable;
}
