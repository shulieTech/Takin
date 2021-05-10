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

/**
 * 方法调用追踪事件:THROWS
 *
 * 注意: Event 的内容会在方法调用周期结束后进行清理，内部的数据将会全部清空
 * 这样做是为了内存回收的速度加快，所以如果需要在方法调用生命周期外引用 Event
 * 需要提前将内部的数据引用
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class CallThrowsEvent extends CallEvent {

    /**
     * 抛出的异常类名称
     */
    private Throwable throwException;

    /**
     * 构造调用事件
     *
     * @param processId      调用过程ID
     * @param invokeId       调用ID
     * @param throwException 抛出的异常类名称
     */
    public CallThrowsEvent(int processId, int invokeId, boolean isInterface, Throwable throwException) {
        super(processId, invokeId, isInterface, EventType.CALL_THROWS);
        this.throwException = throwException;
    }

    public Throwable getThrowException() {
        return throwException;
    }

    @Override
    public void destroy() {
        super.destroy();
        throwException = null;
    }
}
