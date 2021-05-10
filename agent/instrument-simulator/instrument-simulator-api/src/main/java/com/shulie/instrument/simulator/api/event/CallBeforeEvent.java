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
 * 方法调用追踪事件:BEFORE
 *
 * 注意: Event 的内容会在方法调用周期结束后进行清理，内部的数据将会全部清空
 * 这样做是为了内存回收的速度加快，所以如果需要在方法调用生命周期外引用 Event
 * 需要提前将内部的数据引用
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class CallBeforeEvent extends CallEvent {

    /**
     * 代码行号
     */
    private int lineNumber;

    /**
     * 调用类名
     */
    private String owner;

    /**
     * 调用方法名
     */
    private String name;

    /**
     * 调用方法描述
     */
    private String desc;

    /**
     * 构造调用事件
     *
     * @param processId  调用过程ID
     * @param invokeId   调用ID
     * @param lineNumber 代码行号
     * @param owner      调用类名
     * @param name       调用方法名
     * @param desc       调用方法描述
     */
    public CallBeforeEvent(final int processId,
                           final int invokeId,
                           final int lineNumber,
                           final boolean isInterface,
                           final String owner,
                           final String name,
                           final String desc) {
        super(processId, invokeId, isInterface, EventType.CALL_BEFORE);
        this.lineNumber = lineNumber;
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public void destroy() {
        super.destroy();
        owner = null;
        name = null;
        desc = null;
    }
}
