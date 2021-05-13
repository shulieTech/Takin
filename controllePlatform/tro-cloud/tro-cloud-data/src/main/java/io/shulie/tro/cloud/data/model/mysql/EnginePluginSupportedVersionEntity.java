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

/**
 * 引擎插件支持版本信息
 *
 * @author lipeng
 * @date 2021-01-12 4:33 下午
 */
@Data
@TableName(value = "t_engine_plugin_supported_versions")
public class EnginePluginSupportedVersionEntity {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 插件ID */
    @TableField(value = "plugin_id")
    private Long pluginId;

    /** 支持的版本号 */
    @TableField(value = "supported_version")
    private String supportedVersion;

}