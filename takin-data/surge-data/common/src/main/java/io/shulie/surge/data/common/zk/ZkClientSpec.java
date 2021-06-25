/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.surge.data.common.zk;


import io.shulie.surge.data.common.factory.GenericFactorySpec;

public class ZkClientSpec implements GenericFactorySpec<ZkClient> {

    private String zkServers;
    private int connectionTimeoutMillis = 30000;
    private int sessionTimeoutMillis = 60000;

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

    @Override
    public String factoryName() {
        return "ZooKeeper";
    }

    @Override
    public Class<ZkClient> productClass() {
        return ZkClient.class;
    }
}
