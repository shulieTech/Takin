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
package com.shulie.instrument.simulator.core.util;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 用于网络通讯的工具类
 */
public class NetworkUtils {

    /***
     * 测试主机Host的port端口是否被使用
     * @param host 指定IP
     * @param port 指定端口
     * @return TRUE:端口已经被占用;FALSE:端口尚未被占用
     */
    public static boolean isPortInUsing(String host, int port) {
        Socket socket = null;
        try {
            final InetAddress Address = InetAddress.getByName(host);
            socket = new Socket(Address, port);  //建立一个Socket连接
            return socket.isConnected();
        } catch (Throwable cause) {
            // ignore
        } finally {
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }

}
