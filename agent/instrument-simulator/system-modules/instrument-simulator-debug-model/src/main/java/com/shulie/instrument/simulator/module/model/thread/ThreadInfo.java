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
import java.util.List;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 10:28 上午
 */
public class ThreadInfo implements Serializable {
    private final static long serialVersionUID = 1L;
    /**
     * 线程 id
     */
    private long id;

    /**
     * 线程名称
     */
    private String name;

    /**
     * 线程组名称
     */
    private String groupName;

    /**
     * cpu 占用率
     */
    private long cpuUsage;

    /**
     * cpu 占用时长
     */
    private long cpuTime;

    /**
     * 是否中断
     */
    private boolean interrupted;

    /**
     * 线程状态
     */
    private String state;

    /**
     * 锁名称
     */
    private String lockName;

    /**
     * 锁拥有者名称
     */
    private String lockOwnerName;

    /**
     * 锁拥有者 ID
     */
    private long lockOwnerId;

    /**
     * 是否是暂停状态
     */
    private boolean suspended;

    /**
     * 线程是否在 native代码执行中
     */
    private boolean inNative;

    /**
     * 是否是后台运行
     */
    private boolean daemon;

    /**
     * 优先级
     */
    private int priority;

    /**
     * 阻塞时间
     */
    private long blockedTime;

    /**
     * 阻塞次数
     */
    private long blockedCount;

    /**
     * 等待时间
     */
    private long waitedTime;

    /**
     * 等待次数
     */
    private long waitedCount;

    /**
     * 锁信息
     */
    private LockInfo lockInfo;

    /**
     * 线程堆栈
     */
    private List<ThreadStack> threadStacks;

    /**
     * 所有的锁信息
     */
    private List<LockInfo> lockInfos;

    /**
     * lock identity hash code
     */
    private int lockIdentityHashCode;

    /**
     * 阻塞的线程数
     */
    private int blockingThreadCount;

    /**
     * 链路追踪的 traceId
     */
    private String traceId;

    /**
     * 链路追踪的 rpcId
     */
    private String rpcId;

    /**
     * 是否是压测流量
     */
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(long cpuTime) {
        this.cpuTime = cpuTime;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public void setInterrupted(boolean interrupted) {
        this.interrupted = interrupted;
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

    public long getBlockedTime() {
        return blockedTime;
    }

    public void setBlockedTime(long blockedTime) {
        this.blockedTime = blockedTime;
    }

    public long getBlockedCount() {
        return blockedCount;
    }

    public void setBlockedCount(long blockedCount) {
        this.blockedCount = blockedCount;
    }

    public long getWaitedTime() {
        return waitedTime;
    }

    public void setWaitedTime(long waitedTime) {
        this.waitedTime = waitedTime;
    }

    public long getWaitedCount() {
        return waitedCount;
    }

    public void setWaitedCount(long waitedCount) {
        this.waitedCount = waitedCount;
    }

    public int getBlockingThreadCount() {
        return blockingThreadCount;
    }

    public void setBlockingThreadCount(int blockingThreadCount) {
        this.blockingThreadCount = blockingThreadCount;
    }

    public int getLockIdentityHashCode() {
        return lockIdentityHashCode;
    }

    public void setLockIdentityHashCode(int lockIdentityHashCode) {
        this.lockIdentityHashCode = lockIdentityHashCode;
    }

    public long getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(long cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public String getLockOwnerName() {
        return lockOwnerName;
    }

    public void setLockOwnerName(String lockOwnerName) {
        this.lockOwnerName = lockOwnerName;
    }

    public long getLockOwnerId() {
        return lockOwnerId;
    }

    public void setLockOwnerId(long lockOwnerId) {
        this.lockOwnerId = lockOwnerId;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public boolean isInNative() {
        return inNative;
    }

    public void setInNative(boolean inNative) {
        this.inNative = inNative;
    }

    public LockInfo getLockInfo() {
        return lockInfo;
    }

    public void setLockInfo(LockInfo lockInfo) {
        this.lockInfo = lockInfo;
    }

    public List<ThreadStack> getThreadStacks() {
        return threadStacks;
    }

    public void setThreadStacks(List<ThreadStack> threadStacks) {
        this.threadStacks = threadStacks;
    }

    public List<LockInfo> getLockInfos() {
        return lockInfos;
    }

    public void setLockInfos(List<LockInfo> lockInfos) {
        this.lockInfos = lockInfos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (name != null) {
            sb.append(" name=").append(name);
        }

        if (groupName != null) {
            sb.append(" groupName=").append(groupName);
        }
        sb.append(" priority=").append(priority);

        if (cpuUsage >= 0 && cpuUsage <= 100) {
            sb.append(" cpuUsage=").append(cpuUsage).append("%");
        }

        if (cpuTime >= 0) {
            sb.append(" cpuTime=").append(cpuTime);
        }
        sb.append(" state=").append(state);
        sb.append(" interrupted=").append(interrupted);
        sb.append(" daemon=").append(daemon);
        if (lockName != null) {
            sb.append(" on ").append(lockName);
        }
        if (lockOwnerName != null) {
            sb.append(" owned by \"").append(lockOwnerName).append("\" Id=").append(lockOwnerId);
        }
        if (suspended) {
            sb.append(" (suspended)");
        }
        if (inNative) {
            sb.append(" (in native)");
        }
        sb.append('\n');
        int i = 0;
        if (threadStacks != null) {
            for (ThreadStack threadStack : threadStacks) {
                sb.append(threadStack);
            }

            if (i < threadStacks.size()) {
                sb.append("\t...");
                sb.append('\n');
            }
        }

        if (lockInfos != null && lockInfos.size() > 0) {
            sb.append("\n\tNumber of locked synchronizers = ").append(lockInfos.size());
            sb.append('\n');
            for (LockInfo lockInfo : lockInfos) {
                sb.append("\t- ").append(lockInfo);
                if (lockInfo.getIdentityHashCode() == lockIdentityHashCode) {
                    sb.append(" <---- but blocks ").append(blockingThreadCount);
                    sb.append(" other threads!");
                }
                sb.append('\n');
            }
        }
        sb.append('\n');
        return sb.toString().replace("\t", "    ");
    }
}
