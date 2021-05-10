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
package com.shulie.instrument.simulator.module.model.thread;

import com.shulie.instrument.simulator.module.model.thread.StackElement;

import java.io.Serializable;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 10:44 上午
 */
public class MonitorInfo implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 类名称
     */
    private String className;

    /**
     * 唯一的 hashCode
     */
    private int identityHashCode;

    /**
     * 锁的栈深度
     */
    private int lockedStackDepth;

    /**
     * 阻塞的线程数
     */
    private int blockingThreadCount;

    /**
     * 堆栈信息
     */
    private StackElement stackElement;

    public StackElement getStackTraceEle() {
        return stackElement;
    }

    public void setStackTraceEle(StackElement stackElement) {
        this.stackElement = stackElement;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getIdentityHashCode() {
        return identityHashCode;
    }

    public void setIdentityHashCode(int identityHashCode) {
        this.identityHashCode = identityHashCode;
    }

    public int getLockedStackDepth() {
        return lockedStackDepth;
    }

    public void setLockedStackDepth(int lockedStackDepth) {
        this.lockedStackDepth = lockedStackDepth;
    }

    public int getBlockingThreadCount() {
        return blockingThreadCount;
    }

    public void setBlockingThreadCount(int blockingThreadCount) {
        this.blockingThreadCount = blockingThreadCount;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("\t-  locked ").append(className).append('@').append(Integer.toHexString(identityHashCode));
        if (blockingThreadCount != 0) {
            builder.append(" <---- but blocks ").append(blockingThreadCount)
                    .append(" other threads!");
        }
        builder.append("\n");
        return builder.toString();
    }
}
