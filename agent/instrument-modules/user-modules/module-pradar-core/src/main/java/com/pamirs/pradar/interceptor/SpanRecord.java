/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pamirs.pradar.interceptor;

import com.pamirs.pradar.ResultCode;

import java.io.Serializable;

/**
 * Created by xiaobin on 2017/2/6.
 */
public class SpanRecord implements Serializable {
    private final static long serialVersionUID = 1L;
    public final static Object CLEAN_CONTEXT = new Object();
    /**
     * 请求大小
     */
    private long requestSize;
    /**
     * 响应大小
     */
    private long responseSize;
    /**
     * 请求体
     */
    private Object request;
    /**
     * 响应体
     */
    private Object response;
    /**
     * 服务名称
     */
    private String service;
    /**
     * 方法名称
     */
    private String method;

    /**
     * 响应结果码
     */
    private String resultCode = ResultCode.INVOKE_RESULT_SUCCESS;

    /**
     * 上下文
     */
    private Object context;

    /**
     * 远程服务IP
     */
    private String remoteIp;

    /**
     * 远程服务端口
     */
    private String port = "";

    /**
     * 上一个应用名称
     */
    private String upAppName;

    /**
     * 中间件名称
     */
    private String middlewareName;

    /**
     * 方法返回结果
     */
    @Deprecated
    private Object result;

    /**
     * 附加信息
     */
    private String callbackMsg;

    /**
     * 是否是压测请求
     */
    private boolean clusterTest;

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

    public String getUpAppName() {
        return upAppName;
    }

    public void setUpAppName(String upAppName) {
        this.upAppName = upAppName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }

    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getMiddlewareName() {
        return middlewareName;
    }

    public void setMiddlewareName(String middlewareName) {
        this.middlewareName = middlewareName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setPort(int port) {
        this.port = String.valueOf(port);
    }

    @Deprecated
    public Object getResult() {
        return result;
    }

    @Deprecated
    public void setResult(Object result) {
        this.result = result;
    }
}
