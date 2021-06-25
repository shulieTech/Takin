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

package io.shulie.surge.data.runtime.processor;

import io.shulie.surge.data.common.lifecycle.Lifecycle;
import io.shulie.surge.data.runtime.supplier.Supplier;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * {@link Supplier} 生产者与消费者之间
 * 用于数据交换的队列
 *
 * @author pamirs
 */
public interface DataQueue<IN extends Serializable, OUT extends Serializable> extends Lifecycle {

    /**
     * 判断是否还有空余空间提交数据条数
     *
     * @param size
     * @return
     */
    boolean canPublish(int size);

    /**
     * 移除延迟数据
     * @param time
     * @return
     */
    boolean removeDelay(long time);
    /**
     * 发布一个数据。如果队列已满，抛出异常
     *
     * @param data
     * @throws InterruptedException 阻塞期间被打断时抛出的异常
     */
    void publish(OUT data) throws InterruptedException;

    /**
     * 发布多个数据。如果队列已满，抛出异常
     *
     * @param data
     * @throws InterruptedException 阻塞期间被打断时抛出的异常
     */
    void publish(List<OUT> data) throws InterruptedException;


    /**
     * 发布一个数据。如果队列已满，抛出异常
     *
     * @param data
     * @throws InterruptedException 阻塞期间被打断时抛出的异常
     */
    void publish(Map<String, Object> header, IN data) throws InterruptedException;

    /**
     * 发布多个数据。如果队列已满，抛出异常
     *
     * @param data
     * @throws InterruptedException 阻塞期间被打断时抛出的异常
     */
    void publish(Map<String, Object> header, List<IN> data) throws InterruptedException;

}
