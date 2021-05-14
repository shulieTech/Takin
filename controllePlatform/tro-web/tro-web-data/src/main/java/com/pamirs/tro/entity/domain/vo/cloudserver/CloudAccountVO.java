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

package com.pamirs.tro.entity.domain.vo.cloudserver;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-05-09 21:20
 * @Description:
 */

@Data
@ApiModel(description = "授权账号vo")
public class CloudAccountVO implements Serializable {

    private static final long serialVersionUID = 3474926756007127572L;

    @ApiModelProperty(name = "id", value = "账号id")
    private Long id;

    @ApiModelProperty(name = "platformId", value = "云平台id")
    private Long platformId;

    @ApiModelProperty(name = "platformName", value = "平台名称")
    private String platformName;

    @ApiModelProperty(name = "account", value = "账号")
    private String account;

    @ApiModelProperty(name = "status", value = "是否启用")
    private Boolean status;

    @ApiModelProperty(name = "isDelete", value = "是否删除")
    private Boolean isDelete;

    @ApiModelProperty(name = "authorizeParam", value = "认证参数")
    private String authorizeParam;

    private Date gmtCreate;

    private Date gmtUpdate;

}
