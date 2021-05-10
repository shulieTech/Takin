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
package com.shulie.instrument.simulator.agent.core.zk.impl;

/**
 * zk 客户端描述
 *
 * @author xiaobin@shulie.io
 * @since 1.0.0
 */
public class ZkClientSpec {
    /**
     * zk 地址
     */
    private String zkServers;
    /**
     * 连接超时时间(毫秒),默认30秒
     */
    private int connectionTimeoutMillis = 30000;
    /**
     * session 超时时间(毫秒),默认60秒
     */
    private int sessionTimeoutMillis = 60000;

    /**
     * zk 客户端线程名称
     */
    private String threadName = "curator";

    public ZkClientSpec() {
    }

    public ZkClientSpec(String zkServers) {
        this.zkServers = zkServers;
    }

    public String getZkServers() {
        return zkServers;
    }

    public ZkClientSpec setZkServers(String zkServers) {
        this.zkServers = zkServers;
        return this;
    }

    public int getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    public ZkClientSpec setConnectionTimeoutMillis(int connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
        return this;
    }

    public int getSessionTimeoutMillis() {
        return sessionTimeoutMillis;
    }

    public ZkClientSpec setSessionTimeoutMillis(int sessionTimeoutMillis) {
        this.sessionTimeoutMillis = sessionTimeoutMillis;
        return this;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
}
