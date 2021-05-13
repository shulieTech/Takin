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

package io.shulie.tro.web.app.response.linkmanage;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/10/16 5:13 下午
 * @Description:
 */
@Data
@ApiModel(value = "BusinessLinkResponse", description = "业务链路出参")
public class BusinessLinkResponse {

    @ApiModelProperty(name = "id", value = "业务活动id")
    private String id;
    @ApiModelProperty(name = "linkName", value = "业务活动名字")
    private String linkName;
    @ApiModelProperty(name = "entrance", value = "入口")
    private String entrance;
    @ApiModelProperty(name = "ischange", value = "是否变更")
    private String ischange;
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private java.util.Date createTime;
    @ApiModelProperty(name = "updateTime", value = "修改时间")
    private java.util.Date updateTime;
    @ApiModelProperty(name = "techLinkDto", value = "关联的技术链路")
    @JsonProperty("techLinkDto")
    private TechLinkResponse techLinkResponse;
    @ApiModelProperty(name = "candelete", value = "是否可以删除")
    private String candelete;
    @ApiModelProperty(name = "isCore", value = "是否否核心链路 0:不是;1:是'")
    private String isCore;
    @ApiModelProperty(name = "linkLevel", value = "链路等级")
    private String linkLevel;
    @ApiModelProperty(name = "businessDomain", value = "业务域")
    private String businessDomain;
    @ApiModelProperty(name = "child", value = "子业务活动,针对于业务流程详情的出参")
    private BusinessLinkResponse child;

}
