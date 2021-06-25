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

package com.pamirs.tro.entity.domain.dto.linkmanage;

import java.util.Set;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/7/6 下午4:09
 * @Description: 链路拓扑图节点
 */
@Data
@ApiModel(value = "TopologicalGraphNode", description = "链路拓扑图节点")
public class TopologicalGraphNode {
    @ApiModelProperty(name = "key", value = "节点key")
    private String key;
    @ApiModelProperty(name = "nodeName", value = "节点名称")
    private String nodeName;
    @ApiModelProperty(name = "nodeType", value = "节点类型")
    private String nodeType;
    @ApiModelProperty(name = "nodeClass", value = "节点分类")
    private String nodeClass;
    @ApiModelProperty(name = "middlewareType", value = "中间件类型")
    private String middlewareType;
    @ApiModelProperty(name = "middlewareName", value = "中间件名称")
    private String middlewareName;
    @ApiModelProperty(name = "unKnowNodeList", value = "未知节点列表")
    private Set<String> unKnowNodeList;
    @ApiModelProperty(name = "nodeList", value = "已知节点列表")
    private Set<String> nodeList;
    @ApiModelProperty(name = "opData", value = "操作的数据")
    private Set<String> dataList;
    private String serviceName;
}
