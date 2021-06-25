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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/7/6 下午4:26
 * @Description: 链路拓扑图节点关系
 */
@Data
@ApiModel(value = "TopologicalGraphRelation", description = "链路拓扑图节点关系")
public class TopologicalGraphRelation {
    @ApiModelProperty(name = "from", value = "起始端")
    private String from;
    @ApiModelProperty(name = "to", value = "结束端")
    private String to;
}
