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

import com.shulie.instrument.simulator.api.filter.ClassDescriptor;
import com.shulie.instrument.simulator.api.filter.ExtFilter;
import com.shulie.instrument.simulator.api.filter.MethodDescriptor;
import com.shulie.instrument.simulator.api.listener.Listeners;
import com.shulie.instrument.simulator.api.listener.ext.*;
import com.shulie.instrument.simulator.api.resource.LoadedClassDataSource;
import com.shulie.instrument.simulator.api.resource.ModuleEventWatcher;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import com.shulie.instrument.simulator.module.express.ExpressException;
import com.shulie.instrument.simulator.module.express.ExpressFactory;
import com.shulie.instrument.simulator.module.model.trace.TraceNode;
import com.shulie.instrument.simulator.module.model.trace.TraceView;
import com.shulie.instrument.simulator.module.util.ThreadUtil;
import com.shulie.instrument.simulator.module.util.TraceInfo;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xiaobin.zfb
 * @since 2020/9/18 3:26 下午
 */
public class TraceListener extends AdviceListener {
    private final static Logger logger = LoggerFactory.getLogger(TraceListener.class);

    @Resource
    private ModuleEventWatcher moduleEventWatcher;
    @Resource
    private LoadedClassDataSource loadedClassDataSource;

    /**
     * 方法执行耗时
     */
    public static final String COST_VARIABLE = "cost";
    public static final String TARGET = "target";
    public static final String TARGET_CLASS = "targetClass";
    public static final String PARAMS = "params";
    public static final String RETURN_OBJ = "returnObj";
    public static final String THROWABLE = "throwExp";
    private final int level;
    private final int limits;
    private final int wait;
    private final int stopInMills;
    private final String condition;
    private final String express;
    private Queue<Object> traceViews;
    private Set<String> traceMethods;
    private Set<EventWatcher> eventWatchers;
    private CountDownLatch latch;
    private boolean isRoot;
    private long startMills = System.currentTimeMillis();
    private final static ThreadLocal<TraceView> traceViewThreadLocal = new ThreadLocal<TraceView>();

    public TraceListener(boolean isRoot,
                         CountDownLatch latch,
                         Queue<Object> traceViews,
                         Set<String> traceMethods,
                         Set<EventWatcher> eventWatchers,
                         String condition,
                         String express,
                         final int level,
                         final int limits,
                         final int stopInMills,
                         final int wait) {
        this.isRoot = isRoot;
        this.latch = latch;
        this.traceViews = traceViews;
        this.eventWatchers = eventWatchers;
        this.traceMethods = traceMethods;
        this.condition = condition;
        this.express = express;
        this.level = level;
        this.limits = limits;
        this.stopInMills = stopInMills;
        this.wait = wait;
    }


    /**
     * 判断条件是否满足，满足的情况下需要输出结果
     *
     * @param conditionExpress 条件表达式
     * @param advice           当前的advice对象
     * @param cost             本次执行的耗时
     * @return true 如果条件表达式满足
     */
    protected boolean isConditionMet(String conditionExpress, Advice advice, long cost) throws ExpressException {
        return StringUtils.isBlank(conditionExpress)
                || ExpressFactory.threadLocalExpress(advice)
                .bind(COST_VARIABLE, cost)
                .bind(TARGET, advice.getTarget())
                .bind(TARGET_CLASS, advice.getTargetClass())
                .bind(PARAMS, advice.getParameterArray())
                .bind(RETURN_OBJ, advice.getReturnObj())
                .bind(THROWABLE, advice.getThrowable())
                .is(conditionExpress);
    }

    protected Object getExpressionResult(String express, Advice advice, long cost) throws ExpressException {
        if (StringUtils.isBlank(express)) {
            return null;
        }
        return ExpressFactory.threadLocalExpress(advice)
                .bind(COST_VARIABLE, cost)
                .bind(TARGET, advice.getTarget())
                .bind(TARGET_CLASS, advice.getTargetClass())
                .bind(PARAMS, advice.getParameterArray())
                .bind(RETURN_OBJ, advice.getReturnObj())
                .bind(THROWABLE, advice.getThrowable())
                .get(express);
    }

