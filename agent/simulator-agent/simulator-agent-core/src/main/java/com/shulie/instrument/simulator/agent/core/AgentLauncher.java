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
package com.shulie.instrument.simulator.agent.core;

import com.alibaba.fastjson.JSON;
import com.shulie.instrument.simulator.agent.core.register.AgentStatus;
import com.shulie.instrument.simulator.agent.core.response.Response;
import com.shulie.instrument.simulator.agent.core.util.HttpUtils;
import com.shulie.instrument.simulator.agent.core.util.ThrowableUtils;
import com.shulie.instrument.simulator.agent.spi.config.AgentConfig;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Agent启动器
 *
 * @author xiaobin@shulie.io
 * @since 1.0.0
 */
public class AgentLauncher {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 目标应用进程的描述,可以是 pid 也可以是进程名称的模糊匹配
     */
    private String descriptor;

    /**
     * agent 的 baseUrl
     */
    private String baseUrl;

    /**
     * agent 的 home 目录
     */
    private final AgentConfig agentConfig;

    /**
     * 是否是运行中状态
     */
    private AtomicBoolean isRunning = new AtomicBoolean(false);

    public AgentLauncher(final AgentConfig agentConfig) {
        this.agentConfig = agentConfig;
        if (this.agentConfig.getAttachId() != -1) {
            this.descriptor = String.valueOf(this.agentConfig.getAttachId());
        } else if (this.agentConfig.getAttachName() != null) {
            this.descriptor = this.agentConfig.getAttachName();
        } else {
            this.descriptor = System.getProperty("attach.pid");
            if (StringUtils.isBlank(descriptor)) {
                this.descriptor = System.getProperty("attach.name");
            }
        }
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    private static boolean isDigits(final String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 内部启动 agent
     *
     * @param descriptor   agent 描述,可以是 attach 的目标 pid，也可以是目标的进程名称
     * @param agentJarPath agent 所在的 路径
     * @param config
     */
    private void start0(final String descriptor,
                        final String agentJarPath,
                        final String config) throws Throwable {
        if (logger.isDebugEnabled()) {
            logger.debug("AGENT: prepare to attach agent: descriptor={}, agentJarPath={}, config={}", descriptor, agentJarPath, config);
        }
        VirtualMachineDescriptor virtualMachineDescriptor = null;
        String targetJvmPid = descriptor;

        if (isDigits(descriptor)) {
            for (VirtualMachineDescriptor vmDescriptor : VirtualMachine.list()) {
                if (vmDescriptor.id().equals(descriptor)) {
                    virtualMachineDescriptor = vmDescriptor;
                    break;
                }
            }
        }

        if (virtualMachineDescriptor == null) {
            for (VirtualMachineDescriptor vmDescriptor : VirtualMachine.list()) {
                if (vmDescriptor.displayName().contains(descriptor)) {
                    virtualMachineDescriptor = vmDescriptor;
                    break;
                }
            }
        }

        if (virtualMachineDescriptor != null) {
            // 加载agent
            attachAgent(virtualMachineDescriptor, targetJvmPid, agentJarPath, config);
        } else {
            logger.warn("AGENT: can't found attach target: {}", descriptor);
        }
    }

    private static String encodeArg(String arg) {
        try {
            return URLEncoder.encode(arg, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return arg;
        }
    }

    /**
     * 启动agent，返回 agent 访问地址
     *
     * @return
     * @throws Throwable 如果启动出现问题可能会抛出异常
     */
    public void startup() throws Throwable {
        if (!isRunning.compareAndSet(false, true)) {
            return;
        }
        if (logger.isInfoEnabled()) {
            logger.info("prepare to startup agent.");
        }
        try {
            this.baseUrl = null;
            StringBuilder builder = new StringBuilder();
            if (StringUtils.isNotBlank(this.agentConfig.getNamespace())) {
                builder.append("namespace=").append(encodeArg(this.agentConfig.getNamespace()));
            }
            builder.append(";token=").append(encodeArg(this.agentConfig.getToken()));
            if (StringUtils.isNotBlank(this.agentConfig.getAppName())) {
                builder.append(";app.name=").append(encodeArg(this.agentConfig.getAppName()));
            }
            if (StringUtils.isNotBlank(this.agentConfig.getAgentId())) {
                builder.append(";agentId=").append(encodeArg(this.agentConfig.getAgentId()));
            }
            if (StringUtils.isNotBlank(this.agentConfig.getLogPath())) {
                builder.append(";logPath=").append(encodeArg(this.agentConfig.getLogPath()));
            }
            if (StringUtils.isNotBlank(this.agentConfig.getZkServers())) {
                builder.append(";zkServers=").append(encodeArg(this.agentConfig.getZkServers()));
            }
            if (StringUtils.isNotBlank(this.agentConfig.getRegisterPath())) {
                builder.append(";registerPath=").append(encodeArg(this.agentConfig.getRegisterPath()));
            }
            builder.append(";zkConnectionTimeout=").append(this.agentConfig.getZkConnectionTimeout());
            builder.append(";zkSessionTimeout=").append(this.agentConfig.getZkSessionTimeout());
            builder.append(";agentVersion=").append(this.agentConfig.getAgentVersion());
            /**
             * 指定simulator配置文件的获取地址
             */
            final String simulatorConfigUrl = agentConfig.getProperty("simulator.config.url", null);
            if (StringUtils.isNotBlank(simulatorConfigUrl)) {
                builder.append(";prop=").append(encodeArg(simulatorConfigUrl));
            }

            AgentStatus.installing();
            start0(descriptor, agentConfig.getAgentJarPath(), builder.toString());
            String content = null;
            while (true) {
                File resultFile = new File(this.agentConfig.getAgentResultFilePath());
                if (!resultFile.exists()) {
                    Thread.sleep(100);
                    continue;
                }
                content = read(resultFile);
                break;
            }
            if (StringUtils.isBlank(content)) {
                logger.error("AGENT: launch on agent err. can't get a empty result from result file:{}", this.agentConfig.getAgentResultFilePath());
                AgentStatus.installFailed("AGENT: launch on agent err. can't get a empty result from result file:" + this.agentConfig.getAgentResultFilePath());
                throw new RuntimeException("AGENT: launch on agent err. can't get a empty result from result file:" + this.agentConfig.getAgentResultFilePath());
            }
            String[] result = StringUtils.split(content, ';');
            if (ArrayUtils.isEmpty(result) || result.length < 4) {
                logger.error("AGENT: launch on agent err. can't get a correct result from result file [{}] : {}", content, this.agentConfig.getAgentResultFilePath());
                AgentStatus.installFailed("AGENT: launch on agent err. can't get a correct result from result file [" + content + "] :" + this.agentConfig.getAgentResultFilePath());
                throw new RuntimeException("AGENT: launch on agent err. can't get a correct result from result file [" + content + "] :" + this.agentConfig.getAgentResultFilePath());
            }

            this.baseUrl = "http://" + result[2] + ":" + result[3] + "/simulator/" + this.agentConfig.getNamespace() + "/module/http";
            logger.info("AGENT: got a available agent url: {}", this.baseUrl);
        } catch (Throwable throwable) {
            isRunning.set(false);
            String errorMessage = ThrowableUtils.toString(throwable, 1000);
            AgentStatus.installFailed("AGENT: agent startup failed. " + errorMessage);
            logger.error("AGENT: agent startup failed.", throwable);
            throw throwable;
        }
    }

    /**
     * 启动模块
     *
     * @param path
     * @throws Throwable
     */
    public void loadModule(String path) throws Throwable {
        if (logger.isInfoEnabled()) {
            logger.info("prepare to load module from path={}.", path);
        }
        try {
            String loadUrl = baseUrl + File.separator + "management" + File.separator + "load?path=" + path;
            String content = HttpUtils.doGet(loadUrl, agentConfig.getUserAppKey());
            /**
             * 如果返回为空则视为已经停止
             */
            if (content == null) {
                AgentStatus.setError("AGENT: unload module err. got empty content from unload api url, path=" + path);
                throw new RuntimeException("AGENT: unload module err. got empty content from unload api url, path=" + path);
            }

            Response response = JSON.parseObject(content, Response.class);
            if (response.isSuccess()) {
                if (logger.isInfoEnabled()) {
                    logger.info("load module successful from path={}.", path);
                }
                return;
            }

            AgentStatus.setError("AGENT: load module failed. load module got a error response from agent. " + response.getMessage());
            throw new RuntimeException("AGENT: load module failed. load module got a error response from agent. " + response.getMessage());
        } catch (Throwable e) {
            String errorMessage = ThrowableUtils.toString(e, 1000);
            AgentStatus.setError("AGENT: agent shutdown failed. " + errorMessage);
            logger.error("AGENT: agent shutdown failed.", e);
            throw e;
        }
    }

    public void unloadModule(String moduleId) throws Throwable {
        if (logger.isInfoEnabled()) {
            logger.info("prepare to unload module {}.", moduleId);
        }
        try {
            String loadUrl = baseUrl + File.separator + "management" + File.separator + "unload?moduleId=" + moduleId;
            String content = HttpUtils.doGet(loadUrl, agentConfig.getUserAppKey());
            /**
             * 如果返回为空则视为已经停止
             */
            if (content == null) {
                AgentStatus.setError("AGENT: unload module err. got empty content from unload api url, moduleId=" + moduleId);
                throw new RuntimeException("AGENT: unload module err. got empty content from unload api url, moduleId=" + moduleId);
            }

            Response response = JSON.parseObject(content, Response.class);
            if (response.isSuccess()) {
                if (logger.isInfoEnabled()) {
                    logger.info("unload module successful {}.", moduleId);
                }
                return;
            }

            AgentStatus.setError("AGENT: unload moudule failed. unload moudule got a error response from agent. " + response.getMessage());
            throw new RuntimeException("AGENT: unload moudule failed. unload moudule got a error response from agent. " + response.getMessage());
        } catch (Throwable e) {
            String errorMessage = ThrowableUtils.toString(e, 1000);
            AgentStatus.setError("AGENT: unload module failed. " + errorMessage);
            logger.error("AGENT: unload module failed.", e);
            throw e;
        }
    }

    public void reloadModule(String moduleId) throws Throwable {
        if (logger.isInfoEnabled()) {
            logger.info("prepare to reload module {}.", moduleId);
        }
        try {
            String loadUrl = baseUrl + File.separator + "management" + File.separator + "reload?moduleId=" + moduleId;
            String content = HttpUtils.doGet(loadUrl, agentConfig.getUserAppKey());
            /**
             * 如果返回为空则视为已经停止
             */
            if (content == null) {
                AgentStatus.setError("AGENT: reload module err. got empty content from reload api url, moduleId=" + moduleId);
                throw new RuntimeException("AGENT: reload module err. got empty content from reload api url, moduleId=" + moduleId);
            }

            Response response = JSON.parseObject(content, Response.class);
            if (response.isSuccess()) {
                if (logger.isInfoEnabled()) {
                    logger.info("reload module successful {}.", moduleId);
                }
                return;
            }
            AgentStatus.setError("AGENT: reload module failed. reload module got a error response from agent. moduleId=" + moduleId + ", loadUrl=" + loadUrl + " " + response.getMessage());
            throw new RuntimeException("AGENT: reload module failed. reload module got a error response from agent. moduleId=" + moduleId + ", loadUrl=" + loadUrl + " " + response.getMessage());
        } catch (Throwable e) {
            String errorMessage = ThrowableUtils.toString(e, 1000);
            AgentStatus.setError("AGENT: reload module failed. " + errorMessage);
            logger.error("AGENT: reload module failed.", e);
            throw e;
        }
    }

    /**
     * 停止 agent
     */
    public void shutdown() throws Throwable {
        if (!isRunning.compareAndSet(true, false)) {
            return;
        }

        if (logger.isInfoEnabled()) {
            logger.info("prepare to shutdown agent.");
        }

        if (this.baseUrl == null) {
            logger.error("AGENT: agent shutdown failed. agent access url is blank.");
            AgentStatus.setError("AGENT: agent shutdown failed. agent access url is blank.");
            throw new RuntimeException("AGENT: agent shutdown failed. agent access url is blank.");
        }
        try {
            String shutdownUrl = baseUrl + File.separator + "simulator-control" + File.separator + "shutdown";
            String content = HttpUtils.doGet(shutdownUrl, agentConfig.getUserAppKey());
            /**
             * 如果返回为空则视为已经停止
             */
            if (content == null) {
                if (logger.isInfoEnabled()) {
                    logger.info("shutdown agent successful.");
                }
                return;
            }

            Response response = JSON.parseObject(content, Response.class);
            if (response.isSuccess()) {
                int retryTimes = 10;
                while (retryTimes-- > 0) {
                    /**
                     * 如果没有响应，则说明关闭成功了
                     */
                    String result = HttpUtils.doGet(baseUrl + File.separator + "heartbeat" + File.separator + "info", agentConfig.getUserAppKey());
                    if (result == null) {
                        if (logger.isInfoEnabled()) {
                            logger.info("shutdown agent successful.");
                        }
                        return;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    }
                }
            }
            AgentStatus.setError("AGENT: got a error response from agent. shutdown agent failed. " + response.getMessage());
            throw new RuntimeException("AGENT: got a error response from agent. shutdown agent failed.  " + response.getMessage());
        } catch (Throwable e) {
            isRunning.set(true);
            String errorMessage = ThrowableUtils.toString(e, 1000);
            AgentStatus.setError("AGENT: shutdown agent failed. " + errorMessage);
            logger.error("AGENT: agent shutdown failed.", e);
            throw e;
        }
    }

    private static String read(File file) throws Throwable {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            return reader.readLine();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    // 加载Agent
    private void attachAgent(final VirtualMachineDescriptor virtualMachineDescriptor,
                             final String targetJvmPid,
                             final String agentJarPath,
                             final String config) throws Throwable {

        VirtualMachine vmObj = null;
        try {
            if (virtualMachineDescriptor != null) {
                vmObj = VirtualMachine.attach(virtualMachineDescriptor);
            } else {
                if (!isDigits(targetJvmPid)) {
                    logger.error("AGENT: illegal args[0], can't found a vm instance with {} by name. and it is also not a valid digits. agentJarPath={}, config={}", targetJvmPid, agentJarPath, config);
                    throw new IllegalArgumentException("illegal args[0], can't found a vm instance with " + targetJvmPid + " by name. and it is also not a valid digits.");
                }
                vmObj = VirtualMachine.attach(targetJvmPid);
            }
            if (vmObj != null) {
                try {
                    vmObj.loadAgent(agentJarPath, config);
                    logger.info("AGENT: attached to agent success. descriptor={}, agentJarPath={}, config={}", targetJvmPid, agentJarPath, config);
                } catch (Throwable e) {
                    logger.error("AGENT: attach failed. can't found attach target agent.  descriptor={}, agentJarPath={}, config={}", targetJvmPid, agentJarPath, config);
                    throw e;
                }
            } else {
                logger.error("AGENT: attach failed. can't found attach target agent.  descriptor={}, agentJarPath={}, config={}", targetJvmPid, agentJarPath, config);
            }
        } finally {
            if (null != vmObj) {
                vmObj.detach();
            }
        }

    }

}
