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

package io.shulie.tro.web.app.output.scriptmanage.shell;

import java.io.Serializable;

import io.swagger.annotations.ApiParam;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
public class ShellScriptManageContentOutput implements Serializable {
    private static final long serialVersionUID = -3681184638943613401L;

    @ApiParam(value = "脚本内容")
    private String content;

    @ApiParam(value = "脚本版本")
    private Integer ScriptVersion;

    @ApiParam(value = "脚本实例id")
    private Long scriptManageDeployId;

    @ApiParam(value = "描述")
    private String description;
}
