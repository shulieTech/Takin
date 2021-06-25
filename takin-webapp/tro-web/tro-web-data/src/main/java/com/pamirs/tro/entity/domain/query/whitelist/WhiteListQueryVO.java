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

import javax.validation.constraints.NotNull;

import com.pamirs.tro.entity.domain.PagingDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ApiModel(value = "WhiteListQueryVO", description = "白名单列表接口入参")
public class WhiteListQueryVO extends PagingDevice {

    @ApiModelProperty(name = "applicationId", value = "应用ID")
    @NotNull
    private Long applicationId;

    @ApiModelProperty(name = "interfaceType", value = "接口类型")
    private Integer interfaceType;

    @ApiModelProperty(name = "interfaceName", value = "接口名称")
    private String interfaceName;

    @ApiModelProperty(name = "useYn", value = "是否已加入")
    private Integer useYn;

}
