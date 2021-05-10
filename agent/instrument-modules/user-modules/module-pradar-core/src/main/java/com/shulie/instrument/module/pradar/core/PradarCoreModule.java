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
package com.shulie.instrument.module.pradar.core;

import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.PradarService;
import com.pamirs.pradar.internal.GlobalConfigService;
import com.pamirs.pradar.internal.PradarInternalService;
import com.pamirs.pradar.debug.DebugTestInfoPusher;
import com.pamirs.pradar.pressurement.agent.shared.service.EventRouter;
import com.pamirs.pradar.upload.uploader.AgentOnlineUploader;
import com.pamirs.pradar.utils.MonitorCollector;
import com.shulie.instrument.module.pradar.core.action.SetClusterTestAction;
import com.shulie.instrument.module.pradar.core.handler.DefaultExceptionHandler;
import com.shulie.instrument.module.pradar.core.service.DefaultGlobalConfigService;
import com.shulie.instrument.module.pradar.core.service.DefaultPradarInternalService;
import com.shulie.instrument.module.pradar.core.service.DefaultPradarService;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.GlobalSwitch;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.executors.ExecutorServiceFactory;
import com.shulie.instrument.simulator.api.resource.ModuleCommandInvoker;
import com.shulie.instrument.simulator.message.Messager;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.pamirs.pradar.utils.MonitorCollector.initMonitorCollectorTask;

/**
 * 用于公共依赖的模块
 * 需要在module.config文件中指定要导出的包列表, 给其他的模块依赖
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/1 12:22 上午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "pradar-core", version = "1.0.0", author = "xiaobin@shulie.io", description = "pradar core 模式，提供链路追踪 trace 埋点以及压测标等服务")
public class PradarCoreModule extends ModuleLifecycleAdapter implements ExtensionModule {
    private final static Logger logger = LoggerFactory.getLogger(PradarCoreModule.class);

    private ScheduledFuture future;

    @Resource
    private ModuleCommandInvoker moduleCommandInvoker;

    @Override
    public void onActive() throws Throwable {
        DebugTestInfoPusher.setModuleCommandInvoker(moduleCommandInvoker);
        future = ExecutorServiceFactory.GLOBAL_SCHEDULE_EXECUTOR_SERVICE.scheduleAtFixedRate(new DebugTestInfoPusher(), 5, 1, TimeUnit.SECONDS);
        //将simulator home路径和plugin相关的配置全部导入到system property中
        String home = simulatorConfig.getSimulatorHome();
        if (home != null) {
            System.setProperty("simulator.home", home);
        }
        Integer requestSize = simulatorConfig.getIntProperty("plugin.request.size");
        if (requestSize != null) {
            System.setProperty("plugin.request.size", String.valueOf(requestSize));
        }

        Integer responseSize = simulatorConfig.getIntProperty("plugin.response.size");
        if (responseSize != null) {
            System.setProperty("plugin.response.size", String.valueOf(responseSize));
        }

        Boolean requestOn = simulatorConfig.getBooleanProperty("plugin.request.on");
        if (requestOn != null) {
            System.setProperty("plugin.request.on", String.valueOf(requestOn));
        }

        Boolean responseOn = simulatorConfig.getBooleanProperty("plugin.response.on");
        if (responseOn != null) {
            System.setProperty("plugin.response.on", String.valueOf(responseOn));
        }

        /**
         * 向外暴露动作，可以直接在业务代码里面去调用
         * 通过 Messager.doAction("pradar-core","setClusterTest",true|false)这种方式调用
         * 因为在业务代码中只能操作到 Messager 这个类，如果模块中需要暴露一些 Api 可以让业务调用则可以通过这种方式
         */
        registerAction("setClusterTest", new SetClusterTestAction());

        PradarService.registerPradarService(new DefaultPradarService());
        PradarInternalService.registerService(new DefaultPradarInternalService());
        GlobalConfigService.registerService(new DefaultGlobalConfigService());

        /**
         * 注册自定义的异常处理器
         */
        Messager.registerExceptionHandler(new DefaultExceptionHandler());

        initMonitorCollectorTask();
    }

    @Override
    public void onFrozen() throws Throwable {
        EventRouter.router().shutdown();
        AgentOnlineUploader.getInstance().shutdown();
        MonitorCollector.shutdown();
        if (future != null && !future.isDone() && !future.isCancelled()) {
            future.cancel(true);
        }
        Pradar.shutdown();
    }
}
