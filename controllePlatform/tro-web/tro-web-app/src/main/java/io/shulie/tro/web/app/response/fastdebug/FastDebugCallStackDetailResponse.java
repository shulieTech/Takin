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

package io.shulie.tro.web.app.response.fastdebug;

import java.util.List;

import io.shulie.tro.web.common.vo.fastdebug.FastDebugExceptionVO;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.output.fastdebug
 * @date 2021/1/4 10:49 上午
 */
@Data
public class FastDebugCallStackDetailResponse {
    /**
     * 成功失败
     */
    private Boolean success;

    /**
     * 1是压测流量，0是业务流量
     */
    private Boolean clusterTest;

    /**
     * 服务名
     */
    private String serviceName;
    /**
     * 应用名
     */
    private String appName;

    /**
     * 上游的app名称
     */
    private String upAppName;

    /**
     * 中间件名称
     */
    private String middlewareName;

    /**
     * 耗时
     */
    private Long cost;

    /**
     * rpcid
     */
    private String rpcId;

    /**
     * 请求内容
     */
    private String request;

    /**
     * traceId
     */
    private String traceId;

    /**
     * 响应内容
     */
    private String response;

    /**
     * 附加信息
     */
    private String callbackMsg;

    /**
     * agent id
     */
    private String agentId;

    /**
     * 结果编码
     */
    private String resultCode;

    /**
     * 异常
     */
    private List<FastDebugExceptionVO> exception;

}
