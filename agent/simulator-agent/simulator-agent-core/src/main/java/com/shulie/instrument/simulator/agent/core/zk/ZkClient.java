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
package com.shulie.instrument.simulator.agent.core.zk;


import com.netflix.curator.framework.recipes.locks.InterProcessMutex;

import java.util.List;

/**
 * ZooKeeper 客户端实现封装
 *
 * @author xiaobin@shulie.io
 * @since 1.0.0
 */
public interface ZkClient extends Stoppable {

    /**
     * 定义了 znode 节点的创建模式
     */
    public static enum CreateMode {
        /**
         * The znode will not be automatically deleted upon client's disconnect.
         */
        PERSISTENT,
        /**
         * The znode will not be automatically deleted upon client's disconnect,
         * and its name will be appended with a monotonically increasing number.
         */
        PERSISTENT_SEQUENTIAL,
        /**
         * The znode will be deleted upon the client's disconnect.
         */
        EPHEMERAL,
        /**
         * The znode will be deleted upon the client's disconnect, and its name
         * will be appended with a monotonically increasing number.
         */
        EPHEMERAL_SEQUENTIAL;
    }

    /**
     * 创建目录节点
     *
     * @param path
     * @param createMode
     * @throws Exception
     */
    void createDirectory(String path, CreateMode createMode) throws Exception;

    /**
     * 如果目录节点不存在，创建之
     *
     * @param path
     * @throws Exception
     */
    void ensureDirectoryExists(String path) throws Exception;

    /**
     * 如果节点的父节点不存在，创建之
     *
     * @param path
     * @throws Exception
     */
    void ensureParentExists(String path) throws Exception;

    /**
     * 创建节点
     *
     * @param path
     * @param data
     * @param createMode
     * @return 写入的节点数据大小
     * @throws Exception
     */
    int createNode(String path, byte[] data, CreateMode createMode) throws Exception;

    /**
     * 更新已存在的节点的数据
     *
     * @param path
     * @param data
     * @return 写入的节点数据大小
     * @throws Exception
     */
    int updateData(String path, byte[] data) throws Exception;

    /**
     * 把数据保存到节点中，如果节点不存在，则创建之
     *
     * @param path
     * @param data
     * @param createMode
     * @return 写入的节点数据大小
     * @throws Exception
     */
    int saveData(String path, byte[] data, CreateMode createMode) throws Exception;

    /**
     * 创建节点，数据使用压缩
     *
     * @param path
     * @param data
     * @param createMode
     * @return 写入的节点数据大小
     * @throws Exception
     */
    int createCompressedNode(String path, byte[] data, CreateMode createMode) throws Exception;

    /**
     * 更新已存在的节点的数据，数据使用压缩
     *
     * @param path
     * @param data
     * @return 写入的节点数据大小
     * @throws Exception
     */
    int updateCompressedData(String path, byte[] data) throws Exception;

    /**
     * 把数据保存到节点中，如果节点不存在，则创建之，数据使用压缩
     *
     * @param path
     * @param data
     * @param createMode
     * @return 写入的节点数据大小
     * @throws Exception
     */
    int saveCompressedData(String path, byte[] data, CreateMode createMode) throws Exception;

    /**
     * 检查节点是否存在
     *
     * @param path
     * @return
     * @throws Exception
     */
    boolean exists(String path) throws Exception;

    /**
     * 获取节点信息
     *
     * @param path
     * @return
     * @throws Exception
     */
    ZkNodeStat getStat(String path) throws Exception;

    /**
     * 获取节点的数据
     *
     * @param path
     * @return
     * @throws Exception
     */
    byte[] getData(String path) throws Exception;

    /**
     * 获取节点的数据，同时对数据进行解压缩
     *
     * @param path
     * @return
     * @throws Exception
     */
    byte[] getDecompressedData(String path) throws Exception;

    /**
     * 获取节点的数据，如果获取失败返回 <code>null</code>，不抛出异常
     *
     * @param path
     * @return
     */
    byte[] getDataQuietly(String path);

    /**
     * 获取节点的数据，同时对数据进行解压缩，如果获取失败或无此节点，返回 <code>null</code>，不抛出异常
     *
     * @param path
     * @return
     */
    byte[] getDecompressedDataQuietly(String path);

    /**
     * 返回节点下一级的子节点
     *
     * @param path
     * @return
     * @throws Exception
     */
    List<String> listChildren(String path) throws Exception;

    /**
     * 返回节点下一级的子节点，如果获取失败或无此节点，返回 <code>null</code>，不抛出异常
     *
     * @param path
     * @return
     */
    List<String> listChildrenQuietly(String path);

    /**
     * 删除节点，recursive 表示是否递归删除下一级子节点
     *
     * @param path
     * @param recursive
     * @throws Exception
     */
    void delete(String path, boolean recursive) throws Exception;

    /**
     * 删除节点，删除成功返回 <code>true</code>，否则返回 <code>false</code>，不抛出异常
     *
     * @param path
     * @return
     */
    boolean deleteQuietly(String path);

    /**
     * 删除节点，删除成功返回 <code>true</code>，否则返回 <code>false</code>，不抛出异常
     *
     * @param path
     * @param recursive
     * @return
     */
    boolean deleteQuietly(String path, boolean recursive);

    /**
     * 删除节点下面的所有子节点，删除成功返回 <code>true</code>，
     * 否则返回 <code>false</code>，不抛出异常
     *
     * @param path
     * @return
     */
    boolean cleanDirectoryQuietly(String path);

    /**
     * 获取当前的 Zk 服务器列表
     *
     * @return
     */
    String getZkServers();

    /**
     * 创建节点的本地缓存 ZkNodeCache
     *
     * @param path
     * @param isDataCompressed
     * @return
     */
    ZkNodeCache createZkNodeCache(String path, boolean isDataCompressed);

    /**
     * 创建监控子节点的增减变化的本地缓存 ZkPathChildrenCache
     *
     * @param path
     * @return
     */
    ZkPathChildrenCache createPathChildrenCache(String path);

    /**
     * 创建心跳节点
     *
     * @param path
     * @return
     */
    ZkHeartbeatNode createHeartbeatNode(String path);

    /**
     * 创建一个分布式锁
     *
     * @param path
     * @return
     */
    InterProcessMutex createLock(String path);
}
