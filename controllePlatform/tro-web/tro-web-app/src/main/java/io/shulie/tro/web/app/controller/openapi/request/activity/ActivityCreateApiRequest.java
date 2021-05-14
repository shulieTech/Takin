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

package io.shulie.tro.web.app.controller.openapi.request.activity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2021-01-20
 */
@Data
public class ActivityCreateApiRequest {

    @NotBlank
    @ApiModelProperty("业务活动名称")
    private String activityName;

    @ApiModelProperty("应用名称")
    @NotBlank
    private String applicationName;

    @ApiModelProperty(name = "activityLevel", value = "业务活动等级")
    @NotBlank
    private String activityLevel;

    @ApiModelProperty(name = "isCore", value = "业务活动链路是否核心链路 0:不是;1:是")
    @NotNull
    private Integer isCore;

    @ApiModelProperty(name = "businessDomain", value = "业务域")
    @NotBlank
    private String businessDomain;

    @ApiModelProperty(name = "entranceMethod", value = "接口请求方式")
    private String entranceMethod;

    @NotNull
    private Long userId;

    @NotBlank
    private String entranceName;
}
