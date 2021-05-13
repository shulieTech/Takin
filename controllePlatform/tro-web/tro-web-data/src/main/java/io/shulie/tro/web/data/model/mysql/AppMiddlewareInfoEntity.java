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
 * 应用中间件列表信息
 */
@Data
@TableName(value = "t_app_middleware_info")
public class AppMiddlewareInfoEntity {
    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 应用ID
     */
    @TableField(value = "APPLICATION_ID")
    private Long applicationId;

    /**
     * jar名称
     */
    @TableField(value = "JAR_NAME")
    private String jarName;

    /**
     * Pradar插件名称
     */
    @TableField(value = "PLUGIN_NAME")
    private String pluginName;

    /**
     * Jar类型
     */
    @TableField(value = "JAR_TYPE")
    private String jarType;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 是否增强成功 0:有效;1:未生效
     */
    @TableField(value = "ACTIVE")
    private Boolean active;

    /**
     * 是否隐藏 0:隐藏;1:不隐藏
     */
    @TableField(value = "HIDDEN")
    private Boolean hidden;

    /**
     * 是否有效 0:有效;1:无效
     */
    @TableField(value = "IS_DELETED")
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
