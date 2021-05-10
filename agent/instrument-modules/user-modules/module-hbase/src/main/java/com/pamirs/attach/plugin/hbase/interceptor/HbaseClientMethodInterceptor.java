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
package com.pamirs.attach.plugin.hbase.interceptor;

import com.pamirs.attach.plugin.hbase.util.SocketAddressUtils;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.interceptor.AroundInterceptor;
import com.shulie.instrument.simulator.api.listener.ext.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;

/**
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: com.pamirs.attach.plugin.redis.interceptor
 * @Date 2019-09-11 21:33
 */
public class HbaseClientMethodInterceptor extends AroundInterceptor {
    private final static Logger LOGGER = LoggerFactory.getLogger(HbaseClientMethodInterceptor.class.getName());

    @Override
    public void doAfter(Advice advice) {
        try {
            Object[] args = advice.getParameterArray();
            String ip = getEndPoint(args);
            int port = getPort(args);
            Pradar.remoteIp(ip);
            Pradar.remotePort(port);
        } catch (Throwable e) {
        }
    }

    /**
     * Gets end point.
     *
     * @param args the args
     * @return the end point
     */
    protected String getEndPoint(Object[] args) {
        Object param = args[0];
        String remoteIp = "";
        if (null != param) {
            if ("org.apache.hadoop.hbase.ipc.ConnectionId".equals(param.getClass().getName())) {
                try {
                    Field address = param.getClass().getDeclaredField("address");
                    address.setAccessible(true);
                    Object addrs = address.get(param);
                    if (addrs instanceof InetSocketAddress) {
                        remoteIp = SocketAddressUtils.getHostNameFirst((InetSocketAddress) addrs);
                    }
                } catch (Throwable e) {
                    LOGGER.error("", e);
                }

            }
        }
        if (null == remoteIp || "".equals(remoteIp)) {
            if (args != null && args.length > 5) {
                if (args[5] instanceof InetSocketAddress) {
                    remoteIp = SocketAddressUtils.getHostNameFirst((InetSocketAddress) args[5]);
                }
            }
        }
        return remoteIp;
    }

    /**
     * Gets end point.
     *
     * @param args the args
     * @return the end point
     */
    protected int getPort(Object[] args) {
        Object param = args[0];
        int port = -1;
        if (null != param) {
            if ("org.apache.hadoop.hbase.ipc.ConnectionId".equals(param.getClass().getName())) {
                try {
                    Field address = param.getClass().getDeclaredField("address");
                    address.setAccessible(true);
                    Object addrs = address.get(param);
                    if (addrs instanceof InetSocketAddress) {
                        port = ((InetSocketAddress) addrs).getPort();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            }
        }
        return port;
    }
}
