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
package com.shulie.instrument.simulator.module.model.watch;

import java.io.Serializable;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/4 10:06 下午
 */
public class WatchView implements Serializable {
    /**
     * 类名
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 观察的开始时间,单位纳秒
     */
    private long time;

    /**
     * 观察结束时间
     */
    private long end;

    /**
     * 观察的结果
     */
    private Object watches;

    /**
     * 耗时,单位纳秒
     */
    private long cost;

    /**
     * traceId
     */
    private String traceId;

    /**
     * rpcId
     */
    private String rpcId;

    /**
     * 方法执行前的压测标
     */
    private boolean isClusterTestBefore;

    /**
     * 方法执行后的压测标
     */
    private boolean isClusterTestAfter;

    public void begin(String className, String methodName) {
        this.time = System.nanoTime();
        this.className = className;
        this.methodName = methodName;
    }

    public void end() {
        this.end = System.nanoTime();
        this.cost = (end - time) < 0 ? 0 : (end - time);
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

    public boolean isClusterTestBefore() {
        return isClusterTestBefore;
    }

    public void setClusterTestBefore(boolean clusterTestBefore) {
        isClusterTestBefore = clusterTestBefore;
    }

    public boolean isClusterTestAfter() {
        return isClusterTestAfter;
    }

    public void setClusterTestAfter(boolean clusterTestAfter) {
        isClusterTestAfter = clusterTestAfter;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public Object getWatches() {
        return watches;
    }

    public void setWatches(Object watches) {
        this.watches = watches;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", time=" + time +
                ", end=" + end +
                ", watches=" + watches +
                ", cost=" + cost +
                ", traceId='" + traceId + '\'' +
                ", rpcId='" + rpcId + '\'' +
                ", isClusterTestBefore=" + isClusterTestBefore +
                ", isClusterTestAfter=" + isClusterTestAfter +
                '}';
    }
}
