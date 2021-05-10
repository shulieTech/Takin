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


/**
 * 通过 zk 维持心跳的节点，如果成功在 zk 上注册节点，则表示存活，否则不存活。
 *
 * @author pamirs
 */
public interface ZkHeartbeatNode extends Lifecycle {

    /**
     * 获取心跳节点路径，如果未创建节点，返回 <code>null</code>
     *
     * @return
     */
    String getPath();

    /**
     * 设置心跳节点上的数据，如果节点存活，会触发更新 zk，非存活时会保存在内存中
     *
     * @param data
     */
    void setData(byte[] data) throws Exception;

    /**
     * 获取当前心跳节点在内存中缓存的心跳数据
     *
     * @return
     */
    byte[] getData();

    /**
     * 返回当前是否存活
     *
     * @return
     */
    boolean isAlive();

    /**
     * 开始心跳节点
     *
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * 删除节点，停止心跳
     */
    void stop() throws Exception;

    /**
     * 检查当前是否在运行状态
     */
    boolean isRunning();
}
