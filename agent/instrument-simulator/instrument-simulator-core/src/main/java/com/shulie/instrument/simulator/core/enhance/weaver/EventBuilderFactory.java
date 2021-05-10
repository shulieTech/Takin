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

import com.shulie.instrument.simulator.api.event.*;

/**
 * 事件构建工厂
 * <p>
 * 用于构建各种产生的事件，包括 BEFORE、RETURN、THROWS、CALL_BEFORE、CALL_RETURN、CALL_THROWS、CALL_LINE
 */
class EventBuilderFactory {

    /**
     * 构建 BEFORE 事件
     *
     * @param processId       调用流程 ID
     * @param invokeId        调用 ID
     * @param javaClassLoader classloader
     * @param clazz           产生事件的 Class
     * @param javaMethodName  产生事件的方法名称
     * @param javaMethodDesc  产生事件的方法描述
     * @param target          产生事件的目标对象
     * @param argumentArray   产生事件的方法入参
     * @return {@link BeforeEvent}
     */
    public BeforeEvent buildBeforeEvent(final int processId,
                                        final int invokeId,
                                        final ClassLoader javaClassLoader,
                                        final Class clazz,
                                        final String javaMethodName,
                                        final String javaMethodDesc,
                                        final Object target,
                                        final Object[] argumentArray) {
        BeforeEvent event = new BeforeEvent(processId, invokeId, javaClassLoader, clazz, javaMethodName, javaMethodDesc, target, argumentArray);
        return event;
    }

    /**
     * 构建返回事件
     *
     * @param processId 调用流程 ID
     * @param invokeId  调用 ID
     * @param returnObj 返回值
     * @return {@link ReturnEvent}
     */
    public ReturnEvent buildReturnEvent(final int processId,
                                        final int invokeId,
                                        final Object returnObj) {
        ReturnEvent event = new ReturnEvent(processId, invokeId, returnObj);
        return event;
    }

    /**
     * 构建立即返回事件
     *
     * @param processId 调用流程 ID
     * @param invokeId  调用 ID
     * @param returnObj 返回值
     * @return {@link ImmediatelyReturnEvent}
     */
    public ImmediatelyReturnEvent buildImmediatelyReturnEvent(final int processId,
                                                              final int invokeId,
                                                              final Object returnObj) {
        ImmediatelyReturnEvent event = new ImmediatelyReturnEvent(processId, invokeId, returnObj);
        return event;
    }

    /**
     * 构建抛出异常事件，由应用触发
     *
     * @param processId 调用流程 ID
     * @param invokeId  调用 ID
     * @param throwable 应用抛出的异常
     * @return {@link ThrowsEvent}
     */
    public ThrowsEvent buildThrowsEvent(final int processId,
                                        final int invokeId,
                                        final Throwable throwable) {
        ThrowsEvent event = new ThrowsEvent(processId, invokeId, throwable);
        return event;
    }

    /**
     * 构建立即抛出异常事件，由内部触发
     *
     * @param processId 调用流程 ID
     * @param invokeId  调用 ID
     * @param throwable 要抛出的异常
     * @return {@link ImmediatelyThrowsEvent}
     */
    public ImmediatelyThrowsEvent buildImmediatelyThrowsEvent(final int processId,
                                                              final int invokeId,
                                                              final Throwable throwable) {
        ImmediatelyThrowsEvent event = new ImmediatelyThrowsEvent(processId, invokeId, throwable);
        return event;
    }

    /**
     * 构建行调用事件
     *
     * @param processId  调用流程 ID
     * @param invokeId   调用 ID
     * @param lineNumber 行号
     * @return {@link LineEvent}
     */
    public LineEvent buildLineEvent(final int processId,
                                    final int invokeId,
                                    final int lineNumber) {
        LineEvent event = new LineEvent(processId, invokeId, lineNumber);
        return event;
    }

    /**
     * 构建 方法内部的方法调用事件（CALL_BEFORE）
     *
     * @param processId   调用流程 ID
     * @param invokeId    调用 ID
     * @param lineNumber  行号
     * @param isInterface 调用的是否是接口
     * @param owner       owner
     * @param name        方法名称
     * @param desc        方法描述
     * @return {@link CallBeforeEvent}
     */
    public CallBeforeEvent buildCallBeforeEvent(final int processId,
                                                final int invokeId,
                                                final int lineNumber,
                                                final boolean isInterface,
                                                final String owner,
                                                final String name,
                                                final String desc) {
        CallBeforeEvent event = new CallBeforeEvent(processId, invokeId, lineNumber, isInterface, owner, name, desc);
        return event;
    }

    /**
     * 构建方法内部的方法调用(CALL_RETURN)
     *
     * @param processId   调用流程 ID
     * @param invokeId    调用 ID
     * @param isInterface 调用的是否接口
     * @return {@link CallReturnEvent}
     */
    public CallReturnEvent buildCallReturnEvent(final int processId,
                                                final int invokeId,
                                                final boolean isInterface) {
        CallReturnEvent event = new CallReturnEvent(processId, invokeId, isInterface);
        return event;
    }

    /**
     * 构建方法内部的方法调用(CALL_THROWS)
     *
     * @param processId      调用流程 ID
     * @param invokeId       调用 ID
     * @param isInterface    调用的是否是接口
     * @param throwException 抛出的异常
     * @return {@link CallThrowsEvent}
     */
    public CallThrowsEvent buildCallThrowsEvent(final int processId,
                                                final int invokeId,
                                                final boolean isInterface,
                                                final Throwable throwException) {
        CallThrowsEvent event = new CallThrowsEvent(processId, invokeId, isInterface, throwException);
        return event;
    }

    /**
     * 销毁 event，让内存能更快的释放
     *
     * @param event
     */
    public void destroy(Event event) {
        if (event == null) {
            return;
        }
        event.destroy();
    }
}
