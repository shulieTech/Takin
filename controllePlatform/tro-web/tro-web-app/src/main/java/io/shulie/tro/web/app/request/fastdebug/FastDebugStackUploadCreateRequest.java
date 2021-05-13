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

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-12-28 10:49
 * @Description:
 */

@Data
public class FastDebugStackUploadCreateRequest implements Serializable {
    private static final long serialVersionUID = -1942935482204906443L;

    @ApiModelProperty("cmdId")
    private String cmdId;

    @ApiModelProperty("traceId")
    private String traceId;

    @ApiModelProperty("rpcId")
    private String rpcId;

    @ApiModelProperty("agentId")
    private String agentId;

    @ApiModelProperty("应用名称")
    private String appName;

    /**
     * 0: 客户端 ； 1：服务端
     */
    @ApiModelProperty("日志类型")
    private Integer logType;

    /**
     * 日志信息
     */
    private FastDebugStackInfoCreateRequest log;

    /**
     * 机器信息
     */
    private List<FastDebugMachinePerformanceCreateRequest> machineInfo;

    /**
     * 当前rpc是否结束
     */
    private Boolean isRpcEnd;

}
