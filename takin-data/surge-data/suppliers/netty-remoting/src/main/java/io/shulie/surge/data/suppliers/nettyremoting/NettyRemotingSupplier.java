/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.surge.data.suppliers.nettyremoting;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.pamirs.pradar.log.parser.packet.Request;
import com.pamirs.pradar.log.parser.packet.Response;
import com.pamirs.pradar.remoting.RemotingServer;
import com.pamirs.pradar.remoting.netty.NettyCommandProcessor;
import com.pamirs.pradar.remoting.netty.NettyRemotingServer;
import com.pamirs.pradar.remoting.netty.NettyServerConfigurator;
import com.pamirs.pradar.remoting.protocol.*;
import com.pamirs.pradar.remoting.utils.RemotingThreadFactory;
import io.netty.channel.ChannelHandlerContext;
import io.shulie.surge.data.common.lifecycle.LifecycleObserver;
import io.shulie.surge.data.common.pool.DataPoolExecutors;
import io.shulie.surge.data.runtime.common.utils.ApiProcessor;
import io.shulie.surge.data.runtime.disruptor.RingBufferIllegalStateException;
import io.shulie.surge.data.runtime.processor.DataQueue;
import io.shulie.surge.data.runtime.supplier.DefaultMultiProcessorSupplier;
import io.shulie.surge.data.runtime.supplier.Supplier;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author vincent
 */
public final class NettyRemotingSupplier extends DefaultMultiProcessorSupplier {

    private static final Logger logger = LoggerFactory.getLogger(NettyRemotingSupplier.class);

    private int port;
    private static final String MIN = "MIN";
    private static final String MAX = "MAX";

    @Inject
    private ApiProcessor apiProcessor;

    @Inject
    @Named("netty.remoting.server.ports")
    protected String pradarServerPorts;

    @Inject
    @Named("netty.remoting.server.processCores")
    private int coreSize = 4;

    private RemotingServer remotingServer;

    /**
     * 开始获取数据
     *
     * @throws IllegalStateException Queue 尚未设置时抛出此异常
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        // 获取启动的端口
        Map<String, Integer> parsePortRange = parsePort();
        for (int index = parsePortRange.get(MIN); index <= parsePortRange.get(MAX); index++) {
            try {
                port = index;
                remotingServer = getRemotingServer(port);
            } catch (Throwable e) {
                logger.error("next port start " + index);
                continue;
            }
            // 启动成功以后就停止掉
            break;
        }
        super.start();
        apiProcessor.init();
    }

    /**
     * 初始化remoting server
     *
     * @return
     */
    private RemotingServer getRemotingServer(int port) {
        AtomicBoolean rejector = new AtomicBoolean(false);
        ProtocolFactorySelector selector = new DefaultProtocolFactorySelector();
        NettyServerConfigurator config = new NettyServerConfigurator();
        config.setListenPort(port);
        config.setServerSocketRcvBufSize(32 * 1024);
        config.setServerSocketSndBufSize(32 * 1024);
        ProtocolFactorySelector protocolFactorySelector = new DefaultProtocolFactorySelector();
        RemotingServer remotingServer = new NettyRemotingServer(protocolFactorySelector, config);
        remotingServer.registerDefaultProcessor(new NettyCommandProcessor() {
            @Override
            public RemotingCommand processCommand(ChannelHandlerContext ctx, RemotingCommand req) {
                RemotingCommand responseCommand = new RemotingCommand();
                /**
                 * 设置response
                 */
                responseCommand.setCode(CommandCode.SUCCESS);
                responseCommand.setVersion(req.getVersion());
                responseCommand.setProtocolCode(ProtocolCode.KRYO);

                Response response = new Response();
                response.setSuccess(true);
                ProtocolFactory factory = selector.select(req.getProtocolCode());
                try {
                    Request request = factory.decode(Request.class, req);
                    String content = new String(request.getBody());
                    String hostIp = request.getHostIp();
                    String dataVersion = request.getVersion();
                    Byte dataType = request.getDataType();
                    DataQueue queue = queueMap.get(String.valueOf(dataType));
                    Map<String, Object> header = Maps.newHashMap();
                    header.put("hostIp", hostIp);
                    header.put("dataVersion", dataVersion);
                    header.put("dataType", dataType);
                    Iterable<String> iterator = Splitter.on("\r\n").omitEmptyStrings().split(content);
                    List<String> list = Lists.newArrayList(iterator);
                    queue.publish(header, list);
                } catch (RingBufferIllegalStateException e) {
                    logger.error(e.getMessage());
                    response.setSuccess(false);
                    response.setErrorMsg(e.getMessage());
                    responseCommand.setCode(CommandCode.SYSTEM_BUSY);
                } catch (Throwable e) {
                    logger.error("logProcessor fail " + ExceptionUtils.getStackTrace(e));
                    response.setSuccess(false);
                    response.setErrorMsg(e.getMessage());
                    responseCommand.setCode(CommandCode.SYSTEM_ERROR);
                }
                //factory.encode(response, responseCommand);
                return responseCommand;
            }

            @Override
            public boolean reject() {
                try {
                    for (DataQueue dataQueue : queueMap.values()) {
                        dataQueue.canPublish(1000);
                    }
                    return false;
                } catch (RingBufferIllegalStateException e) {
                    logger.error(e.getMessage());
                    return true;
                }
            }
        }, Executors.newCachedThreadPool());
        remotingServer.start();
        return remotingServer;
    }

    private ExecutorService getDefaultExecutors(int coreSize, AtomicBoolean rejector) {
        return DataPoolExecutors.newDefaultNoQueueExecutors(coreSize, coreSize * 2, 3, TimeUnit.SECONDS, RemotingThreadFactory.
                newThreadFactory("RemotingServerProcessThreadPoolExecutor-%d", false), new NettyRejectPolicy(rejector));
    }

    public class NettyRejectPolicy implements RejectedExecutionHandler {
        private AtomicBoolean rejector;

        public NettyRejectPolicy(AtomicBoolean rejector) {
            this.rejector = rejector;
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            rejector.set(true);
        }
    }

    /**
     * 停止获取数据
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        remotingServer.shutdown();
    }

    /**
     * 检查当前是否在运行状态
     */
    @Override
    public boolean isRunning() {
        return super.isRunning();
    }

    /**
     * 获取开放的端口信息
     *
     * @return
     */
    private Map<String, Integer> parsePort() {
        Map<String, Integer> parseMap = Maps.newHashMap();
        try {
            String rangeStr = pradarServerPorts.substring(pradarServerPorts.indexOf("[") + 1, pradarServerPorts.lastIndexOf("]"));
            String[] rangeSplit = rangeStr.split(",");
            parseMap.put(MIN, Integer.parseInt(rangeSplit[0]));
            parseMap.put(MAX, Integer.parseInt(rangeSplit[1]));
        } catch (Throwable e) {
            logger.error("parse port fail");
            throw new RuntimeException("解析端口失败");
        }
        return parseMap;
    }


    @Override
    public void addObserver(LifecycleObserver<Supplier> observer) {
        super.addObserver(observer);
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }
}
