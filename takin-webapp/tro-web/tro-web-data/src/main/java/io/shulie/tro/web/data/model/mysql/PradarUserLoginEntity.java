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
 * 用户登录表
 */
@Data
@TableName(value = "pradar_user_login")
public class PradarUserLoginEntity {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "password")
    private String password;

    @TableField(value = "dept")
    private String dept;

    /**
     * 1：测试， 2：开发， 3：运维， 4：管理
     */
    @TableField(value = "user_type")
    private Boolean userType;

    @TableField(value = "is_deleted")
    private Boolean isDeleted;

    @TableField(value = "gmt_created")
    private LocalDateTime gmtCreated;

    @TableField(value = "gmt_modified")
    private LocalDateTime gmtModified;
}
