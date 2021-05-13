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

package io.shulie.tro.web.app.request.scriptmanage;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2021/1/18 5:17 下午
 * @Description:
 */
@Data
public class SupportJmeterPluginNameRequest {
    /**
     * 关联类型(业务活动)
     */
    @ApiModelProperty("关联类型")
    @JsonProperty("relatedType")
    private String refType;
    @NotNull
    private String relatedType;

    /**
     * 关联值(活动id)
     */
    @ApiModelProperty("关联值")
    @JsonProperty("relatedId")
    private String refValue;
    @NotNull
    private String relatedId;
}
