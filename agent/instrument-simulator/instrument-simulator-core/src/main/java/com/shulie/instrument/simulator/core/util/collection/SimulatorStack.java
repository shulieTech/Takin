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
package com.shulie.instrument.simulator.core.util.collection;

/**
 * 堆栈
 *
 * @param <E> 堆栈元素类型
 */
public interface SimulatorStack<E> {
    /**
     * 出栈
     *
     * @return
     */
    E pop();

    /**
     * 入栈
     *
     * @param e
     */
    void push(E e);

    /**
     * 取出栈顶的数据
     *
     * @return
     */
    E peek();

    /**
     * 判断栈是否为空
     *
     * @return
     */
    boolean isEmpty();

    /**
     * 判断当前栈是否是最后一个数据
     *
     * @return
     */
    boolean isLast();

    /**
     * 取出最后一个位置的数据
     *
     * @return
     */
    E peekLast();

    /**
     * 获取栈的深度
     *
     * @return
     */
    int deep();

}
