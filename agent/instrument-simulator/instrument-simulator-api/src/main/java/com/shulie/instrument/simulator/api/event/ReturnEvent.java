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
 * 方法调用RETURN事件
 *
 * 注意: Event 的内容会在方法调用周期结束后进行清理，内部的数据将会全部清空
 * 这样做是为了内存回收的速度加快，所以如果需要在方法调用生命周期外引用 Event
 * 需要提前将内部的数据引用
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class ReturnEvent extends InvokeEvent {

    /**
     * 调用返回值
     */
    private Object returnObj;

    /**
     * 构造调用RETURN事件
     *
     * @param processId 调用过程ID
     * @param invokeId  调用ID
     * @param returnObj    调用返回值(void方法返回值为null)
     */
    public ReturnEvent(final int processId,
                       final int invokeId,
                       final Object returnObj) {
        super(processId, invokeId, EventType.RETURN);
        this.returnObj = returnObj;
    }

    /**
     * 构造调用RETURN事件，
     * 主要开放给{@link ImmediatelyReturnEvent}构造所使用
     *
     * @param eventType 必须是{@link EventType#RETURN}或{@link EventType#IMMEDIATELY_RETURN}两者之一的值
     * @param processId 调用过程ID
     * @param invokeId  调用ID
     * @param returnObj    调用返回值(void方法返回值为null)
     */
    ReturnEvent(final EventType eventType,
                final int processId,
                final int invokeId,
                final Object returnObj) {
        super(processId, invokeId, eventType);
        this.returnObj = returnObj;

        // 对入参进行校验
        if (eventType != EventType.IMMEDIATELY_RETURN
                && eventType != EventType.RETURN) {
            throw new IllegalArgumentException(String.format("type must be %s or %s", EventType.RETURN, EventType.IMMEDIATELY_RETURN));
        }

    }

    public Object getReturnObj() {
        return returnObj;
    }

    @Override
    public void destroy() {
        super.destroy();
        returnObj = null;
    }
}
