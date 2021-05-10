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
package com.shulie.instrument.simulator.core.manager.impl;

import com.shulie.instrument.simulator.api.filter.Filter;
import com.shulie.instrument.simulator.api.listener.ext.BuildingForListeners;
import com.shulie.instrument.simulator.api.listener.ext.EventWatchCondition;
import com.shulie.instrument.simulator.api.listener.ext.Progress;
import com.shulie.instrument.simulator.api.listener.ext.WatchCallback;
import com.shulie.instrument.simulator.api.resource.DumpResult;
import com.shulie.instrument.simulator.api.resource.ModuleEventWatcher;
import com.shulie.instrument.simulator.api.util.Sequencer;
import com.shulie.instrument.simulator.compatible.transformer.SimulatorClassFileTransformer;
import com.shulie.instrument.simulator.core.CoreModule;
import com.shulie.instrument.simulator.core.enhance.weaver.EventListenerHandler;
import com.shulie.instrument.simulator.core.manager.CoreLoadedClassDataSource;
import com.shulie.instrument.simulator.core.util.matcher.ExtFilterMatcher;
import com.shulie.instrument.simulator.core.util.matcher.GroupMatcher;
import com.shulie.instrument.simulator.core.util.matcher.Matcher;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.shulie.instrument.simulator.api.filter.ExtFilterFactory.make;


/**
 * 默认模块事件观察者实现
 */
