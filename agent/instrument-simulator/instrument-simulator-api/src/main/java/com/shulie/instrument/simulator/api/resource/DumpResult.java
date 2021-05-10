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
package com.shulie.instrument.simulator.api.resource;

import java.io.File;
import java.util.Map;

/**
 * dump 结果描述
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/30 11:27 下午
 */
public class DumpResult {
    /**
     * dump 的结果
     */
    private Map<String, File> dumpResult;
    /**
     * 对应的 watchId
     */
    private int watchId;

    public DumpResult(Map<String, File> dumpResult, int watchId) {
        this.dumpResult = dumpResult;
        this.watchId = watchId;
    }

    public Map<String, File> getDumpResult() {
        return dumpResult;
    }

    public void setDumpResult(Map<String, File> dumpResult) {
        this.dumpResult = dumpResult;
    }

    public int getWatchId() {
        return watchId;
    }

    public void setWatchId(int watchId) {
        this.watchId = watchId;
    }

    public static DumpResult build(int watchId, Map<String, File> dumpResult) {
        return new DumpResult(dumpResult, watchId);
    }
}
