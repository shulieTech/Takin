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
package com.shulie.instrument.simulator.module.model.memory;

import java.io.Serializable;
import java.util.List;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 4:20 下午
 */
public class MemoryInfo implements Serializable {
    private final static long serialVersionUID = 1L;
    public static final String TYPE_HEAP = "heap";
    public static final String TYPE_NON_HEAP = "nonheap";
    public static final String TYPE_BUFFER_POOL = "buffer_pool";

    /**
     * 堆内存
     */
    private MemoryEntry heapMemory;
    /**
     * 堆内存详情
     */
    private List<MemoryEntry> heapMemories;

    /**
     * 非堆内存
     */
    private MemoryEntry nonheapMemory;

    /**
     * 非堆内存
     */
    private List<MemoryEntry> nonheapMemories;

    /**
     * buffer pool 内存
     */
    private List<MemoryEntry> bufferPoolMemories;

    public MemoryEntry getNonheapMemory() {
        return nonheapMemory;
    }

    public void setNonheapMemory(MemoryEntry nonheapMemory) {
        this.nonheapMemory = nonheapMemory;
    }

    public MemoryEntry getHeapMemory() {
        return heapMemory;
    }

    public void setHeapMemory(MemoryEntry heapMemory) {
        this.heapMemory = heapMemory;
    }

    public List<MemoryEntry> getHeapMemories() {
        return heapMemories;
    }

    public void setHeapMemories(List<MemoryEntry> heapMemories) {
        this.heapMemories = heapMemories;
    }

    public List<MemoryEntry> getNonheapMemories() {
        return nonheapMemories;
    }

    public void setNonheapMemories(List<MemoryEntry> nonheapMemories) {
        this.nonheapMemories = nonheapMemories;
    }

    public List<MemoryEntry> getBufferPoolMemories() {
        return bufferPoolMemories;
    }

    public void setBufferPoolMemories(List<MemoryEntry> bufferPoolMemories) {
        this.bufferPoolMemories = bufferPoolMemories;
    }

    @Override
    public String toString() {
        return "{" +
                "heapMemory=" + heapMemory +
                ", heapMemories=" + heapMemories +
                ", nonheapMemory=" + nonheapMemory +
                ", nonheapMemories=" + nonheapMemories +
                ", bufferPoolMemories=" + bufferPoolMemories +
                '}';
    }
}
