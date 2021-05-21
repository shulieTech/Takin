/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.shulie.surge.data.runtime.supplier;


import io.shulie.surge.data.common.lifecycle.Lifecycle;
import io.shulie.surge.data.common.lifecycle.LifecycleObservable;
import io.shulie.surge.data.runtime.processor.DataQueue;

import java.util.Map;

/**
 * 数据提供者，必须在 start()、update() 前 。
 * 支持 start() 运行之后，继续动态 update()
 *
 * @author pamirs
 */
interface MultiProcessorSupplier extends Supplier {
    /**
     * 开始获取数据
     *
     * @throws IllegalStateException Queue 尚未设置时抛出此异常
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * 停止获取数据
     *
     * @throws Exception
     */
    void stop() throws Exception;

    /**
     * 设置使用的队列
     *
     * @param queueMap
     * @throws IllegalStateException 已经执行了 {@link #start()} 时抛出此异常
     */
    void setQueue(Map<String, DataQueue> queueMap) throws IllegalStateException;

}
