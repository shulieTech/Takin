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

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ThreadCpuChartResponse
 * @Description 线程cpu图表
 * @Author qianshui
 * @Date 2020/11/4 上午11:14
 */
@Data
@ApiModel("线程分析图表返回值")
public class ThreadCpuChartResponse extends TimeChartResponse {
    private static final long serialVersionUID = 6592376201693158275L;

    @ApiModelProperty(value = "线程数量")
    private Integer threadCount;

    @ApiModelProperty(value = "cpu使用率")
    private BigDecimal cpuRate;

    @ApiModelProperty(value = "基础信息id")
    private String baseId;

    private Long timestamp;
}