    @Override
    public void before(Advice advice) throws Throwable {
        final TraceView traceView;
        if (advice.isProcessTop()) {
            if (isRoot) {
                traceView = new TraceView();
                traceView.begin(advice.getTargetClass().getName(), advice.getBehavior().getName(), advice.getClassLoader().toString());
                traceView.setTraceTime(traceView.getRoot().getStart());
                TraceInfo traceInfo = ThreadUtil.getTraceInfo(Thread.currentThread());
                if (traceInfo != null) {
                    traceView.setTraceId(traceInfo.getTraceId());
                    traceView.setRpcId(traceInfo.getRpcId());
                    traceView.setClusterTestBefore(traceInfo.isClusterTest());
                }
                advice.attach(traceView);
                traceViewThreadLocal.set(traceView);
            } else {
                traceView = traceViewThreadLocal.get();
                if (traceView == null) {
                    return;
                }
                traceView.begin(advice.getTargetClass().getName(), advice.getBehavior().getName(), advice.getClassLoader().toString());
                advice.attach(traceView);
            }
        } else {
            traceView = advice.getProcessTop().attachment();
            traceView.begin(advice.getTargetClass().getName(), advice.getBehavior().getName(), advice.getClassLoader().toString());
        }
    }

    @Override
    public void afterReturning(Advice advice) throws Throwable {
        finish(advice);
        if (isRoot) {
            traceViewThreadLocal.remove();
        }
    }

    @Override
    public void afterThrowing(Advice advice) throws Throwable {
        finish(advice);
        if (isRoot) {
            traceViewThreadLocal.remove();
        }
    }

    private int getLevel(TraceView traceView) {
        TraceNode root = traceView.getRoot();
        AtomicInteger count = new AtomicInteger(0);
        getLevel(root, count);
        return count.get();
    }

    private void getLevel(TraceNode traceNode, AtomicInteger count) {
        if (traceNode == null) {
            return;
        }
        /**
         * 如果节点需要 skip,则设置层级为 level
         */
        if (traceNode.isSkip(stopInMills)) {
            count.set(level);
            return;
        }
        getLevel(traceNode.findMaxCost(), count);
    }

    /**
     * 结束本次的调用链
     *
     * @param advice
     */
    private void finish(Advice advice) {
        try {
            /**
             * 如果是一个层级的顶点根入口
             */
            if (isRoot && advice.isProcessTop()) {
                TraceView traceView = advice.getProcessTop().attachment();
                if (traceView == null) {
                    return;
                }

                traceView.end();
                traceView.setTotalCost(traceView.getRootCost());
                traceView.setClassloader(traceView.getRoot() == null ? null : traceView.getRoot().getClassloader());
                TraceInfo traceInfo = ThreadUtil.getTraceInfo(Thread.currentThread());
                if (traceInfo != null) {
                    traceView.setClusterTestAfter(traceInfo.isClusterTest());
                }
                /**
                 * 根节点的 trace
                 */
                if (isRoot) {
                    rootTrace(advice, traceView);
                }
            } else {
                final TraceView traceView = advice.getProcessTop().attachment();
                if (traceView == null) {
                    return;
                }
                traceView.end();

                TraceNode traceNode = traceView.getCurrentMaxCost();
                traceView.getCurrent().setChildren(traceNode.getChildren());
                traceNode = traceView.getCurrentMaxCost();
                addNextLevelTrace(traceNode);

            }
        } catch (Exception e) {
            logger.warn("watch failed, condition is: {}, express is: {} ", condition, express, e);

        }
    }

