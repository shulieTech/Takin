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
package com.shulie.instrument.simulator.api;

import static com.shulie.instrument.simulator.api.ProcessControlException.State.RETURN_IMMEDIATELY;
import static com.shulie.instrument.simulator.api.ProcessControlException.State.THROWS_IMMEDIATELY;

/**
 * 流程控制异常
 * <p>用于控制事件处理器处理事件走向</p>
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public final class ProcessControlException extends Exception {

    /**
     * 流程控制状态
     */
    private final State state;

    /**
     * 回应结果对象(直接返回或者抛出异常)
     */
    private final Object result;

    /**
     * 是否忽略流程的后续事件
     */
    private final boolean isIgnoreProcessEvent;

    ProcessControlException(State state, Object result) {
        this(false, state, result);
    }

    ProcessControlException(boolean isIgnoreProcessEvent, State state, Object result) {
        this.isIgnoreProcessEvent = isIgnoreProcessEvent;
        this.state = state;
        this.result = result;
    }

    /**
     * 中断当前代码处理流程,并立即返回指定对象
     *
     * @param object 返回对象
     * @throws ProcessControlException 抛出立即返回流程控制异常
     */
    static void throwReturnImmediately(final Object object) throws ProcessControlException {
        throw new ProcessControlException(RETURN_IMMEDIATELY, object);
    }

    /**
     * 中断当前代码处理流程,并抛出指定异常
     *
     * @param throwable 指定异常
     * @throws ProcessControlException 抛出立即抛出异常流程控制异常
     */
    static void throwThrowsImmediately(final Throwable throwable) throws ProcessControlException {
        throw new ProcessControlException(THROWS_IMMEDIATELY, throwable);
    }

    /**
     * 判断是否需要主动忽略处理后续所有事件流
     *
     * @return 是否需要主动忽略处理后续所有事件流
     */
    public boolean isIgnoreProcessEvent() {
        return isIgnoreProcessEvent;
    }

    public State getState() {
        return state;
    }

    public Object getResult() {
        return result;
    }

    @Override
    public Throwable fillInStackTrace() {
        return null;
    }


    /**
     * 流程控制状态
     */
    public enum State {

        /**
         * 立即返回
         */
        RETURN_IMMEDIATELY,

        /**
         * 立即抛出异常
         */
        THROWS_IMMEDIATELY,

        /**
         * 不干预任何流程
         */
        NONE_IMMEDIATELY

    }

}
