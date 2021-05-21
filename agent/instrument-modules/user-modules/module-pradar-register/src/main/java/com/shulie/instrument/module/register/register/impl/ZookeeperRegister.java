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
package com.shulie.instrument.module.register.register.impl;

import com.alibaba.fastjson.JSON;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarCoreUtils;
import com.pamirs.pradar.PradarSwitcher;
import com.pamirs.pradar.common.PropertyPlaceholderHelper;
import com.pamirs.pradar.common.RuntimeUtils;
import com.pamirs.pradar.event.ErrorEvent;
import com.pamirs.pradar.event.Event;
import com.pamirs.pradar.event.PradarSwitchEvent;
import com.pamirs.pradar.exception.PradarException;
import com.shulie.instrument.module.register.register.Register;
import com.shulie.instrument.module.register.register.RegisterOptions;
import com.shulie.instrument.module.register.zk.ZkClient;
import com.shulie.instrument.module.register.zk.ZkHeartbeatNode;
import com.shulie.instrument.module.register.zk.ZkNodeStat;
import com.shulie.instrument.module.register.zk.impl.NetflixCuratorZkClientFactory;
import com.shulie.instrument.module.register.zk.impl.ZkClientSpec;
import com.shulie.instrument.simulator.api.executors.ExecutorServiceFactory;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Description
 * @Author xiaobin.zfb
 * @mail xiaobin@shulie.io
 * @Date 2020/8/20 9:55 上午
 */
public class ZookeeperRegister implements Register {
    private final static Logger LOGGER = LoggerFactory.getLogger(ZookeeperRegister.class.getName());
    private String basePath;
    private String appName;
    private String heartbeatPath;
    private ZkClient zkClient;
    private ZkHeartbeatNode heartbeatNode;
    private String md5;
    private SimulatorConfig simulatorConfig;
    private Set<String> jars;
    private AtomicBoolean isStarted = new AtomicBoolean(false);
    private final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();

    /**
     * 获取agentId
     *
     * @return
     */
    public String getAgentId() {
        String agentId = simulatorConfig.getAgentId();
        if (StringUtils.isBlank(agentId)) {
            return new StringBuilder(PradarCoreUtils.getLocalAddress()).append("-").append(RuntimeUtils.getPid()).toString();
        } else {
            Properties properties = new Properties();
            properties.setProperty("pid", String.valueOf(RuntimeUtils.getPid()));
            properties.setProperty("hostname", PradarCoreUtils.getHostName());
            properties.setProperty("ip", PradarCoreUtils.getLocalAddress());
            PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("${", "}");
            return propertyPlaceholderHelper.replacePlaceholders(agentId, properties);
        }
    }

