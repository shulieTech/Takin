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

package com.pamirs.tro.entity.domain.vo.entracemanage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2020/7/5 03:15
 * @Description:
 */
@Data
@ApiModel(value = "ApiCreateVo", description = "新增")
public class ApiCreateVo {
    @ApiModelProperty(name = "applicationName", value = "应用名")
    private String applicationName;
    @ApiModelProperty(name = "api", value = "入口地址")
    private String api;
    @ApiModelProperty(name = "method", value = "方法类型")
    private String method;
}
