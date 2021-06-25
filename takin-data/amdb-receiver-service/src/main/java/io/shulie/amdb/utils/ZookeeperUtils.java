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
