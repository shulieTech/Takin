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
package com.shulie.instrument.simulator.message.exception;

/**
 * 异常处理器
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2021/1/14 11:33 上午
 */
public interface ExceptionHandler {
    /**
     * 处理异常
     *
     * @param throwable
     * @param listener
     */
    void handleException(Throwable throwable, String message, Object listener) throws Throwable;
}
