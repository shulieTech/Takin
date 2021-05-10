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
package com.shulie.instrument.simulator.module.mgr;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.event.EventType;
import com.shulie.instrument.simulator.api.resource.ModuleManager;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import com.shulie.instrument.simulator.module.model.info.CommandInfo;
import org.apache.commons.lang.ClassUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 仿真器信息模块
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "info", version = "1.0.0", author = "")
public class InfoModule implements ExtensionModule {
    private final static Logger LOGGER = LoggerFactory.getLogger(InfoModule.class);

    @Resource
    private SimulatorConfig simulatorConfig;
    @Resource
    private ModuleManager moduleManager;

    @Command(value = "info", description = "查看模拟器基本信息")
    public CommandResponse info() throws IOException {

        try {
            final StringBuilder versionSB = new StringBuilder()
                    .append("                    NAMESPACE : ").append(simulatorConfig.getNamespace()).append("\n")
                    .append("                      AGENTID : ").append(simulatorConfig.getAgentId()).append("\n")
                    .append("                      APPNAME : ").append(simulatorConfig.getAppName()).append("\n")
                    .append("                         MODE : ").append(simulatorConfig.getMode()).append("\n")
                    .append("                  SERVER_ADDR : ").append(simulatorConfig.getServerAddress().getHostName()).append("\n")
                    .append("                  SERVER_PORT : ").append(simulatorConfig.getServerAddress().getPort()).append("\n")
                    .append("               UNSAFE_SUPPORT : ").append(simulatorConfig.isEnableUnsafe() ? "ENABLE" : "DISABLE").append("\n")
                    .append("               SIMULATOR_HOME : ").append(simulatorConfig.getSimulatorHome()).append("\n")
                    .append("                AGENT-VERSION : ").append(simulatorConfig.getSimulatorVersion()).append("\n")
                    .append("            SIMULATOR-VERSION : ").append(simulatorConfig.getSimulatorVersion()).append("\n")
                    .append("            SYSTEM_MODULE_LIB : ").append(simulatorConfig.getSystemModuleLibPath()).append("\n")
                    .append(" BIZ_CLASSLOADER_INJECT_FILES : ").append(simulatorConfig.getBizClassLoaderInjectFiles()).append("\n")
                    .append("          SYSTEM_PROVIDER_LIB : ").append(simulatorConfig.getSystemProviderLibPath()).append("\n")
                    .append("                   ZK_SERVERS : ").append(simulatorConfig.getZkServers()).append("\n");
            return CommandResponse.success(versionSB.toString());
        } catch (Throwable e) {
            return CommandResponse.failure(e);
        }

    }

    @Command("event-pool")
    public CommandResponse eventPool() throws IOException {

        try {
            StringBuilder builder = new StringBuilder();
            for (EventType eventType : EventType.values()) {
                builder.append(String.format("%18s", eventType)).append("\n");
            }
            return CommandResponse.success(builder.toString());
        } catch (Throwable e) {
            return CommandResponse.failure(e);
        }

    }

    @Command(value = "commands", description = "查看所有的命令")
    public CommandResponse modules() {
        try {
            List<CommandInfo> commands = new ArrayList<CommandInfo>();
            for (ExtensionModule module : moduleManager.list()) {
                Class<?> classOfModule = module.getClass();
                // 判断模块是否实现了@Information标记
                if (!classOfModule.isAnnotationPresent(ModuleInfo.class)) {
                    continue;
                }

                final ModuleInfo info = classOfModule.getAnnotation(ModuleInfo.class);
                if (info == null) {
                    continue;
                }
                for (final Method method : getMethodsListWithAnnotation(module.getClass(), Command.class)) {
                    final Command commandAnnotation = method.getAnnotation(Command.class);
                    if (null == commandAnnotation) {
                        continue;
                    }

                    CommandInfo commandInfo = new CommandInfo();
                    commandInfo.setModuleId(info.id());
                    commandInfo.setCommand(commandAnnotation.value());
                    commandInfo.setCommandDescription(commandAnnotation.description());
                    commands.add(commandInfo);
                }
            }
            return CommandResponse.success(commands);
        } catch (Throwable e) {
            LOGGER.error("SIMULATOR: execute command info/commands error.", e);
            return CommandResponse.failure(e);
        }
    }

    private static List<Method> getMethodsListWithAnnotation(final Class<?> cls, final Class<? extends Annotation> annotationCls) {
        return getMethodsListWithAnnotation(cls, annotationCls, false, false);
    }

    private static List<Method> getMethodsListWithAnnotation(final Class<?> cls,
                                                             final Class<? extends Annotation> annotationCls,
                                                             boolean searchSupers, boolean ignoreAccess) {

        List<Class<?>> classes = (searchSupers ? getAllSuperclassesAndInterfaces(cls)
                : new ArrayList<Class<?>>());
        classes.add(0, cls);
        final List<Method> annotatedMethods = new ArrayList<Method>();
        for (Class<?> acls : classes) {
            final Method[] methods = (ignoreAccess ? acls.getDeclaredMethods() : acls.getMethods());
            for (final Method method : methods) {
                if (method.getAnnotation(annotationCls) != null) {
                    annotatedMethods.add(method);
                }
            }
        }
        return annotatedMethods;
    }

    private static List<Class<?>> getAllSuperclassesAndInterfaces(final Class<?> cls) {
        if (cls == null) {
            return null;
        }

        final List<Class<?>> allSuperClassesAndInterfaces = new ArrayList<Class<?>>();
        List<Class<?>> allSuperclasses = ClassUtils.getAllSuperclasses(cls);
        int superClassIndex = 0;
        List<Class<?>> allInterfaces = ClassUtils.getAllInterfaces(cls);
        int interfaceIndex = 0;
        while (interfaceIndex < allInterfaces.size() ||
                superClassIndex < allSuperclasses.size()) {
            Class<?> acls;
            if (interfaceIndex >= allInterfaces.size()) {
                acls = allSuperclasses.get(superClassIndex++);
            } else if (superClassIndex >= allSuperclasses.size()) {
                acls = allInterfaces.get(interfaceIndex++);
            } else if (interfaceIndex < superClassIndex) {
                acls = allInterfaces.get(interfaceIndex++);
            } else if (superClassIndex < interfaceIndex) {
                acls = allSuperclasses.get(superClassIndex++);
            } else {
                acls = allInterfaces.get(interfaceIndex++);
            }
            allSuperClassesAndInterfaces.add(acls);
        }
        return allSuperClassesAndInterfaces;
    }
}
