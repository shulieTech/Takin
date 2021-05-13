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
 * 角色表
 */
@Data
@TableName(value = "t_tro_role")
public class TroRoleEntity {
    /**
     * 角色id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 应用id
     */
    @TableField(value = "application_id")
    private Long applicationId;

    /**
     * 角色名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 角色别名
     */
    @TableField(value = "alias")
    private String alias;

    /**
     * 角色编码
     */
    @TableField(value = "code")
    private String code;

    /**
     * 角色描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 状态(0:启用 1:禁用)
     */
    @TableField(value = "status")
    private Boolean status;

    /**
     * 扩展字段，k-v形式存在
     */
    @TableField(value = "features")
    private String features;

    /**
     * 租户id
     */
    @TableField(value = "customer_id")
    private Long customerId;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否有效 0:有效;1:无效
     */
    @TableField(value = "is_deleted")
    private Boolean isDeleted;
}
