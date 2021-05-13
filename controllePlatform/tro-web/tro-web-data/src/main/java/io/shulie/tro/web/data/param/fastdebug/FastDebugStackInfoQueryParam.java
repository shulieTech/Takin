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

package io.shulie.tro.web.data.param.fastdebug;

import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-12-31 10:48
 * @Description:
 */

@Data
public class FastDebugStackInfoQueryParam {

    private String logLevel;

    private String traceId;

    private String rpcId;

    private Integer type;

    private String agentId;

    private String appName;

    private Boolean isStack;


}
