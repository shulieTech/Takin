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
package com.shulie.instrument.simulator.core.enhance.weaver;

import com.shulie.instrument.simulator.api.ProcessControlException;
import com.shulie.instrument.simulator.api.event.BeforeEvent;
import com.shulie.instrument.simulator.api.event.Event;
import com.shulie.instrument.simulator.api.event.EventType;
import com.shulie.instrument.simulator.api.event.InvokeEvent;
import com.shulie.instrument.simulator.api.listener.EventListener;
import com.shulie.instrument.simulator.core.classloader.BizClassLoaderHolder;
import com.shulie.instrument.simulator.core.util.ReflectUtils;
import com.shulie.instrument.simulator.api.guard.SimulatorGuard;
import com.shulie.instrument.simulator.message.MessageHandler;
import com.shulie.instrument.simulator.message.Messager;
import com.shulie.instrument.simulator.message.Result;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.shulie.instrument.simulator.message.Result.newNone;
import static com.shulie.instrument.simulator.message.Result.newThrows;
import static org.apache.commons.lang.StringUtils.join;

/**
 * 事件处理
 */
public class EventListenerHandler implements MessageHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    // 调用序列生成器
    private final AtomicInteger invokeIdSequencer = new AtomicInteger(1000);

    // 全局处理器ID:处理器映射集合
    private final Map<Integer/*LISTENER_ID*/, InvokeProcessor> mappingOfEventProcessor
            = new ConcurrentHashMap<Integer, InvokeProcessor>();

    /**
     * 全局的单例
     */
    private static EventListenerHandler singleton = new EventListenerHandler();

    /**
     * 获取事件监听器处理的单例
     *
     * @return {@link EventListenerHandler}
     */
    public static EventListenerHandler getSingleton() {
        return singleton;
    }

    /**
     * 注册事件处理器
     *
     * @param listenerId      事件监听器ID
     * @param listener        事件监听器
     * @param eventEventTypes 监听事件集合
     */
    public void active(final int listenerId,
                       final EventListener listener,
                       final EventType[] eventEventTypes) {
        mappingOfEventProcessor.put(listenerId, new InvokeProcessor(listenerId, listener, eventEventTypes));
        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: activated listener[id={};target={};] event={}",
                    listenerId,
                    listener,
                    join(eventEventTypes, ",")
            );
        }
    }

    /**
     * 取消事件处理器
     *
     * @param listenerId 事件处理器ID
     */
    public void frozen(int listenerId) {
        final InvokeProcessor processor = mappingOfEventProcessor.remove(listenerId);
        if (null == processor) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: ignore frozen listener={}, because not found.", listenerId);
            }
            return;
        }

        if (logger.isInfoEnabled()) {
            logger.info("SIMULATOR: frozen listener[id={};target={};]",
                    listenerId,
                    processor.listener
            );
        }
        /**
         * processor 清理
         */
        processor.clean();
    }

    /**
     * 调用出发事件处理&调用执行流程控制
     *
     * @param listenerId 处理器ID
     * @param processId  调用过程ID
     * @param invokeId   调用ID
     * @param event      调用事件
     * @param processor  事件处理器
     * @return 处理返回结果
     * @throws Throwable 当出现未知异常时,且事件处理器为中断流程事件时抛出
     */
    private Result handleEvent(final int listenerId,
                               final int processId,
                               final int invokeId,
                               final Class clazz,
                               final Event event,
                               final InvokeProcessor processor) throws Throwable {
        final EventListener listener = processor.listener;

        /**
         * 如果当前事件不在事件监听器处理列表中，则直接返回，不处理事件
         */
        if (!ArrayUtils.contains(processor.eventEventTypes, event.getType())) {
            return newNone();
        }

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: on-event: event -> eventType={}, processId={}, invokeId={}, listenerId={}",
                        event.getType(),
                        processId,
                        invokeId,
                        listenerId
                );
            }
            /**
             * 调用事件处理
             */
            listener.onEvent(event);
        } catch (ProcessControlException pce) {
            /**
             * 代码执行流程变更，如返回或者抛出异常
             */
            if (!processor.isRunning()) {
                logger.warn("SIMULATOR: EventProcessor is closed. {}", clazz.getName());
                return Result.newNone();
            }
            final InvokeProcessor.InvokeProcess invokeProcess = processor.processRef.get();

            final ProcessControlException.State state = pce.getState();
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: on-event: event -> eventType={}, processId={}, invokeId={}, listenerId={}, process-changed: {}. isIgnoreProcessEvent={};",
                        event.getType(),
                        processId,
                        invokeId,
                        listenerId,
                        state,
                        pce.isIgnoreProcessEvent()
                );
            }

            /**
             * 如果流程控制要求忽略后续处理所有事件，则需要在此处进行标记
             */
            if (pce.isIgnoreProcessEvent()) {
                invokeProcess.markIgnoreProcess();
            }

            switch (state) {

                // 立即返回对象
                case RETURN_IMMEDIATELY: {

                    /**
                     * 如果已经禁止后续返回任何事件了，则不进行后续的操作
                     */
                    if (pce.isIgnoreProcessEvent()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("SIMULATOR: on-event: event -> eventType={}, processId={}, invokeId={}, listenerId={}, ignore immediately-return-event, isIgnored.",
                                    event.getType(),
                                    processId,
                                    invokeId,
                                    listenerId
                            );
                        }
                    } else {
                        /**
                         * 补偿立即返回事件, 补偿立即返回事件为了保证BEFORE、RETURN/THROWS 能成对的调用
                         * 避免调用栈出现不对称的情况
                         * 如果 BEFORE 事件触发立即返回事件，
                         */
                        compensateProcessControlEvent(pce, processor, invokeProcess, event);
                    }

                    /**
                     * 如果是在BEFORE中立即返回，则后续不会再有RETURN事件产生
                     * 这里需要主动对齐堆栈
                     */
                    if (event.getType() == EventType.BEFORE) {
                        invokeProcess.popInvokeId();
                    }

                    /**
                     * 让流程立即返回
                     */
                    return Result.newReturn(pce.getResult());

                }

                // 立即抛出异常
                case THROWS_IMMEDIATELY: {

                    final Throwable throwable = (Throwable) pce.getResult();

                    /**
                     * 如果已经禁止后续返回任何事件了，则不进行后续的操作
                     */
                    if (pce.isIgnoreProcessEvent()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("SIMULATOR: on-event: event -> eventType={}, processId={}, invokeId={}, listenerId={}, ignore immediately-throws-event, isIgnored.",
                                    event.getType(),
                                    processId,
                                    invokeId,
                                    listenerId
                            );
                        }
                    } else {

                        /**
                         *  如果是在BEFORE中立即抛出，则后续不会再有THROWS事件产生
                         *  这里需要主动对齐堆栈
                         */
                        if (event.getType() == EventType.BEFORE) {
                            invokeProcess.popInvokeId();
                        }

                        /**
                         * 标记本次异常由ImmediatelyException产生，让下次异常事件处理直接忽略
                         */
                        if (event.getType() != EventType.THROWS) {
                            invokeProcess.markExceptionFromImmediately();
                        }

                        /**
                         * 补偿立即抛出事件
                         */
                        compensateProcessControlEvent(pce, processor, invokeProcess, event);
                    }

                    /**
                     * 让流程立即抛出异常
                     */
                    return Result.newThrows(throwable);

                }

                /**
                 * 什么都不操作，立即返回
                 */
                case NONE_IMMEDIATELY:
                default: {
                    return newNone();
                }
            }

        } catch (Throwable throwable) {
            /**
             * BEFORE处理异常,打日志,并通知下游不需要进行处理
             */

            /**
             * 如果当前事件处理器是可中断的事件处理器,则对外抛出UnCaughtException
             * 中断当前方法
             */
            if (ReflectUtils.isInterruptEventHandler(listener)) {
                throw throwable;
            } else {
                /**
                 * 普通事件处理器则可以打个日志后,直接放行
                 */
                if (Messager.getExceptionHandler() != null) {
                    Messager.getExceptionHandler().handleException(throwable,
                            String.format("on-event: event -> eventType=%s, processId=%s, invokeId=%s, listenerId=%s occur an error.", event.getType().name(), String.valueOf(processId), String.valueOf(invokeId), String.valueOf(listenerId)),
                            listener);
                } else {
                    logger.warn("SIMULATOR: on-event: event -> eventType={}, processId={}, invokeId={}, listenerId={} occur an error.",
                            event.getType(),
                            processId,
                            invokeId,
                            listenerId,
                            throwable
                    );
                }
            }
        }

        /**
         * 默认返回不进行任何流程变更
         */
        return newNone();
    }

    /**
     * 构建流程控制事件的补偿，流程控制事件包括立即返回和立即抛出异常
     *
     * @param pce           流程控制异常
     * @param processor     调用流程处理器
     * @param invokeProcess 调用流程
     * @param event         当前的事件，这个事件是触发流程控制事件的源事件
     */
    private void compensateProcessControlEvent(ProcessControlException pce, InvokeProcessor processor, InvokeProcessor.InvokeProcess invokeProcess, Event event) {

        /**
         * 判断是否需要补偿
         */
        if (!(event instanceof InvokeEvent)
                || !ArrayUtils.contains(processor.eventEventTypes, event.getType())) {
            return;
        }

        final InvokeEvent iEvent = (InvokeEvent) event;
        final Event compensateEvent;

        if (pce.getState() == ProcessControlException.State.RETURN_IMMEDIATELY) {
            /**
             * 构建补偿立即返回事件
             */
            compensateEvent = invokeProcess
                    .getEventFactory()
                    .buildImmediatelyReturnEvent(iEvent.getProcessId(), iEvent.getInvokeId(), pce.getResult());
        } else if (pce.getState() == ProcessControlException.State.THROWS_IMMEDIATELY) {
            /**
             * 构建补偿立即抛出事件
             */
            compensateEvent = invokeProcess
                    .getEventFactory()
                    .buildImmediatelyThrowsEvent(iEvent.getProcessId(), iEvent.getInvokeId(), (Throwable) pce.getResult());
        } else {
            /**
             * 其他情况直接忽略,不补偿
             */
            return;
        }

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: compensate-event: event -> eventType={}, processId={}, invokeId={}, listenerId={} when ori-event:{}",
                        compensateEvent.getType(),
                        iEvent.getProcessId(),
                        iEvent.getInvokeId(),
                        processor.listenerId,
                        event.getType()
                );
            }
            /**
             * 需要触发补偿事件，因为后续的 AdviceAdapterListener 需要对此事件做出感应
             * 清理栈等，如果是自定义的实现，也可以提供渠道感应 IMMEDIATELY 事件
             */
            processor.listener.onEvent(compensateEvent);
        } catch (Throwable cause) {
            logger.warn("SIMULATOR: compensate-event: event -> eventType={}, processId={}, invokeId={}, listenerId={} when ori-event:{} occur error.",
                    compensateEvent.getType(),
                    iEvent.getProcessId(),
                    iEvent.getInvokeId(),
                    processor.listenerId,
                    event.getType(),
                    cause
            );
        } finally {
            invokeProcess.getEventFactory().destroy(compensateEvent);
        }
    }

    /**
     * 判断堆栈是否错位
     *
     * @param processId    调用流程 ID
     * @param invokeId     调用 ID
     * @param isEmptyStack 堆栈是否为空
     */
    private boolean checkProcessStack(final int processId,
                                      final int invokeId,
                                      final boolean isEmptyStack) {
        return (processId == invokeId && !isEmptyStack)
                || (processId != invokeId && isEmptyStack);
    }

    @Override
    public Result handleOnBefore(int listenerId, Object[] argumentArray, Class clazz, String javaMethodName, String javaMethodDesc, Object target) throws Throwable {

        /**
         * 在守护区内产生的事件不需要响应
         */
        if (SimulatorGuard.getInstance().isInGuard()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is in protecting, ignore processing before-event", listenerId);
            }
            return newNone();
        }

        /**
         * 获取事件处理器
         */
        final InvokeProcessor processor = mappingOfEventProcessor.get(listenerId);

        /**
         * 如果尚未注册,则直接返回,不做任何处理
         */
        if (null == processor) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is not activated, ignore processing before-event.", listenerId);
            }
            return newNone();
        }

        if (!processor.isRunning()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} EventProcessor is closed, ignore processing before-event.", listenerId);
            }
            return newNone();
        }

        /**
         * 获取调用流程
         */
        final InvokeProcessor.InvokeProcess invokeProcess = processor.processRef.get();

        /**
         * 如果当前处理ID被忽略，则立即返回
         */
        if (invokeProcess.isIgnoreProcess()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is marked ignore process!", listenerId);
            }
            return newNone();
        }

        /**
         * BEFORE 事件时产生新的 invokeId
         */
        int invokeId = invokeIdSequencer.getAndIncrement();
        invokeProcess.pushInvokeId(invokeId);

        /**
         * 调用过程ID
         */
        final int processId = invokeProcess.getProcessId();

        ClassLoader javaClassLoader = clazz.getClassLoader();
        /**
         * 放置业务类加载器
         */
        BizClassLoaderHolder.setBizClassLoader(javaClassLoader);
        final BeforeEvent event = invokeProcess.getEventFactory().buildBeforeEvent(
                processId,
                invokeId,
                javaClassLoader,
                clazz,
                javaMethodName,
                javaMethodDesc,
                target,
                argumentArray
        );
        try {
            return handleEvent(listenerId, processId, invokeId, clazz, event, processor);
        } finally {
            invokeProcess.getEventFactory().destroy(event);
            BizClassLoaderHolder.clearBizClassLoader();
        }
    }

    @Override
    public Result handleOnThrows(int listenerId, Class clazz, Throwable throwable) throws Throwable {
        try {
            BizClassLoaderHolder.setBizClassLoader(clazz.getClassLoader());
            return handleOnEnd(listenerId, clazz, throwable, false);
        } finally {
            BizClassLoaderHolder.clearBizClassLoader();
        }
    }

    @Override
    public Result handleOnReturn(int listenerId, Class clazz, Object object) throws Throwable {
        try {
            BizClassLoaderHolder.setBizClassLoader(clazz.getClassLoader());
            return handleOnEnd(listenerId, clazz, object, true);
        } finally {
            BizClassLoaderHolder.clearBizClassLoader();
        }
    }


    private Result handleOnEnd(final int listenerId,
                               final Class clazz,
                               final Object object,
                               final boolean isReturn) throws Throwable {

        /**
         * 在守护区内产生的事件不需要响应
         */
        if (SimulatorGuard.getInstance().isInGuard()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is in protecting, ignore processing {}-event", listenerId, isReturn ? "return" : "throws");
            }
            return newNone();
        }

        final InvokeProcessor processor = mappingOfEventProcessor.get(listenerId);

        /**
         * 如果尚未注册,则直接返回,不做任何处理
         */
        if (null == processor) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is not activated, ignore processing return-event|throws-event.", listenerId);
            }
            return newNone();
        }

        if (!processor.isRunning()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} EventProcessor is closed, ignore processing before-event.", listenerId);
            }
            return newNone();
        }

        final InvokeProcessor.InvokeProcess invokeProcess = processor.processRef.get();

        // 如果当前调用过程信息堆栈是空的,说明
        // 1. BEFORE/RETURN错位
        // 2. super.<init>
        // 处理方式是直接返回,不做任何事件的处理和代码流程的改变,放弃对super.<init>的观察
        if (invokeProcess.isEmptyStack()) {
            return newNone();
        }

        // 如果异常来自于ImmediatelyException，则忽略处理直接返回抛异常
        final boolean isExceptionFromImmediately = !isReturn && invokeProcess.rollingIsExceptionFromImmediately();
        if (isExceptionFromImmediately) {
            return newThrows((Throwable) object);
        }

        // 继续异常处理
        final int processId = invokeProcess.getProcessId();
        final int invokeId = invokeProcess.popInvokeId();

        // 忽略事件处理
        // 放在stack.pop()后边是为了对齐执行栈
        if (invokeProcess.isIgnoreProcess()) {
            return newNone();
        }

        // 如果ProcessId==InvokeId说明已经到栈顶，此时需要核对堆栈是否为空
        // 如果不为空需要输出日志进行告警
        if (checkProcessStack(processId, invokeId, invokeProcess.isEmptyStack())) {
            logger.warn("SIMULATOR: ERROR process-stack. pid={};iid={};listener={};",
                    processId,
                    invokeId,
                    listenerId
            );
        }

        final Event event = isReturn
                ? invokeProcess.getEventFactory().buildReturnEvent(processId, invokeId, object)
                : invokeProcess.getEventFactory().buildThrowsEvent(processId, invokeId, (Throwable) object);

        try {
            return handleEvent(listenerId, processId, invokeId, clazz, event, processor);
        } finally {
            invokeProcess.getEventFactory().destroy(event);
        }

    }


    @Override
    public void handleOnCallBefore(final int listenerId, final Class clazz, final boolean isInterface, final int lineNumber, final String owner, final String name, final String desc) throws Throwable {

        // 在守护区内产生的事件不需要响应
        if (SimulatorGuard.getInstance().isInGuard()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is in protecting, ignore processing call-before-event", listenerId);
            }
            return;
        }

        final InvokeProcessor wrap = mappingOfEventProcessor.get(listenerId);
        if (null == wrap) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is not activated, ignore processing call-before-event.", listenerId);
            }
            return;
        }

        if (!wrap.isRunning()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} EventProcessor is closed, ignore processing before-event.", listenerId);
            }
            return;
        }

        final InvokeProcessor.InvokeProcess invokeProcess = wrap.processRef.get();

        // 如果当前调用过程信息堆栈是空的,有两种情况
        // 1. CALL_BEFORE事件和BEFORE事件错位
        // 2. 当前方法是<init>，而CALL_BEFORE事件触发是当前方法在调用父类的<init>
        //    super.<init>会导致CALL_BEFORE事件优先于BEFORE事件
        // 但如果按照现在的架构要兼容这种情况，比较麻烦，所以暂时先放弃了这部分的消息，可惜可惜
        if (invokeProcess.isEmptyStack()) {
            return;
        }

        final int processId = invokeProcess.getProcessId();
        final int invokeId = invokeProcess.getInvokeId();

        // 如果事件处理流被忽略，则直接返回，不产生后续事件
        if (invokeProcess.isIgnoreProcess()) {
            return;
        }

        final Event event = invokeProcess
                .getEventFactory()
                .buildCallBeforeEvent(processId, invokeId, lineNumber, isInterface, owner, name, desc);
        try {
            BizClassLoaderHolder.setBizClassLoader(clazz.getClassLoader());
            handleEvent(listenerId, processId, invokeId, clazz, event, wrap);
        } finally {
            invokeProcess.getEventFactory().destroy(event);
            BizClassLoaderHolder.clearBizClassLoader();
        }
    }

    @Override
    public void handleOnCallReturn(final int listenerId, final Class clazz, final boolean isInterface) throws Throwable {

        // 在守护区内产生的事件不需要响应
        if (SimulatorGuard.getInstance().isInGuard()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is in protecting, ignore processing call-return-event", listenerId);
            }
            return;
        }

        final InvokeProcessor wrap = mappingOfEventProcessor.get(listenerId);
        if (null == wrap) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is not activated, ignore processing call-return-event.", listenerId);
            }
            return;
        }

        if (!wrap.isRunning()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} EventProcessor is closed, ignore processing before-event.", listenerId);
            }
            return;
        }

        final InvokeProcessor.InvokeProcess invokeProcess = wrap.processRef.get();
        if (invokeProcess.isEmptyStack()) {
            return;
        }

        final int processId = invokeProcess.getProcessId();
        final int invokeId = invokeProcess.getInvokeId();

        // 如果事件处理流被忽略，则直接返回，不产生后续事件
        if (invokeProcess.isIgnoreProcess()) {
            return;
        }

        final Event event = invokeProcess
                .getEventFactory()
                .buildCallReturnEvent(processId, invokeId, isInterface);
        try {
            BizClassLoaderHolder.setBizClassLoader(clazz.getClassLoader());
            handleEvent(listenerId, processId, invokeId, clazz, event, wrap);
        } finally {
            invokeProcess.getEventFactory().destroy(event);
            BizClassLoaderHolder.clearBizClassLoader();
        }
    }

    @Override
    public void handleOnCallThrows(final int listenerId, final Class clazz, final boolean isInterface, Throwable e) throws Throwable {

        // 在守护区内产生的事件不需要响应
        if (SimulatorGuard.getInstance().isInGuard()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is in protecting, ignore processing call-throws-event", listenerId);
            }
            return;
        }

        final InvokeProcessor wrap = mappingOfEventProcessor.get(listenerId);
        if (null == wrap) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is not activated, ignore processing call-throws-event.", listenerId);
            }
            return;
        }

        if (!wrap.isRunning()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} EventProcessor is closed, ignore processing before-event.", listenerId);
            }
            return;
        }

        final InvokeProcessor.InvokeProcess invokeProcess = wrap.processRef.get();
        if (invokeProcess.isEmptyStack()) {
            return;
        }

        final int processId = invokeProcess.getProcessId();
        final int invokeId = invokeProcess.getInvokeId();

        // 如果事件处理流被忽略，则直接返回，不产生后续事件
        if (invokeProcess.isIgnoreProcess()) {
            return;
        }

        final Event event = invokeProcess
                .getEventFactory()
                .buildCallThrowsEvent(processId, invokeId, isInterface, e);
        try {
            BizClassLoaderHolder.setBizClassLoader(clazz.getClassLoader());
            handleEvent(listenerId, processId, invokeId, clazz, event, wrap);
        } finally {
            invokeProcess.getEventFactory().destroy(event);
            BizClassLoaderHolder.clearBizClassLoader();
        }
    }

    @Override
    public void handleOnLine(int listenerId, Class clazz, int lineNumber) throws Throwable {

        // 在守护区内产生的事件不需要响应
        if (SimulatorGuard.getInstance().isInGuard()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is in protecting, ignore processing call-line-event", listenerId);
            }
            return;
        }

        final InvokeProcessor wrap = mappingOfEventProcessor.get(listenerId);
        if (null == wrap) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} is not activated, ignore processing line-event.", listenerId);
            }
            return;
        }

        if (!wrap.isRunning()) {
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: listener={} EventProcessor is closed, ignore processing before-event.", listenerId);
            }
            return;
        }

        final InvokeProcessor.InvokeProcess invokeProcess = wrap.processRef.get();

        // 如果当前调用过程信息堆栈是空的,说明BEFORE/LINE错位
        // 处理方式是直接返回,不做任何事件的处理和代码流程的改变
        if (invokeProcess.isEmptyStack()) {
            return;
        }

        final int processId = invokeProcess.getProcessId();
        final int invokeId = invokeProcess.getInvokeId();

        // 如果事件处理流被忽略，则直接返回，不产生后续事件
        if (invokeProcess.isIgnoreProcess()) {
            return;
        }

        final Event event = invokeProcess.getEventFactory().buildLineEvent(processId, invokeId, lineNumber);
        try {
            BizClassLoaderHolder.setBizClassLoader(clazz.getClassLoader());
            handleEvent(listenerId, processId, invokeId, clazz, event, wrap);
        } finally {
            invokeProcess.getEventFactory().destroy(event);
            BizClassLoaderHolder.clearBizClassLoader();
        }
    }
}
