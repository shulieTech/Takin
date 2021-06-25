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

package com.pamirs.tro.entity.domain.dto.settle;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName AccountBalanceDTO
 * @Description
 * @Author qianshui
 * @Date 2020/5/9 下午5:55
 */
@Data
@ApiModel(description = "流量明细")
public class AccountBalanceDTO implements Serializable {

    private static final long serialVersionUID = -5305861767482068856L;

    @ApiModelProperty(value = "时间")
    private String gmtCreate;

    @ApiModelProperty(value = "明细")
    private String flowAmount;

    @ApiModelProperty(value = "余额")
    private String leftAmount;

    @ApiModelProperty(value = "类型")
    private String sceneCode;

    @ApiModelProperty(value = "备注")
    private String remark;
}
