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

package io.shulie.tro.web.app.response.perfomanceanaly;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: mubai
 * @Date: 2020-11-16 14:03
 * @Description:
 */
@Data
@ApiModel(value = "type-value-date 前端模型")
public class TypeValueDateVo  implements Serializable {
    private static final long serialVersionUID = 5588552828849352311L;

    @ApiModelProperty(name = "date",value = "时间")
    private String date ;

    @ApiModelProperty(name = "value",value = "value")
    private Object value ;

    @ApiModelProperty(name = "type",value = "type")
    private String type ;
}
