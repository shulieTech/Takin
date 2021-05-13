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

package io.shulie.tro.web.app.request.activity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.shulie.tro.web.amdb.bean.common.EntranceTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-12-29
 */
@Data
@ApiModel("创建业务活动")
public class ActivityCreateRequest {

    @NotBlank
    @ApiModelProperty("业务活动名称")
    private String activityName;

    @NotBlank
    @ApiModelProperty("应用名称")
    private String applicationName;

    @NotNull
    @ApiModelProperty("入口类型")
    private EntranceTypeEnum type;

    @ApiModelProperty("链路id")
    private String linkId;

    @ApiModelProperty(name = "link_level", value = "业务活动等级")
    @NotBlank
    @JsonProperty("link_level")
    private String activityLevel;

    @ApiModelProperty(name = "isCore", value = "业务活动链路是否核心链路 0:不是;1:是")
    @NotNull
    private Integer isCore;

    @ApiModelProperty(name = "businessDomain", value = "业务域")
    @NotBlank
    private String businessDomain;

    private String method;

    private String rpcType;

    private String extend;

    private String serviceName;

    /**
     * 入口服务传参
     */
    @NotBlank
    private String entranceName;

}
