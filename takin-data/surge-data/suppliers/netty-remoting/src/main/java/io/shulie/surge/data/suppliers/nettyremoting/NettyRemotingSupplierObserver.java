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

package io.shulie.surge.data.suppliers.nettyremoting;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.shulie.surge.data.common.lifecycle.LifecycleObserver;
import io.shulie.surge.data.common.utils.IpAddressUtils;
import io.shulie.surge.data.common.zk.ZkClient;
import io.shulie.surge.data.common.zk.ZkHeartbeatNode;
import io.shulie.surge.data.runtime.supplier.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * netty remoting supplier 观察者
 *
 * @author vincent
 */
public final class NettyRemotingSupplierObserver implements LifecycleObserver<Supplier> {

    private static final Logger logger = LoggerFactory.getLogger(NettyRemotingSupplierObserver.class);

    @Inject
    private ZkClient zkClient;

    private ZkHeartbeatNode heartbeatNode;

    private Map<String, String> hostMap;

    private boolean defaultRegistZk;

    @Inject
    @Named("config.log.pradar.server")
    protected transient String pradarServerPath;
    @Inject
    @Named("config.log.pradar.cloud.server")
    protected transient String pradarCloudServerPath;

    public NettyRemotingSupplierObserver(Map<String, String> hostMap, boolean defaultRegistZk) {
        this.hostMap = hostMap;
        this.defaultRegistZk = defaultRegistZk;
    }

    @Override
    public void beforeStart(Supplier target) {

    }

    @Override
    public void afterStart(Supplier target) {
        NettyRemotingSupplier nettyRemotingSupplier = (NettyRemotingSupplier) target;
        if (defaultRegistZk) {
            String host = IpAddressUtils.getLocalAddress();
            int port = nettyRemotingSupplier.getPort();
            registerToZk(host, port, pradarServerPath);
            addressRelation(host, port);
        }
    }

    private void addressRelation(String host, int port) {
        if (hostMap == null || hostMap.isEmpty() || !hostMap.containsKey(host)) {
            return;
        }
        registerToZk(hostMap.get(host), port, pradarCloudServerPath);
    }

    private void registerToZk(String host, int port, String pradarServerPath) {
        String taskPath = pradarServerPath + "/" + host + ":" + port;
        this.heartbeatNode = zkClient.createHeartbeatNode(taskPath);
        try {
            heartbeatNode.start();
        } catch (Exception e) {
            throw new RuntimeException("fail to start heartbeat node for path " + heartbeatNode.getPath(), e);
        }

        boolean alive = heartbeatNode.isAlive();
        if (alive) {
            logger.info("successfully regist node to zookeeper, path={}", heartbeatNode.getPath());
        } else {
            logger.warn("fail to regist heartbeat node to zookeeper, path={}", heartbeatNode.getPath());
            throw new RuntimeException("fail to regist heartbeat node for path " + heartbeatNode.getPath());
        }
    }

    @Override
    public void beforeStop(Supplier target) {

    }

    @Override
    public void afterStop(Supplier target) {

    }


}
