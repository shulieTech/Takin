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
package com.shulie.instrument.simulator.module.model.tomcat;

import java.util.List;

public class TomcatInfo {

    private List<ConnectorStats> connectorStats;
    private List<ThreadPool> threadPools;

    public TomcatInfo() {
    }

    public List<ConnectorStats> getConnectorStats() {
        return connectorStats;
    }

    public void setConnectorStats(List<ConnectorStats> connectorStats) {
        this.connectorStats = connectorStats;
    }

    public List<ThreadPool> getThreadPools() {
        return threadPools;
    }

    public void setThreadPools(List<ThreadPool> threadPools) {
        this.threadPools = threadPools;
    }

    public static class ConnectorStats {
        private String name;
        private double qps;
        private double rt;
        private double error;
        private long received;
        private long sent;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getQps() {
            return qps;
        }

        public void setQps(double qps) {
            this.qps = qps;
        }

        public double getRt() {
            return rt;
        }

        public void setRt(double rt) {
            this.rt = rt;
        }

        public double getError() {
            return error;
        }

        public void setError(double error) {
            this.error = error;
        }

        public long getReceived() {
            return received;
        }

        public void setReceived(long received) {
            this.received = received;
        }

        public long getSent() {
            return sent;
        }

        public void setSent(long sent) {
            this.sent = sent;
        }
    }

    public static class ThreadPool {
        private String name;
        private long busy;
        private long total;

        public ThreadPool() {
        }

        public ThreadPool(String name, long busy, long total) {
            this.name = name;
            this.busy = busy;
            this.total = total;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getBusy() {
            return busy;
        }

        public void setBusy(long busy) {
            this.busy = busy;
        }

        public long getTotal() {
            return total;
        }

        public void setTotal(long total) {
            this.total = total;
        }
    }
}
