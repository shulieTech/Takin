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

package com.pamirs.tro.entity.domain.vo.linkmanage;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2020/1/3 22:54
 * @Description:
 */
@Data
@ApiModel(value = "BusinessFlowVo", description = "业务流程入参树")
public class BusinessFlowVo {
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    @ApiModelProperty(name = "sceneName", value = "业务流程名字")
    private String sceneName;
    @ApiModelProperty(name = "isCore", value = "是否核心")
    private String isCore;
    @ApiModelProperty(name = "sceneLevel", value = "场景等级")
    private String sceneLevel;
    @ApiModelProperty(name = "root", value = "根节点")
    private List<BusinessFlowTree> root;
}
