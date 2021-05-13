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

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2020/1/9 14:24
 * @Description:
 */
@ApiModel(value = "MiddleWareNameDto", description = "中间件名字")
@Data
public class MiddleWareNameDto {

    @ApiModelProperty(name = "label", value = "中间件名字")
    private String label;
    @ApiModelProperty(name = "value", value = "中间件名字")
    private String value;
    @ApiModelProperty(name = "children", value = "下属的中间版本")
    private List<MiddleWareVersionDto> children;
}
