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

package io.shulie.tro.web.app.request.log;

import io.shulie.tro.common.beans.page.PagingDevice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/9/24 3:50 下午
 * @Description:
 */
@Data
@ApiModel(value = "OperationLogQueryRequest", description = "操作日志入参")
public class OperationLogQueryRequest extends PagingDevice {

    private static final long serialVersionUID = 4157696081035578844L;

    @ApiModelProperty(value = "操作开始时间")
    private String startTime;

    @ApiModelProperty(value = "操作结束时间")
    private String endTime;

    @ApiModelProperty(value = "操作人")
    private String userName;
}
