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

import java.util.concurrent.TimeUnit;

/**
 * 时间滚动策略
 *
 * @author vincent
 */
public class TimedRotationPolicy implements RotationPolicy {

    /**
     * 滚动间隔
     */
    private long interval;

    public TimedRotationPolicy(int count, TimeUnit units) {
        this.interval = units.toMillis(count);
    }

    protected TimedRotationPolicy(long interval) {
        this.interval = interval;
    }

    /**
     * @param offset current offset of file being written
     * @return true if a rotation should be performed
     */
    @Override
    public boolean mark(long offset) {
        return false;
    }

    /**
     * Called after the rotates.
     */
    @Override
    public void reset() {

    }

    @Override
    public RotationPolicy copy() {
        return new TimedRotationPolicy(this.interval);
    }

    public long getInterval() {
        return this.interval;
    }

}