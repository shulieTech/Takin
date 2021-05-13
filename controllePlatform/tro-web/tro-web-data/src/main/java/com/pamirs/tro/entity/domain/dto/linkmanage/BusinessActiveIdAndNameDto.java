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
 * @Date: 2019/12/26 17:03
 * @Description:
 */
@Data
@ApiModel(value = "BusinessActiveIdAndNameDto", description = "业务活动款名字和id")
public class BusinessActiveIdAndNameDto {

    @ApiModelProperty(name = "id", value = "业务活动id")
    private String id;
    @ApiModelProperty(name = "businessActiveName", value = "业务活动名字")
    private String businessActiveName;
}
