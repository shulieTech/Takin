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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/12/24 16:15
 * @Description:
 */
@Data
@ApiModel(value = "SystemProcessIdAndNameDto", description = "系统流程名字和id")
public class SystemProcessIdAndNameDto {
    @ApiModelProperty(name = "id", value = "系统流程id")
    private String id;
    @ApiModelProperty(name = "systemProcessName", value = "系统流程名字")
    private String systemProcessName;
}
