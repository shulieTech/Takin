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

package io.shulie.tro.cloud.data.model.mysql;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 调度记录引擎插件引用表
 *
 * @author lipeng
 * @date 2021-01-13 11:24 上午
 */
@Data
@TableName(value = "t_schedule_record_engine_plugins_ref")
public class ScheduleRecordEnginePluginRefEntity {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 调度记录id
     *
     */
    @TableField(value = "schedule_record_id")
    private Long scheduleRecordId;

    /**
     * 引擎插件存放目录
     *
     */
    @TableField(value = "engine_plugin_file_path")
    private String enginePluginFilePath;

    /**
     * 创建时间
     *
     */
    @TableField(value = "gmt_create")
    private LocalDateTime gmtCreate;
}
