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

package com.pamirs.tro.entity.domain.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: mubai<chengjiacai @ shulie.io>
 * @Date: 2020-04-07 22:58
 * @Description:
 */

@Data
@ApiModel(value = "AppStatusVO", description = "应用异常vo")
public class AppStatusVO implements Serializable {
    private static final long serialVersionUID = 7784138249203384109L;

    @ApiModelProperty(name = "applicationName", value = "项目名称")
    private String applicationName;

    @ApiModelProperty(name = "exceptionInfo", value = "异常信息")
    private String exceptionInfo;

    public AppStatusVO(String applicationName, String exceptionInfo) {
        this.applicationName = applicationName;
        this.exceptionInfo = exceptionInfo;
    }

    public AppStatusVO() {
    }
}
