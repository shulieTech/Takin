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

package com.pamirs.tro.entity.domain.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.pamirs.tro.entity.domain.vo.linkmanage.MiddleWareEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 应用明细
 * @author: CaoYanFei@ShuLie.io
 * @create: 2020-07-19 23:07
 **/
@Data
@ApiModel(value = "applicationDetailDto", description = "应用明细出参")
@Deprecated
public class ApplicationDetailDto implements Serializable {
    @ApiModelProperty(name = "middleWare", value = "中间件列表")
    List<MiddleWareEntity> middleWare;
    @ApiModelProperty(name = "applicationName", value = "应用名称")
    private String applicationName;
    @ApiModelProperty(name = "rpcType", value = "类型")
    private Integer rpcType;
    @ApiModelProperty(name = "rpcTypeTitle", value = "类型名称")
    private String rpcTypeTitle;
    @ApiModelProperty(name = "rpcData", value = "数据")
    private String rpcData;
    @ApiModelProperty(name = "nodes", value = "已知节点列表")
    private Set<String> nodes;
    @ApiModelProperty(name = "unKnowNodes", value = "未知节点列表")
    private Set<String> unKnowNodes;
}
