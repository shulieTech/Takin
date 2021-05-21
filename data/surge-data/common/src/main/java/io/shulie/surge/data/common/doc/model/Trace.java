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

package io.shulie.surge.data.common.doc.model;

import io.shulie.surge.data.common.doc.annotation.Document;
import io.shulie.surge.data.common.doc.annotation.Field;
import io.shulie.surge.data.common.doc.annotation.FieldType;
import io.shulie.surge.data.common.doc.annotation.Id;

import java.io.Serializable;

/**
 * @Author: xingchen
 * @ClassName: Trace
 * @Package: io.shulie.surge.data.common.elastic.model
 * @Date: 2020/11/2616:06
 * @Description:
 */
@Document(indexName = "t_trace")
public class Trace implements Serializable {
    /**
     * 当前应用名称
     */
    @Field(type = FieldType.Keyword)
    private String appName;

    /**
     * trace id
     */
    @Id
    private String traceId;

    /**
     * 层级
     */
    private int level;

    /**
     * 父序号
     */
    private int parentIndex;

    /**
     * 序号
     */
    private int index;

    /**
     * rpcId
     */
    private String rpcId;

    /**
     * rpc类型
     */
    private int rpcType = 0;

    /**
     * 日志类型
     */
    private int logType = 0;

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
    private long startTime;

    /**
     * 耗时
     */
    private long cost;

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
     * 端口
     */
    private String port;

    /**
     * 结果编码
     */
    private String resultCode;

    /**
     * 请求大小
     */
    private long requestSize;

    /**
     * 响应大小
     */
    private long responseSize;

    /**
     * 请求内容
     */
    private String request;

    /**
     * 响应内容
     */
    private String response;

    /**
     * 是否是压测流量
     */
    private boolean clusterTest;

    /**
     * 附加信息，如sql
     */
    private String callbackMsg;

    /**
     * 采样值
     */
    private int samplingInterval = 1;

    /**
     * 本地方法追踪时的ID
     */
    private String localId;

    /**
     * 通过rpc之间传递的attributes
     */
    private String attributes;

    /**
     * 本地的attributes，不通过rpc之前传递
     */
    private String localAttributes;

    /**
     * 是否是异步
     */
    private boolean async;

    /**
     * 日志版本号
     */
    protected String version;

    /**
     * 日志所属机器ip
     */
    protected String hostIp;

    /**
     * agent id
     */
    protected String agentId;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getParentIndex() {
        return parentIndex;
    }

    public void setParentIndex(int parentIndex) {
        this.parentIndex = parentIndex;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getRpcId() {
        return rpcId;
    }

    public void setRpcId(String rpcId) {
        this.rpcId = rpcId;
    }

    public int getRpcType() {
        return rpcType;
    }

    public void setRpcType(int rpcType) {
        this.rpcType = rpcType;
    }

    public int getLogType() {
        return logType;
    }

    public void setLogType(int logType) {
        this.logType = logType;
    }

    public String getTraceAppName() {
        return traceAppName;
    }

    public void setTraceAppName(String traceAppName) {
        this.traceAppName = traceAppName;
    }

    public String getUpAppName() {
        return upAppName;
    }

    public void setUpAppName(String upAppName) {
        this.upAppName = upAppName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getMiddlewareName() {
        return middlewareName;
    }

    public void setMiddlewareName(String middlewareName) {
        this.middlewareName = middlewareName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public long getRequestSize() {
        return requestSize;
    }

    public void setRequestSize(long requestSize) {
        this.requestSize = requestSize;
    }

    public long getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(long responseSize) {
        this.responseSize = responseSize;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isClusterTest() {
        return clusterTest;
    }

    public void setClusterTest(boolean clusterTest) {
        this.clusterTest = clusterTest;
    }

    public String getCallbackMsg() {
        return callbackMsg;
    }

    public void setCallbackMsg(String callbackMsg) {
        this.callbackMsg = callbackMsg;
    }

    public int getSamplingInterval() {
        return samplingInterval;
    }

    public void setSamplingInterval(int samplingInterval) {
        this.samplingInterval = samplingInterval;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getLocalAttributes() {
        return localAttributes;
    }

    public void setLocalAttributes(String localAttributes) {
        this.localAttributes = localAttributes;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}
