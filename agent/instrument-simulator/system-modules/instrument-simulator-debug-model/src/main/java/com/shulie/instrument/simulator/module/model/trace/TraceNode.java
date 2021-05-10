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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/4 10:07 下午
 */
public class TraceNode implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 父节点
     */
    private transient TraceNode parent;

    /**
     * 子点节
     */
    private final List<TraceNode> children = new ArrayList<TraceNode>();

    /**
     * 类名
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 是否是接口
     */
    private boolean isInterface;

    /**
     * 开始时间戳
     */
    private long start;
    /**
     * 结束时间戳
     */
    private long end;

    /**
     * 是否成功
     */
    private boolean success = true;

    /**
     * 抛出的异常
     */
    private String errorMsg;

    /**
     * 行号
     */
    private long line = -1;

    /**
     * 耗时
     */
    private long cost;

    /**
     * 类加载器
     */
    private String classloader;

    public TraceNode(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public TraceNode(TraceNode parent, String className, String methodName) {
        this.parent = parent;
        this.className = className;
        this.methodName = methodName;
    }

    public TraceNode next(String className, String methodName) {
        TraceNode traceNode = new TraceNode(this, className, methodName);
        this.children.add(traceNode);
        return traceNode;
    }


    public TraceNode begin() {
        this.start = System.nanoTime();
        return this;
    }

    public TraceNode end() {
        this.end = System.nanoTime();
        this.cost = (end - start) < 0 ? 0 : (end - start);
        return this;
    }

    public String getClassloader() {
        return classloader;
    }

    public void setClassloader(String classloader) {
        this.classloader = classloader;
    }

    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    /**
     * 判断是否需要跳过
     *
     * @param stopInMills
     * @return
     */
    public boolean isSkip(long stopInMills) {
        return (getCost() / 1000000) <= stopInMills || isSkip();
    }

    /**
     * 是否忽略
     *
     * @return
     */
    public boolean isSkip() {
        return isJdkClass(className);
    }

    public boolean isSuccess() {
        return success;
    }

    public long getLine() {
        return line;
    }

    public void setLine(long line) {
        this.line = line;
    }

    /**
     * 查找耗时最大的节点，便于后续高亮展示
     */
    public TraceNode findMaxCost() {
        TraceNode maxCost = null;
        for (TraceNode traceNode : children) {
            if (maxCost == null) {
                maxCost = traceNode;
            } else {
                if (traceNode.getCost() > maxCost.getCost()) {
                    maxCost = traceNode;
                }
            }
        }
        return maxCost;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        if (errorMsg != null) {
            this.success = true;
        }
    }

    /**
     * 是否根节点
     *
     * @return true / false
     */
    boolean isRoot() {
        return null == parent;
    }

    /**
     * 判断是否是 jdk 的类
     *
     * @param className
     * @return
     */
    private boolean isJdkClass(String className) {
        return className.startsWith("java.");
    }

    public long getCost() {
        return cost;
    }

    public TraceNode getParent() {
        return parent;
    }

    public List<TraceNode> getChildren() {
        return children;
    }

    public void setChildren(List<TraceNode> children) {
        if (children != null) {
            this.children.clear();
            this.children.addAll(children);
        }
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

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "{" +
                "className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", isInterface=" + isInterface +
                ", start=" + start +
                ", end=" + end +
                ", cost=" + cost +
                ", success=" + success +
                ", errorMsg='" + errorMsg + '\'' +
                ", line=" + line +
                ", children=" + children +
                '}';
    }
}
