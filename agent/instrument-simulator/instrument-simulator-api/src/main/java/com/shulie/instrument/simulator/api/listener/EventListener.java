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
package com.shulie.instrument.simulator.api.listener;

import com.shulie.instrument.simulator.api.event.Event;

/**
 * 事件监听器
 * 类增强的实现只是给类插了几个固定事件的桩，当对应的桩被执行后，才会产生对应的事件，所有的事件的处理都需要
 * 依赖事件监听器。通知监听器则是在事件监听器基础上封装出来的，对于对事件的转化封装出来通知
 * 这样设计的好处在于最大的限度防止在增强业务类时污染业务类
 *
 * @see com.shulie.instrument.simulator.api.listener.ext.AdviceListener
 * @see com.shulie.instrument.simulator.message.Messager
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public interface EventListener {

    /**
     * 触发事件处理
     * <p>
     * 事件处理是整个Simulator最重要的一环,也是整个系统设计的精髓所在
     * </p>
     * <pre>
     * {@code
     * 事件流转流程
     *
     *                                        +-------+
     *                                        |       |
     * +========+  <return>             +========+    | <return immediately>
     * |        |  <return immediately> |        |    |
     * | BEFORE |---------------------->| RETURN |<---+
     * |        |                       |        |
     * +========+                       +========+
     *     |                              |    ^
     *     |         <throws immediately> |    |
     *     |                              |    | <return immediately>
     *     |                              v    |
     *     |                            +========+
     *     |                            |        |
     *     +--------------------------->| THROWS |<---+
     *                    <throws>      |        |    |
     *        <throws immediately>      +========+    | <throws immediately>
     *                                        |       |
     *                                        +-------+
     * }
     * </pre>
     *
     * @param event 触发事件
     * @throws Throwable 处理异常
     */
    void onEvent(Event event) throws Throwable;

    /**
     * 清理 EventListener
     */
    void clean();

}
