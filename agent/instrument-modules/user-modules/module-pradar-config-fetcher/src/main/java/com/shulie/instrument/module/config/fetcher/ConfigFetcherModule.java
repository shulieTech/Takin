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
package com.shulie.instrument.module.config.fetcher;

import com.pamirs.pradar.internal.PradarInternalService;
import com.shulie.instrument.module.config.fetcher.config.ConfigManager;
import com.shulie.instrument.module.config.fetcher.config.DefaultConfigFetcher;
import com.shulie.instrument.module.config.fetcher.config.resolver.zk.ZookeeperOptions;
import com.shulie.instrument.module.config.fetcher.interval.ISamplingRateConfigFetcher;
import com.shulie.instrument.module.config.fetcher.interval.SamplingRateConfigFetcher;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.executors.ExecutorServiceFactory;
import com.shulie.instrument.simulator.api.guard.SimulatorGuard;
import com.shulie.instrument.simulator.api.resource.SimulatorConfig;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/1 12:45 上午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "pradar-config-fetcher", version = "1.0.0", author = "xiaobin@shulie.io", description = "配置拉取模块,定时1分钟拉取一次配置")
public class ConfigFetcherModule extends ModuleLifecycleAdapter implements ExtensionModule {
    private final static Logger logger = LoggerFactory.getLogger(ConfigFetcherModule.class);
    private volatile boolean isActive;

    @Resource
    private SimulatorConfig simulatorConfig;

    private ScheduledFuture future;

    private ISamplingRateConfigFetcher samplingRateConfigFetcher;

    private ZookeeperOptions buildZookeeperOptions() {
        ZookeeperOptions zookeeperOptions = new ZookeeperOptions();
        zookeeperOptions.setName("zookeeper");
        zookeeperOptions.setZkServers(simulatorConfig.getZkServers());
        zookeeperOptions.setConnectionTimeoutMillis(simulatorConfig.getZkConnectionTimeout());
        zookeeperOptions.setSessionTimeoutMillis(simulatorConfig.getZkSessionTimeout());
        return zookeeperOptions;
    }

    @Override
    public void onActive() throws Throwable {
        isActive = true;
        final String configFetchType = simulatorConfig.getProperty("pradar.config.fetch.type", "http");
        this.future = ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.schedule(new Runnable() {
            @Override
            public void run() {
                if (!isActive) {
                    return;
                }
                try {
                    if (StringUtils.equalsIgnoreCase(configFetchType, "zookeeper")) {
                        ConfigManager.getInstance(buildZookeeperOptions()).initAll();
                    } else {
                        int interval = simulatorConfig.getIntProperty("pradar.config.fetch.interval", 60);
                        String unit = simulatorConfig.getProperty("pradar.config.fetch.unit", "SECONDS");
                        TimeUnit timeUnit = TimeUnit.valueOf(unit);
                        ConfigManager.getInstance(interval, timeUnit).initAll();
                    }
                    // 采样率配置拉取
                    samplingRateConfigFetcher = SimulatorGuard.getInstance().doGuard(ISamplingRateConfigFetcher.class, new SamplingRateConfigFetcher(buildZookeeperOptions(), simulatorConfig));
                    samplingRateConfigFetcher.start();
                } catch (Throwable e) {
                    logger.warn("SIMULATOR: Config Fetch module start failed. log data can't push to the server.", e);
                    ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.schedule(this, 5, TimeUnit.SECONDS);
                }
            }
        }, 0, TimeUnit.SECONDS);

        PradarInternalService.registerConfigFetcher(new DefaultConfigFetcher());
    }

    @Override
    public void onFrozen() throws Throwable {
        isActive = false;
        if (samplingRateConfigFetcher != null) {
            samplingRateConfigFetcher.stop();
        }
        int interval = simulatorConfig.getIntProperty("config.fetch.interval", 60);
        String unit = simulatorConfig.getProperty("config.fetch.unit", "SECONDS");
        TimeUnit timeUnit = TimeUnit.valueOf(unit);
        ConfigManager.getInstance(interval, timeUnit).destroy();
        if (this.future != null && !this.future.isDone() && !this.future.isCancelled()) {
            this.future.cancel(true);
        }
    }
}
