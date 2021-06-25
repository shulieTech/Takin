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

package io.shulie.tro.web.app.request.scriptmanage.shell;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.request.scriptmanage.shell
 * @date 2020/12/14 12:49 下午
 */
@Data
public class ShellScriptManageRequest {
    /**
     * 名称
     */
    @JsonProperty("scriptName")
    @ApiModelProperty(name = "scriptName", value = "脚本名")
    private String name;

    /**
     * 描述
     */
    @JsonProperty("description")
    @ApiModelProperty(name = "description", value = "脚本描述")
    private String description;

    /**
     * shell脚本内容
     */
    @JsonProperty("content")
    @ApiModelProperty(name = "content", value = "脚本内容")
    private String content;
}
