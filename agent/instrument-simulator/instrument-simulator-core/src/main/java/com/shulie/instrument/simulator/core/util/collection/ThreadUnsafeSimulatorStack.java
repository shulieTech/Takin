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

import java.util.NoSuchElementException;

import static java.lang.System.arraycopy;

/**
 * 线程不安全不固定栈深的堆栈实现
 * 比默认的实现带来3倍的性能提升
 *
 * @param <E> 堆栈元素类型
 */
public class ThreadUnsafeSimulatorStack<E> implements SimulatorStack<E> {

    private final static int EMPTY_INDEX = -1;
    private final static int DEFAULT_STACK_DEEP = 12;

    private Object[] elementArray;
    private int index = EMPTY_INDEX;

    public ThreadUnsafeSimulatorStack() {
        this(DEFAULT_STACK_DEEP);
    }

    public ThreadUnsafeSimulatorStack(int stackSize) {
        this.elementArray = new Object[stackSize];
    }


    /**
     * 自动扩容
     * 当前堆栈最大深度不满足期望时会自动扩容(2倍扩容)
     *
     * @param expectDeep 期望堆栈深度
     */
    private void ensureCapacityInternal(int expectDeep) {
        final int currentStackSize = elementArray.length;
        if (elementArray.length <= expectDeep) {
            final Object[] newElementArray = new Object[currentStackSize * 2];
            arraycopy(elementArray, 0, newElementArray, 0, currentStackSize);
            this.elementArray = newElementArray;
        }
    }

    private void checkForPopOrPeek() {
        // stack is empty
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public E pop() {
        checkForPopOrPeek();
        final E e = (E) elementArray[index];
        elementArray[index--] = null;
        return e;
    }

    @Override
    public void push(E e) {
        ensureCapacityInternal(index + 1);
        elementArray[++index] = e;
    }

    @Override
    public E peek() {
        checkForPopOrPeek();
        //noinspection unchecked
        return (E) elementArray[index];
    }

    @Override
    public boolean isEmpty() {
        return index == EMPTY_INDEX;
    }

    @Override
    public boolean isLast() {
        return index == 0;
    }

    @Override
    public E peekLast() {
        checkForPopOrPeek();
        return (E) elementArray[0];
    }

    @Override
    public int deep() {
        return index + 1;
    }

    /**
     * 获取所有栈的内容
     *
     * @return
     */
    public Object[] getElementArray() {
        return elementArray;
    }

    /**
     * 获取当前的下标位置
     *
     * @return
     */
    public int getIndex() {
        return index;
    }
}
