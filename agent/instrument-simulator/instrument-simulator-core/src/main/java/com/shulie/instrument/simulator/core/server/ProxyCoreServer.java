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
import com.shulie.instrument.simulator.core.server.jetty.JettyCoreServer;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;

public class ProxyCoreServer implements CoreServer {

    private final static Class<? extends CoreServer> classOfCoreServerImpl
            = JettyCoreServer.class;

    private final CoreServer proxy;

    private ProxyCoreServer(CoreServer proxy) {
        this.proxy = proxy;
    }


    @Override
    public boolean isBind() {
        return proxy.isBind();
    }

    @Override
    public void unbind() throws IOException {
        proxy.unbind();
    }

    @Override
    public InetSocketAddress getLocal() throws IOException {
        return proxy.getLocal();
    }

    @Override
    public void bind(CoreConfigure config, Instrumentation inst) throws IOException {
        proxy.bind(config, inst);
    }

    @Override
    public void destroy() {
        proxy.destroy();
    }

    @Override
    public String toString() {
        return "proxy:" + proxy.toString();
    }

    public static CoreServer getInstance() {
        try {
            return new ProxyCoreServer(
                    (CoreServer) classOfCoreServerImpl
                            .getMethod("getInstance")
                            .invoke(null)
            );
        } catch (Throwable cause) {
            throw new RuntimeException(cause);
        }
    }

}
