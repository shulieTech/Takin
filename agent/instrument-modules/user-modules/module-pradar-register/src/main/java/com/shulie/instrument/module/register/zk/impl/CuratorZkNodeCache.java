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
import com.netflix.curator.framework.api.BackgroundCallback;
import com.netflix.curator.framework.api.CuratorEvent;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import com.netflix.curator.utils.ZKPaths;
import com.shulie.instrument.module.register.zk.ZkNodeCache;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 在 Apache Curator 的 NodeCache 基础上修改的实现，做了一定精简。
 * 做这个实现的主要原因是 Netflix Curator 并没有提供这个实现。
 *
 * @author pamirs
 */
public class CuratorZkNodeCache implements ZkNodeCache {

    private static final Logger logger = LoggerFactory.getLogger(CuratorZkNodeCache.class);

    private CuratorFramework client;

    private final String path;
    private final boolean dataIsCompressed;

    private Runnable updateListener;
    private Executor updateExecutor;

    private final AtomicReference<byte[]> data = new AtomicReference<byte[]>(null);
    private final AtomicBoolean isConnected = new AtomicBoolean(true);
    private final AtomicBoolean running = new AtomicBoolean(false);

    public CuratorZkNodeCache(CuratorFramework client, String path, boolean dataIsCompressed) {
        this.client = client;
        this.path = path;
        this.dataIsCompressed = dataIsCompressed;
    }

    @Override
    public byte[] getData() {
        return data.get();
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setUpdateListener(Runnable runnable) {
        this.updateListener = runnable;
    }

    @Override
    public void setUpdateExecutor(Executor executor) {
        this.updateExecutor = executor;
    }

    @Override
    public void stop() {
        running.set(false);
        updateListener = null;
        updateExecutor = null;
        client.getConnectionStateListenable().removeListener(connectionStateListener);
    }

    @Override
    public void start() throws Exception {
        start(false);
    }

    @Override
    public void startAndRefresh() throws Exception {
        start(true);
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void refresh() throws Exception {
        internalRebuild();
    }

    private void start(boolean requireRebuild) throws Exception {
        ZKPaths.mkdirs(client.getZookeeperClient().getZooKeeper(), path, false);
        client.getConnectionStateListenable().addListener(connectionStateListener);

        if (requireRebuild) {
            internalRebuild();
        }

        reset();
    }

    private void internalRebuild() throws Exception {
        byte[] bytes;
        try {
            bytes = client.getData().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            bytes = null;
        }
        doSetNewData(bytes);
    }

    private void reset() throws Exception {
        if (running.get() && isConnected.get() && client.isStarted()) {
            client.checkExists().usingWatcher(watcher).inBackground(backgroundCallback).forPath(path);
        }
    }

    private void setNewData(byte[] newData) {
        Executor ex = updateExecutor;
        if (ex != null) {
            ex.execute(new SetNewDataTask(newData));
        } else {
            doSetNewData(newData);
        }
    }

    private void doSetNewData(byte[] newData) {
        byte[] dataToSet;
        if (dataIsCompressed && newData != null) {
            try {
                dataToSet = ZipCompressionProvider.decompress(path, newData);
            } catch (Exception e) {
                logger.warn("fail to decompress NodeCache of path={}", path, e);
                return;
            }
        } else {
            dataToSet = newData;
        }
        byte[] previousData = data.getAndSet(dataToSet);
        Runnable updateListener = this.updateListener;
        if (updateListener != null && running.get() && !Arrays.equals(previousData, dataToSet)) {
            updateListener.run();
        }
    }

    /**
     * 更新的动作交给外部线程池处理（避免解压缩堵塞 zk 线程）
     */
    private class SetNewDataTask implements Runnable {

        private final byte[] newData;

        public SetNewDataTask(byte[] newData) {
            this.newData = newData;
        }

        @Override
        public void run() {
            doSetNewData(newData);
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
                            internalRebuild();
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
            try {
                logger.trace("WatchedEvent: {}", event.getType());
                reset();
            } catch (Throwable e) {
                logger.warn("fail to reset in watch event, path={}", path, e);
            }
        }
    };

    private final BackgroundCallback backgroundCallback = new BackgroundCallback() {
        @Override
        public void processResult(CuratorFramework client, CuratorEvent event)
                throws Exception {
            if (!running.get() || !isConnected.get()) {
                return;
            }

            switch (event.getType()) {
                case GET_DATA: {
                    if (event.getResultCode() == KeeperException.Code.OK.intValue()) {
                        logger.trace("GET_DATA: {}", event);
                        setNewData(event.getData());
                    }
                    break;
                }

                case EXISTS: {
                    if (event.getResultCode() == KeeperException.Code.NONODE.intValue()) {
                        logger.trace("EXISTS=>NONODE: {}", event);
                        setNewData(null);
                    } else if (event.getResultCode() == KeeperException.Code.OK.intValue()) {
                        logger.trace("EXISTS=>OK: {}", event);
                        client.getData().inBackground(backgroundCallback).forPath(path);
                    }
                    break;
                }

                default:
                    logger.info("Unknown CuratorEvent: {}", event.getType());
                    break;
            }
        }
    };
}
