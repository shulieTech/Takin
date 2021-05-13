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

package com.pamirs.tro.entity.domain.query.whitelist;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "WhiteListOperateVO", description = "（批量）加入/取消白名单接口入参")
public class WhiteListOperateVO {

    @ApiModelProperty(name = "applicationId", value = "应用ID")
    @NotNull
    private Long applicationId;

    @ApiModelProperty(name = "ids", value = "白名单ID列表")
    @NotEmpty
    private List<String> ids;

    @ApiModelProperty(name = "type", value = "操作类型:移出白名单:0 加入白名单:1")
    private Integer type;

}
