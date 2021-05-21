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

import com.shulie.instrument.simulator.agent.api.ConfigProvider;
import com.shulie.instrument.simulator.agent.core.classloader.ProviderClassLoader;
import com.shulie.instrument.simulator.agent.core.config.AgentConfigImpl;
import com.shulie.instrument.simulator.agent.core.config.ConfigProviderImpl;
import com.shulie.instrument.simulator.agent.core.config.CoreConfig;
import com.shulie.instrument.simulator.agent.core.exception.AgentDownloadException;
import com.shulie.instrument.simulator.agent.core.register.Register;
import com.shulie.instrument.simulator.agent.core.register.RegisterFactory;
import com.shulie.instrument.simulator.agent.core.register.RegisterOptions;
import com.shulie.instrument.simulator.agent.core.util.JarUtils;
import com.shulie.instrument.simulator.agent.core.util.LogbackUtils;
import com.shulie.instrument.simulator.agent.spi.AgentLoadSpi;
import com.shulie.instrument.simulator.agent.spi.CommandExecutor;
import com.shulie.instrument.simulator.agent.spi.command.Command;
import com.shulie.instrument.simulator.agent.spi.command.impl.*;
import com.shulie.instrument.simulator.agent.spi.config.AgentConfig;
import com.shulie.instrument.simulator.agent.spi.impl.AgentLoadSpiImpl;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileFilter;
import java.lang.annotation.Annotation;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/16 8:50 下午
 */
public class CoreLauncher {
    private final static Logger LOGGER = LoggerFactory.getLogger(CoreLauncher.class);
    private AgentLauncher launcher;
    private AgentLoadSpi agentLoadSpi;
    private final CoreConfig coreConfig;
    private final AgentConfig agentConfig;
    private final ConfigProvider configProvider;
    private final Instrumentation instrumentation;
    private final ClassLoader classLoader;
    private final String tagName;

    /**
     * agent 默认延迟时间设置5分钟
     */
    private int delay = 300;
    private TimeUnit unit = TimeUnit.SECONDS;

    private final ScheduledExecutorService startService;

    public CoreLauncher(final String agentHome) {
        this(agentHome, -1L, null, null, null, null);
    }

