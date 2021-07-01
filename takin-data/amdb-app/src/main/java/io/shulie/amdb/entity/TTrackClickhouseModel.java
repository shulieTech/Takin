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

package io.shulie.amdb.entity;

import com.alibaba.fastjson.JSONObject;
import com.pamirs.pradar.log.parser.trace.RpcBased;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.Map;

public class TTrackClickhouseModel {
    private String appName;
    private String traceId;
    private String entranceNodeId;
    private String entranceId;
    private int level;
    private int parentIndex;
    private int index;
    private String rpcId;
    private int rpcType = 0;
    private int logType = 0;
    private String traceAppName;
    private String upAppName;
    private long startTime;
    private long cost;
    private String middlewareName;
    private String serviceName;
    private String methodName;
    private String remoteIp;
    private String port;
    private String resultCode;
    private long requestSize;
    private long responseSize;
    private String request;
    private String response;
    private boolean clusterTest;
    private String callbackMsg;
    private int samplingInterval = 1;
    private String localId;
    private String attributes;
    private String localAttributes;
    private boolean async;
    private long timeMin;
    private String entranceServiceType;
    long dateToMin;
    String parsedServiceName;
    String parsedMethod;
    String parsedAppName;
    String parsedExtend;
    String log;
    String version;
    String hostIp;
    String agentId;

    public TTrackClickhouseModel() {
    }

    public RpcBased getRpcBased() {
        RpcBased rpcBased = new RpcBased();
        rpcBased.setAppName(appName);
        rpcBased.setTraceId(traceId);
        rpcBased.setEntranceNodeId(entranceNodeId);
        rpcBased.setEntranceId(entranceId);
        rpcBased.setLevel(level);
        rpcBased.setParentIndex(parentIndex);
        rpcBased.setIndex(index);
        rpcBased.setRpcId(rpcId);
        rpcBased.setRpcType(rpcType);
        rpcBased.setLogType(logType);
        rpcBased.setTraceAppName(traceAppName);
        rpcBased.setUpAppName(upAppName);
        rpcBased.setStartTime(startTime);
        rpcBased.setCost(cost);
        rpcBased.setMiddlewareName(middlewareName);
        rpcBased.setServiceName(serviceName);
        rpcBased.setMethodName(methodName);
        rpcBased.setRemoteIp(remoteIp);
        rpcBased.setPort(port);
        rpcBased.setResultCode(resultCode);
        rpcBased.setRequestSize(requestSize);
        rpcBased.setResponseSize(responseSize);
        rpcBased.setRequest(request);
        rpcBased.setResponse(response);
        rpcBased.setClusterTest(clusterTest);
        rpcBased.setCallbackMsg(callbackMsg);
        rpcBased.setSamplingInterval(samplingInterval);
        rpcBased.setLocalId(localId);
        rpcBased.setAttributes(JSONObject.parseObject(attributes, Map.class));
        rpcBased.setLocalAttributes(JSONObject.parseObject(localAttributes, Map.class));
        rpcBased.setAsync(async);
        rpcBased.setLog(log);
        rpcBased.setVersion(version);
        rpcBased.setHostIp(hostIp);
        rpcBased.setAgentId(agentId);
        return rpcBased;
    }

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

    public String getEntranceNodeId() {
        return entranceNodeId;
    }

    public void setEntranceNodeId(String entranceNodeId) {
        this.entranceNodeId = entranceNodeId;
    }

    public String getEntranceId() {
        return entranceId;
    }

    public void setEntranceId(String entranceId) {
        this.entranceId = entranceId;
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

    public long getTimeMin() {
        return timeMin;
    }

    public void setTimeMin(long timeMin) {
        this.timeMin = timeMin;
    }

    public String getEntranceServiceType() {
        return entranceServiceType;
    }

    public void setEntranceServiceType(String entranceServiceType) {
        this.entranceServiceType = entranceServiceType;
    }

    public long getDateToMin() {
        return dateToMin;
    }

    public void setDateToMin(long dateToMin) {
        this.dateToMin = dateToMin;
    }

    public String getParsedServiceName() {
        return parsedServiceName;
    }

    public void setParsedServiceName(String parsedServiceName) {
        this.parsedServiceName = parsedServiceName;
    }

    public String getParsedMethod() {
        return parsedMethod;
    }

    public void setParsedMethod(String parsedMethod) {
        this.parsedMethod = parsedMethod;
    }

    public String getParsedAppName() {
        return parsedAppName;
    }

    public void setParsedAppName(String parsedAppName) {
        this.parsedAppName = parsedAppName;
    }

    public String getParsedExtend() {
        return parsedExtend;
    }

    public void setParsedExtend(String parsedExtend) {
        this.parsedExtend = parsedExtend;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
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
