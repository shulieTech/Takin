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

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/7/6 下午4:05
 * @Description: 链路拓扑图
 */
@Data
@ApiModel(value = "TopologicalGraphVo", description = "链路拓扑图节点关系")
public class TopologicalGraphVo {
    @ApiModelProperty(name = "graphNodes", value = "节点信息列表")
    private List<TopologicalGraphNode> graphNodes = new ArrayList<>();
    @ApiModelProperty(name = "graphRelations", value = "节点关系列表")
    private List<TopologicalGraphRelation> graphRelations = new ArrayList<>();
}
