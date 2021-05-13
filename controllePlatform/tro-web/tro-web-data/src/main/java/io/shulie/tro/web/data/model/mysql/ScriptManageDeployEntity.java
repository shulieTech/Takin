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

@Data
@TableName(value = "t_script_manage_deploy")
public class ScriptManageDeployEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "script_id")
    private Long scriptId;

    /**
     * 名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 关联类型(业务活动)
     */
    @TableField(value = "ref_type")
    private String refType;

    /**
     * 关联值(活动id)
     */
    @TableField(value = "ref_value")
    private String refValue;

    /**
     * 脚本类型;0为jmeter脚本
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 0代表新建，1代表调试通过
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 操作人id
     */
    @TableField(value = "create_user_id")
    private Long createUserId;

    /**
     * 操作人名称
     */
    @TableField(value = "create_user_name")
    private String createUserName;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_update")
    private Date gmtUpdate;

    @TableField(value = "script_version")
    private Integer scriptVersion;

    @TableField(value = "is_deleted")
    private Integer isDeleted;

    /**
     * 拓展字段
     */
    @TableField(value = "feature")
    private String feature;

    /**
     * 描述字段
     */
    @TableField(value = "description")
    private String description;
}
