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

package io.shulie.tro.web.app.response.application;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-12-29
 */
@Data
@ApiModel("服务入口链路详情返回对象")
public class ApplicationEntranceDetailResponse1 {

    @ApiModelProperty("应用名称")
    private String applicationName;

    @ApiModelProperty("是否是入口应用")
    private Boolean rootApplication;

    @ApiModelProperty("实例节点名称")
    private List<ApplicationEntranceDetailInstanceResponse1> instances;

    @ApiModelProperty("提供的服务")
    private List<ApplicationEntranceDetailProvidersResponse1> providerServices;

    @ApiModelProperty("调用的服务")
    private List<ApplicationEntranceDetailCallResponse1> callServices;

    @ApiModelProperty("调用的下游节点")
    private List<ApplicationEntranceDetailDownStreamResponse1> downStreams;

    @Data
    @ApiModel("应用实例对象")
    public static class ApplicationEntranceDetailInstanceResponse1 {

        @ApiModelProperty("ip")
        private String ip;

        @ApiModelProperty("pid")
        private String pid;

        @ApiModelProperty("agentId")
        private String agentId;
    }

    @Data
    @ApiModel("提供的服务")
    public static class ApplicationEntranceDetailProvidersResponse1 {

        @ApiModelProperty("服务类型")
        private String serviceType;

        @ApiModelProperty("拓展信息")
        private List<String> extendInfo;

    }

    @Data
    @ApiModel("调用的服务")
    public static class ApplicationEntranceDetailCallResponse1 {

        @ApiModelProperty("服务类型")
        private String serviceType;

        private List<ApplicationEntranceDetailCallItemResponse1> items;

    }

    @Data
    @ApiModel("调用服务详情")
    public static class ApplicationEntranceDetailCallItemResponse1 {

        private String title;

        private List<String> info;
    }

    @Data
    @ApiModel("下游服务")
    public static class ApplicationEntranceDetailDownStreamResponse1 {

        @ApiModelProperty("应用名称")
        private String applicationName;

    }
}
