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
package com.shulie.instrument.simulator.module.trace;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.ModuleLifecycleAdapter;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.filter.ClassDescriptor;
import com.shulie.instrument.simulator.api.filter.ExtFilter;
import com.shulie.instrument.simulator.api.filter.Filter;
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
import com.shulie.instrument.simulator.module.util.ConcurrentHashSet;
import org.apache.commons.lang.ArrayUtils;
import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * trace命令
 * <p>测试用模块</p>
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "trace", version = "1.0.0", author = "xiaobin@shulie.io", description = "方法追踪模块")
public class TraceModule extends ModuleLifecycleAdapter implements ExtensionModule {
    private final static Logger logger = LoggerFactory.getLogger(TraceModule.class);

    @Resource
    private ModuleEventWatcher moduleEventWatcher;
    @Resource
    private LoadedClassDataSource loadedClassDataSource;

    private Set<Class<?>> getProxyInterfaceImplClasses(final Class<?> clazz) {
        if (!Proxy.isProxyClass(clazz)) {
            return new HashSet<Class<?>>(Arrays.asList(clazz));
        }
        return loadedClassDataSource.find(new Filter() {
            @Override
            public boolean doClassNameFilter(String javaClassName) {
                return true;
            }

            @Override
            public boolean doClassFilter(ClassDescriptor classDescriptor) {
                String[] interfaces = classDescriptor.getInterfaceTypeJavaClassNameArray();
                return ArrayUtils.contains(interfaces, clazz.getName());
            }

            @Override
            public List<BuildingForListeners> doMethodFilter(MethodDescriptor methodDescriptor) {
                return Collections.EMPTY_LIST;
            }

            @Override
            public List<BuildingForListeners> getAllListeners() {
                return Collections.EMPTY_LIST;
            }
        });
    }

    @Command(value = "info", description = "方法追踪")
    public CommandResponse trace(final Map<String, String> param) {
        final String classPattern = param.get("class");
        String methodPattern = param.get("method");
        final String condition = param.get("condition");
        final String express = param.get("express");
        /**
         * 如果 wait 和 count 都没有填写，则默认统计20条
         */
        final int wait = ParameterUtils.getInt(param, "wait", 5000);


        /**
         * 最多层数
         */
        final int level = ParameterUtils.getInt(param, "level", 0);

        /**
         * 条数限定
         */
        final int limits = ParameterUtils.getInt(param, "limits", 100);
        if (StringUtil.isEmpty(classPattern)) {
            return CommandResponse.failure("class must not be empty.");
        }

        if (StringUtil.isEmpty(methodPattern)) {
            methodPattern = "*";
        }
        Set<EventWatcher> childrenWatchers = new ConcurrentHashSet<EventWatcher>();
        List<EventWatcher> watchers = new ArrayList<EventWatcher>();
        try {
            if (wait > 10 * 60 * 1000) {
                return CommandResponse.failure("wait 最大等待时间不能超过10分钟");
            }

            if (limits > 5000) {
                return CommandResponse.failure("limits 最大不能超过5000");
            }

            /**
             * 多少毫秒以下停止
             */
            final int stopInMills = ParameterUtils.getInt(param, "stop", -1);

            Queue<Object> traceViews = new ConcurrentLinkedQueue<Object>();
            Set<String> traceMethods = new ConcurrentHashSet<String>();
            Set<Class<?>> classes = findClasses(classPattern);
            Set<Class<?>> instrumentClasses = new HashSet<Class<?>>();
            boolean foundInterface = false;
            boolean foundEnum = false;
            boolean foundAnnotation = false;
            for (Class clazz : classes) {
                if (clazz.isInterface()) {
                    Set<Class<?>> implClasses = findImplClasses(clazz);
                    if (!implClasses.isEmpty()) {
                        for (Class implClass : implClasses) {
                            instrumentClasses.addAll(getProxyInterfaceImplClasses(implClass));
                        }
                    } else {
                        foundInterface = true;
                    }
                } else if (!clazz.isEnum() && !clazz.isAnnotation()) {
                    instrumentClasses.addAll(getProxyInterfaceImplClasses(clazz));
                } else if (clazz.isEnum()) {
                    foundEnum = true;
                } else if (clazz.isAnnotation()) {
                    foundAnnotation = true;
                }
            }

            if (instrumentClasses.isEmpty()) {
                String errorMsg = "can't found class:" + classPattern + ".";
                if (foundInterface) {
                    errorMsg = "can't found impl class with interface:" + classPattern + ".";
                } else if (foundEnum) {
                    errorMsg = "can't trace class because of it is a enum:" + classPattern;
                } else if (foundAnnotation) {
                    errorMsg = "can't trace class because of it is a annotation:" + classPattern;
                }

                return CommandResponse.failure(errorMsg);
            }

            final CountDownLatch latch = new CountDownLatch(instrumentClasses.size());
            for (Class clazz : instrumentClasses) {
                EventWatcher watcher = new EventWatchBuilder(moduleEventWatcher)
                        .onClass(clazz.getName()).includeSubClasses()
                        .onAnyBehavior(methodPattern)
                        .withInvoke().withCall()
                        .onListener(Listeners.of(TraceListener.class,
                                true,
                                latch,
                                traceViews,
                                traceMethods,
                                childrenWatchers,
                                condition,
                                express,
                                level,
                                limits,
                                stopInMills,
                                wait))
                        .onClass().onWatch();
                watchers.add(watcher);
            }

            if (wait > 0) {
                latch.await(wait, TimeUnit.SECONDS);
            } else if (limits > 0) {
                latch.await();
            }
            return CommandResponse.success(traceViews);
        } catch (Throwable e) {
            logger.error("SIMULATOR: trace module err! class={}, method={}, express={}, condition={}, limits={}, wait={}",
                    classPattern, methodPattern, express, condition, limits, wait, e);
            return CommandResponse.failure(e);
        } finally {
            for (EventWatcher watcher : watchers) {
                try {
                    watcher.onUnWatched();
                } catch (Throwable e) {
                    logger.error("SIMULATOR: trace module unwatched failed! class={}, method={}, express={}, condition={}, limits={}, wait={}",
                            classPattern, methodPattern, express, condition, limits, wait, e);
                }
            }
            for (EventWatcher eventWatcher : childrenWatchers) {
                try {
                    eventWatcher.onUnWatched();
                } catch (Throwable e) {
                    logger.error("SIMULATOR: trace module unwatched failed! class={}, method={}, express={}, condition={}, limits={}, wait={}",
                            classPattern, methodPattern, express, condition, limits, wait, e);
                }
            }
        }

    }

