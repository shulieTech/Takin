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

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
public class ShellScriptManageUpdateRequest extends ShellScriptManageRequest implements Serializable {
    private static final long serialVersionUID = 459472161282660909L;

    /**
     * 脚本实例id
     */
    @JsonProperty("scriptDeployId")
    @ApiModelProperty(name = "scriptDeployId", value = "脚本实例ID")
    private Long scriptDeployId;

    ///**
    // * 脚本实例id
    // */
    //@JsonProperty("oldScriptDeployId")
    //@ApiModelProperty(name = "oldScriptDeployId", value = "原脚本实例ID")
    //private Long oldScriptDeployId;


    /**
     * 脚本版本
     */
    @JsonProperty("scriptVersion")
    @ApiModelProperty(name = "scriptVersion", value = "脚本版本")
    private Integer scriptVersion;



}
