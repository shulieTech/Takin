package io.shulie.amdb.utils;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
//@Component
public class ZookeeperUtils {

    private ZkClient zkClient;

    @Value("${config.zk.servers}")
    String zkServers;
    @Value("${config.zk.sessionTimeout}")
    int sessionTimeout;
    @Value("${config.zk.connectionTimeout}")
    int connectionTimeout;

    @PostConstruct
    public void init() {
        try {
            this.zkClient = new ZkClient(zkServers, sessionTimeout, connectionTimeout, new SerializableSerializer());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ZkClient getZkClient() {
        return zkClient;
    }
}
