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
package com.shulie.instrument.simulator.core.server.jetty;

import com.shulie.instrument.simulator.api.GlobalSwitch;
import com.shulie.instrument.simulator.api.LoadMode;
import com.shulie.instrument.simulator.core.CoreConfigure;
import com.shulie.instrument.simulator.core.Simulator;
import com.shulie.instrument.simulator.core.server.CoreServer;
import com.shulie.instrument.simulator.core.server.jetty.servlet.ModuleHttpServlet;
import com.shulie.instrument.simulator.core.util.Initializer;
import com.shulie.instrument.simulator.core.util.LogbackUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

import static com.shulie.instrument.simulator.core.util.NetworkUtils.isPortInUsing;
import static java.lang.String.format;
import static org.eclipse.jetty.servlet.ServletContextHandler.NO_SESSIONS;

/**
 * Jetty实现的Http服务器
 */
public class JettyCoreServer implements CoreServer {

    private static volatile CoreServer coreServer;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Initializer initializer = new Initializer(true);

    private Server httpServer;
    private CoreConfigure config;
    private Simulator simulator;
    private ExecutorService loadUserModuleService;

    /**
     * 单例
     *
     * @return CoreServer单例
     */
    public static CoreServer getInstance() {
        if (null == coreServer) {
            synchronized (CoreServer.class) {
                if (null == coreServer) {
                    coreServer = new JettyCoreServer();
                }
            }
        }
        return coreServer;
    }

    @Override
    public boolean isBind() {
        return initializer.isInitialized();
    }

    @Override
    public void unbind() throws IOException {
        try {

            initializer.destroyProcess(new Initializer.Processor() {
                @Override
                public void process() throws Throwable {

                    if (null != httpServer) {

                        // stop http server
                        if (logger.isInfoEnabled()) {
                            logger.info("SIMULATOR: {} is stopping", JettyCoreServer.this);
                        }
                        httpServer.stop();
                        if (logger.isInfoEnabled()) {
                            logger.info("SIMULATOR: {} is stopped", JettyCoreServer.this);
                        }
                    }

                }
            });

            // destroy http server
            if (logger.isInfoEnabled()) {
                logger.info("SIMULATOR: {} is destroying", this);
            }
            while (!httpServer.isStopped()) {
            }
            httpServer.destroy();
            if (logger.isInfoEnabled()) {
                logger.info("SIMULATOR: {} is destroyed", this);
            }

        } catch (Throwable cause) {
            logger.warn("SIMULATOR: {} unBind failed.", this, cause);
            throw new IOException("unBind failed.", cause);
        }
    }

    @Override
    public InetSocketAddress getLocal() throws IOException {
        if (!isBind()
                || null == httpServer) {
            throw new IOException("server was not bind yet.");
        }

        SelectChannelConnector scc = null;
        final Connector[] connectorArray = httpServer.getConnectors();
        if (null != connectorArray) {
            for (final Connector connector : connectorArray) {
                if (connector instanceof SelectChannelConnector) {
                    scc = (SelectChannelConnector) connector;
                    break;
                }
            }
        }

        if (null == scc) {
            throw new IllegalStateException("not found SelectChannelConnector");
        }

        return new InetSocketAddress(
                scc.getHost(),
                scc.getLocalPort()
        );
    }

    /**
     * 初始化Jetty's ContextHandler
     */
    private void initHttpContextHandler() {
        final String namespace = config.getNamespace();
        final ServletContextHandler context = new ServletContextHandler(NO_SESSIONS);

        final String contextPath = "/" + namespace;
        context.setContextPath(contextPath);
        context.setClassLoader(getClass().getClassLoader());

        // module-http-servlet
        final String pathSpec = "/*";
        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: initializing http-handler. path={}", contextPath + "/*");
        }
        context.addServlet(
                new ServletHolder(new ModuleHttpServlet(config, simulator.getCoreModuleManager())),
                pathSpec
        );

        httpServer.setHandler(context);
    }

    private void initHttpServer() {

        final String serverIp = config.getServerIp();
        final int serverPort = config.getServerPort();

        // 防止端口可重用导致端口占用, server 还是能正常启动, 需要判断一下
        if (isPortInUsing(serverIp, serverPort)) {
            throw new IllegalStateException(format("address[%s:%s] already in using, server bind failed.",
                    serverIp,
                    serverPort
            ));
        }

        httpServer = new Server(new InetSocketAddress(serverIp, serverPort));
        QueuedThreadPool qtp = new QueuedThreadPool();
        /**
         * jetty线程设置为daemon，防止应用启动失败进程无法正常退出
         */
        qtp.setDaemon(true);
        qtp.setName("simulator-jetty-qtp-" + qtp.hashCode());

        httpServer.setThreadPool(qtp);


    }

    @Override
    public synchronized void bind(final CoreConfigure config, final Instrumentation inst) throws IOException {
        this.config = config;
        this.loadUserModuleService = new ThreadPoolExecutor(1, 1, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "Load-User-Module-Thread");
                t.setDaemon(true);
                return t;
            }
        });
        try {
            initializer.initProcess(new Initializer.Processor() {
                @Override
                public void process() throws Throwable {
                    LogbackUtils.init(
                            config.getNamespace(),
                            config.getConfigLibPath() + File.separator + "simulator-logback.xml"
                    );
                    if (logger.isInfoEnabled()) {
                        logger.info("SIMULATOR: initializing server. config={}", config);
                    }
                    simulator = new Simulator(config, inst);
                    initHttpServer();
                    initHttpContextHandler();
                    httpServer.start();
                }
            });

            // 初始化加载所有的模块
            GlobalSwitch.setModuleLoader(new GlobalSwitch.ModuleLoader() {
                @Override
                public void load(Runnable runnable) {
                    loadUserModuleService.submit(runnable);
                }

                @Override
                public void unload(Runnable runnable) {
                }
            });
            this.loadUserModuleService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        simulator.getCoreModuleManager().reset();
                    } catch (Throwable cause) {
                        logger.warn("SIMULATOR: reset occur error when initializing.", cause);
                    }
                }
            });

            final InetSocketAddress local = getLocal();
            if (logger.isInfoEnabled()) {
                System.out.println(String.format("SIMULATOR: initialized server. actual bind to %s:%s", local.getHostName(), local.getPort()));
                logger.info("SIMULATOR: initialized server. actual bind to {}:{}",
                        local.getHostName(),
                        local.getPort()
                );
            }

        } catch (Throwable cause) {
            // 这里会抛出到目标应用层，所以在这里留下错误信息
            logger.error("SIMULATOR: initialize server failed.", cause);

            if (config.getLaunchMode() == LoadMode.ATTACH) {
                // 对外抛出到目标应用中
                throw new IOException("server bind failed.", cause);
            }
            //AGENT模式直接忽略，不让上层应用感知，只需要将日志打印出来即可
            return;
        }

        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: {} bind success.", this);
        }
    }

    @Override
    public void destroy() {
        // 关闭JVM-SIMULATOR
        if (null != simulator) {
            simulator.destroy();
        }

        // 关闭HTTP服务器
        if (isBind()) {
            try {
                unbind();
            } catch (IOException e) {
                logger.warn("SIMULATOR: {} unBind failed when destroy.", this, e);
            }
        }

        // 关闭LOGBACK
        LogbackUtils.destroy();
    }

    @Override
    public String toString() {
        return format("server[%s:%s]", config.getServerIp(), config.getServerPort());
    }
}
