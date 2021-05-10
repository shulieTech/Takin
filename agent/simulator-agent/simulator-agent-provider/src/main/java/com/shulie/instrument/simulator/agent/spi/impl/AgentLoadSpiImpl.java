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
package com.shulie.instrument.simulator.agent.spi.impl;

import com.shulie.instrument.simulator.agent.api.ConfigProvider;
import com.shulie.instrument.simulator.agent.api.model.AppConfig;
import com.shulie.instrument.simulator.agent.api.model.ModuleConfig;
import com.shulie.instrument.simulator.agent.spi.AgentLoadSpi;
import com.shulie.instrument.simulator.agent.spi.CommandExecutor;
import com.shulie.instrument.simulator.agent.spi.command.impl.*;
import com.shulie.instrument.simulator.agent.spi.config.AgentConfig;
import com.shulie.instrument.simulator.agent.spi.impl.utils.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/17 8:07 下午
 */
public class AgentLoadSpiImpl implements AgentLoadSpi {
    private Logger logger = LoggerFactory.getLogger(AgentLoadSpiImpl.class);

    @Resource
    private ConfigProvider configProvider;

    private AtomicBoolean isInited = new AtomicBoolean(false);
    /**
     * 应用配置, 配置变更会导致 agent 升级
     */
    private AppConfig appConfig;

    public AgentLoadSpiImpl() {
    }

