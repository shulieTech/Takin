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
package com.shulie.instrument.simulator.core.server;

import com.shulie.instrument.simulator.core.CoreConfigure;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;

/**
 * 内核服务器
 */
public interface CoreServer {

    /**
     * 判断服务器是否已经绑定端口
     *
     * @return 服务器是否已经绑定端口
     */
    boolean isBind();

    /**
     * 服务器解除端口绑定
     *
     * @throws IOException 解除绑定失败
     */
    void unbind() throws IOException;

    /**
     * 获取服务器绑定本地网络信息
     *
     * @return 服务器绑定本地网络信息
     * @throws IOException 绑定失败
     */
    InetSocketAddress getLocal() throws IOException;

    /**
     * 服务器绑定端口
     *
     * @param config 内核配置信息
     * @param inst   inst
     * @throws IOException 绑定失败
     */
    void bind(CoreConfigure config, Instrumentation inst) throws IOException;

    /**
     * 销毁服务器
     */
    void destroy();

}
