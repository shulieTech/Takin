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
package com.shulie.instrument.simulator.agent.core.register.impl;

import com.alibaba.fastjson.JSON;
import com.shulie.instrument.simulator.agent.core.register.AgentStatus;
import com.shulie.instrument.simulator.agent.core.register.AgentStatusListener;
import com.shulie.instrument.simulator.agent.core.register.Register;
import com.shulie.instrument.simulator.agent.core.register.RegisterOptions;
import com.shulie.instrument.simulator.agent.core.util.AddressUtils;
import com.shulie.instrument.simulator.agent.core.util.PidUtils;
import com.shulie.instrument.simulator.agent.core.util.PropertyPlaceholderHelper;
import com.shulie.instrument.simulator.agent.core.zk.ZkClient;
import com.shulie.instrument.simulator.agent.core.zk.ZkHeartbeatNode;
import com.shulie.instrument.simulator.agent.core.zk.ZkNodeStat;
import com.shulie.instrument.simulator.agent.core.zk.impl.NetflixCuratorZkClientFactory;
import com.shulie.instrument.simulator.agent.core.zk.impl.ZkClientSpec;
import com.shulie.instrument.simulator.agent.spi.config.AgentConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * zookeeper 注册器实现
 *
 * @author xiaobin.zfb
 * @since 2020/8/20 9:55 上午
 */
