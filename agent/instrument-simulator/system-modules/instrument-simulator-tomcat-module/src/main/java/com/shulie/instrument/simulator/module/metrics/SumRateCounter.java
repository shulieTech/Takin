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

/**
 * <pre>
 * 统计传入的数据是总数的速率。
 * 比如传入的数据是所有请求的数量，5秒数据为：
 * 267, 457, 635, 894, 1398
 * 则统计的平均速率是：( (457-267) + (635-457) + (894-635) + (1398-894) ) / 4 = 282
 * </pre>
 *
 */
public class SumRateCounter {

    RateCounter rateCounter;

    Long previous = null;

    public SumRateCounter() {
        rateCounter = new RateCounter();
    }

    public SumRateCounter(int size) {
        rateCounter = new RateCounter(size);
    }

    public int size() {
        return rateCounter.size();
    }

    public void update(long value) {
        if (previous == null) {
            previous = value;
            return;
        }
        rateCounter.update(value - previous);
        previous = value;
    }

    public double rate() {
        return rateCounter.rate();
    }

}
