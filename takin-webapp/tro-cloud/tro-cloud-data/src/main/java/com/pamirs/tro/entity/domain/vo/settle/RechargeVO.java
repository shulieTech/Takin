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

package com.pamirs.tro.entity.domain.vo.settle;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RechargeVO
 * @Description
 * @Author qianshui
 * @Date 2020/5/9 下午4:47
 */
@Data
@ApiModel(description = "充值流量")
public class RechargeVO implements Serializable {

    private static final long serialVersionUID = -2495969329977126310L;

    @NotNull
    @ApiModelProperty(value = "客户ID")
    private Long customId;

    @NotNull
    @ApiModelProperty(value = "充值现金")
    private BigDecimal cashAmount;

    @NotNull
    @ApiModelProperty(value = "客户流量")
    private BigDecimal flowAmount;
}