    /**
     * 添加下一级的 trace
     *
     * @param traceNode
     */
    private void addNextLevelTrace(final TraceNode traceNode) {
        if (traceNode != null
                && level > 0
                && traceMethods.add(traceNode.getClassName() + "." + traceNode.getMethodName())
                && !traceNode.isSkip(stopInMills)) {
            /**
             * 如果是接口则找出所有的实现类并增强
             */
            if (traceNode.isInterface()) {
                Set<Class<?>> classes = loadedClassDataSource.find(new ExtFilter() {
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
                        return ArrayUtils.contains(interfaces, traceNode.getClassName());
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
                if (CollectionUtils.isNotEmpty(classes)) {
                    /**
                     * 增强所有接口的实现类
                     */
                    for (Class clazz : classes) {
                        if (Proxy.isProxyClass(clazz)) {
                            Class[] interfaces = clazz.getInterfaces();
                            if (ArrayUtils.isNotEmpty(interfaces)) {
                                for (final Class interfaceClass : interfaces) {
                                    Set<Class<?>> implClasses = loadedClassDataSource.find(new ExtFilter() {
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
                                            return ArrayUtils.contains(interfaces, interfaceClass.getName());
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
                                    for (Class implClass : implClasses) {
                                        final EventWatcher watcher = new EventWatchBuilder(moduleEventWatcher)
                                                .onClass(implClass.getName()).includeSubClasses()
                                                .onBehavior(traceNode.getMethodName())
                                                .withInvoke().withCall()
                                                .onListener(Listeners.of(getClass(), false, latch, traceViews, traceMethods, eventWatchers, condition, express, level - 1, limits, stopInMills))
                                                .onClass().onWatch();
                                        eventWatchers.add(watcher);
                                    }
                                }
                            }
                        } else {
                            final EventWatcher watcher = new EventWatchBuilder(moduleEventWatcher)
                                    .onClass(clazz.getName()).includeSubClasses()
                                    .onBehavior(traceNode.getMethodName())
                                    .withInvoke().withCall()
                                    .onListener(Listeners.of(getClass(), false, latch, traceViews, traceMethods, eventWatchers, condition, express, level - 1, limits, stopInMills))
                                    .onClass().onWatch();
                            eventWatchers.add(watcher);
                        }

                    }
                }

            } else {
                final EventWatcher watcher = new EventWatchBuilder(moduleEventWatcher)
                        .onClass(traceNode.getClassName()).includeSubClasses()
                        .onBehavior(traceNode.getMethodName())
                        .withInvoke().withCall()
                        .onListener(Listeners.of(getClass(), false, latch, traceViews, traceMethods, eventWatchers, condition, express, level - 1, limits, stopInMills))
                        .onClass().onWatch();
                eventWatchers.add(watcher);
            }
        }
    }

    /**
     * 只有根节点才会进行追踪
     *
     * @param advice    advice
     * @param traceView traceView
     * @throws ExpressException 如果表达式出现错误会抛出 ExpressException
     */
    private void rootTrace(Advice advice, TraceView traceView) throws ExpressException {
        /**
         * 不满足条件的直接忽略
         */
        if (!isConditionMet(condition, advice, traceView.getRootCost())) {
            return;
        }

        if ((limits > 0 && traceViews.size() < limits) || limits <= 0) {
            /**
             * 获取当前节点下的最大耗时节点
             */
            final TraceNode traceNode = traceView.getCurrentMaxCost();
            /**
             * 当level大于0并且该方法没有被追踪过并且耗时
             */
            addNextLevelTrace(traceNode);

            /**
             * 检查 traceView 是不是完整的，如果不是完整的则整体丢弃掉
             */
            if (getLevel(traceView) != level) {
                /**
                 * 清空 attachment
                 */
                advice.getProcessTop().attach(null);
                return;
            }

            traceView.setDaemon(Thread.currentThread().isDaemon());
            traceView.setPriority(Thread.currentThread().getPriority());
            traceView.setThreadId(Thread.currentThread().getId());
            traceView.setThreadName(Thread.currentThread().getName());
            /**
             * 如果是顶点则获取监测的数据
             */
            traceView.setWatches(getExpressionResult(express, advice, traceView.getTotalCost()));
            traceViews.add(traceView);
        }
        if (limits > 0 && traceViews.size() >= limits
                || (System.currentTimeMillis() - startMills) >= wait) {
            if (latch != null) {
                latch.countDown();
            }
        }
    }

    @Override
    public void beforeCall(final Advice advice,
                           final int callLineNum,
                           final boolean isInterface,
                           final String callJavaClassName,
                           final String callJavaMethodName,
                           final String callJavaMethodDesc) {
        final TraceView traceView = advice.getProcessTop().attachment();
        traceView.begin(callJavaClassName, callJavaMethodName, advice.getClassLoader().toString()).setLine(callLineNum);
        traceView.setInterface(isInterface);
    }

    @Override
    public void afterCallReturning(final Advice advice,
                                   final int callLineNum,
                                   final boolean isInterface,
                                   final String callJavaClassName,
                                   final String callJavaMethodName,
                                   final String callJavaMethodDesc) {
        final TraceView traceView = advice.getProcessTop().attachment();
        traceView.setInterface(isInterface);
        traceView.end();
    }

    @Override
    public void afterCallThrowing(final Advice advice,
                                  final int callLineNum,
                                  final boolean isInterface,
                                  final String callJavaClassName,
                                  final String callJavaMethodName,
                                  final String callJavaMethodDesc,
                                  final Throwable callThrowable) {
        final TraceView traceView = advice.getProcessTop().attachment();
        traceView.setInterface(isInterface);
        traceView.error(callJavaClassName);
        traceView.end();
    }
}
