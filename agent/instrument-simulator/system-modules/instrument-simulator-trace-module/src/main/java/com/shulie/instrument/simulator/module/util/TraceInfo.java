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
package com.shulie.instrument.simulator.module.util;

import java.io.Serializable;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/7 12:02 上午
 */
public class TraceInfo implements Serializable {
    private final static long serialVersionUID = 1L;
    private String traceId;
    private String rpcId;
    private boolean isClusterTest;

    public boolean isClusterTest() {
        return isClusterTest;
    }

    public void setClusterTest(boolean clusterTest) {
        isClusterTest = clusterTest;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getRpcId() {
        return rpcId;
    }

    public void setRpcId(String rpcId) {
        this.rpcId = rpcId;
    }

    @Override
    public String toString() {
        return "{" +
                "traceId='" + traceId + '\'' +
                ", rpcId='" + rpcId + '\'' +
                '}';
    }
}