    private Set<Class<?>> findImplClasses(final Class clazz) {
        return loadedClassDataSource.find(new ExtFilter() {
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
                String[] interfaces = classDescriptor.getInterfaceTypeJavaClassNameArray();
                return ArrayUtils.contains(interfaces, clazz.getName());
            }

            @Override
            public List<BuildingForListeners> doMethodFilter(MethodDescriptor methodDescriptor) {
                return Collections.EMPTY_LIST;
            }

            @Override
            public List<BuildingForListeners> getAllListeners() {
                return Collections.EMPTY_LIST;
            }
        });
    }

    private Set<Class<?>> findClasses(final String classPattern) {
        return loadedClassDataSource.find(new ExtFilter() {
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
                return patternMatching(javaClassName, new String[]{classPattern}, PatternType.WILDCARD);
            }

            @Override
            public boolean doClassFilter(ClassDescriptor classDescriptor) {
                return patternMatching(classDescriptor.getClassName(), new String[]{classPattern}, PatternType.WILDCARD);
            }

            @Override
            public List<BuildingForListeners> doMethodFilter(MethodDescriptor methodDescriptor) {
                return Collections.EMPTY_LIST;
            }

            @Override
            public List<BuildingForListeners> getAllListeners() {
                return Collections.EMPTY_LIST;
            }
        });
    }

    /**
     * 模式匹配
     *
     * @param string      目标字符串
     * @param patterns    模式字符串
     * @param patternType 匹配模式
     * @return TRUE:匹配成功 / FALSE:匹配失败
     */
    private static boolean patternMatching(final String string,
                                           final String[] patterns,
                                           final PatternType patternType) {
        switch (patternType) {
            case WILDCARD:
                if (patterns == null || patterns.length == 0) {
                    return false;
                }
                for (String p : patterns) {
                    boolean matches = StringUtil.matching(string, p);
                    if (matches) {
                        return true;
                    }
                }
                return false;
            case REGEX:
                if (patterns == null || patterns.length == 0) {
                    return false;
                }
                for (String p : patterns) {
                    boolean matches = string.matches(p);
                    if (matches) {
                        return true;
                    }
                }
                return false;
            default:
                return false;
        }
    }

}
