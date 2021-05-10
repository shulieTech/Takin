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
package com.shulie.instrument.simulator.api.listener.ext;

import com.shulie.instrument.simulator.api.annotation.Interrupted;
import com.shulie.instrument.simulator.api.event.*;
import com.shulie.instrument.simulator.api.listener.EventListener;
import com.shulie.instrument.simulator.api.listener.Interruptable;
import com.shulie.instrument.simulator.api.util.BehaviorDescriptor;
import com.shulie.instrument.simulator.api.util.CacheGet;
import com.shulie.instrument.simulator.api.util.LazyGet;
import com.shulie.instrument.simulator.api.util.StringUtil;

import java.util.Stack;

/**
 * 通知监听器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class AdviceAdapterListener implements EventListener, Interruptable {

    private final AdviceListener adviceListener;
    private final boolean isInterruptable;

    public AdviceAdapterListener(final AdviceListener adviceListener) {
        this.adviceListener = adviceListener;
        this.isInterruptable = isInterruptEventHandler(adviceListener);
    }

    private final ThreadLocal<OpStack> opStackRef = new ThreadLocal<OpStack>() {
        @Override
        protected OpStack initialValue() {
            return new OpStack();
        }
    };

    @Override
    final public void onEvent(final Event event) throws Throwable {
        final OpStack opStack = opStackRef.get();
        try {
            switchEvent(opStack, event);
        } finally {
            // 如果执行到TOP的最后一个事件，则需要主动清理占用的资源
            if (opStack.isEmpty()) {
                opStackRef.remove();
            }
        }

    }

    @Override
    public void clean() {
        opStackRef.remove();
        adviceListener.clean();
    }


    // 执行事件
    private void switchEvent(final OpStack opStack,
                             final Event event) throws Throwable {

        switch (event.getType()) {
            case BEFORE: {
                final BeforeEvent bEvent = (BeforeEvent) event;
                final ClassLoader loader = toClassLoader(bEvent.getJavaClassLoader());
                final Advice advice = new Advice(
                        bEvent.getProcessId(),
                        bEvent.getInvokeId(),
                        new LazyGet<Behavior>() {
                            private final Class _clazz = bEvent.getClazz();
                            private final String _javaMethodName = bEvent.getJavaMethodName();
                            private final String _javaMethodDesc = bEvent.getJavaMethodDesc();

                            @Override
                            protected Behavior initialValue() throws Throwable {
                                return toBehavior(
                                        _clazz,
                                        _javaMethodName,
                                        _javaMethodDesc
                                );
                            }
                        },
                        bEvent.getClazz(),
                        loader,
                        bEvent.getArgumentArray(),
                        bEvent.getTarget()
                );

                final Advice top;
                final Advice parent;

                // 顶层调用
                if (opStack.isEmpty()) {
                    top = parent = advice;
                }

                // 非顶层
                else {
                    parent = opStack.peek().advice;
                    top = parent.getProcessTop();
                }

                advice.applyBefore(top, parent);

                opStack.pushForBegin(advice);
                adviceListener.before(advice);
                break;
            }

            /**
             * 这里需要感知到IMMEDIATELY，因为如果 IMMEDIATELY 事件在 BEFORE中触发时
             * 需要清空后续的调用栈
             */
            case IMMEDIATELY_THROWS:
            case IMMEDIATELY_RETURN: {
                final InvokeEvent invokeEvent = (InvokeEvent) event;
                WrapAdvice advice = opStack.popByExpectInvokeId(invokeEvent.getInvokeId());
                if (advice != null) {
                    advice.destroy();
                }
                break;
            }

            case RETURN: {
                final ReturnEvent rEvent = (ReturnEvent) event;
                final WrapAdvice wrapAdvice = opStack.popByExpectInvokeId(rEvent.getInvokeId());
                if (null != wrapAdvice) {
                    Advice advice = wrapAdvice.advice.applyReturn(rEvent.getReturnObj());
                    try {
                        adviceListener.afterReturning(advice);
                    } finally {
                        adviceListener.after(advice);
                    }
                    wrapAdvice.destroy();
                }
                break;
            }
            case THROWS: {
                final ThrowsEvent tEvent = (ThrowsEvent) event;
                final WrapAdvice wrapAdvice = opStack.popByExpectInvokeId(tEvent.getInvokeId());
                if (null != wrapAdvice) {
                    Advice advice = wrapAdvice.advice.applyThrows(tEvent.getThrowable());
                    try {
                        adviceListener.afterThrowing(advice);
                    } finally {
                        adviceListener.after(advice);
                    }
                    wrapAdvice.destroy();
                }
                break;
            }

            case CALL_BEFORE: {
                final CallBeforeEvent cbEvent = (CallBeforeEvent) event;
                final WrapAdvice wrapAdvice = opStack.peekByExpectInvokeId(cbEvent.getInvokeId());
                if (null == wrapAdvice) {
                    return;
                }
                final CallTarget target;
                wrapAdvice.attach(target = new CallTarget(
                        cbEvent.getLineNumber(),
                        cbEvent.isInterface(),
                        toJavaClassName(cbEvent.getOwner()),
                        cbEvent.getName(),
                        cbEvent.getDesc()
                ));
                adviceListener.beforeCall(
                        wrapAdvice.advice,
                        target.callLineNum,
                        target.isInterface,
                        target.callJavaClassName,
                        target.callJavaMethodName,
                        target.callJavaMethodDesc
                );
                break;
            }

            case CALL_RETURN: {
                final CallReturnEvent crEvent = (CallReturnEvent) event;
                final WrapAdvice wrapAdvice = opStack.peekByExpectInvokeId(crEvent.getInvokeId());
                if (null == wrapAdvice) {
                    return;
                }
                final CallTarget target = wrapAdvice.attachment();
                if (null == target) {
                    // 这里做一个容灾保护，防止在callBefore()中发生什么异常导致beforeCall()之前失败
                    return;
                }
                try {
                    adviceListener.afterCallReturning(
                            wrapAdvice.advice,
                            target.callLineNum,
                            target.isInterface,
                            target.callJavaClassName,
                            target.callJavaMethodName,
                            target.callJavaMethodDesc
                    );
                } finally {
                    adviceListener.afterCall(
                            wrapAdvice.advice,
                            target.callLineNum,
                            target.callJavaClassName,
                            target.callJavaMethodName,
                            target.callJavaMethodDesc,
                            null
                    );
                }
                break;
            }

            case CALL_THROWS: {
                final CallThrowsEvent ctEvent = (CallThrowsEvent) event;
                final WrapAdvice wrapAdvice = opStack.peekByExpectInvokeId(ctEvent.getInvokeId());
                if (null == wrapAdvice) {
                    return;
                }
                final CallTarget target = wrapAdvice.attachment();
                if (null == target) {
                    // 这里做一个容灾保护，防止在callBefore()中发生什么异常导致beforeCall()之前失败
                    return;
                }
                try {
                    adviceListener.afterCallThrowing(
                            wrapAdvice.advice,
                            target.callLineNum,
                            target.isInterface,
                            target.callJavaClassName,
                            target.callJavaMethodName,
                            target.callJavaMethodDesc,
                            ctEvent.getThrowException()
                    );
                } finally {
                    adviceListener.afterCall(
                            wrapAdvice.advice,
                            target.callLineNum,
                            target.callJavaClassName,
                            target.callJavaMethodName,
                            target.callJavaMethodDesc,
                            ctEvent.getThrowException()
                    );
                }
                break;
            }

            case LINE: {
                final LineEvent lEvent = (LineEvent) event;
                final WrapAdvice wrapAdvice = opStack.peekByExpectInvokeId(lEvent.getInvokeId());
                if (null == wrapAdvice) {
                    return;
                }
                adviceListener.beforeLine(wrapAdvice.advice, lEvent.getLineNumber());
                break;
            }

            default:
                //ignore
        }//switch
    }

    @Override
    public boolean isInterrupted() {
        return isInterruptable;
    }

    /**
     * 判断是否是中断式事件处理器
     *
     * @param adviceListener 事件监听器
     * @return TRUE:中断式;FALSE:非中断式
     */
    private static boolean isInterruptEventHandler(final AdviceListener adviceListener) {
        if (adviceListener.getClass().isAnnotationPresent(Interrupted.class)) {
            return true;
        }
        if (adviceListener instanceof Interruptable) {
            return ((Interruptable) adviceListener).isInterrupted();
        }
        return false;
    }


    // --- 以下为内部操作实现 ---


    /**
     * 通知操作堆栈
     */
    private class OpStack {

        private final Stack<WrapAdvice> adviceStack = new Stack<WrapAdvice>();

        boolean isEmpty() {
            return adviceStack.isEmpty();
        }

        WrapAdvice peek() {
            return adviceStack.peek();
        }

        void pushForBegin(final Advice advice) {
            adviceStack.push(new WrapAdvice(advice));
        }

        WrapAdvice pop() {
            return !adviceStack.isEmpty()
                    ? adviceStack.pop()
                    : null;
        }

        /**
         * 在通知堆栈中，BEFORE:[RETURN/THROWS]的invokeId是配对的，
         * 如果发生错位则说明BEFORE的事件没有被成功压入堆栈，没有被正确的处理，外界没有正确感知BEFORE
         * 所以这里也要进行修正行的忽略对应的[RETURN/THROWS]
         *
         * @param expectInvokeId 期待的invokeId
         *                       必须要求和BEFORE的invokeId配对
         * @return 如果invokeId配对成功，则返回对应的Advice，否则返回null
         */
        WrapAdvice popByExpectInvokeId(final int expectInvokeId) {
            return !adviceStack.isEmpty()
                    && adviceStack.peek().advice.getInvokeId() == expectInvokeId
                    ? adviceStack.pop()
                    : null;
        }

        WrapAdvice peekByExpectInvokeId(final int expectInvokeId) {
            return !adviceStack.isEmpty()
                    && adviceStack.peek().advice.getInvokeId() == expectInvokeId
                    ? adviceStack.peek()
                    : null;
        }

    }

    // change internalClassName to javaClassName
    private String toJavaClassName(final String internalClassName) {
        if (StringUtil.isEmpty(internalClassName)) {
            return internalClassName;
        } else {
            return internalClassName.replace('/', '.');
        }
    }

    // 提取ClassLoader，从BeforeEvent中获取到的ClassLoader
    private ClassLoader toClassLoader(ClassLoader loader) {
        return null == loader
                // 如果此处为null，则说明遇到了来自Bootstrap的类，
                ? AdviceAdapterListener.class.getClassLoader()
                : loader;
    }

    /**
     * 行为缓存KEY对象
     */
    private static class BehaviorCacheKey {
        private final Class<?> clazz;
        private final String javaMethodName;
        private final String javaMethodDesc;

        private BehaviorCacheKey(final Class<?> clazz,
                                 final String javaMethodName,
                                 final String javaMethodDesc) {
            this.clazz = clazz;
            this.javaMethodName = javaMethodName;
            this.javaMethodDesc = javaMethodDesc;
        }

        @Override
        public int hashCode() {
            return clazz.hashCode()
                    + javaMethodName.hashCode()
                    + javaMethodDesc.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (null == o
                    || !(o instanceof BehaviorCacheKey)) {
                return false;
            }
            final BehaviorCacheKey key = (BehaviorCacheKey) o;
            return clazz.equals(key.clazz)
                    && javaMethodName.equals(key.javaMethodName)
                    && javaMethodDesc.equals(key.javaMethodDesc);
        }

    }

    // 行为缓存，为了增加性能，不要每次都从class通过反射获取行为
    private final CacheGet<BehaviorCacheKey, Behavior> toBehaviorCacheGet
            = new CacheGet<BehaviorCacheKey, Behavior>() {
        @Override
        protected Behavior load(BehaviorCacheKey key) {
            if ("<init>".equals(key.javaMethodName)) {
                for (final java.lang.reflect.Constructor constructor : key.clazz.getDeclaredConstructors()) {
                    if (key.javaMethodDesc.equals(new BehaviorDescriptor(constructor).getDescriptor())) {
                        return new Constructor(constructor);
                    }
                }
            } else {
                for (final java.lang.reflect.Method method : key.clazz.getDeclaredMethods()) {
                    if (key.javaMethodName.equals(method.getName())
                            && key.javaMethodDesc.equals(new BehaviorDescriptor(method).getDescriptor())) {
                        return new Method(method);
                    }
                }
            }
            return null;
        }
    };

    /**
     * CALL目标对象
     */
    private static class CallTarget {

        final int callLineNum;
        final boolean isInterface;
        final String callJavaClassName;
        final String callJavaMethodName;
        final String callJavaMethodDesc;

        CallTarget(int callLineNum, boolean isInterface, String callJavaClassName, String callJavaMethodName, String callJavaMethodDesc) {
            this.callLineNum = callLineNum;
            this.isInterface = isInterface;
            this.callJavaClassName = callJavaClassName;
            this.callJavaMethodName = callJavaMethodName;
            this.callJavaMethodDesc = callJavaMethodDesc;
        }
    }

    /**
     * 通知内部封装，主要是要封装掉attachment
     */
    private static class WrapAdvice implements Attachment {

        Advice advice;
        Object attachment;

        WrapAdvice(Advice advice) {
            this.advice = advice;
        }

        @Override
        public void attach(Object attachment) {
            this.attachment = attachment;
        }

        @Override
        public <T> T attachment() {
            return (T) attachment;
        }

        /**
         * 销毁 advice
         */
        public void destroy() {
            attachment = null;
            if (advice != null) {
                advice.destroy();
            }
            advice = null;
        }
    }

    /**
     * 根据提供的行为名称、行为描述从指定的Class中获取对应的行为
     *
     * @param clazz          指定的Class
     * @param javaMethodName 行为名称
     * @param javaMethodDesc 行为参数声明
     * @return 匹配的行为
     * @throws NoSuchMethodException 如果匹配不到行为，则抛出该异常
     */
    private Behavior toBehavior(final Class<?> clazz,
                                final String javaMethodName,
                                final String javaMethodDesc) throws NoSuchMethodException {
        final Behavior behavior = toBehaviorCacheGet.getFromCache(new BehaviorCacheKey(clazz, javaMethodName, javaMethodDesc));
        if (null == behavior) {
            throw new NoSuchMethodException(String.format("%s.%s(%s)", clazz.getName(), javaMethodName, javaMethodDesc));
        }
        return behavior;
    }

}
