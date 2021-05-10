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
package com.shulie.instrument.simulator.module.model.trace;

import com.shulie.instrument.simulator.api.ThrowableUtils;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/4 10:06 下午
 */
public class TraceView implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 线程名称
     */
    private String threadName;

    /**
     * 线程 ID
     */
    private long threadId;

    /**
     * 是否是守护线程
     */
    private boolean daemon;

    /**
     * 优先级
     */
    private int priority;

    /**
     * classloader
     */
    private String classloader;

    /**
     * 根节点
     */
    private TraceNode root;

    /**
     * 当前节点
     */
    private transient TraceNode current;

    /**
     * 观察的结果
     */
    private Object watches;

    /**
     * 开始时间,单位纳秒
     */
    private long traceTime;

    /**
     * 总耗时,单位纳秒
     */
    private long totalCost;

    /**
     * traceId
     */
    private String traceId;
    /**
     * rpcId
     */
    private String rpcId;
    /**
     * 方法执行前的压测流量标
     */
    private boolean isClusterTestBefore;

    /**
     * 方法执行后的压测流量标
     */
    private boolean isClusterTestAfter;


    public TraceView() {
        this.watches = new HashMap<String, Object>();
    }

    public TraceNode begin(String className, String methodName,String classloader) {
        if (root == null) {
            root = new TraceNode(className, methodName);
            root.begin();
            root.setClassloader(classloader);
            this.current = root;
            return root;
        } else {
            TraceNode traceNode = this.current.next(className, methodName);
            traceNode.begin();
            traceNode.setClassloader(classloader);
            this.current = traceNode;
            return traceNode;
        }
    }

    public String getClassloader() {
        return classloader;
    }

    public void setClassloader(String classloader) {
        this.classloader = classloader;
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

    public void setTraceTime(long traceTime) {
        this.traceTime = traceTime;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }

    public long getTotalCost() {
        return totalCost;
    }

    public long getRootCost() {
        return root.getCost();
    }

    public void error(Throwable throwable) {
        this.current.setErrorMsg(ThrowableUtils.toString(throwable));
    }

    public void error(String errorMsg) {
        this.current.setErrorMsg(errorMsg);
    }

    public void end() {
        this.current.end();
        /**
         * 如果当前节点不是根节点，则直接将当前节点设置为父节点
         */
        if (this.current != this.root) {
            this.current = this.current.getParent();
        }
    }

    public void setInterface(boolean isInterface) {
        this.current.setInterface(isInterface);
    }

    public Object getWatches() {
        return watches;
    }

    public void setWatches(Object watches) {
        this.watches = watches;
    }


    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getTraceTime() {
        return traceTime;
    }

    public TraceNode getRoot() {
        return root;
    }

    public void setRoot(TraceNode root) {
        this.root = root;
    }

    public TraceNode getCurrent() {
        return current;
    }

    public void setCurrent(TraceNode current) {
        this.current = current;
    }

    public TraceNode getCurrentMaxCost() {
        return current.findMaxCost();
    }

    @Override
    public String toString() {
        return "{" +
                "threadName='" + threadName + '\'' +
                ", threadId=" + threadId +
                ", daemon=" + daemon +
                ", priority=" + priority +
                ", traceId=" + traceId +
                ", rpcId=" + rpcId +
                ", isClusterTestBefore=" + isClusterTestBefore +
                ", isClusterTestAfter=" + isClusterTestAfter +
                ", root=" + root +
                ", watches=" + watches +
                '}';
    }
}
