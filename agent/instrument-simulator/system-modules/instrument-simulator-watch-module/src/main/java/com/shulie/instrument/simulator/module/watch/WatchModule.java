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
package com.shulie.instrument.simulator.module.watch;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.listener.ext.EventWatchBuilder;
import com.shulie.instrument.simulator.api.listener.ext.EventWatcher;
import com.shulie.instrument.simulator.api.resource.ModuleEventWatcher;
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
 * @since 2020/11/6 11:06 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "watch", version = "1.0.0", author = "xiaobin@shulie.io", description = "方法 watch 模块")
public class WatchModule extends ParamSupported implements ExtensionModule {
    private final static Logger logger = LoggerFactory.getLogger(WatchModule.class);

    @Resource
    private ModuleEventWatcher moduleEventWatcher;

    @Command(value = "info", description = "watch 方法")
    public CommandResponse info(final Map<String, String> args) {
        String classPattern = getParameter(args, "class");
        String methodPattern = getParameter(args, "method");
        String express = getParameter(args, "express");
        String condition = getParameter(args, "condition");
        final int limits = getIntParameter(args, "limits", 20);
        final int wait = getIntParameter(args, "wait", -1);

        if (StringUtil.isEmpty(classPattern)) {
            return CommandResponse.failure("class must not be empty.");
        }

        if (StringUtil.isEmpty(methodPattern)) {
            methodPattern = "*";
        }

        EventWatcher watcher = null;
        try {

            if (limits > 5000) {
                return CommandResponse.failure("limits 最大不能超过5000");
            }
            final CountDownLatch latch = new CountDownLatch(1);
            Queue<Object> watchViews = new ConcurrentLinkedQueue<Object>();
            watcher = new EventWatchBuilder(moduleEventWatcher)
                    .onClass(classPattern).includeSubClasses()
                    .onBehavior(methodPattern)
                    .withInvoke().withCall()
                    .onListener(Listeners.of(WatchListener.class, latch, watchViews, condition, express, wait != -1 ? -1 : limits))
                    .onClass().onWatch();

            if (wait > 0) {
                latch.await(wait, TimeUnit.SECONDS);
            } else if (limits > 0) {
                latch.await();
            }
            return CommandResponse.success(watchViews);
        } catch (Throwable e) {
            logger.error("SIMULATOR: watch module err! class={}, method={}, express={}, condition={}, limits={}, wait={}",
                    classPattern, methodPattern, express, condition, limits, wait, e);
            return CommandResponse.failure(e);
        } finally {
            if (watcher != null) {
                try {
                    watcher.onUnWatched();
                } catch (Throwable e) {
                    logger.error("SIMULATOR: watch module unwatched failed! class={}, method={}, express={}, condition={}, limits={}, wait={}",
                            classPattern, methodPattern, express, condition, limits, wait, e);
                }
            }
        }
    }
}
