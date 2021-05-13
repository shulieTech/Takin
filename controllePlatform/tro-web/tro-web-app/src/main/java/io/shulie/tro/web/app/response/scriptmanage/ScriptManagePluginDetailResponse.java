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

package io.shulie.tro.web.app.response.scriptmanage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: fanxx
 * @Date: 2021/1/18 5:33 下午
 * @Description:
 */
@Data
public class ScriptManagePluginDetailResponse {

    @ApiModelProperty(name = "pluginInfo", value = "插件详情列表")
    private List<SinglePluginConfigResponse> pluginInfo;
}
