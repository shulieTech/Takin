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
package com.shulie.instrument.module.register.zk;

import java.util.List;
import java.util.concurrent.Executor;

/**
 * 监视 ZooKeeper 节点的子节点的增加、删除更新，并把内容缓存在内存中。
 * 创建方式：{@link ZkClient#createPathChildrenCache(String)}
 *
 * @author pamirs
 */
public interface ZkPathChildrenCache extends Lifecycle {

    /**
     * 获取缓存在内存中的子节点，如果节点不存在，返回 <code>null</code>
     *
     * @return
     */
    List<String> getChildren();

    List<String> getAddChildren();

    List<String> getDeleteChildren();

    /**
     * 获取监视的节点路径
     *
     * @return
     */
    String getPath();

    /**
     * 设置更新回调，回调使用 zk 线程执行，需要业务保证逻辑非常简短。
     *
     * @param runnable
     */
    void setUpdateListener(Runnable runnable);

    /**
     * 设置更新回调使用指定的 executor 执行。
     *
     * @param executor
     */
    void setUpdateExecutor(Executor executor);

    /**
     * 开始监视更新，第一次更新异步的方式进行，第一次更新会触发回调
     *
     * @throws Exception
     */
    @Override
    void start() throws Exception;

    /**
     * 开始监视更新，并完成第一次阻塞更新，第一次更新不会回调
     *
     * @throws Exception
     */
    void startAndRefresh() throws Exception;

    /**
     * 停止监视更新，回调会被清空
     */
    @Override
    void stop();

    /**
     * 手动刷新，不会触发回调
     *
     * @throws Exception
     */
    void refresh() throws Exception;

    /**
     * 检查当前是否在运行状态
     */
    boolean isRunning();
}
