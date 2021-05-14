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

import java.io.Serializable;

/**
 * @author zhaoyong
 */
@Data
public class TraceManageDeployQueryRequest implements Serializable {
    private static final long serialVersionUID = 3858196416002264786L;

    /**
     * 传入traceManageId 查询所有信息
     */
    @ApiModelProperty(value = "传入traceManageId 查询所有信息")
    private Long id;

    /**
     * 追踪凭证id
     */
    @ApiModelProperty(value = "追踪凭证id")
    private String sampleId;
}
