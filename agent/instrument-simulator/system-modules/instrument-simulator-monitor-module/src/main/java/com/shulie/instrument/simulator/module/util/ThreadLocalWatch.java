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
package com.shulie.instrument.simulator.module.util;

/**
 * 简单的调用计时器。
 */
public class ThreadLocalWatch {

    private final ThreadLocal<LongStack> timestampRef = new ThreadLocal<LongStack>() {
        @Override
        protected LongStack initialValue() {
            return new LongStack(1024 * 4);
        }
    };

    public long start() {
        final long timestamp = System.nanoTime();
        timestampRef.get().push(timestamp);
        return timestamp;
    }

    public long cost() {
        return (System.nanoTime() - timestampRef.get().pop());
    }

    /**
     * <pre>
     * 一个特殊的stack，为了追求效率，避免扩容。
     * 因为这个stack的push/pop 并不一定成对调用，比如可能push执行了，但是后面的流程被中断了，pop没有被执行。
     * 如果不固定大小，一直增长的话，极端情况下可能应用有内存问题。
     * 如果到达容量，pos会重置，循环存储数据。所以使用这个Stack如果在极端情况下统计的数据会不准确，只用于monitor/watch等命令的计时。
     *
     * </pre>
     *
     * @author hengyunabc 2019-11-20
     */
    static class LongStack {
        private long[] array;
        private int pos = 0;
        private int cap;

        public LongStack(int maxSize) {
            array = new long[maxSize];
            cap = array.length;
        }

        public int size() {
            return pos;
        }

        public void push(long value) {
            if (pos < cap) {
                array[pos++] = value;
            } else {
                // if array is full, reset pos
                pos = 0;
                array[pos++] = value;
            }
        }

        public long pop() {
            if (pos > 0) {
                pos--;
                return array[pos];
            } else {
                pos = cap;
                pos--;
                return array[pos];
            }
        }
    }
}
