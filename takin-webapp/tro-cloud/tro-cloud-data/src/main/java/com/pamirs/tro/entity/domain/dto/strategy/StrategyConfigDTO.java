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

package com.pamirs.tro.entity.domain.dto.strategy;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName StrategyConfigDTO
 * @Description
 * @Author qianshui
 * @Date 2020/5/9 下午2:06
 */
@Data
@ApiModel(description = "分配策略列表")
public class StrategyConfigDTO implements Serializable {

    private static final long serialVersionUID = -8740307347149572470L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "配置名称")
    private String strategyName;

    @ApiModelProperty(value = "并发数")
    private Integer threadNum;

    @ApiModelProperty(value = "tps数")
    private Integer tpsNum;

    @ApiModelProperty(value = "cpu")
    private BigDecimal cpuNum;

    @ApiModelProperty(value = "内存")
    private BigDecimal memorySize;

    @ApiModelProperty(value = "最后修改时间")
    private String updateTime;

    @ApiModelProperty(value = "发布方式")
    private String deploymentMethod;
}
