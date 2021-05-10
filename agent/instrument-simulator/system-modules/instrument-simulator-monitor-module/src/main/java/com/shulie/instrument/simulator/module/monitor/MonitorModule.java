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
package com.shulie.instrument.simulator.module.monitor;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.listener.ext.EventWatchBuilder;
import com.shulie.instrument.simulator.api.listener.ext.EventWatcher;
import com.shulie.instrument.simulator.api.resource.ModuleEventWatcher;
import com.shulie.instrument.simulator.api.util.ParameterUtils;
import com.shulie.instrument.simulator.api.util.StringUtil;
import com.shulie.instrument.simulator.module.ParamSupported;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/7 2:18 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "monitor", version = "1.0.0", author = "xiaobin@shulie.io", description = "监视器模块")
public class MonitorModule extends ParamSupported implements ExtensionModule {
    private final static Logger logger = LoggerFactory.getLogger(MonitorModule.class);
    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Command(value = "info", description = "监听代码/方法执行信息")
    public CommandResponse info(final Map<String, String> args) {
        final String classPattern = args.get("class");
        String methodPattern = args.get("method");
        /**
         * 如果 wait 和 count 都没有填写，则默认统计20条
         */
        final int wait = ParameterUtils.getInt(args, "wait", -1);
        /**
         * 条数限定
         */
        final int limits = ParameterUtils.getInt(args, "limits", 100);

        if (StringUtil.isEmpty(classPattern)) {
            return CommandResponse.failure("class must not be empty.");
        }

        if (StringUtil.isEmpty(methodPattern)) {
            methodPattern = "*";
        }

        EventWatcher watcher = null;
        try {
            if (wait > 10 * 60 * 1000) {
                return CommandResponse.failure("wait 最大等待时间不能超过10分钟");
            }

            final CountDownLatch latch = new CountDownLatch(1);

            Queue<Object> traceViews = new ConcurrentLinkedQueue<Object>();
            watcher = new EventWatchBuilder(moduleEventWatcher)
                    .onClass(classPattern).includeSubClasses()
                    .onBehavior(methodPattern)
                    .withInvoke().withCall()
                    .onListener(Listeners.of(MonitorListener.class, latch, traceViews, wait != -1 ? -1 : limits))
                    .onClass().onWatch();

            if (wait > 0) {
                latch.await(wait, TimeUnit.SECONDS);
            } else if (limits > 0) {
                latch.await();
            }
            return CommandResponse.success(traceViews);
        } catch (Throwable e) {
            logger.error("SIMULATOR: monitor module err! class={}, method={}, limits={}, wait={}",
                    classPattern, methodPattern, limits, wait, e);
            return CommandResponse.failure(e);
        } finally {
            if (watcher != null) {
                try {
                    watcher.onUnWatched();
                } catch (Throwable e) {
                    logger.error("SIMULATOR: monitor module unwatched failed! class={}, method={}, limits={}, wait={}",
                            classPattern, methodPattern, limits, wait, e);
                }
            }
        }
    }


}
