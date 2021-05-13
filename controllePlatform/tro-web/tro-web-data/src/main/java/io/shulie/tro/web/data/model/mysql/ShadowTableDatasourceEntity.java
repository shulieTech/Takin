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
 * 影子表数据源配置
 */
@Data
@TableName(value = "t_shadow_table_datasource")
public class ShadowTableDatasourceEntity {
    /**
     * 影子表数据源id
     */
    @TableId(value = "SHADOW_DATASOURCE_ID", type = IdType.AUTO)
    private Long shadowDatasourceId;

    /**
     * 关联app_mn主键id
     */
    @TableField(value = "APPLICATION_ID")
    private Long applicationId;

    /**
     * 数据库ip端口  xx.xx.xx.xx:xx
     */
    @TableField(value = "DATABASE_IPPORT")
    private String databaseIpport;

    /**
     * 数据库表明
     */
    @TableField(value = "DATABASE_NAME")
    private String databaseName;

    /**
     * 是否使用 影子表 1 使用 0 不使用
     */
    @TableField(value = "USE_SHADOW_TABLE")
    private Integer useShadowTable;

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
