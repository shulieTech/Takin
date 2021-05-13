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

package com.pamirs.tro.entity.domain.vo.linkmanage.queryparam;

import java.io.Serializable;

import com.pamirs.tro.entity.domain.PagingDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2019/12/2 10:39
 * @Description:
 */
@Data
@ApiModel(value = "BusinessQueryVo", description = "业务链路页面查询vo")
public class BusinessQueryVo extends PagingDevice implements Serializable {
    private static final long serialVersionUID = 4891597070201989044L;

    @ApiModelProperty(name = "businessLinkName", value = "业务链路名字")
    private String businessLinkName;
    @ApiModelProperty(name = "techLinkName", value = "技术链路名字")
    private String techLinkName;
    @ApiModelProperty(name = "entrance", value = "链路入口")
    private String entrance;
    @ApiModelProperty(name = "ischange", value = "是否变更")
    private String ischange;
    @ApiModelProperty(name = "domain", value = "业务域")
    private String domain;
    @ApiModelProperty(name = "middleWareType", value = "中间件类型")
    private String middleWareType;
    @ApiModelProperty(name = "middleWareName", value = "中间件名称")
    private String middleWareName;
    @ApiModelProperty(name = "version", value = "中间件版本号")
    private String version;
   /* @ApiModelProperty(name = "relatedLinkId", value = "业务链路关联的技术链路id")
    private String relatedLinkId;*/
}
