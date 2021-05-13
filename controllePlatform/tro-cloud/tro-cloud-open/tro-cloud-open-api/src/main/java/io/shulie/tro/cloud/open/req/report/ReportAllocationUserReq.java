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

package io.shulie.tro.cloud.open.req.report;

import io.shulie.tro.cloud.open.req.HttpCloudRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 压测报表分配负责人
 */
@Data
public class ReportAllocationUserReq  extends HttpCloudRequest implements Serializable {

    private static final long serialVersionUID = 5944661718697091740L;


    @NotNull
    @ApiModelProperty(value = "主键ID")
    private Long dataId;

    @NotNull
    @ApiModelProperty(value = "用户ID")
    private Long userId;
}
