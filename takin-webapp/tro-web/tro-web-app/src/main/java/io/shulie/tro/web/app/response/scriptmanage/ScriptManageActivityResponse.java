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

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaoyong
 * 业务活动对象
 */
@Data
public class ScriptManageActivityResponse implements Serializable {
    private static final long serialVersionUID = 3865160039625320519L;

    @ApiModelProperty(name = "id", value = "业务活动id")
    private String id;

    @ApiModelProperty(name = "name", value = "业务活动名字")
    @JsonProperty("name")
    private String businessActiveName;

    @JsonProperty("scriptList")
    private List<ScriptManageDeployActivityResponse> scriptManageDeployResponses;
}
