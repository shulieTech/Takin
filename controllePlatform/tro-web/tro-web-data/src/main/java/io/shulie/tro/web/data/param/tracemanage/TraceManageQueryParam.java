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

package io.shulie.tro.web.data.param.tracemanage;

import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
public class TraceManageQueryParam {

    /**
     * 报告id
     */
    private Long reportId;

    private String agentId;

    /**
     * 服务器ip
     */
    private String serverIp;

    /**
     * 进程id
     */
    private Integer processId;
}
