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

package io.shulie.tro.web.app.request.perfomanceanaly;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: mubai
 * @Date: 2020-11-17 09:53
 * @Description:
 */

@Data
@ApiModel(value = "压力机修改入参莫模型")
public class PressureMachineUpdateRequest implements Serializable {
    private static final long serialVersionUID = 1921691612428565110L;

    @ApiModelProperty(value = "id")
    private Long id ;
    /**
     * 标签
     */
    @ApiModelProperty(value = "标签")
    private String flag;

    /**
     * 网络带宽总大小
     * 通过前端编辑
     */
    @ApiModelProperty(value = "网络带宽总代小")
    private Long transmittedTotal;


}
