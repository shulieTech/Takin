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

package io.shulie.tro.cloud.biz.output.engine;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 引擎插件信息出参
 *
 * @author lipeng
 * @date 2021-01-20 4:56 下午
 */
@Data
@ApiModel("引擎插件文件信息出参")
public class EnginePluginSimpleInfoOutput implements Serializable {

    @ApiModelProperty(value = "插件ID", dataType = "long")
    private Long pluginId;

    @ApiModelProperty(value = "插件名称", dataType = "string")
    private String pluginName;

    @ApiModelProperty(value = "插件类型", dataType = "string")
    private String pluginType;

    @ApiModelProperty(value = "更新时间", dataType = "string")
    private String gmtUpdate;

}
