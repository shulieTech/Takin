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

package io.shulie.surge.data.common.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.LongAdder;

/**
 * 数量滚动策略
 *
 * @author vincent
 */
public class CountRotationPolicy implements RotationPolicy {
    private static final Logger LOG = LoggerFactory.getLogger(CountRotationPolicy.class);
    private long maxSize;
    private long lastOffset = 0;
    private LongAdder currentSize = new LongAdder();


    public CountRotationPolicy(long maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean mark(long offset) {
        this.currentSize.add(offset);
        return this.currentSize.longValue() >= this.maxSize;
    }

    public static void main(String[] args) throws InterruptedException {
        CountRotationPolicy policy = new CountRotationPolicy(3);
        while (true) {
            if (policy.mark(1)) {
                System.out.println("滚动");
                policy.reset();
            } else {
                System.out.println("累加");
            }
            Thread.sleep(2000);
        }
    }

    @Override
    public void reset() {
        this.currentSize.reset();
        this.lastOffset = 0;
    }

    @Override
    public RotationPolicy copy() {
        return new CountRotationPolicy(this.maxSize);
    }

}
