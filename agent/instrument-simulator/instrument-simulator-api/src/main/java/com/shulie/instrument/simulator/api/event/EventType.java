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
package com.shulie.instrument.simulator.api.event;

import com.shulie.instrument.simulator.api.ProcessControlException;

/**
 * 事件枚举类型
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public enum EventType {
    /**
     * 加载事件
     */
    LOAD,

    /**
     * 调用:BEFORE
     */
    BEFORE,

    /**
     * 调用:RETURN
     */
    RETURN,

    /**
     * 调用:THROWS
     */
    THROWS,

    /**
     * 调用:LINE
     * 一行被调用了
     */
    LINE,

    /**
     * 调用:CALL_BEFORE
     * 一个方法被调用之前
     */
    CALL_BEFORE,

    /**
     * 调用:CALL_RETURN
     * 一个方法被调用正常返回之后
     */
    CALL_RETURN,

    /**
     * 调用:CALL_THROWS
     * 一个方法被调用抛出异常之后
     */
    CALL_THROWS,


    /**
     * 立即调用:RETURN
     * 由{@link ProcessControlException#throwReturnImmediately(Object)}触发
     */
    IMMEDIATELY_RETURN,

    /**
     * 立即调用:THROWS
     * 由{@link ProcessControlException#throwThrowsImmediately(Throwable)}触发
     */
    IMMEDIATELY_THROWS;

    /**
     * 空类型
     */
    public static final EventType[] EMPTY = new EventType[0];

}
