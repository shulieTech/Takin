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
@TableName(value = "t_tro_authority")
public class TroAuthorityEntity {
    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private String roleId;

    /**
     * 资源id
     */
    @TableField(value = "resource_id")
    private String resourceId;

    /**
     * 操作类型(0:all,1:query,2:create,3:update,4:delete,5:start,6:stop,7:export,8:enable,9:disable,10:auth)
     */
    @TableField(value = "action")
    private String action;

    /**
     * 权限范围
     */
    @TableField(value = "scope")
    private String scope;

    /**
     * 是否启用 0:启用;1:禁用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 对象类型(0:角色 1:用户)
     */
    @TableField(value = "object_type")
    private Boolean objectType;

    /**
     * 对象id:角色,用户
     */
    @TableField(value = "object_id")
    private String objectId;

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
