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
package com.shulie.instrument.simulator.module.runtime;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.module.ParamSupported;
import com.shulie.instrument.simulator.module.model.runtime.RuntimeInfo;
import org.kohsuke.MetaInfServices;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 4:48 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "runtime", version = "1.0.0", author = "xiaobin@shulie.io", description = "运行时模块")
public class RuntimeModule extends ParamSupported implements ExtensionModule {
    @Command(value = "info", description = "获取JVM运行时信息")
    public CommandResponse info(final Map<String, String> args) {
        try {
            final RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            final ClassLoadingMXBean classLoadingMXBean = ManagementFactory.getClassLoadingMXBean();
            RuntimeInfo runtimeInfo = new RuntimeInfo();
            runtimeInfo.setTotalLoadedClassCount(classLoadingMXBean.getTotalLoadedClassCount());
            runtimeInfo.setLoadedClassCount(classLoadingMXBean.getLoadedClassCount());
            runtimeInfo.setUnloadedClassCount(classLoadingMXBean.getUnloadedClassCount());
            runtimeInfo.setVerbose(classLoadingMXBean.isVerbose());

            runtimeInfo.setName(runtimeMXBean.getName());
            runtimeInfo.setStartTime(runtimeMXBean.getStartTime());
            runtimeInfo.setManagementSpecVersion(runtimeMXBean.getManagementSpecVersion());
            runtimeInfo.setSpecName(runtimeMXBean.getSpecName());
            runtimeInfo.setSpecVersion(runtimeMXBean.getSpecVersion());
            runtimeInfo.setSpecVendor(runtimeMXBean.getSpecVendor());
            runtimeInfo.setVmName(runtimeMXBean.getVmName());
            runtimeInfo.setVmVersion(runtimeMXBean.getVmVersion());
            runtimeInfo.setVmVendor(runtimeMXBean.getVmVendor());
            runtimeInfo.setInputArguments(runtimeMXBean.getInputArguments());
            runtimeInfo.setClassPath(runtimeMXBean.getClassPath());
            runtimeInfo.setBootClassPath(runtimeMXBean.getBootClassPath());
            runtimeInfo.setLibraryPath(runtimeMXBean.getLibraryPath());

            runtimeInfo.setOsName(System.getProperty("os.name"));
            runtimeInfo.setOsVersion(System.getProperty("os.version"));
            runtimeInfo.setJavaVersion(System.getProperty("java.version"));
            runtimeInfo.setJavaHome(System.getProperty("java.home"));
            runtimeInfo.setSystemLoadAverage(ManagementFactory.getOperatingSystemMXBean().getSystemLoadAverage());
            runtimeInfo.setProcessors(ManagementFactory.getOperatingSystemMXBean().getAvailableProcessors());
            runtimeInfo.setArch(ManagementFactory.getOperatingSystemMXBean().getArch());
            runtimeInfo.setUptime(runtimeMXBean.getUptime() / 1000);
            return CommandResponse.success(runtimeInfo);
        } catch (Throwable e) {
            return CommandResponse.failure(e);
        }
    }
}
