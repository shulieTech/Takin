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

package com.pamirs.tro.entity.domain.dto.linkmanage.linkstatistics;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/12/10 19:35
 * @Description: 链路标记页面的统计图出参
 */
@Data
public class LinkHistoryInfoDto {

    @ApiModelProperty(name = "businessFlowTotalCount", value = "业务流程覆盖接入总数")
    private String businessFlowTotalCount;
    @ApiModelProperty(name = "businessFlowPressureCount", value = "业务流程=>压测已覆盖")
    private String businessFlowPressureCount;
    @ApiModelProperty(name = "businessFlowPressureRate", value = "业务流程(压测覆盖率)")
    private String businessFlowPressureRate;

    @ApiModelProperty(name = "applicationTotalCount", value = "应用覆盖情况的接入总数")
    private String applicationTotalCount;
    @ApiModelProperty(name = "applicationPressureCount", value = "应用覆盖情况=>压测已覆盖")
    private String applicationPressureCount;
    @ApiModelProperty(name = "applicationPressureRate", value = "应用覆盖情况(压测覆盖率)")
    private String applicationPressureRate;

    @ApiModelProperty(name = "businessCover", value = "业务覆盖趋势图的数据")
    private List<BusinessCoverDto> businessCover;
    @ApiModelProperty(name = "systemProcess", value = "  系统流程节点接入趋势图的数据")
    private List<SystemProcessDto> systemProcess;
    @ApiModelProperty(name = "applicationRemote", value = "应用接入趋势")
    private List<ApplicationRemoteDto> applicationRemote;
}
