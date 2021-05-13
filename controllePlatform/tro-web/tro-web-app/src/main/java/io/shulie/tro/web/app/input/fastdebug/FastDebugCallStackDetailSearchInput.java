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

package io.shulie.tro.web.app.input.fastdebug;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.request.fastdebug
 * @date 2020/12/28 11:19 上午
 */
@Data
public class FastDebugCallStackDetailSearchInput {
    private Long resultId;

    private String traceId;

    private String rpcId;

    private String appName;

    private String agentId;

    private Integer logType;

    /**
     * 节点类型，丢失节点用到
     */
    private Integer type;

}
