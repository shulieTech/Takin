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


import com.shulie.instrument.module.register.zk.*;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.recipes.locks.InterProcessMutex;
import com.netflix.curator.utils.ZKPaths;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.common.PathUtils;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;

/**
 * 封装了 Netflix Curator 的实现
 *
 * @author pamirs
 */
public class NetflixCuratorZkClient implements ZkClient {

    private static final byte[] EMPTY_BYTES = new byte[0];

    private final CuratorFramework cf;
    private final String zkServers;

    public NetflixCuratorZkClient(CuratorFramework cf, String zkServers) {
        this.cf = cf;
        this.zkServers = zkServers;
    }

    public CuratorFramework getCuratorFramework() {
        return cf;
    }

    @Override
    public String getZkServers() {
        return zkServers;
    }

    private org.apache.zookeeper.CreateMode cast(CreateMode mode) {
        switch (mode) {
            case PERSISTENT:
                return org.apache.zookeeper.CreateMode.PERSISTENT;

            case EPHEMERAL:
                return org.apache.zookeeper.CreateMode.EPHEMERAL;

            case PERSISTENT_SEQUENTIAL:
                return org.apache.zookeeper.CreateMode.PERSISTENT_SEQUENTIAL;

            case EPHEMERAL_SEQUENTIAL:
                return org.apache.zookeeper.CreateMode.EPHEMERAL_SEQUENTIAL;

            default:
                return null;
        }
    }

    private ZkNodeStat cast(Stat stat) {
        return stat == null ? null : new ZkNodeStat(stat.getCzxid(),
                stat.getMzxid(), stat.getCtime(), stat.getMtime(),
                stat.getVersion(), stat.getCversion(), stat.getAversion(),
                stat.getEphemeralOwner(), stat.getDataLength(),
                stat.getNumChildren(), stat.getPzxid());
    }

    @Override
    public void createDirectory(String path, CreateMode createMode) throws Exception {
        createNode(path, EMPTY_BYTES, createMode);
    }

    @Override
    public void ensureDirectoryExists(String path) throws Exception {
        ZKPaths.mkdirs(cf.getZookeeperClient().getZooKeeper(), path, true);
    }

    @Override
    public void ensureParentExists(String path) throws Exception {
        ZKPaths.mkdirs(cf.getZookeeperClient().getZooKeeper(), path, false);
    }

    @Override
    public int createNode(String path, byte[] data, CreateMode createMode) throws Exception {
        cf.create().withMode(cast(createMode)).forPath(path, data);
        return data.length;
    }

    @Override
    public int updateData(String path, byte[] data) throws Exception {
        cf.setData().forPath(path, data);
        return data.length;
    }

    @Override
    public int saveData(String path, byte[] data, CreateMode createMode) throws Exception {
        // 由于更新的机会更多，所以优化成更新优先
        try {
            return updateData(path, data);
        } catch (KeeperException e) {
            if (e.code() == Code.NONODE) {
                ensureParentExists(path);
                return createNode(path, data, createMode);
            } else {
                throw e;
            }
        }
    }

    @Override
    public int createCompressedNode(String path, byte[] data, CreateMode createMode) throws Exception {
        byte[] compressedData = ZipCompressionProvider.compress(path, data);
        cf.create().withMode(cast(createMode)).forPath(path, compressedData);
        return compressedData.length;
    }

    @Override
    public int updateCompressedData(String path, byte[] data) throws Exception {
        byte[] compressedData = ZipCompressionProvider.compress(path, data);
        cf.setData().forPath(path, compressedData);
        return compressedData.length;
    }

    @Override
    public int saveCompressedData(String path, byte[] data, CreateMode createMode) throws Exception {
        // 由于更新的机会更多，所以优化成更新优先
        try {
            return updateCompressedData(path, data);
        } catch (KeeperException e) {
            if (e.code() == Code.NONODE) {
                ensureParentExists(path);
                return createCompressedNode(path, data, createMode);
            } else {
                throw e;
            }
        }
    }

    @Override
    public boolean exists(String path) throws Exception {
        return cf.checkExists().forPath(path) != null;
    }

    @Override
    public ZkNodeStat getStat(String path) throws Exception {
        return cast(cf.checkExists().forPath(path));
    }

    @Override
    public byte[] getData(String path) throws Exception {
        return cf.getData().forPath(path);
    }

    @Override
    public byte[] getDecompressedData(String path) throws Exception {
        byte[] compressedData = cf.getData().forPath(path);
        return ZipCompressionProvider.decompress(path, compressedData);
    }

    @Override
    public byte[] getDataQuietly(String path) {
        try {
            return getData(path);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public byte[] getDecompressedDataQuietly(String path) {
        try {
            return getDecompressedData(path);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<String> listChildren(String path) throws Exception {
        return cf.getChildren().forPath(path);
    }

    @Override
    public List<String> listChildrenQuietly(String path) {
        try {
            return listChildren(path);
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public void delete(String path, boolean recursive) throws Exception {
        if (recursive) {
            deleteChildren(cf.getZookeeperClient().getZooKeeper(), path, true);
        } else {
            cf.delete().forPath(path);
        }
    }

    @Override
    public boolean deleteQuietly(String path, boolean recursive) {
        try {
            delete(path, recursive);
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    @Override
    public boolean deleteQuietly(String path) {
        try {
            delete(path, true);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean cleanDirectoryQuietly(String path) {
        try {
            List<String> children = listChildren(path);
            for (String child : children) {
                delete(path + "/" + child, true);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Recursively deletes children of a node.
     *
     * @param zookeeper  the client
     * @param path       path of the node to delete
     * @param deleteSelf flag that indicates that the node should also get deleted
     * @throws InterruptedException
     * @throws KeeperException
     */
    private void deleteChildren(ZooKeeper zookeeper, String path, boolean deleteSelf) throws InterruptedException,
            KeeperException {
        PathUtils.validatePath(path);

        List<String> children = zookeeper.getChildren(path, null);
        for (String child : children) {
            String fullPath = ZKPaths.makePath(path, child);
            deleteChildren(zookeeper, fullPath, true);
        }

        if (deleteSelf) {
            try {
                zookeeper.delete(path, -1);
            } catch (KeeperException.NotEmptyException e) {
                //someone has created a new child since we checked ... delete again.
                deleteChildren(zookeeper, path, deleteSelf);
            } catch (KeeperException.NoNodeException e) {
                // ignore... someone else has deleted the node it since we checked
            }
        }
    }

    @Override
    public ZkNodeCache createZkNodeCache(String path, boolean isDataCompressed) {
        return new CuratorZkNodeCache(cf, path, isDataCompressed);
    }

    @Override
    public ZkPathChildrenCache createPathChildrenCache(String path) {
        return new CuratorZkPathChildrenCache(cf, path);
    }

    @Override
    public ZkHeartbeatNode createHeartbeatNode(String path) {
        return new CuratorZkHeartbeatNode(cf, path);
    }

    @Override
    public InterProcessMutex createLock(String path) {
        return new InterProcessMutex(cf, path);
    }

    @Override
    public void stop() throws Exception {
        cf.close();
    }
}
