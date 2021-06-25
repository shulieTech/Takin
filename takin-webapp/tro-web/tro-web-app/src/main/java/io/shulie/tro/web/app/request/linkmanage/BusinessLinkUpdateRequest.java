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

package io.shulie.tro.web.app.request.linkmanage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/11/30 16:11
 * @Description:
 */
@Data
@ApiModel(value = "BusinessLinkUpdateRequest", description = "业务活动入参")
public class BusinessLinkUpdateRequest {

    @ApiModelProperty(name = "linkid", value = "业务活动id")
    private String id;

    @ApiModelProperty(name = "linkName", value = "业务活动名字")
    private String linkName;

    @ApiModelProperty(name = "link_level", value = "业务活动等级")
    private String link_level;

    @ApiModelProperty(name = "parent_business_id", value = "业务活动的上级业务活动")
    private String parent_business_id;

    @ApiModelProperty(name = "isCore", value = "业务活动链路是否核心链路 0:不是;1:是")
    private Integer isCore;

    @ApiModelProperty(name = "businessDomain", value = "业务域")
    private String businessDomain;

    @ApiModelProperty(name = "relatedTechLinkId", value = "关联的系统流程id")
    private String relatedTechLinkId;
}
