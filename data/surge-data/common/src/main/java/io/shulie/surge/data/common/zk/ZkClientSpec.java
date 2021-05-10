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
