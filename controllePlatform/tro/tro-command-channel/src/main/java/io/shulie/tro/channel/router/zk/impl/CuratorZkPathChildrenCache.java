package io.shulie.tro.channel.router.zk.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.recipes.cache.ChildData;
import com.netflix.curator.framework.recipes.cache.PathChildrenCache;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheEvent;
import com.netflix.curator.framework.recipes.cache.PathChildrenCacheListener;
import com.netflix.curator.framework.state.ConnectionState;
import com.netflix.curator.framework.state.ConnectionStateListener;
import io.shulie.tro.channel.router.zk.ZkChildListener;
import io.shulie.tro.channel.router.zk.ZkPathChildrenCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Preconditions.checkState;

/**
 * 在 Apache Curator 的 PathChildrenCache 基础上修改的实现，做了精简，
 *
 * @author pamirs
 */
public class CuratorZkPathChildrenCache implements ZkPathChildrenCache {

    private static final Logger logger = LoggerFactory.getLogger(CuratorZkPathChildrenCache.class);

    private CuratorFramework client;
    private final String path;
    private final AtomicReference<List<String>> child = new AtomicReference<List<String>>(Collections.EMPTY_LIST);
    private final AtomicBoolean isConnected = new AtomicBoolean(true);
    private final AtomicBoolean running = new AtomicBoolean(false);
    private PathChildrenCache childCache;
    private ZkChildListener listener;

    public CuratorZkPathChildrenCache(CuratorFramework client, String path, ZkChildListener listener) {
        this.client = client;
        this.path = path;
        this.listener = listener;
    }

    @Override
    public List<String> getChildren() {
        if (this.childCache == null) {
            return null;
        }

        List<ChildData> currentData = this.childCache.getCurrentData();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < currentData.size(); i++) {
            String path = currentData.get(i).getPath();
            result.add(path);
        }
        return result;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void stop() throws Exception {
        running.set(false);
        client.getConnectionStateListenable().removeListener(connectionStateListener);
        childCache.close();
        delete();
    }

    @Override
    public void start() throws Exception {
        checkState(running.compareAndSet(false, true), "Node cache has been started");
        client.getConnectionStateListenable().addListener(connectionStateListener);
        reset();
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public void refresh() throws Exception {
        reset();
    }

    @Override
    public void delete() throws Exception {
        this.client.delete().guaranteed().inBackground().forPath(path);
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
                        } catch (Exception e) {
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

    private void reset() throws Exception {
        if (running.get() && isConnected.get()) {
            pathWatch(this.path);
        }
    }

    /**
     * 注册监听
     * TreeCache: 可以将指定的路径节点作为根节点（祖先节点），对其所有的子节点操作进行监听，
     * 呈现树形目录的监听，可以设置监听深度，最大监听深度为 int 类型的最大值。
     */
    private void pathWatch(String path) throws Exception {
        childCache = new PathChildrenCache(client, path,false);
        childCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                ChildData data = event.getData();
                if (data != null){
                    String childPath = data.getPath();
                    if (!childPath.equals(path)){
                        listener.call(client,event);
                    }
                }
            }
        });
        childCache.start();
    }

}
