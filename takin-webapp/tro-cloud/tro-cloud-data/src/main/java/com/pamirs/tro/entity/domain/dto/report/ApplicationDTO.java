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

package com.pamirs.tro.entity.domain.dto.report;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ApplicationDTO
 * @Description 应用对象
 * @Author qianshui
 * @Date 2020/7/22 下午3:14
 */

@ApiModel
@Data
public class ApplicationDTO implements Serializable {

    private static final long serialVersionUID = 2965776774948266201L;

    @ApiModelProperty(value = "应用")
    private String applicationName;

    @ApiModelProperty(value = "风险机器")
    private Integer riskCount;

    @ApiModelProperty(value = "总机器")
    private Integer totalCount;

}
