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

package io.shulie.tro.web.data.result.tracenode;

import java.util.Objects;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.result.tracenode
 * @date 2021/1/5 2:15 下午
 */
@Data
public class TraceNodeInfoResult {
    private Long id;

    /**
     * 应用名
     */
    private String appName;

    /**
     * traceId
     */
    private String traceId;

    /**
     * rpcid
     */
    private String rpcId;
    /**
     * logType
     */
    private Integer logType;

    /**
     * 入口的app名称
     */
    private String traceAppName;

    /**
     * 上游的app名称
     */
    private String upAppName;

    /**
     * 开始时间的时间戳
     */
    private Long startTime;

    /**
     * 耗时
     */
    private Long cost;

    /**
     * 中间件名称
     */
    private String middlewareName;

    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 远程IP
     */
    private String remoteIp;

    /**
     * 结果编码
     */
    private String resultCode;

    /**
     * 请求内容
     */
    private String request;

    /**
     * 响应内容
     */
    private String response;

    /**
     * 1是压测流量，0是业务流量
     */
    private Boolean clusterTest;

    /**
     * 附加信息
     */
    private String  callbackMsg;

    /**
     * 日志所属机器ip
     */
    private String hostIp;

    /**
     * agent id
     */
    private String agentId;

    /**
     * 顾客id
     */
    private String customId;

    /**
     * rpc类型
     */
    private Integer rpcType;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 是否下游有未知节点
     */
    private Boolean isUpperUnknownNode;

    @Override
    public String toString() {
        return  appName + '|' + agentId + '|' +  rpcId  + '|' + logType + '|' +  rpcType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        TraceNodeInfoResult result = (TraceNodeInfoResult)o;
        return appName.equals(result.appName) && traceId.equals(result.traceId) && rpcId.equals(result.rpcId) && logType
            .equals(result.logType) && agentId.equals(result.agentId) && rpcType.equals(result.rpcType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appName, traceId, rpcId, logType, agentId, rpcType);
    }
}
