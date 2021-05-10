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
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import org.apache.commons.lang.reflect.MethodUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;

@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "control", version = "1.0.0", author = " xiaobin@shulie.io", description = "控制模块,提供模拟器关闭服务")
public class ControlModule implements ExtensionModule {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private SimulatorConfig simulatorConfig;

    /**
     * 卸载jvm-simulator
     *
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private void uninstall() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final Class<?> classOfAgentLauncher = getClass().getClassLoader()
                .loadClass("com.shulie.instrument.simulator.agent.AgentLauncher");

        MethodUtils.invokeStaticMethod(
                classOfAgentLauncher,
                "uninstall",
                simulatorConfig.getNamespace()
        );
    }

    @Command(value = "shutdown", description = "关闭模块器服务")
    public CommandResponse shutdown() {
        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: prepare to shutdown jvm-simulator[{}].", simulatorConfig.getNamespace());
        }
        try {
            // 关闭HTTP服务器
            final Thread shutdownInstrumentSimulatorHook = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        logger.info("instrument-simulator[{}] prepare to shutdown.", simulatorConfig.getNamespace());
                        /**
                         * 先悠着点，防止请求没返回之前就被卸载了，导致响应无法收到
                         */
                        Thread.sleep(100L);
                        uninstall();
                        logger.info("instrument-simulator[{}] shutdown finished.", simulatorConfig.getNamespace());
                    } catch (Throwable cause) {
                        logger.warn("SIMULATOR: shutdown instrument-simulator[{}] failed.", simulatorConfig.getNamespace(), cause);
                    }
                }
            }, String.format("shutdown-instrument-simulator-%s-hook", simulatorConfig.getNamespace()));
            shutdownInstrumentSimulatorHook.setDaemon(true);
            shutdownInstrumentSimulatorHook.start();
            return CommandResponse.success(true);
        } catch (Throwable e) {
            return CommandResponse.failure(e);
        }
    }

}