    public CoreLauncher(String agentHome, long attachId, String attachName, String tagName, Instrumentation instrumentation, ClassLoader classLoader) {
        this.coreConfig = new CoreConfig(agentHome);
        this.instrumentation = instrumentation;
        this.classLoader = classLoader;
        this.tagName = tagName;
        this.coreConfig.setAttachId(attachId);
        this.coreConfig.setAttachName(attachName);
        this.agentConfig = new AgentConfigImpl(this.coreConfig);
        System.setProperty("SIMULATOR_LOG_PATH", this.agentConfig.getLogPath());
        System.setProperty("SIMULATOR_LOG_LEVEL", this.agentConfig.getLogLevel());
        LogbackUtils.init(this.agentConfig.getLogConfigFile());
        this.launcher = new AgentLauncher(this.agentConfig, instrumentation, classLoader);
        this.configProvider = new ConfigProviderImpl(this.agentConfig);
        initAgentLoader();
        this.startService = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread t = new Thread(runnable, "Agent-Start-Instrument-Service");
                t.setDaemon(true);
                return t;
            }
        });
    }


    /**
     * 初始化 Agent Loader
     */
    private void initAgentLoader() {
        List<File> files = JarUtils.readFiles(new File(this.coreConfig.getProviderFilePath()), new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile() && file.exists() && file.canRead();
            }
        });
        if (files != null && !files.isEmpty()) {
            URL[] urls = JarUtils.toURLs(files);
            ProviderClassLoader classLoader = new ProviderClassLoader(urls, CoreLauncher.class.getClassLoader());
            ServiceLoader<AgentLoadSpi> serviceLoader = ServiceLoader.load(AgentLoadSpi.class, classLoader);
            for (AgentLoadSpi agentLoadSpi : serviceLoader) {
                this.agentLoadSpi = agentLoadSpi;
                break;
            }
        }

        if (this.agentLoadSpi == null) {
            this.agentLoadSpi = new AgentLoadSpiImpl();
        }

        try {
            inject(this.agentLoadSpi);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 注入资源
     *
     * @param object 目标对象
     * @throws IllegalAccessException
     */
    private void inject(Object object) throws IllegalAccessException {
        final Field[] resourceFieldArray = getFieldsWithAnnotation(object.getClass(), Resource.class);
        if (ArrayUtils.isEmpty(resourceFieldArray)) {
            return;
        }
        for (final Field resourceField : resourceFieldArray) {
            final Class<?> fieldType = resourceField.getType();
            // ConfigProvider 注入
            if (ConfigProvider.class.isAssignableFrom(fieldType)) {
                FieldUtils.writeField(resourceField, object, this.configProvider, true);
            }
        }
    }

    private static Field[] getFieldsWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        final List<Field> annotatedFieldsList = getFieldsListWithAnnotation(cls, annotationCls);
        return annotatedFieldsList.toArray(new Field[0]);
    }

    private static List<Field> getFieldsListWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        final List<Field> allFields = getAllFieldsList(cls);
        final List<Field> annotatedFields = new ArrayList<Field>();
        for (final Field field : allFields) {
            if (field.getAnnotation(annotationCls) != null) {
                annotatedFields.add(field);
            }
        }
        return annotatedFields;
    }

    private static List<Field> getAllFieldsList(final Class<?> cls) {
        final List<Field> allFields = new ArrayList<Field>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    /**
     * 启动
     *
     * @throws Throwable
     */
    public void start() throws Throwable {
        RegisterFactory.init(agentConfig);
        this.startService.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    Register register = RegisterFactory.getRegister(agentConfig.getProperty("register.name", "zookeeper"));
                    RegisterOptions registerOptions = buildRegisterOptions(agentConfig);
                    register.init(registerOptions);
                    register.start();

                    agentLoadSpi.loadAgentLib(agentConfig);
                    agentLoadSpi.addCommandExecutor(agentConfig, new CommandExecutor() {
                        @Override
                        public void execute(Command command) throws Throwable {
                            if (command instanceof StartCommand) {
                                launcher.startup();
                            } else if (command instanceof StopCommand) {
                                launcher.shutdown();
                            } else if (command instanceof LoadModuleCommand) {
                                launcher.loadModule(((LoadModuleCommand) command).getPath());
                            } else if (command instanceof UnloadModuleCommand) {
                                launcher.unloadModule(((UnloadModuleCommand) command).getModuleId());
                            } else if (command instanceof ReloadModuleCommand) {
                                launcher.reloadModule(((ReloadModuleCommand) command).getModuleId());
                            }
                        }
                    });
                } catch (AgentDownloadException e) {
                    LOGGER.error("SIMULATOR: download agent occur exception. ", e);
                    startService.schedule(this, 10, TimeUnit.SECONDS);
                } catch (Throwable t) {
                    LOGGER.error("SIMULATOR: agent start occur exception. ", t);
                    startService.schedule(this, 10, TimeUnit.SECONDS);
                }
            }
        }, delay, unit);
        if (LOGGER.isInfoEnabled()) {
            if (tagName != null) {
                LOGGER.info("SIMULATOR: current load tag file name {}.", tagName);
            } else {
                LOGGER.warn("SIMULATOR: current can't found tag name. may be agent file is incomplete.");
            }
            LOGGER.info("SIMULATOR: agent will start {} {} later... please wait for a while moment.", delay, unit.toString());
        }
    }

    private RegisterOptions buildRegisterOptions(AgentConfig agentConfig) {
        RegisterOptions registerOptions = new RegisterOptions();
        registerOptions.setAppName(agentConfig.getAppName());
        registerOptions.setRegisterBasePath(agentConfig.getProperty("simulator.client.zk.path", "/config/log/simulator/client"));
        registerOptions.setRegisterName(agentConfig.getProperty("simulator.hearbeat.register.name", "zookeeper"));
        registerOptions.setZkServers(agentConfig.getProperty("simulator.zk.servers", "localhost:2181"));
        registerOptions.setConnectionTimeoutMillis(agentConfig.getIntProperty("simulator.zk.connection.timeout.ms", 30000));
        registerOptions.setSessionTimeoutMillis(agentConfig.getIntProperty("simulator.zk.session.timeout.ms", 60000));
        return registerOptions;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setUnit(TimeUnit unit) {
        if (this.unit != null) {
            this.unit = unit;
        }
    }
}
