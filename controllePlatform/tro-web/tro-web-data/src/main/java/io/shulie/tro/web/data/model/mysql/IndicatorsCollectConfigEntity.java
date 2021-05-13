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
 * 指标采集配置
 */
@Data
@TableName(value = "t_indicators_collect_config")
public class IndicatorsCollectConfigEntity {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 压测引擎名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 关联字典表dictionary_id
     */
    @TableField(value = "type")
    private String type;

    /**
     * 采集周期
     */
    @TableField(value = "collect_time")
    private Integer collectTime;

    /**
     * 指标抓去插件
     */
    @TableField(value = "plugin_path")
    private String pluginPath;

    /**
     * 启动类名称
     */
    @TableField(value = "class_name")
    private String className;

    /**
     * jar名称
     */
    @TableField(value = "jar_name")
    private String jarName;

    /**
     * 0-可用 1-不可用
     */
    @TableField(value = "status")
    private Boolean status;

    /**
     * 是否删除 0-未删除、1-已删除
     */
    @TableField(value = "is_deleted")
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
