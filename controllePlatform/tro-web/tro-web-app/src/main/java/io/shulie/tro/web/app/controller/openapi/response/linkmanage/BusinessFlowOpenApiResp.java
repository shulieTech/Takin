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

package io.shulie.tro.web.app.controller.openapi.response.linkmanage;

import java.io.Serializable;
import java.util.List;

import com.pamirs.tro.entity.domain.dto.linkmanage.ExistBusinessActiveDto;
import com.pamirs.tro.entity.domain.vo.linkmanage.BusinessFlowTree;
import com.pamirs.tro.entity.domain.vo.linkmanage.MiddleWareEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
@ApiModel(value = "BusinessFlowOpenApiResp", description = "业务流程详情出参(最新")
public class BusinessFlowOpenApiResp implements Serializable {
    @ApiModelProperty(name = "id", value = "主键")
    private String id;
    @ApiModelProperty(name = "businessProcessName", value = "业务流程名字")
    private String businessProcessName;
    @ApiModelProperty(name = "isCode", value = "是否核心流程")
    private String isCode;
    @ApiModelProperty(name = "level", value = "业务流程等级")
    private String level;
    @ApiModelProperty(name = "middleWareEntities", value = "中间件汇总")
    private List<MiddleWareEntity> middleWareEntities;
    @ApiModelProperty(name = "roots", value = "业务流程节点树")
    private List<BusinessFlowTree> roots;
    @ApiModelProperty(name = "existBusinessActive", value = "已经存在的业务活动对象集合")
    private List<ExistBusinessActiveDto> existBusinessActive;
}
