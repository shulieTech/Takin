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
import com.shulie.instrument.module.register.zk.ZkPathChildrenCache;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 在 Apache Curator 的 PathChildrenCache 基础上修改的实现，做了精简，
 * 只监视子节点的增删，其内容的修改不会被监视。
 *
 * @author pamirs
 */
public class CuratorZkPathChildrenCache implements ZkPathChildrenCache {

    private static final Logger logger = LoggerFactory.getLogger(CuratorZkPathChildrenCache.class);

    private CuratorFramework client;

    private final String path;

    private Runnable updateListener;
    private Executor updateExecutor;

    private final AtomicReference<List<String>> data = new AtomicReference<List<String>>(Collections.EMPTY_LIST);
    private final AtomicReference<List<String>> last = new AtomicReference<List<String>>(Collections.EMPTY_LIST);
    private final AtomicBoolean isConnected = new AtomicBoolean(true);
    private final AtomicBoolean running = new AtomicBoolean(false);

    public CuratorZkPathChildrenCache(CuratorFramework client, String path) {
        this.client = client;
        this.path = path;
    }

    @Override
    public List<String> getChildren() {
        return data.get() == null ? Collections.EMPTY_LIST : data.get();
    }

    @Override
    public List<String> getAddChildren() {
        List<String> lastNodes = last.get();
        List<String> currentNodes = data.get();
        return getLeft(currentNodes, lastNodes);
    }

    @Override
    public List<String> getDeleteChildren() {
        List<String> lastNodes = last.get();
        List<String> currentNodes = data.get();
        return getRight(currentNodes, lastNodes);
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

    private static List<String> getLeft(List<String> list1, List<String> list2) {
        if (list1 == null) {
            list1 = Collections.EMPTY_LIST;
        }
        if (list2 == null) {
            list2 = Collections.EMPTY_LIST;
        }

        List<String> left = new ArrayList<String>(list1);
        left.removeAll(list2);
        return left;
    }

    private static List<String> getRight(List<String> list1, List<String> list2) {
        if (list1 == null) {
            list1 = Collections.EMPTY_LIST;
        }
        if (list2 == null) {
            list2 = Collections.EMPTY_LIST;
        }
        List<String> right = new ArrayList<String>(list2);
        right.removeAll(list1);
        return right;
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
        running.compareAndSet(false, true);
        client.getConnectionStateListenable().addListener(connectionStateListener);

        if (requireRebuild) {
            internalRebuild();
        }

        reset();
    }

    private void internalRebuild() throws Exception {
        List<String> children;
        try {
            children = client.getChildren().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            children = null;
        }
        last.set(data.get());
        if (null == children) {
            children = new ArrayList<String>();
        }
        data.set(children);
    }

    private void reset() throws Exception {
        if (running.get() && isConnected.get()) {
            ZKPaths.mkdirs(client.getZookeeperClient().getZooKeeper(), path, false);
            client.getChildren().usingWatcher(childrenWatcher).inBackground(backgroundCallback).forPath(path);
            client.checkExists().usingWatcher(dataWatcher).inBackground(backgroundCallback).forPath(path);
        }
    }

    private void setNewData(List<String> newData) throws InterruptedException {
        if (null == newData) {
            newData = new ArrayList<String>();
        }
        List<String> previousData = data.getAndSet(newData);
        //如果节点数据一致，则不做通知
        if (newData != null && previousData != null && isEqualCollection(newData, previousData)) {
            return;
        }
        Runnable updateListener = this.updateListener;
        if (updateListener != null && running.get() && (previousData != newData || (previousData != null && !isEqualCollection(newData, previousData)))) {
            Executor ex = updateExecutor;
            if (ex == null) {
                updateListener.run();
            } else {
                ex.execute(updateListener);
            }
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

    private final Watcher dataWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            try {
                if (running.get() && isConnected.get()) {
                    client.checkExists().usingWatcher(dataWatcher)
                            .inBackground(backgroundCallback).forPath(path);
                    // 节点被创建时，增加对其子节点的监视
                    if (event.getType() == EventType.NodeCreated) {
                        client.getChildren().usingWatcher(childrenWatcher)
                                .inBackground(backgroundCallback).forPath(path);
                    }
                }
            } catch (Throwable e) {
                logger.warn("fail to reset in watch event, path={}", path, e);
            }
        }
    };

    private final Watcher childrenWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            try {
                if (running.get() && isConnected.get() && client.isStarted()) {
                    client.getChildren().usingWatcher(childrenWatcher)
                            .inBackground(backgroundCallback).forPath(path);
                }
            } catch (Throwable e) {
                logger.warn("fail to reset in watch event, path={}", path, e);
            }
        }
    };

    private final BackgroundCallback backgroundCallback = new BackgroundCallback() {
        @Override
        public void processResult(CuratorFramework client, CuratorEvent event)
                throws Exception {
            switch (event.getType()) {
                case CHILDREN: {
                    setNewData(event.getChildren());
                    break;
                }

                case EXISTS: {
                    if (event.getResultCode() == KeeperException.Code.NONODE.intValue()) {
                        setNewData(null);
                    }
                    break;
                }

                default:
                    logger.info("Unexpected CuratorEvent: {}", event.getType());
                    break;
            }
        }
    };

    public static boolean isEqualCollection(final Collection a, final Collection b) {
        if (a.size() != b.size()) {
            return false;
        } else {
            Map mapa = getCardinalityMap(a);
            Map mapb = getCardinalityMap(b);
            if (mapa.size() != mapb.size()) {
                return false;
            } else {
                Iterator it = mapa.keySet().iterator();
                while (it.hasNext()) {
                    Object obj = it.next();
                    if (getFreq(obj, mapa) != getFreq(obj, mapb)) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private static final int getFreq(final Object obj, final Map freqMap) {
        Integer count = (Integer) freqMap.get(obj);
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

    public static Map getCardinalityMap(final Collection coll) {
        Map count = new HashMap();
        for (Iterator it = coll.iterator(); it.hasNext(); ) {
            Object obj = it.next();
            Integer c = (Integer) (count.get(obj));
            if (c == null) {
                count.put(obj, 1);
            } else {
                count.put(obj, c.intValue() + 1);
            }
        }
        return count;
    }
}
