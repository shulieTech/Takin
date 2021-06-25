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

package com.pamirs.tro.entity.domain.vo.dsmanage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Create by xuyh at 2020/3/15 18:17.
 */
@Data
@ApiModel(value = "DsAgentVO", description = "影子库表配置入参")
public class DsAgentVO {
    @ApiModelProperty(name = "applicationName", value = "应用名称")
    private String applicationName;

    @ApiModelProperty(name = "dsType", value = "库表类型，0：影子库 1：影子表")
    private Byte dsType;

    @ApiModelProperty(name = "url", value = "数据库url")
    private String url;

    @ApiModelProperty(name = "status", value = "状态")
    private Byte status;

    @ApiModelProperty(name = "config", value = "影子表配置")
    private String shadowTableConfig;

    @ApiModelProperty(name = "config", value = "影子库配置")
    private Configurations shadowDbConfig;
}
