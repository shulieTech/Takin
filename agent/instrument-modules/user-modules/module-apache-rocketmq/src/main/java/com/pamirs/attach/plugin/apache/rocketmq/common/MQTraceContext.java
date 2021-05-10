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
package com.pamirs.attach.plugin.apache.rocketmq.common;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 消息轨迹上下文
 *
 * @author: wangxian<tangdahu @ pamirs.top>
 * @date:2016/09/29
 */
public class MQTraceContext {
    private MQType mqType;
    private String requestId = UUID.randomUUID().toString().replaceAll("-", "");
    private String group;
    private String errorMsg;
    private boolean success;
    private String status;
    private int resultCode;
    private boolean async;
    private long startTime = System.currentTimeMillis();
    private long costTime;
    private Map<String, String> rpcContextInner;
    private List<MQTraceBean> traceBeans;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public MQType getMqType() {
        return mqType;
    }

    public void setMqType(MQType mqType) {
        this.mqType = mqType;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public Map<String, String> getRpcContextInner() {
        return rpcContextInner;
    }

    public void setRpcContextInner(Map<String, String> rpcContextInner) {
        this.rpcContextInner = rpcContextInner;
    }

    public List<MQTraceBean> getTraceBeans() {
        return traceBeans;
    }

    public void setTraceBeans(List<MQTraceBean> traceBeans) {
        this.traceBeans = traceBeans;
    }
}
