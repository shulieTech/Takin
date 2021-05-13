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
 * 影子表配置表
 */
@Data
@TableName(value = "t_shadow_table_config")
public class ShadowTableConfigEntity {
    /**
     * 影子表配置id
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 影子表所属数据源
     */
    @TableField(value = "SHADOW_DATASOURCE_ID")
    private Long shadowDatasourceId;

    /**
     * 需要使用影子表的表名
     */
    @TableField(value = "SHADOW_TABLE_NAME")
    private String shadowTableName;

    /**
     * 该表 有哪些操作
     */
    @TableField(value = "SHADOW_TABLE_OPERATION")
    private String shadowTableOperation;

    /**
     * 是否使用 影子表 1 使用 0 不使用
     */
    @TableField(value = "ENABLE_STATUS")
    private Integer enableStatus;

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
