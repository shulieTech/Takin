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

package com.pamirs.tro.entity.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-06-29 11:50
 * @Description:
 */

@Data
@ApiModel(value = "quickAccessVo", description = "快捷入口")
public class QuickAccessVo {

    @ApiModelProperty(name = "id", value = "主键")
    private Long id;

    @ApiModelProperty(name = "customId", value = "客户id")
    private Long customId;

    @ApiModelProperty(name = "quickName", value = "入口名称")
    private String quickName;

    @ApiModelProperty(name = "quickLogo", value = "logo地址")
    private String quickLogo;

    @ApiModelProperty(name = "urlAddress", value = "接口地址")
    private String urlAddress;

    @ApiModelProperty(name = "order", value = "顺序")
    private Integer order;

}
