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
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RiskApplicationCountDTO
 * @Description 风险机器 应用 数量 汇总
 * @Author qianshui
 * @Date 2020/7/22 下午2:42
 */
@ApiModel
@Data
public class RiskApplicationCountDTO implements Serializable {

    private static final long serialVersionUID = 4060772976299354894L;

    /**
     * 应用数量
     */
    @ApiModelProperty(value = "应用数量")
    private Integer applicationCount;

    /**
     * 机器总数
     */
    @ApiModelProperty(value = "机器总数")
    private Integer machineCount;

    /**
     * 应用列表
     */
    @ApiModelProperty(value = "应用列表")
    private List<ApplicationDTO> applicationList;
}
