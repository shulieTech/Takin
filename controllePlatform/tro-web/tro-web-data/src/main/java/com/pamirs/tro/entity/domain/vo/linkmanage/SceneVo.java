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

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/11/29 15:28
 * @Description:场景入参
 */
@Data
@ApiModel(value = "SceneVo", description = "业务流程入参")
public class SceneVo implements Serializable {
    //当前需要新增的节点对象
    @ApiModelProperty(name = "currentVo", value = " 当前需要新增的节点对象")
    EntranceVo currentVo;
    //上一级对象业务活动id
    @ApiModelProperty(name = "parentBusinessId", value = " 需要靠挂的业务活动节点id或者已知的同级的业务活动Id")
    String parentBusinessId;
    //同一级节点或者子节点
    @ApiModelProperty(name = "ischild", value = "针对于parentBusinessId，是parentBusinessId的同级节点或者孩子节点")
    boolean ischild;
    @ApiModelProperty(name = "id", value = "业务流程id")
    private Long id;
    @ApiModelProperty(name = "sceneName", value = "业务流程名字")
    private String sceneName;
    @ApiModelProperty(name = "isCore", value = "业务活动类型 0:核心,1:非核心")
    private Integer isCore;
    @ApiModelProperty(name = "sceneLevel", value = "业务活动级别")
    private String sceneLevel;
}
