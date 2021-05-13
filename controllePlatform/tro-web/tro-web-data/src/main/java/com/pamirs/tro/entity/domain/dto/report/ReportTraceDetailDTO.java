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
 * @ClassName ReportCountDTO
 * @Description 压测报告；统计返回
 * @Author qianshui
 * @Date 2020/7/22 下午2:19
 */
@ApiModel
@Data
public class ReportTraceDetailDTO implements Serializable {

    private static final long serialVersionUID = 8928035842416997931L;

    @ApiModelProperty(value = "主键")
    private Integer id;

    @ApiModelProperty(value = "入口名称")
    private String interfaceName;

    @ApiModelProperty(value = "应用名称")
    private String applicationName;

    @ApiModelProperty(value = "状态")
    private Boolean succeeded;

    @ApiModelProperty(value = "请求参数")
    private String params;

    @ApiModelProperty(value = "请求耗时，单位ms")
    private Long costTime;

    @ApiModelProperty(value = "距离最开始的时间节点,单位ms")
    private Long offsetStartTime;

    @ApiModelProperty(value = "下级节点")
    private List<ReportTraceDetailDTO> nextNodes;

    @ApiModelProperty(value = "是否压测流量")
    private Boolean clusterTest;

    private String agentId;

    private String entryHostIp;

    private Integer index;

    private String rpcId;

    private Boolean nodeSuccess;

    /**
     * 节点ip
     */
    private String nodeIp;

    private String response;



}
