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

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
@ApiModel(value = "BusinessActiveViewListOpenApiResp", description = "业务活动列表出参")
public class BusinessActiveViewListOpenApiResp implements Serializable {
    @ApiModelProperty(name = "businessActiceId", value = "业务活动主键")
    private String businessActiceId;
    @ApiModelProperty(name = "businessActiveName", value = "业务活动名字")
    private String businessActiveName;
    @ApiModelProperty(name = "ischange", value = "是否变更")
    private String ischange;
    @ApiModelProperty(name = "middleWareList", value = "中间件集合")
    private List<String> middleWareList;
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private java.util.Date createTime;
    @ApiModelProperty(name = "candelete", value = "是否可以删除")
    private String candelete;
    @ApiModelProperty(name = "systemProcessName", value = "系统流程名字")
    private String systemProcessName;
    @ApiModelProperty(name = "businessDomain", value = "业务域")
    private String businessDomain;
    @ApiModelProperty(name = "managerName", value = "负责人")
    private String managerName;
    @ApiModelProperty(name = "managerId", value = "负责人id")
    private Long managerId;
    @ApiModelProperty(name = "canEdit", value = "是否可编辑")
    private Boolean canEdit = true;
    @ApiModelProperty(name = "canRemove", value = "是否可删除")
    private Boolean canRemove = true;
}
