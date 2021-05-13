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

package com.pamirs.tro.entity.domain.vo.strategy;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName StrategyConfigAddVO
 * @Description
 * @Author qianshui
 * @Date 2020/5/9 下午3:21
 */
@Data
@ApiModel(description = "配置策略信息")
public class StrategyConfigAddVO implements Serializable {

    private static final long serialVersionUID = -7643529208229180591L;

    @NotNull
    @ApiModelProperty(value = "调度策略名称")
    private String strategyName;

    @NotNull
    @ApiModelProperty(value = "调度策略配置")
    private String strategyConfig;
}
