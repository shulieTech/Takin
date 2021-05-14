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

package io.shulie.tro.web.app.request.fastdebug;

import javax.validation.constraints.NotNull;

import com.pamirs.tro.entity.domain.PagingDevice;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-12-29 09:59
 * @Description:
 */

@Data
public class FastDebugAppLogRequest extends PagingDevice {

    @NotNull(message = "appName 不能为空")
    @ApiModelProperty(value = "应用名称")
    private String appName;

    @ApiModelProperty("agentId")
    @NotNull(message = "agentId 不能为空")
    private String agentId;

    @ApiModelProperty("文件路径")
    @NotNull(message = "filePath 不能为空")
    private String filePath;

    @ApiModelProperty("traceId")
    @NotNull(message = "traceId 不能为空")
    private String traceId;

}
