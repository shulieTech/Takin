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

package com.pamirs.tro.entity.domain.dto.linkmanage;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "TechLinkDto.UnKnowNode", description = "技术链路出参-未知节点")
@Data
public class UnKnowNode implements Serializable {
    @ApiModelProperty(name = "rpcUrl", value = "请求地址")
    String rpcUrl;
    @ApiModelProperty(name = "rpcType", value = "请求类型")
    Integer rpcType;
    @ApiModelProperty(name = "rpcTypeTitle", value = "请求类型标题")
    String rpcTypeTitle;
    @ApiModelProperty(name = "rpcRequestParam", value = "请求参数")
    String rpcRequestParam;
    @ApiModelProperty(name = "rpcSrc", value = "请求来源")
    String rpcSrc;
}
