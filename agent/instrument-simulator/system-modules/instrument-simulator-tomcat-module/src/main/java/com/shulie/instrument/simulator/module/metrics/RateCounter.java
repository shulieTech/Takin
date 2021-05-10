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
package com.shulie.instrument.simulator.module.metrics;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * <pre>
 * 统计平均速率，比如统计5秒内的平均速率。
 * 5秒的数据是：234, 345,124,366,235，
 * 则速率是 (234+345+124+366+235)/5 = 260
 *
 * </pre>
 */
public class RateCounter {
    private static final int BITS_PER_LONG = 63;
    public static final int DEFAULT_SIZE = 5;

    private final AtomicLong count = new AtomicLong();
    private final AtomicLongArray values;

    public RateCounter() {
        this(DEFAULT_SIZE);
    }

    public RateCounter(int size) {
        this.values = new AtomicLongArray(size);
        for (int i = 0; i < values.length(); i++) {
            values.set(i, 0);
        }
        count.set(0);
    }

    public int size() {
        final long c = count.get();
        if (c > values.length()) {
            return values.length();
        }
        return (int) c;
    }

    public void update(long value) {
        final long c = count.incrementAndGet();
        if (c <= values.length()) {
            values.set((int) c - 1, value);
        } else {
            final long r = nextLong(c);
            if (r < values.length()) {
                values.set((int) r, value);
            }
        }
    }

    public double rate() {
        long c = count.get();
        int countLength = 0;
        long sum = 0;
        if (c > values.length()) {
            countLength = values.length();
        } else {
            countLength = (int) c;
        }

        for (int i = 0; i < countLength; ++i) {
            sum += values.get(i);
        }

        return sum / (double) countLength;
    }

    /**
     * Get a pseudo-random long uniformly between 0 and n-1. Stolen from
     * {@link Random#nextInt()}.
     *
     * @param n the bound
     * @return a value select randomly from the range {@code [0..n)}.
     */
    private static long nextLong(long n) {
        long bits, val;
        Random random = new Random();
        do {
            bits = random.nextLong() & (~(1L << BITS_PER_LONG));
            val = bits % n;
        } while (bits - val + (n - 1) < 0L);
        return val;
    }

}
