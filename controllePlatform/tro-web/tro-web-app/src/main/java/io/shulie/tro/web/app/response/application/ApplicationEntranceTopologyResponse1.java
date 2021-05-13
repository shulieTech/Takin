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

import io.shulie.amdb.common.enums.NodeTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-12-29
 */
@Data
@ApiModel("入口服务拓扑图返回值")
public class ApplicationEntranceTopologyResponse1 {

    @ApiModelProperty("节点")
    private List<ApplicationEntranceTopologyNodeResponse1> nodes;

    @ApiModelProperty("边")
    private List<ApplicationEntranceTopologyEdgeResponse1> edges;

    @Data
    @ApiModel("入口服务拓扑图节点返回值")
    public static class ApplicationEntranceTopologyNodeResponse1 {

        @ApiModelProperty("节点id")
        private String id;

        @ApiModelProperty("节点显示")
        private String label;

        @ApiModelProperty("是否根节点")
        private Boolean root;

        @ApiModelProperty("节点类型")
        private NodeTypeEnum type;

        @ApiModelProperty("拓展信息")
        private String title;

    }

    @Data
    @ApiModel("入口服务拓扑图边返回值")
    public static class ApplicationEntranceTopologyEdgeResponse1 {

        @ApiModelProperty("源节点ID")
        private String source;

        @ApiModelProperty("目标节点ID")
        private String target;

        @ApiModelProperty("节点ID")
        private String label;

        @ApiModelProperty("边访问类型")
        private String type;

        @ApiModelProperty("拓展信息")
        private List<String> extendInfo;
    }

}
