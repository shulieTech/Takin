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
 * @since 2020/11/3 10:32 上午
 */
public class ThreadStack implements Serializable {
    private final static long serialVersionUID = 1L;
    /**
     * 线程堆栈节点
     */
    private StackElement stackElement;
    /**
     * 锁信息
     */
    private LockInfo lockInfo;
    /**
     * 监视器信息
     */
    private List<MonitorInfo> monitorInfos;

    private Thread.State state;

    public void setState(Thread.State state) {
        this.state = state;
    }

    public StackElement getStackTraceElement() {
        return stackElement;
    }

    public void setStackTraceElement(StackElement stackElement) {
        this.stackElement = stackElement;
    }

    public LockInfo getLockInfo() {
        return lockInfo;
    }

    public void setLockInfo(LockInfo lockInfo) {
        this.lockInfo = lockInfo;
    }

    public List<MonitorInfo> getMonitorInfos() {
        return monitorInfos;
    }

    public void setMonitorInfos(List<MonitorInfo> monitorInfos) {
        this.monitorInfos = monitorInfos;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (stackElement != null) {
            builder.append("\tat ").append(stackElement).append("\n");
        }
        if (lockInfo != null) {
            switch (state) {
                case BLOCKED:
                    builder.append("\t-  blocked on ").append(lockInfo);
                    builder.append('\n');
                    break;
                case WAITING:
                    builder.append("\t-  waiting on ").append(lockInfo);
                    builder.append('\n');
                    break;
                case TIMED_WAITING:
                    builder.append("\t-  waiting on ").append(lockInfo);
                    builder.append('\n');
                    break;
                default:
            }
            builder.append(lockInfo);
        }
        if (monitorInfos != null) {
            for (MonitorInfo monitorInfo : monitorInfos) {
                builder.append(monitorInfo);
            }
        }
        return builder.toString();
    }
}
