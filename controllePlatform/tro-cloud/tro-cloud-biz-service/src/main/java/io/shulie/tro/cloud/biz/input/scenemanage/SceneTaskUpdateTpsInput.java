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

package io.shulie.tro.cloud.biz.input.scenemanage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author zhaoyong
 */
@Data
public class SceneTaskUpdateTpsInput implements Serializable {
    private static final long serialVersionUID = -5189025661201526286L;

    @NotNull
    @ApiModelProperty(value = "sceneId")
    private Long sceneId;

    @NotNull
    @ApiModelProperty(value = "reportId")
    private Long reportId;

    @NotNull
    @ApiModelProperty(value = "customerId")
    private Long customerId;

    @NotNull
    @ApiModelProperty(value = "tpsNum")
    private Long tpsNum;
}
