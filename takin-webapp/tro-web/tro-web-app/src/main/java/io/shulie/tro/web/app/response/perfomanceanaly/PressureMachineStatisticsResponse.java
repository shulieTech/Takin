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
import java.math.BigDecimal;

/**
 * @Author: mubai
 * @Date: 2020-11-13 17:47
 * @Description:
 */

@Data
@ApiModel(value = "压力机统计模型")
public class PressureMachineStatisticsResponse implements Serializable {

    private static final long serialVersionUID = 8766077804185872988L;
    /**
     * 总数量
     */
    @ApiModelProperty(value = "机器总数")
    private Integer machineTotal;

    /**
     * 压测中数量
     */
    @ApiModelProperty(value = "压测中数量")
    private Integer machinePressured;

    /**
     * 空闲机器数量
     */
    @ApiModelProperty(value = "空闲数量")
    private Integer machineFree;

    /**
     * 离线机器数量
     */
    @ApiModelProperty(value = "离线数量")
    private Integer machineOffline;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private String gmtCreate;

    @ApiModelProperty(value = "空闲比率")
    private BigDecimal freePercent;

    @ApiModelProperty(value = "压测中比率")
    private BigDecimal pressuredPercent;

    @ApiModelProperty(value = "离线比率")
    private BigDecimal offlinePercent;

}