    private byte[] getHeartbeatDatas() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("address", PradarCoreUtils.getLocalAddress());
        map.put("host", PradarCoreUtils.getHostName());
        map.put("name", RuntimeUtils.getName());
        map.put("pid", String.valueOf(RuntimeUtils.getPid()));
        map.put("agentId", getAgentId());
        map.put("agentVersion", simulatorConfig.getAgentVersion());
        map.put("simulatorVersion", simulatorConfig.getSimulatorVersion());
        map.put("md5", md5);
        map.put("agentStatus", "INSTALLED");
        //服务的 url
        String serviceUrl = "http://" + simulatorConfig.getServerAddress().getAddress().getHostAddress() + ":" + simulatorConfig.getServerAddress().getPort() + "/simulator/" + this.simulatorConfig.getNamespace() + "/module/http";
        map.put("service", serviceUrl);
        map.put("port", String.valueOf(simulatorConfig.getServerAddress().getPort()));
        map.put("status", String.valueOf(PradarSwitcher.isClusterTestEnabled()));
        if (PradarSwitcher.isClusterTestEnabled()) {
            map.put("errorCode", "");
            map.put("errorMsg", "");
        } else {
            map.put("errorCode", StringUtils.defaultIfBlank(PradarSwitcher.getErrorCode(), ""));
            map.put("errorMsg", StringUtils.defaultIfBlank(PradarSwitcher.getErrorMsg(), ""));
        }
        map.put("agentLanguage", "JAVA");
        map.put("userId", Pradar.PRADAR_USER_ID);
        map.put("jars", toJarFileString(jars));
        String str = JSON.toJSONString(map);
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            return str.getBytes();
        }
    }

    @Override
    public String getName() {
        return "zookeeper";
    }

    private String toJarFileString(Set<String> jars) {
        if (jars == null || jars.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String jar : jars) {
            builder.append(jar).append(';');
        }
        return builder.toString();
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
        } catch (Throwable e) {
            LOGGER.warn("clean expired register node error.", e);
        }
    }

    private Set<String> loadAllJars() {
        String classPath = runtimeMXBean.getClassPath();
        String[] files = StringUtils.split(classPath, File.pathSeparator);
        Set<String> list = new HashSet<String>();
        String javaHome = System.getProperty("java.home");
        String simulatorHome = simulatorConfig.getSimulatorHome();
        String tmpDir = System.getProperty("java.io.tmpdir");
        for (String file : files) {
            /**
             * 如果是 jdk 的 jar 包，过滤掉
             */
            if (StringUtils.isNotBlank(javaHome) && StringUtils.startsWith(file, javaHome)) {
                continue;
            }
            /**
             * 如果是仿真器的 jar 包，过滤掉
             */
            if (StringUtils.startsWith(file, simulatorHome)) {
                continue;
            }
            /**
             * 如果是监时目录加载的 jar 包，则过滤掉, simulator 所有的扩展 jar 包
             * 都会从临时目录加载
             */
            if (StringUtils.isNotBlank(tmpDir) && StringUtils.startsWith(file, tmpDir)) {
                continue;
            }

            /**
             * 如果 jar包是这一些打头的，也过滤掉
             */
            if (StringUtils.startsWith(file, "pradar-")
                    || StringUtils.startsWith(file, "simulator-")
                    || StringUtils.startsWith(file, "module-")) {
                continue;
            }
            list.add(file);
        }
        return list;
    }

    @Override
    public void init(RegisterOptions registerOptions) {
        if (registerOptions == null) {
            throw new NullPointerException("RegisterOptions is null");
        }
        this.basePath = registerOptions.getRegisterBasePath();
        this.appName = registerOptions.getAppName();
        this.md5 = registerOptions.getMd5();
        this.simulatorConfig = registerOptions.getSimulatorConfig();
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("[pradar-register] prepare to init register zookeeper node. {}", getAgentId());
        }
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
        } catch (PradarException e) {
            LOGGER.error("[pradar-register] ZookeeperRegister init error.", e);
            throw e;
        } catch (Throwable e) {
            LOGGER.error("[pradar-register] ZookeeperRegister init error.", e);
            throw new PradarException(e);
        }
        String client = Pradar.AGENT_ID;
        try {
            this.zkClient.ensureDirectoryExists(registerBasePath);
        } catch (Throwable e) {
            LOGGER.error("[register] ensureDirectoryExists err:{}", registerBasePath, e);
        }
        this.heartbeatPath = registerBasePath + '/' + client;
        cleanExpiredNodes(registerBasePath);
        this.heartbeatNode = this.zkClient.createHeartbeatNode(this.heartbeatPath);
        PradarSwitcher.registerListener(new PradarSwitcher.PradarSwitcherListener() {
            @Override
            public void onListen(Event event) {
                if (event instanceof PradarSwitchEvent || event instanceof ErrorEvent) {
                    refresh();
                }
            }
        });
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("[pradar-register] init register zookeeper node successful. {}", getAgentId());
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
            LOGGER.info("[pradar-register] prepare to register zookeeper node. {}", getAgentId());
        }
        try {
            this.jars = loadAllJars();
            this.heartbeatNode.start();
            this.heartbeatNode.setData(getHeartbeatDatas());
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("[pradar-register] register zookeeper node successful. {}", getAgentId());
            }
        } catch (Throwable e) {
            LOGGER.error("[pradar-register] register node to zk for heartbeat node err: {}!", heartbeatPath, e);
        }
        //5秒扫描一次当前应用所有加载的jar列表
        ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.schedule(new Runnable() {
            @Override
            public void run() {
                if (!isStarted.get()) {
                    return;
                }
                /**
                 * 如果还未启动则下次再执行
                 */
                if (!heartbeatNode.isAlive() || !heartbeatNode.isRunning()) {
                    ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.schedule(this, 5, TimeUnit.SECONDS);
                    return;
                }

                try {
                    if (!zkClient.exists(heartbeatPath)) {
                        zkClient.ensureDirectoryExists(heartbeatPath);
                    }
                } catch (Throwable e) {
                    LOGGER.error("[pradar-register] zk ensureDirectoryExists err: {}!", heartbeatPath);
                }

                Set<String> jarFiles = loadAllJars();
                if (!collectionEquals(jarFiles, jars)) {
                    jars = jarFiles;
                }
                try {
                    heartbeatNode.setData(getHeartbeatDatas());
                } catch (Throwable e) {
                    LOGGER.error("[pradar-register] update heartbeat node agent data err: {}!", heartbeatPath, e);
                }
                ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.schedule(this, 10, TimeUnit.SECONDS);
            }
        }, 5, TimeUnit.SECONDS);
        isStarted.compareAndSet(false, true);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("[pradar-register] start to register zookeeper node successful. {}", getAgentId());
        }
    }

    public static boolean collectionEquals(Collection source, Collection target) {
        if (source == target) {
            return true;
        }
        if (source == null || target == null) {
            return false;
        }
        return source.equals(target);
    }

    @Override
    public void stop() {
        if (!isStarted.compareAndSet(true, false)) {
            return;
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("[pradar-register] prepare to stop register zookeeper node. {}", getAgentId());
        }
        if (this.heartbeatNode != null) {
            try {
                this.heartbeatNode.stop();
            } catch (Throwable e) {
                LOGGER.error("[pradar-register] unregister node to zk for heartbeat node err: {}!", heartbeatPath, e);
            }
        }
        this.zkClient.deleteQuietly(this.heartbeatPath);
        try {
            this.zkClient.stop();
        } catch (Throwable e) {
            LOGGER.error("[register] stop zkClient failed!", e);
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("[pradar-register] stop register zookeeper node successful. {}", getAgentId());
        }
    }

    @Override
    public void refresh() {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("[pradar-register] prepare to refresh register zookeeper node. {}", getAgentId());
        }
        if (isStarted.get()) {
            try {
                heartbeatNode.setData(getHeartbeatDatas());
            } catch (Throwable e) {
                LOGGER.error("[pradar-register] refresh node data to zk for heartbeat node err: {}!", heartbeatPath, e);
            }
        } else {
            ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        heartbeatNode.setData(getHeartbeatDatas());
                    } catch (Throwable e) {
                        LOGGER.error("[pradar-register] refresh node data to zk for heartbeat node err: {}!", heartbeatPath, e);
                        ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.schedule(this, 5, TimeUnit.SECONDS);
                    }
                }
            }, 0, TimeUnit.SECONDS);
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("[pradar-register] refresh register zookeeper node successful. {}", getAgentId());
        }

    }
}
