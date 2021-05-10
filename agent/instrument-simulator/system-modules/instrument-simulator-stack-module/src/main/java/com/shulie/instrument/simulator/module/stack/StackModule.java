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
package com.shulie.instrument.simulator.module.stack;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.filter.ClassDescriptor;
import com.shulie.instrument.simulator.api.filter.ExtFilter;
import com.shulie.instrument.simulator.api.filter.MethodDescriptor;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;
import com.shulie.instrument.simulator.api.listener.ext.EventWatchBuilder;
import com.shulie.instrument.simulator.api.listener.ext.EventWatcher;
import com.shulie.instrument.simulator.api.listener.ext.PatternType;
import com.shulie.instrument.simulator.api.resource.LoadedClassDataSource;
import com.shulie.instrument.simulator.api.resource.ModuleEventWatcher;
import com.shulie.instrument.simulator.api.util.ParameterUtils;
import com.shulie.instrument.simulator.api.util.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.MetaInfServices;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/1/29 1:56 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "stack", version = "1.0.0", author = "xiaobin@shulie.io", description = "方法线程堆栈模块")
public class StackModule extends ModuleLifecycleAdapter implements ExtensionModule {
    @Resource
    private ModuleEventWatcher moduleEventWatcher;
    @Resource
    private LoadedClassDataSource loadedClassDataSource;

    @Command(value = "info", description = "查看方法执行线程堆栈")
    public CommandResponse info(Map<String, String> args) {
        final String classPattern = args.get("class");
        final String methodPattern = args.get("method");
        final String type = args.get("pattenType");
        final int wait = ParameterUtils.getInt(args, "wait", 5000);
        final PatternType patternType = PatternType.of(type);
        if (StringUtils.isBlank(classPattern)) {
            return CommandResponse.failure("class can't be empty.");
        }

        if (StringUtils.isBlank(methodPattern)) {
            return CommandResponse.failure("method can't be empty.");
        }

        if (!hasClass(classPattern, patternType)) {
            return CommandResponse.failure("class is not found with patternType :" + patternType.name() + "! " + classPattern);
        }
        CountDownLatch latch = new CountDownLatch(1);
        EventWatcher watcher = null;
        try {
            List list = new ArrayList();
            watcher = new EventWatchBuilder(moduleEventWatcher)
                    .onClass(classPattern)
                    .onAnyBehavior(methodPattern)
                    .withInvoke()
                    .onListener(Listeners.of(StackListener.class, latch, list))
                    .onClass().onWatch();
            if (wait <= 0) {
                latch.wait();
            } else {
                latch.await(wait, TimeUnit.SECONDS);
            }
            return CommandResponse.success(list);
        } catch (Throwable e) {
            return CommandResponse.failure("get [" + classPattern + "] [" + methodPattern + "] stack error!", e);
        } finally {
            if (watcher != null) {
                watcher.onUnWatched();
            }
        }

    }

    private boolean hasClass(final String classPattern, final PatternType patternType) {
        return !loadedClassDataSource.find(new ExtFilter() {
            @Override
            public boolean isIncludeSubClasses() {
                return false;
            }

            @Override
            public boolean isIncludeBootstrap() {
                return true;
            }

            @Override
            public boolean doClassNameFilter(String javaClassName) {
                return true;
            }

            @Override
            public boolean doClassFilter(ClassDescriptor classDescriptor) {
                return patternMatching(classDescriptor.getClassName(), classPattern, patternType);
            }

            @Override
            public List<BuildingForListeners> doMethodFilter(MethodDescriptor methodDescriptor) {
                return Collections.EMPTY_LIST;
            }

            @Override
            public List<BuildingForListeners> getAllListeners() {
                return Collections.EMPTY_LIST;
            }
        }).isEmpty();
    }

    /**
     * 模式匹配
     *
     * @param string      目标字符串
     * @param pattern     模式字符串
     * @param patternType 匹配模式
     * @return TRUE:匹配成功 / FALSE:匹配失败
     */
    private static boolean patternMatching(final String string,
                                           final String pattern,
                                           final PatternType patternType) {
        switch (patternType) {
            case WILDCARD:
                if (StringUtils.isBlank(pattern)) {
                    return false;
                }
                boolean matches = StringUtil.matching(string, pattern);
                if (matches) {
                    return true;
                }
                return false;
            case REGEX:
                if (StringUtils.isBlank(pattern)) {
                    return false;
                }
                if (".*".equals(pattern)) {
                    return true;
                }
                matches = string.matches(pattern);
                if (matches) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }
}