    @Override
    public void loadAgentLib(AgentConfig agentConfig) {
        if (logger.isInfoEnabled()) {
            logger.info("prepare to load agent lib.");
        }
        /**
         * simulator home目录
         */
        File file = new File(agentConfig.getSimulatorHome());
        final boolean useAgentLocalFile = agentConfig.getBooleanProperty("agent.use.local", false);
        boolean downloadFromRemote = true;
        if (useAgentLocalFile) {
            if (file.exists()) {
                downloadFromRemote = false;
            }
        }
        if (downloadFromRemote) {
            File f = configProvider.downloadAgent(file.getAbsolutePath() + "_tmp");
            if (f != null) {
                if (file.exists()) {
                    FileUtils.delete(file);
                }
                f.renameTo(file);
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info("load agent lib successful.");
        }
    }

    @Override
    public void addCommandExecutor(final AgentConfig agentConfig, final CommandExecutor commandExecutor) {
        if (!isInited.compareAndSet(false, true)) {
            return;
        }
        try {
            AppConfig config = configProvider.getAppConfig();
            /**
             * 如果是第一次启动
             */
            if (appConfig == null) {
                if (!config.isRunning()) {
                    appConfig = config;
                    return;
                }

                try {
                    commandExecutor.execute(new StartCommand());
                } catch (Throwable t) {
                    logger.error("AGENT: prepare to start agent failed.", t);
                    return;
                }

                appConfig = config;
            } else {
                try {
                    compareAndTriggerEvent(appConfig, config, agentConfig, commandExecutor);
                    appConfig = config;
                } catch (Throwable e) {
                    logger.error("AGENT: prepare to trigger event failed.", e);
                    return;
                }
            }
        } catch (Throwable e) {
            logger.error("AGENT: start agent err! retry next.", e);
        }
    }

    /**
     * 升级框架则将全部的包全部拉取一遍，然后全部升级，包括所有的插件
     *
     * @param oldConfig       当前 agent的应用配置
     * @param agentConfig     agent配置
     * @param commandExecutor 命令执行器
     */
    private void upgradeFramework(AppConfig oldConfig, AgentConfig agentConfig, CommandExecutor commandExecutor) {
        File file = new File(agentConfig.getSimulatorHome());
        File f = configProvider.downloadAgent(file.getAbsolutePath() + "_tmp");
        if (f != null) {
            if (oldConfig.isRunning()) {
                try {
                    commandExecutor.execute(new StopCommand());
                } catch (Throwable e) {
                    logger.error("AGENT: upgrade to stop agent err. ", e);
                    throw new RuntimeException("AGENT: upgrade to stop agent err. ", e);
                }
            }
            if (file.exists()) {
                FileUtils.delete(file);
            }
            f.renameTo(file);

            try {
                commandExecutor.execute(new StartCommand());
            } catch (Throwable e) {
                logger.error("AGENT: upgrade to start agent err. ", e);
                throw new RuntimeException("AGENT: upgrade to start agent err. ", e);
            }
        }
    }

    /**
     * 关闭
     *
     * @param commandExecutor
     */
    private void shutdown(CommandExecutor commandExecutor) {
        try {
            commandExecutor.execute(new StopCommand());
        } catch (Throwable e) {
            logger.error("AGENT: shutdown agent err. ", e);
            throw new RuntimeException("AGENT: shutdown agent err. ", e);
        }
    }

    /**
     * 比较配置并且触发事件
     *
     * @param oldConfig       旧的配置
     * @param newConfig       新的配置
     * @param agentConfig     Agent 配置
     * @param commandExecutor 命令执行器
     */
    private void compareAndTriggerEvent(AppConfig oldConfig, AppConfig newConfig, AgentConfig agentConfig, CommandExecutor commandExecutor) {
        /**
         * 如果当前最新的配置是停止,则需要判断老的判断是否是运行，如果是则需要停止
         */
        if (!newConfig.isRunning()) {
            shutdown(commandExecutor);
            return;
        }

        /**
         * 如果框架版本不一致，则升级整个框架，拉取完整的框架运行包
         */
        if (!StringUtils.equals(oldConfig.getAgentVersion(), newConfig.getAgentVersion())) {
            upgradeFramework(oldConfig, agentConfig, commandExecutor);
            return;
        }

        File file = new File(agentConfig.getSimulatorHome());
        /**
         * 新增的模块配置
         */
        List<ModuleConfig> addModuleConfigs = calculateAddModules(oldConfig, newConfig);
        if (addModuleConfigs != null && !addModuleConfigs.isEmpty()) {
            for (ModuleConfig moduleConfig : addModuleConfigs) {
                File f = configProvider.downloadModule(moduleConfig.getModuleId(), moduleConfig.getVersion(), file.getAbsolutePath());
                if (f != null) {
                    moduleConfig.setPath(f.getAbsolutePath());
                    try {
                        commandExecutor.execute(new LoadModuleCommand(f.getAbsolutePath()));
                    } catch (Throwable e) {
                        logger.error("AGENT: load module err. moduleName={}, version={}, path={}", moduleConfig.getModuleId(), moduleConfig.getVersion(), moduleConfig.getPath(), e);
                        throw new RuntimeException("AGENT: load module err. moduleName=" + moduleConfig.getModuleId() + ", version=" + moduleConfig.getVersion() + ", path=" + moduleConfig.getPath(), e);
                    }
                }
            }
        }


        /**
         * 删除的配置模块
         */
        List<ModuleConfig> removeModuleConfigs = calculateRemoveModules(oldConfig, newConfig);
        if (removeModuleConfigs != null && !removeModuleConfigs.isEmpty()) {
            for (ModuleConfig moduleConfig : removeModuleConfigs) {
                try {
                    commandExecutor.execute(new UnloadModuleCommand(moduleConfig.getModuleId()));
                } catch (Throwable e) {
                    logger.error("AGENT: unload module err. moduleName={}, version={}, path={}", moduleConfig.getModuleId(), moduleConfig.getVersion(), moduleConfig.getPath(), e);
                    throw new RuntimeException("AGENT: unload module err. moduleName=" + moduleConfig.getModuleId() + ", version=" + moduleConfig.getVersion() + ", path=" + moduleConfig.getPath(), e);
                }
            }
        }

        /**
         * 修改的模块配置
         */
        List<ModuleConfig> modifyModuleConfigs = calculateModifyModules(oldConfig, newConfig);
        if (modifyModuleConfigs != null && !modifyModuleConfigs.isEmpty()) {
            for (ModuleConfig moduleConfig : addModuleConfigs) {
                File f = configProvider.downloadModule(moduleConfig.getModuleId(), moduleConfig.getVersion(), file.getAbsolutePath());
                if (f != null) {
                    moduleConfig.setPath(f.getAbsolutePath());
                    try {
                        commandExecutor.execute(new ReloadModuleCommand(moduleConfig.getModuleId()));
                    } catch (Throwable e) {
                        logger.error("AGENT: reload module err. moduleName={}, version={}, path={}", moduleConfig.getModuleId(), moduleConfig.getVersion(), moduleConfig.getPath(), e);
                        throw new RuntimeException("AGENT: reload module err. moduleName=" + moduleConfig.getModuleId() + ", version=" + moduleConfig.getVersion() + ", path=" + moduleConfig.getPath(), e);
                    }
                }
            }
        }
    }

    /**
     * 计算新增的模块
     *
     * @param oldConfig 当前的配置
     * @param newConfig 新的配置
     * @return 模块配置列表
     */
    private List<ModuleConfig> calculateAddModules(AppConfig oldConfig, AppConfig newConfig) {
        Map<String, ModuleConfig> oldModuleConfigs = oldConfig == null ? Collections.EMPTY_MAP : oldConfig.getModuleConfigs();
        Map<String, ModuleConfig> newModuleConfigs = newConfig == null ? Collections.EMPTY_MAP : newConfig.getModuleConfigs();
        Set<String> newKeys = newModuleConfigs.keySet();
        newKeys = new HashSet<String>(newKeys);
        newKeys.removeAll(oldModuleConfigs.keySet());
        List<ModuleConfig> moduleConfigs = new ArrayList<ModuleConfig>();
        for (String key : newKeys) {
            moduleConfigs.add(newModuleConfigs.get(key));
        }

        return moduleConfigs;
    }

    /**
     * 计算已经移除的模块
     *
     * @param oldConfig 当前的配置
     * @param newConfig 新的配置
     * @return 模块配置列表
     */
    private List<ModuleConfig> calculateRemoveModules(AppConfig oldConfig, AppConfig newConfig) {
        Map<String, ModuleConfig> oldModuleConfigs = oldConfig == null ? Collections.EMPTY_MAP : oldConfig.getModuleConfigs();
        Map<String, ModuleConfig> newModuleConfigs = newConfig == null ? Collections.EMPTY_MAP : newConfig.getModuleConfigs();
        Set<String> oldKeys = oldModuleConfigs.keySet();
        oldKeys = new HashSet<String>(oldKeys);
        oldKeys.removeAll(newModuleConfigs.keySet());
        List<ModuleConfig> moduleConfigs = new ArrayList<ModuleConfig>();
        for (String key : oldKeys) {
            moduleConfigs.add(oldModuleConfigs.get(key));
        }

        return moduleConfigs;
    }

    /**
     * 计算修改的模块
     *
     * @param oldConfig 当前的配置
     * @param newConfig 新拉取到的配置
     * @return 模块配置列表
     */
    private List<ModuleConfig> calculateModifyModules(AppConfig oldConfig, AppConfig newConfig) {
        Map<String, ModuleConfig> oldModuleConfigs = oldConfig == null ? Collections.EMPTY_MAP : oldConfig.getModuleConfigs();
        Map<String, ModuleConfig> newModuleConfigs = newConfig == null ? Collections.EMPTY_MAP : newConfig.getModuleConfigs();
        Set<String> newKeys = newModuleConfigs.keySet();
        newKeys = new HashSet<String>(newKeys);
        newKeys.retainAll(oldModuleConfigs.keySet());
        List<ModuleConfig> moduleConfigs = new ArrayList<ModuleConfig>();
        for (String key : newKeys) {
            moduleConfigs.add(oldModuleConfigs.get(key));
        }
        return moduleConfigs;
    }
}