public class ZookeeperRegister implements Register {
    private final static Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegister.class.getName());
    /**
     * 基础路径
     */
    private String basePath;
    /**
     * app 名称
     */
    private String appName;
    /**
     * 心跳节点路径
     */
    private String heartbeatPath;
    /**
     * zk 客户端
     */
    private ZkClient zkClient;
    /**
     * 心跳节点
     */
    private ZkHeartbeatNode heartbeatNode;
    /**
     * 定时服务，定时上报
     */
    private ScheduledExecutorService executorService;
    private AtomicBoolean isStarted = new AtomicBoolean(false);

    private AgentConfig agentConfig;

    public ZookeeperRegister(AgentConfig agentConfig) {
        this.agentConfig = agentConfig;
    }

    private byte[] getHeartbeatDatas() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("address", AddressUtils.getLocalAddress());
        map.put("host", AddressUtils.getHostName());
        map.put("name", PidUtils.getName());
        map.put("pid", String.valueOf(PidUtils.getPid()));
        map.put("agentId", getAgentId());
        map.put("agentStatus", AgentStatus.getAgentStatus());
        map.put("errorCode", AgentStatus.getErrorCode());
        map.put("errorMsg", AgentStatus.getErrorMessage());
        map.put("agentLanguage", "JAVA");
        map.put("agentVersion", agentConfig.getAgentVersion());
        String str = JSON.toJSONString(map);
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str.getBytes();
        }
    }

    /**
     * 获取agentId
     *
     * @return
     */
    public String getAgentId() {
        String agentId = agentConfig.getAgentId();
        if (StringUtils.isBlank(agentId)) {
            StringBuilder builder = new StringBuilder();
            builder.append(AddressUtils.getLocalAddress()).append('-').append(PidUtils.getPid());
            return builder.toString();
        } else {
            Properties properties = new Properties();
            properties.setProperty("pid", String.valueOf(PidUtils.getPid()));
            properties.setProperty("hostname", AddressUtils.getHostName());
            properties.setProperty("ip", AddressUtils.getLocalAddress());
            PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");
            return propertyPlaceholderHelper.replacePlaceholders(agentId, properties);
        }
    }

    @Override
    public String getName() {
        return "zookeeper";
    }

    @Override
    public void init(RegisterOptions registerOptions) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("prepare to init zookeeper register.");
        }

        if (registerOptions == null) {
            throw new NullPointerException("RegisterOptions is null");
        }
        this.basePath = registerOptions.getRegisterBasePath();
        this.appName = registerOptions.getAppName();
        String registerBasePath = null;
        if (StringUtils.endsWith(basePath, "/")) {
            registerBasePath = this.basePath + appName;
        } else {
            registerBasePath = this.basePath + '/' + appName;
        }
        try {
            ZkClientSpec zkClientSpec = new ZkClientSpec();
            zkClientSpec.setZkServers(registerOptions.getZkServers());
            zkClientSpec.setConnectionTimeoutMillis(registerOptions.getConnectionTimeoutMillis());
            zkClientSpec.setSessionTimeoutMillis(registerOptions.getSessionTimeoutMillis());
            zkClientSpec.setThreadName("heartbeat");
            this.zkClient = NetflixCuratorZkClientFactory.getInstance().create(zkClientSpec);
        } catch (RuntimeException e) {
            throw e;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        String client = getAgentId();
        try {
            this.zkClient.ensureDirectoryExists(registerBasePath);
        } catch (Exception e) {
            LOGGER.error("ensureDirectoryExists err:{}", registerBasePath, e);
        }
        this.heartbeatPath = registerBasePath + '/' + client;
        cleanExpiredNodes(registerBasePath);
        this.heartbeatNode = this.zkClient.createHeartbeatNode(this.heartbeatPath);
        AgentStatus.registerListener(new AgentStatusListener() {
            @Override
            public void onListen() {
                refresh();
            }
        });
        this.executorService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "Scan-App-Jar-Thread");
                t.setDaemon(true);
                return t;
            }
        });
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("init zookeeper register successful.");
        }
    }

    /**
     * 清除过期的节点,防止 zookeeper 低版本时有版本不致的 bug 导致过期的心跳节点删除不掉的问题
     *
     * @param path
     */
    private void cleanExpiredNodes(String path) {
        try {
            List<String> children = this.zkClient.listChildren(path);
            if (children != null) {
                for (String node : children) {
                    ZkNodeStat stat = this.zkClient.getStat(path + '/' + node);
                    if (stat == null) {
                        continue;
                    }
                    if (stat.getEphemeralOwner() == 0) {
                        zkClient.deleteQuietly(path + '/' + node);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.warn("clean expired register node error.", e);
        }
    }

    @Override
    public String getPath() {
        return heartbeatPath;
    }

    @Override
    public void start() {
        if (isStarted.get()) {
            return;
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("prepare to start zookeeper register.");
        }
        try {
            this.heartbeatNode.start();
            this.heartbeatNode.setData(getHeartbeatDatas());
            LOGGER.info("start zookeeper register successful.");
        } catch (Throwable e) {
            LOGGER.error("register node to zk for heartbeat node err: {}!", heartbeatPath, e);
        }
        isStarted.compareAndSet(false, true);
    }

    @Override
    public void stop() {
        if (!isStarted.compareAndSet(true, false)) {
            return;
        }
        if (this.heartbeatNode != null) {
            try {
                this.heartbeatNode.stop();
            } catch (Throwable e) {
                LOGGER.error("unregister node to zk for heartbeat node err: {}!", heartbeatPath, e);
            }
        }
        this.zkClient.deleteQuietly(this.heartbeatPath);
        try {
            this.zkClient.stop();
        } catch (Exception e) {
            LOGGER.error("stop zkClient failed!", e);
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    @Override
    public void refresh() {
        if (isStarted.get()) {
            try {
                heartbeatNode.setData(getHeartbeatDatas());
            } catch (Exception e) {
                LOGGER.error("[register] refresh node data to zk for heartbeat node err: {}!", heartbeatPath, e);
            }
        } else {
            /**
             * 使用定时线程去更新数据，防止连接 zk 时出现问题数据没有被更新上去
             */
            executorService.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        heartbeatNode.setData(getHeartbeatDatas());
                    } catch (Exception e) {
                        LOGGER.error("[register] refresh node data to zk for heartbeat node err: {}!", heartbeatPath, e);
                        executorService.schedule(this, 5, TimeUnit.SECONDS);
                    }
                }
            }, 0, TimeUnit.SECONDS);
        }

    }
}
