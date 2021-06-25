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
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName BottleneckInterfaceDTO
 * @Description 瓶颈接口DTO
 * @Author qianshui
 * @Date 2020/7/22 下午2:31
 */
@ApiModel
@Data
public class BottleneckInterfaceDTO implements Serializable {

    private static final long serialVersionUID = -7218988438361002963L;

    /**
     * 排名
     */
    @ApiModelProperty(value = "排名")
    private Integer rank;

    /**
     * 应用
     */
    @ApiModelProperty(value = "应用")
    private String applicationName;

    /**
     * 接口
     */
    @ApiModelProperty(value = "接口")
    private String interfaceName;

    /**
     * tps
     */
    @ApiModelProperty(value = "tps")
    private BigDecimal tps;

    /**
     * rt
     */
    @ApiModelProperty(value = "rt")
    private BigDecimal rt;

    /**
     * 成功率
     */
    @ApiModelProperty(value = "成功率")
    private BigDecimal successRate;

}
