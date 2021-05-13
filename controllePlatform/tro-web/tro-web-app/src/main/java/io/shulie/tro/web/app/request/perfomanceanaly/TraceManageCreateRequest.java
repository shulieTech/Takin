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

package io.shulie.tro.web.app.request.perfomanceanaly;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author shulie
 */
@Data
public class TraceManageCreateRequest implements Serializable {
    private static final long serialVersionUID = 390682552754253293L;

    /**
     * 追踪实例id，如果是第一次，则没有，如果是某个方法的子方法，则有
     */
    @ApiModelProperty(value = "追踪实例id")
    private Long traceManageDeployId;

    /**
     * 追踪对象
     */
    @ApiModelProperty(value = "追踪对象")
    @NotNull
    private String traceObject;

    /**
     * 报告id
     */
    @NotNull
    @ApiModelProperty(value = "报告id")
    private Long reportId;

    /**
     * 进程名称
     */
    @NotNull
    @ApiModelProperty(value = "进程名称")
    private String processName;




}
