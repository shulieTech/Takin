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

import com.pamirs.tro.entity.domain.PagingDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Auther: vernon
 * @Date: 2020/7/3 21:24
 * @Description:
 */
@Data
@ApiModel(value = "EntranceApiVo", description = "入口api入参")

public class EntranceApiVo extends PagingDevice {
    @ApiModelProperty(name = "applicationName", value = "应用名")
    private String applicationName;
    @ApiModelProperty(name = "api", value = "入口地址")
    private String api;
}