public class DefaultModuleEventWatcher implements ModuleEventWatcher {
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultModuleEventWatcher.class);

    private final Instrumentation inst;
    private final CoreLoadedClassDataSource classDataSource;
    private final CoreModule coreModule;
    private final boolean isEnableUnsafe;
    private final String namespace;

    // 观察ID序列生成器
    private final Sequencer watchIdSequencer = new Sequencer();


    DefaultModuleEventWatcher(final Instrumentation inst,
                              final CoreLoadedClassDataSource classDataSource,
                              final CoreModule coreModule,
                              final boolean isEnableUnsafe,
                              final String namespace) {
        this.inst = inst;
        this.classDataSource = classDataSource;
        this.coreModule = coreModule;
        this.isEnableUnsafe = isEnableUnsafe;
        this.namespace = namespace;
    }

    // 开始进度
    private void beginProgress(final Progress progress,
                               final int total) {
        if (null != progress) {
            try {
                progress.begin(total);
            } catch (Throwable cause) {
                LOGGER.warn("SIMULATOR: begin progress failed.", cause);
            }
        }
    }

    // 结束进度
    private void finishProgress(final Progress progress, final int cCnt, final int mCnt) {
        if (null != progress) {
            try {
                progress.finish(cCnt, mCnt);
            } catch (Throwable cause) {
                LOGGER.warn("SIMULATOR: finish progress failed.", cause);
            }
        }
    }

    /**
     * 重新增强对应的 class
     */
    private void reTransformClasses(
            final int watchId,
            final List<Class<?>> waitingReTransformClasses,
            final Progress progress) throws Throwable {
        reTransformClasses(watchId, waitingReTransformClasses, progress, false);
    }

    /**
     * 重新增强对应的 class
     */
    private void reTransformClasses(
            final int watchId,
            final List<Class<?>> waitingReTransformClasses,
            final Progress progress, boolean delete) throws Throwable {
        // 需要形变总数
        final int total = waitingReTransformClasses.size();

        // 如果找不到需要被重新增强的类则直接返回
        if (CollectionUtils.isEmpty(waitingReTransformClasses)) {
            LOGGER.debug("SIMULATOR: reTransformClasses={};module={};watch={} not found any class;",
                    waitingReTransformClasses, coreModule.getUniqueId(), watchId);
            return;
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SIMULATOR: reTransformClasses={};module={};watch={};",
                    waitingReTransformClasses, coreModule.getUniqueId(), watchId);
        }

        int index = 0;
        for (final Class<?> waitingReTransformClass : waitingReTransformClasses) {
            index++;
            try {
                if (null != progress) {
                    try {
                        progress.progressOnSuccess(waitingReTransformClass, index);
                    } catch (Throwable cause) {
                        // 在进行进度汇报的过程中抛出异常,直接进行忽略,因为不影响形变的主体流程
                        // 仅仅只是一个汇报作用而已
                        LOGGER.warn("SIMULATOR: watch={} in module={} on {} report progressOnSuccess occur exception at index={};total={};",
                                watchId, coreModule.getUniqueId(), waitingReTransformClass,
                                index - 1, total,
                                cause
                        );
                    }
                }
                if (!inst.isModifiableClass(waitingReTransformClass)) {
                    LOGGER.info("SIMULATOR: watch={} in module={} single reTransform {} class not supported, at index={};total={};",
                            watchId, coreModule.getUniqueId(), waitingReTransformClass,
                            index - 1, total
                    );
                    continue;
                }
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("SIMULATOR: pre{} watch={} in module={} single reTransform {}, at index={};total={};",
                            delete ? " delete reTransformClasses" : "",
                            watchId, coreModule.getUniqueId(), waitingReTransformClass,
                            index - 1, total
                    );
                }
                inst.retransformClasses(waitingReTransformClass);
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("SIMULATOR: {}watch={} in module={} single reTransform {} success, at index={};total={};",
                            delete ? "successful delete reTransformClasses " : "",
                            watchId, coreModule.getUniqueId(), waitingReTransformClass,
                            index - 1, total
                    );
                }
            } catch (UnsupportedOperationException e) {
                LOGGER.warn("SIMULATOR: watch={} in module={} single reTransform {} failed, at index={};total={}.",
                        watchId, coreModule.getUniqueId(), waitingReTransformClass,
                        index - 1, total,
                        e
                );
                if (null != progress) {
                    try {
                        progress.progressOnFailed(waitingReTransformClass, index, e);
                    } catch (Throwable cause) {
                        LOGGER.warn("SIMULATOR: watch={} in module={} on {} report progressOnFailed occur exception, at index={};total={};",
                                watchId, coreModule.getUniqueId(), waitingReTransformClass,
                                index - 1, total,
                                cause
                        );
                    }
                }
                throw e;
            } catch (Throwable causeOfReTransform) {
                /**
                 * retransformClasses 可能会导致 VerifyError，暂时还未找到产生这个问题的原因
                 * 先屏蔽忽略这个问题，后续再查产生这个问题的原因
                 * TODO
                 */
                LOGGER.warn("SIMULATOR: watch={} in module={} single reTransform {} failed, at index={};total={}. ignore this class.",
                        watchId, coreModule.getUniqueId(), waitingReTransformClass,
                        index - 1, total,
                        causeOfReTransform
                );
                if (null != progress) {
                    try {
                        progress.progressOnFailed(waitingReTransformClass, index, causeOfReTransform);
                    } catch (Throwable cause) {
                        LOGGER.warn("SIMULATOR: watch={} in module={} on {} report progressOnFailed occur exception, at index={};total={};",
                                watchId, coreModule.getUniqueId(), waitingReTransformClass,
                                index - 1, total,
                                cause
                        );
                    }
                }
                throw causeOfReTransform;
            }
        }//for

    }

    @Override
    public int watch(final Filter filter) {
        return watch(filter, null);
    }


    @Override
    public int watch(final Filter filter,
                     final Progress progress) {
        return watch(new ExtFilterMatcher(make(filter)), progress);
    }


    @Override
    public int watch(final EventWatchCondition condition,
                     final Progress progress) {
        return watch(ExtFilterMatcher.toOrGroupMatcher(condition.getOrFilterArray()), progress);
    }


    private DumpResult dump(final Matcher matcher, Progress progress) {
        final int watchId = watchIdSequencer.next();
        // 给对应的模块追加ClassFileTransformer
        SimulatorClassFileTransformer proxy = DumpTransformer.build(coreModule, coreModule.getSimulatorConfig(), matcher, isEnableUnsafe, watchId);
        // 注册到CoreModule中
        coreModule.getSimulatorClassFileTransformers().add(proxy);

        //这里addTransformer后，接下来引起的类加载都会经过sandClassFileTransformer
        inst.addTransformer(proxy, true);


        // 查找需要渲染的类集合
        final List<Class<?>> waitingReTransformClasses = classDataSource.findForReTransform(matcher);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("SIMULATOR: watch={} in module={} found {} classes for watch(ing).",
                    watchId,
                    coreModule.getUniqueId(),
                    waitingReTransformClasses.size()
            );
        }

        int effectClassCount = 0, effectMethodCount = 0;

        // 进度通知启动
        beginProgress(progress, waitingReTransformClasses.size());
        try {

            // 应用JVM
            try {
                reTransformClasses(watchId, waitingReTransformClasses, progress);
            } catch (Throwable throwable) {
                //ignore
                /**
                 * retransformClasses 可能会导致 VerifyError，暂时还未找到产生这个问题的原因
                 * 先屏蔽忽略这个问题，后续再查产生这个问题的原因
                 * TODO
                 */
            }

            // 计数
            effectClassCount += proxy.getAffectStatistic().getEffectClassCount();
            effectMethodCount += proxy.getAffectStatistic().getEffectMethodCount();
        } finally {
            finishProgress(progress, effectClassCount, effectMethodCount);
        }

        return DumpResult.build(watchId, proxy.getDumpResult());
    }

    @Override
    public DumpResult dump(Filter filter, Progress progress) {
        return dump(new ExtFilterMatcher(make(filter)), progress);
    }

    // 这里是用matcher重制过后的watch
    private int watch(final Matcher matcher,
                      final Progress progress) {
        final int watchId = watchIdSequencer.next();
        // 给对应的模块追加ClassFileTransformer
        final SimulatorClassFileTransformer transformer = new DefaultSimulatorClassFileTransformer(this,
                watchId, coreModule, matcher, isEnableUnsafe, namespace);

        SimulatorClassFileTransformer proxy = CostDumpTransformer.wrap(BytecodeDumpTransformer.wrap(transformer, coreModule.getSimulatorConfig()), coreModule.getSimulatorConfig());
        // 注册到CoreModule中
        coreModule.getSimulatorClassFileTransformers().add(proxy);

        //这里addTransformer后，接下来引起的类加载都会经过sandClassFileTransformer
        inst.addTransformer(proxy, true);

        // 查找需要渲染的类集合
        final List<Class<?>> waitingReTransformClasses = classDataSource.findForReTransform(matcher);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("SIMULATOR: watch={} in module={} found {} classes for watch(ing).",
                    watchId,
                    coreModule.getUniqueId(),
                    waitingReTransformClasses.size()
            );
        }

        int effectClassCount = 0, effectMethodCount = 0;

        // 进度通知启动
        beginProgress(progress, waitingReTransformClasses.size());
        try {

            // 应用JVM
            try {
                reTransformClasses(watchId, waitingReTransformClasses, progress);
            } catch (Throwable throwable) {
                //ignore
                /**
                 * retransformClasses 可能会导致 VerifyError，暂时还未找到产生这个问题的原因
                 * 先屏蔽忽略这个问题，后续再查产生这个问题的原因
                 * TODO
                 */
            }

            // 计数
            effectClassCount += proxy.getAffectStatistic().getEffectClassCount();
            effectMethodCount += proxy.getAffectStatistic().getEffectMethodCount();


            // 激活增强类
            if (coreModule.isActivated()) {
                List<BuildingForListeners> listeners = proxy.getAllListeners();
                if (CollectionUtils.isNotEmpty(listeners)) {
                    for (BuildingForListeners buildingForListeners : listeners) {
                        EventListenerHandler.getSingleton()
                                .active(buildingForListeners.getListenerId(), proxy.getEventListeners().get(buildingForListeners.getListenerId()), buildingForListeners.getEventTypes());
                    }
                }
            }

        } finally {
            finishProgress(progress, effectClassCount, effectMethodCount);
        }

        return watchId;
    }

    @Override
    public void delete(final int watcherId,
                       final Progress progress) {

        final Set<Matcher> waitingRemoveMatcherSet = new LinkedHashSet<Matcher>();

        // 找出待删除的SimulatorClassFileTransformer
        final Iterator<SimulatorClassFileTransformer> it = coreModule.getSimulatorClassFileTransformers().iterator();
        int cCnt = 0, mCnt = 0;
        while (it.hasNext()) {
            final SimulatorClassFileTransformer simulatorClassFileTransformer = it.next();
            if (watcherId == simulatorClassFileTransformer.getWatchId()) {

                List<BuildingForListeners> listeners = simulatorClassFileTransformer.getAllListeners();
                if (CollectionUtils.isNotEmpty(listeners)) {
                    for (BuildingForListeners buildingForListeners : listeners) {
                        // 冻结所有关联代码增强
                        EventListenerHandler.getSingleton()
                                .frozen(buildingForListeners.getListenerId());
                    }
                }


                // 在JVM中移除掉命中的ClassFileTransformer
                inst.removeTransformer(simulatorClassFileTransformer);

                // 计数
                cCnt += simulatorClassFileTransformer.getAffectStatistic().getEffectClassCount();
                mCnt += simulatorClassFileTransformer.getAffectStatistic().getEffectMethodCount();

                // 追加到待删除过滤器集合
                waitingRemoveMatcherSet.add((Matcher) simulatorClassFileTransformer.getMatcher());

                // 清除掉该SimulatorClassFileTransformer
                it.remove();

            }
        }

        // 查找需要删除后重新渲染的类集合
        final List<Class<?>> waitingReTransformClasses = classDataSource.findForReTransform(
                new GroupMatcher.Or(waitingRemoveMatcherSet.toArray(new Matcher[0]))
        );
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("SIMULATOR: watch={} in module={} found {} classes for delete.",
                    watcherId,
                    coreModule.getUniqueId(),
                    waitingReTransformClasses.size()
            );
        }

        beginProgress(progress, waitingReTransformClasses.size());
        try {
            // 应用JVM
            try {
                reTransformClasses(watcherId, waitingReTransformClasses, progress, true);
            } catch (Throwable throwable) {
                //ignore
                /**
                 * retransformClasses 可能会导致 VerifyError，暂时还未找到产生这个问题的原因
                 * 先屏蔽忽略这个问题，后续再查产生这个问题的原因
                 * TODO
                 */
            }
        } finally {
            finishProgress(progress, cCnt, mCnt);
        }
    }

    @Override
    public void delete(int watcherId) {
        delete(watcherId, null);
    }

    @Override
    public void watching(Filter filter, WatchCallback watchCb) throws Throwable {
        watching(filter, null, watchCb, null);
    }

    @Override
    public void close() {
    }


    @Override
    public void watching(final Filter filter,
                         final Progress wProgress,
                         final WatchCallback watchCb,
                         final Progress dProgress) throws Throwable {
        final int watchId = watch(new ExtFilterMatcher(make(filter)), wProgress);
        try {
            watchCb.watchCompleted();
        } finally {
            delete(watchId, dProgress);
        }
    }

}
