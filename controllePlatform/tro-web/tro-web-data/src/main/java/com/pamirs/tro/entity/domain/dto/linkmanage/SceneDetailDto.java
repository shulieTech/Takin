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

import java.util.List;

import com.pamirs.tro.entity.domain.vo.linkmanage.MiddleWareEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/12/12 19:10
 * @Description:系统流程详情出参数
 */
@Data
@ApiModel(value = "sceneDetailDto", description = "业务流程详情出参")

public class SceneDetailDto {
    @ApiModelProperty(name = "businessProcessName", value = "业务流程名字")
    private String businessProcessName;
    @ApiModelProperty(name = "isCode", value = "是否核心流程")
    private String isCode;
    @ApiModelProperty(name = "level", value = "业务流程等级")
    private String level;
    @ApiModelProperty(name = "middleWareEntities", value = "中间件汇总")
    private List<MiddleWareEntity> middleWareEntities;
    @ApiModelProperty(name = "businessLinkDtos", value = "业务活动集合,支持嵌套")
    private List<BusinessLinkDto> businessLinkDtos;
}
