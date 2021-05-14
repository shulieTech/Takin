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
import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/11/29 14:59
 * @Description:
 */
@Data
@ApiModel(value = "TechLinkVo", description = "技术链路入参")
public class TechLinkVo implements Serializable {

    @ApiModelProperty(name = "linkId", value = "系统流程id")
    private String linkId;
    @NotNull
    @ApiModelProperty(name = "entrance", value = "入口")
    private String entrance;
    @NotNull
    @ApiModelProperty(name = "linkName", value = "链路名")
    private String linkName;
    @ApiModelProperty(name = "body", value = "流程消息体")
    private String body;
    //中间件集合
    @ApiModelProperty(name = "middleWareLists", value = "中间件集合")
    private List<MiddleWareEntity> middleWareLists;

}
