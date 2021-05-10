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
package com.shulie.instrument.simulator.module.model.gc;

import java.io.Serializable;

public class GcInfo implements Serializable {
    /**
     * young gc 次数
     */
    private long youngGcCount;
    /**
     * young gc 时间,单位毫秒
     */
    private long youngGcTime;
    /**
     * old gc 次数
     */
    private long oldGcCount;
    /**
     * old gc 时间,单位毫秒
     */
    private long oldGcTime;

    public long getYoungGcCount() {
        return youngGcCount;
    }

    public void setYoungGcCount(long youngGcCount) {
        this.youngGcCount = youngGcCount;
    }

    public long getYoungGcTime() {
        return youngGcTime;
    }

    public void setYoungGcTime(long youngGcTime) {
        this.youngGcTime = youngGcTime;
    }

    public long getOldGcCount() {
        return oldGcCount;
    }

    public void setOldGcCount(long oldGcCount) {
        this.oldGcCount = oldGcCount;
    }

    public long getOldGcTime() {
        return oldGcTime;
    }

    public void setOldGcTime(long oldGcTime) {
        this.oldGcTime = oldGcTime;
    }

    @Override
    public String toString() {
        return "{" +
                "youngGcCount=" + youngGcCount +
                ", youngGcTime=" + youngGcTime +
                ", oldGcCount=" + oldGcCount +
                ", oldGcTime=" + oldGcTime +
                '}';
    }
}
