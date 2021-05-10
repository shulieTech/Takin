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
package com.shulie.instrument.module.register.zk.impl;

import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import com.netflix.curator.utils.ZKPaths;
import com.shulie.instrument.module.register.zk.ZkHeartbeatNode;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * zk 心跳节点的实现
 *
 * @author pamirs
 */
public class CuratorZkHeartbeatNode implements ZkHeartbeatNode {

    private static final Logger logger = LoggerFactory.getLogger(CuratorZkHeartbeatNode.class);

    private CuratorFramework client;

    private final String path;
    private byte[] data;

    private final AtomicBoolean isConnected = new AtomicBoolean(true);
    private final AtomicBoolean alive = new AtomicBoolean(false);
    private final AtomicBoolean running = new AtomicBoolean(false);

    public CuratorZkHeartbeatNode(CuratorFramework client, String path) {
        this.client = client;
        this.path = path;
        this.data = new byte[0];
    }

    @Override
    public byte[] getData() {
        return data;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setData(byte[] data) throws Exception {
        if (running.get() && alive.get()) {
            client.setData().forPath(path, data);
        }
        this.data = data;
    }

    @Override
    public boolean isAlive() {
        return alive.get();
    }

    @Override
    public void stop() throws Exception {
        running.set(false);
        client.getConnectionStateListenable().removeListener(connectionStateListener);
        client.delete().forPath(path);
        alive.set(false);
    }

    @Override
    public void start() throws Exception {
        running.compareAndSet(false, true);
        client.getConnectionStateListenable().addListener(connectionStateListener);
        reset();
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    private void reset() throws Exception {
        if (running.get() && isConnected.get() && client.isStarted()) {
            try {
                client.setData().forPath(path, data);
            } catch (KeeperException e) {
                if (e.code() == Code.NONODE) {
                    ZKPaths.mkdirs(client.getZookeeperClient().getZooKeeper(), path, false);
                    client.create().withMode(CreateMode.EPHEMERAL).forPath(path, data);
                } else {
                    throw e;
                }
            }
            logger.info("heartbeat node is set alive, path={}", path);
            client.checkExists().usingWatcher(watcher).forPath(path);
            alive.set(true);
        }
    }

    private final ConnectionStateListener connectionStateListener = new ConnectionStateListener() {
        @Override
        public void stateChanged(CuratorFramework client, ConnectionState newState) {
            switch (newState) {
                case RECONNECTED: {
                    if (isConnected.compareAndSet(false, true)) {
                        try {
                            reset();
                            logger.info("recovered from RECONNECTED event, path={}", path);
                        } catch (Throwable e) {
                            logger.error("fail to reset after reconnection, path={}", path, e);
                        }
                    }
                    break;
                }

                case SUSPENDED:
                case LOST: {
                    isConnected.set(false);
                    alive.set(false);
                    break;
                }

                default:
                    ;
            }
        }
    };

    private final Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if (event.getType() == Watcher.Event.EventType.NodeDeleted) {
                try {
                    reset();
                } catch (Throwable e) {
                    logger.warn("fail to reset in watch event, path={}", path, e);
                }
            }
        }
    };
}
