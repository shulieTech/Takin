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
package com.pamirs.attach.plugin.es.shadowserver.utils;

import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.pamirs.pradar.exception.PressureMeasureError;
import org.apache.http.HttpHost;
import org.elasticsearch.client.Node;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;

/**
 * @author jirenhe | jirenhe@shulie.io
 * @since 2021/04/12 3:14 下午
 */
public class HostAndPort {

    private final String host;

    private final int port;

    public static final boolean usingInetSocket = TransportAddress.class.isInterface();

    private static volatile Constructor inetSocketConstructor;

    public HostAndPort(String address) {
        String[] tmp = address.split(":");
        if (tmp.length != 2) {
            throw new IllegalArgumentException("wrong address!");
        }
        this.host = tmp[0];
        this.port = Integer.parseInt(tmp[1]);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Node toNode() {
        return new Node(toHttpHost());
    }

    public TransportAddress toTransportAddress() throws UnknownHostException {
        if (usingInetSocket) {
            Constructor constructor = getInetSocketConstructor();
            try {
                return (TransportAddress)constructor.newInstance(InetAddress.getByName(host), port);
            } catch (Exception e) {
                throw new PressureMeasureError("无法构造InetSocketTransportAddress，当前版本不兼容！");
            }
        } else {
            return new TransportAddress(InetAddress.getByName(host), port);
        }
    }

    private Constructor getInetSocketConstructor() {
        if (inetSocketConstructor == null) {
            synchronized (HostAndPort.class) {
                if (inetSocketConstructor == null) {
                    try {
                        inetSocketConstructor = InetSocketTransportAddress.class.getConstructor(InetAddress.class,
                            int.class);
                    } catch (NoSuchMethodException e) {
                        throw new PressureMeasureError("无法构造InetSocketTransportAddress，当前版本不兼容！");
                    }
                }
            }
        }
        return inetSocketConstructor;
    }

    public HttpHost toHttpHost() {
        return new HttpHost(this.host, this.port);
    }
}
