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
public class TraceManageQueryListRequest implements Serializable {
    private static final long serialVersionUID = -8735572479422484403L;

    /**
     * 报告id
     */
    @ApiModelProperty(value = "报告id")
    private Long reportId;

    @ApiModelProperty(value = "agentId")
    private String agentId;

    /**
     * 服务器ip
     */
    @ApiModelProperty(value = "服务器ip")
    private String serverIp;

    /**
     * 进程id
     */
    @ApiModelProperty(value = "进程id")
    private Integer processId;
}
