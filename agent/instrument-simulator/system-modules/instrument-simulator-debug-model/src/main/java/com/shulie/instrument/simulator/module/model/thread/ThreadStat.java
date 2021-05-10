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

import java.io.Serializable;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 2:35 下午
 */
public class ThreadStat implements Serializable {
    private final static long serialVersionUID = 1L;
    /**
     * 线程 ID
     */
    private long threadId;
    /**
     * 线程名称
     */
    private String threadName;
    /**
     * 线程组名称
     */
    private String groupName;
    /**
     * cpu 使用率
     */
    private long cpuUsage;
    /**
     * cpu 时间
     */
    private long cpuTime;

    /**
     * 是否是中断
     */
    private boolean interrupted;

    /**
     * 是否是守护线程
     */
    private boolean daemon;

    /**
     * 优先级
     */
    private int priority;

    /**
     * 线程
     */
    private Thread thread;

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
    }

    public long getThreadId() {
        return threadId;
    }

    public void setThreadId(long threadId) {
        this.threadId = threadId;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(long cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public long getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(long cpuTime) {
        this.cpuTime = cpuTime;
    }
}
