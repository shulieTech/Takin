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
package com.pamirs.pradar.debug.model;

import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author wangjian
 * @since 2021/1/4 14:39
 */
public class DebugTestInfo {

    /**
     * 链路id
     */
    private String traceId;

    /**
     * rpc操作id
     */
    private String rpcId;

    /**
     * rpc操作type
     */
    private Integer logType;

    /**
     * agent节点id
     */
    private String agentId;

    /**
     * 节点应用名称
     */
    private String appName;

    /**
     * 调试流量日志
     */
    private Log log;

    /**
     * 调试机器信息
     */
    private List<MechineInfo> machineInfo;

    /**
     * 是否rpc结束
     */
    private boolean isRpcEnd = false;

    /**
     * 日志回调，生成日志
     */
    private LogCallback logCallback;

    public LogCallback getLogCallback() {
        return logCallback;
    }

    public void setLogCallback(LogCallback logCallback) {
        this.logCallback = logCallback;
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

    public Integer getLogType() {
        return logType;
    }

    public void setLogType(Integer logType) {
        this.logType = logType;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(Log log) {
        this.log = log;
    }

    public List<MechineInfo> getMachineInfo() {
        return machineInfo;
    }

    public void setMachineInfo(List<MechineInfo> machineInfo) {
        this.machineInfo = machineInfo;
    }

    public boolean isRpcEnd() {
        return isRpcEnd;
    }

    public void setRpcEnd(boolean rpcEnd) {
        isRpcEnd = rpcEnd;
    }

    public static class Log {

        private String level;

        private String content;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class MechineInfo {
        private String index;

        /**
         * cpu 使用率
         */
        private String cpuUsage;

        /**
         * cpu 负载
         */
        private String cpuLoad;

        /**
         * 内存使用率
         */
        private String memoryUsage;

        /**
         * 内存使用量
         */
        private String memoryTotal;

        /**
         * io 等待率
         */
        private String ioWait;

        /**
         * younggc 次数
         */
        private long youngGcCount;

        /**
         * young gc 耗时
         */
        private long youngGcTime;

        /**
         * old gc 次数
         */
        private long oldGcCount;

        /**
         * old gc 耗时
         */
        private long oldGcTime;



        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getCpuUsage() {
            return cpuUsage;
        }

        public void setCpuUsage(String cpuUsage) {
            this.cpuUsage = cpuUsage;
        }

        public String getCpuLoad() {
            return cpuLoad;
        }

        public void setCpuLoad(String cpuLoad) {
            this.cpuLoad = cpuLoad;
        }

        public String getMemoryUsage() {
            return memoryUsage;
        }

        public void setMemoryUsage(String memoryUsage) {
            this.memoryUsage = memoryUsage;
        }

        public String getMemoryTotal() {
            return memoryTotal;
        }

        public void setMemoryTotal(String memoryTotal) {
            this.memoryTotal = memoryTotal;
        }

        public String getIoWait() {
            return ioWait;
        }

        public void setIoWait(String ioWait) {
            this.ioWait = ioWait;
        }

        public long getYoungGcCount() {
            return youngGcCount;
        }

        public void setYoungGcCount(long youngGcCount) {
            this.youngGcCount = youngGcCount;
        }

        public long getYoungGcTime() {
            return youngGcTime;
        }

        public void setYoungGcTime(long youngGcTime) {
            this.youngGcTime = youngGcTime;
        }

        public long getOldGcCount() {
            return oldGcCount;
        }

        public void setOldGcCount(long oldGcCount) {
            this.oldGcCount = oldGcCount;
        }

        public long getOldGcTime() {
            return oldGcTime;
        }

        public void setOldGcTime(long oldGcTime) {
            this.oldGcTime = oldGcTime;
        }
    }

    public boolean isSameRpcOperation(DebugTestInfo target) {
        if (null == target) {
            return false;
        }
        if (StringUtils.equals(this.getTraceId(), target.getTraceId())
                && StringUtils.equals(this.getRpcId(), target.getRpcId())) {
            return true;
        }
        return false;
    }

    public interface LogCallback {
        Log getLog();
    }
}
