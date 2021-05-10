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
package com.shulie.instrument.simulator.module.model.monitor;

import java.io.Serializable;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/4 10:06 下午
 */
public class MonitorView implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 最大耗时
     */
    private long maxCost;

    /**
     * 平均耗时
     */
    private long avgCost;

    /**
     * 最小耗时
     */
    private long minCost;

    /**
     * 统计条数
     */
    private long count;

    /**
     * 成功条数
     */
    private long success;

    /**
     * 失败条数
     */
    private long fail;

    /**
     * 是否刚初始化
     */
    private transient boolean isInit = true;

    public void fail(MonitorView monitorView, long cost) {
        if (monitorView.isInit) {
            this.avgCost = cost;
            this.maxCost = cost;
            this.minCost = cost;
            this.className = monitorView.getClassName();
            this.methodName = monitorView.getMethodName();
            this.fail = 1;
            this.success = 0;
            this.count = 1;
        } else {
            this.avgCost = (monitorView.getCount() * monitorView.getAvgCost() + cost) / (monitorView.getCount() + 1);
            this.maxCost = Math.max(monitorView.getMaxCost(), cost);
            this.minCost = Math.min(monitorView.getMinCost(), cost);
            this.className = monitorView.getClassName();
            this.methodName = monitorView.getMethodName();
            this.fail = monitorView.getFail() + 1;
            this.success = monitorView.getSuccess();
            this.count = monitorView.getCount() + 1;
        }
        isInit = false;
    }

    public void success(MonitorView monitorView, long cost) {
        if (monitorView.isInit) {
            this.avgCost = cost;
            this.maxCost = cost;
            this.minCost = cost;
            this.className = monitorView.getClassName();
            this.methodName = monitorView.getMethodName();
            this.fail = 0;
            this.success = 1;
            this.count = 1;
        } else {
            this.avgCost = (monitorView.getCount() * monitorView.getAvgCost() + cost) / (monitorView.getCount() + 1);
            this.maxCost = Math.max(monitorView.getMaxCost(), cost);
            this.minCost = Math.min(monitorView.getMinCost(), cost);
            this.className = monitorView.getClassName();
            this.methodName = monitorView.getMethodName();
            this.fail = monitorView.getFail();
            this.success = monitorView.getSuccess() + 1;
            this.count = monitorView.getCount() + 1;
        }
        isInit = false;
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

    public long getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(long maxCost) {
        this.maxCost = maxCost;
    }

    public long getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(long avgCost) {
        this.avgCost = avgCost;
    }

    public long getMinCost() {
        return minCost;
    }

    public void setMinCost(long minCost) {
        this.minCost = minCost;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getSuccess() {
        return success;
    }

    public void setSuccess(long success) {
        this.success = success;
    }

    public long getFail() {
        return fail;
    }

    public void setFail(long fail) {
        this.fail = fail;
    }

    @Override
    public String toString() {
        return "{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", maxCost=" + maxCost +
                ", avgCost=" + avgCost +
                ", minCost=" + minCost +
                ", count=" + count +
                ", success=" + success +
                ", fail=" + fail +
                '}';
    }
}
