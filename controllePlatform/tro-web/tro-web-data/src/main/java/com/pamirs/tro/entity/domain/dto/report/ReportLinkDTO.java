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
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName ReportLinkDTO
 * @Description 报告链路汇总
 * @Author qianshui
 * @Date 2020/8/17 下午7:32
 */
@Data
@ApiModel
public class ReportLinkDTO implements Serializable {

    private static final long serialVersionUID = 5417689631079772178L;
    @ApiModelProperty(value = "子Links")
    List<ReportLinkDTO> children;
    @ApiModelProperty(value = "服务")
    private String serviceName;
    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "应用")
    private String applicationName;
    @ApiModelProperty(value = "请求数")
    private Integer requestCount;
    @ApiModelProperty(value = "请求比")
    private BigDecimal requestRate;
    @ApiModelProperty(value = "tps")
    private Integer tps;
    @ApiModelProperty(value = "成功率")
    private BigDecimal successRate;
    @ApiModelProperty(value = "最大RT")
    private BigDecimal maxRt;
    @ApiModelProperty(value = "最小RT")
    private BigDecimal minRt;
    @ApiModelProperty(value = "平均RT")
    private BigDecimal avgRt;
    @ApiModelProperty(value = "traceId")
    private String traceId;
    @ApiModelProperty(value = "瓶颈标识")
    private Integer bottleneckFlag;
    @ApiModelProperty(value = "偏移量")
    private Integer offset;
}
