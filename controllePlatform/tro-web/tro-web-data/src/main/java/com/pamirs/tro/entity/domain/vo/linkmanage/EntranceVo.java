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
 * @Date: 2019/11/30 16:56
 * @Description:
 */
@Data
@ApiModel(value = "EntranceApiVo", description = "入口入参")
public class EntranceVo implements Serializable {

    @ApiModelProperty(name = "ischange", value = "单条业务流程是否变更")
    private String ischange;
    @ApiModelProperty(name = "relatedBusinessId", value = "业务活动id")
    private String relatedBusinessId;
    @ApiModelProperty(name = "relateTechId", value = "系统流程id")
    private String relateTechId;
    @ApiModelProperty(name = "入口", value = "系统流程id")
    private String entrance;

    @ApiModelProperty(name = "parentBusinessId", value = "针对业务流程配置的上级业务活动id")
    private String parentBusinessId;

}
