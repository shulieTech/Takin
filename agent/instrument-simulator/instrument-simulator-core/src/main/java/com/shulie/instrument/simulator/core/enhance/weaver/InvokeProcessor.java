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

import com.shulie.instrument.simulator.api.event.EventType;
import com.shulie.instrument.simulator.api.listener.EventListener;
import com.shulie.instrument.simulator.core.util.collection.SimulatorStack;
import com.shulie.instrument.simulator.core.util.collection.ThreadUnsafeSimulatorStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 调用流程处理器
 * <p>
 * 一个 Listener 会拥有一个独立的调用处理器来专门负责该 Listener 产生的各种事件的处理
 */
class InvokeProcessor {

    private final static Logger logger = LoggerFactory.getLogger(InvokeProcessor.class);
    /**
     * 事件监听器 ID
     */
    final int listenerId;
    /**
     * 事件监听器
     */
    final EventListener listener;
    /**
     * 需要支持的事件类型列表
     */
    final EventType[] eventEventTypes;

    /**
     * 调用流程的 ThreadLocal，这个地方需要使用实例变量，因为需要根据按照线程和 {@link InvokeProcessor} 来隔离调用流程
     */
    final ThreadLocal<InvokeProcess> processRef = new ThreadLocal<InvokeProcess>() {
        @Override
        protected InvokeProcess initialValue() {
            return new InvokeProcess();
        }
    };

    /**
     * 调用流程处理器的运行状态¬
     */
    final AtomicBoolean isRunning = new AtomicBoolean(true);

    /**
     * 调用流程处理器构建器
     *
     * @param listenerId      调用流程 ID
     * @param listener        监听器
     * @param eventEventTypes 支持的事件列表
     */
    InvokeProcessor(final int listenerId,
                    final EventListener listener,
                    final EventType[] eventEventTypes) {

        this.listenerId = listenerId;
        this.eventEventTypes = eventEventTypes;
        this.listener = listener;
    }

    /**
     * 判断是否运行中状态
     *
     * @return
     */
    boolean isRunning() {
        return isRunning.get();
    }

    /**
     * 清理
     */
    void clean() {
        if (!isRunning.compareAndSet(true, false)) {
            return;
        }
        listener.clean();
        processRef.remove();
    }

    /**
     * 描述一个次调用过程，当一个方法开始调用时生成 invokeId，第一个产生的 invokeId 即为 processId
     * 因为方法存在嵌套调用的情况，所以在一次 process 过程中可能会生成一个或者多个 invokeId
     * 只有在方法结束(RETURN/THROWS 事件触发)时 invokeId 才会弹出，代表当前的方法调用已经结束
     * 当所有的栈被清空时表明当前的调用流程已经结束
     */
    class InvokeProcess {

        /**
         * 事件工厂，一个流程有一个单独的事件工厂
         */
        private final EventBuilderFactory eventBuilderFactory
                = new EventBuilderFactory();

        /**
         * 一次方法调用流程的堆栈
         */
        private final SimulatorStack<Integer> stack
                = new ThreadUnsafeSimulatorStack<Integer>();

        /**
         * 是否需要忽略整个调用过程
         * 当需要立即返回、立即抛出异常场景下需要忽略后续的流程
         */
        private boolean isIgnoreProcess = false;

        /**
         * 标记异常是否来源于 IMMEDIATELY_THROWS 事件触发
         */
        private boolean isExceptionFromImmediately = false;

        /**
         * 压入调用ID
         * 当压入的第一个调用 ID 即为流程 ID，在 BEFORE 事件中产生
         *
         * @param invokeId 调用ID
         */
        void pushInvokeId(int invokeId) {
            stack.push(invokeId);
            if (logger.isDebugEnabled()) {
                logger.debug("SIMULATOR: push process-stack, process-id={};invoke-id={};deep={};listener={};",
                        stack.peekLast(),
                        invokeId,
                        stack.deep(),
                        listenerId
                );
            }
        }

        /**
         * 弹出调用ID，每一次 BEFORE、CALL_BEFORE、LINE 时产生一个新的调用 ID
         *
         * @return 调用ID
         */
        int popInvokeId() {
            final int invokeId = stack.pop();
            if (logger.isDebugEnabled()) {
                final int processId = stack.peekLast();
                logger.debug("SIMULATOR: pop process-stack, process-id={};invoke-id={};deep={};listener={};",
                        processId,
                        invokeId,
                        stack.deep(),
                        listenerId
                );
            }
            if (stack.isEmpty()) {
                processRef.remove();
                if (logger.isDebugEnabled()) {
                    logger.debug("SIMULATOR: clean TLS: event-processor, listener={};", listenerId);
                }
            }
            return invokeId;
        }

        /**
         * 获取调用ID
         *
         * @return 调用ID
         */
        int getInvokeId() {
            return stack.peek();
        }

        /**
         * 获取调用过程ID
         *
         * @return 调用过程ID
         */
        int getProcessId() {
            return stack.peekLast();
        }

        /**
         * 是否空堆栈
         *
         * @return TRUE:是；FALSE：否
         */
        boolean isEmptyStack() {
            return stack.isEmpty();
        }

        /**
         * 当前调用过程是否需要被忽略
         *
         * @return TRUE：需要忽略；FALSE：不需要忽略
         */
        boolean isIgnoreProcess() {
            return isIgnoreProcess;
        }

        /**
         * 标记调用过程需要被忽略
         */
        void markIgnoreProcess() {
            isIgnoreProcess = true;
        }

        /**
         * 判断当前异常是否来自于ImmediatelyThrowsException，
         * 如果当前的异常来自于ImmediatelyThrowsException，则会清空当前标志位
         *
         * @return TRUE:来自于；FALSE：不来自于
         */
        boolean rollingIsExceptionFromImmediately() {
            if (isExceptionFromImmediately) {
                isExceptionFromImmediately = false;
                return true;
            }
            return false;
        }

        /**
         * 标记当前调用异常来自于ImmediatelyThrowsException
         */
        void markExceptionFromImmediately() {
            isExceptionFromImmediately = true;
        }

        /**
         * 获取事件工厂
         *
         * @return 事件工厂
         */
        EventBuilderFactory getEventFactory() {
            return eventBuilderFactory;
        }

    }
}
