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

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "t_application_ds_manage")
public class ApplicationDsManageEntity {
    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 应用主键
     */
    @TableField(value = "APPLICATION_ID")
    private Long applicationId;

    /**
     * 应用名称
     */
    @TableField(value = "APPLICATION_NAME")
    private String applicationName;

    /**
     * 存储类型 0:数据库 1:缓存
     */
    @TableField(value = "DB_TYPE")
    private Integer dbType;

    /**
     * 方案类型 0:影子库 1:影子表 2:影子server
     */
    @TableField(value = "DS_TYPE")
    private Integer dsType;

    /**
     * 数据库url,影子表需填
     */
    @TableField(value = "URL")
    private String url;

    /**
     * xml配置
     */
    @TableField(value = "CONFIG")
    private String config;

    /**
     * 解析后配置
     */
    @TableField(value = "PARSE_CONFIG")
    private String parseConfig;

    /**
     * 状态 0:启用；1:禁用
     */
    @TableField(value = "STATUS")
    private Integer status;

    /**
     * 租户id
     */
    private Long customerId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME")
    private Date updateTime;

    /**
     * 是否有效 0:有效;1:无效
     */
    @TableLogic
    @TableField(value = "IS_DELETED")
    private Integer isDeleted;
}
