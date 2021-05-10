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
package com.shulie.instrument.simulator.module.memory;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.module.ParamSupported;
import com.shulie.instrument.simulator.module.model.memory.MemoryEntry;
import com.shulie.instrument.simulator.module.model.memory.MemoryInfo;
import org.kohsuke.MetaInfServices;

import java.lang.management.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.shulie.instrument.simulator.module.model.memory.MemoryInfo.TYPE_HEAP;
import static com.shulie.instrument.simulator.module.model.memory.MemoryInfo.TYPE_NON_HEAP;


/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 4:00 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "memory", version = "1.0.0", author = "xiaobin@shulie.io", description = "内存模块")
public class MemoryModule extends ParamSupported implements ExtensionModule {

    @Command(value = "info", description = "内存信息")
    public CommandResponse info(final Map<String, String> args) {
        try {
            List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

            MemoryInfo memoryInfo = new MemoryInfo();
            //heap
            MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
            memoryInfo.setHeapMemory(createMemoryEntry(TYPE_HEAP, heapMemoryUsage));
            List<MemoryEntry> heapMemEntries = new ArrayList<MemoryEntry>();
            for (MemoryPoolMXBean poolMXBean : memoryPoolMXBeans) {
                if (MemoryType.HEAP.equals(poolMXBean.getType())) {
                    MemoryUsage usage = poolMXBean.getUsage();
                    heapMemEntries.add(createMemoryEntry(poolMXBean.getName(), usage));
                }
            }
            memoryInfo.setHeapMemories(heapMemEntries);
            //non-heap
            MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
            List<MemoryEntry> nonheapMemEntries = new ArrayList<MemoryEntry>();
            memoryInfo.setNonheapMemory(createMemoryEntry(TYPE_NON_HEAP, nonHeapMemoryUsage));
            for (MemoryPoolMXBean poolMXBean : memoryPoolMXBeans) {
                if (MemoryType.NON_HEAP.equals(poolMXBean.getType())) {
                    MemoryUsage usage = poolMXBean.getUsage();
                    nonheapMemEntries.add(createMemoryEntry(poolMXBean.getName(), usage));
                }
            }
            memoryInfo.setNonheapMemories(nonheapMemEntries);
            memoryInfo.setBufferPoolMemories(getBufferPoolMemoryInfo());

            return CommandResponse.success(memoryInfo);
        } catch (Throwable e) {
            return CommandResponse.failure(e);
        }
    }

    private static List<MemoryEntry> getBufferPoolMemoryInfo() {
        try {
            List<MemoryEntry> bufferPoolMemEntries = new ArrayList<MemoryEntry>();
            @SuppressWarnings("rawtypes")
            Class bufferPoolMXBeanClass = Class.forName("java.lang.management.BufferPoolMXBean");
            @SuppressWarnings("unchecked")
            List<BufferPoolMXBean> bufferPoolMXBeans = ManagementFactory.getPlatformMXBeans(bufferPoolMXBeanClass);
            for (BufferPoolMXBean mbean : bufferPoolMXBeans) {
                long used = mbean.getMemoryUsed();
                long total = mbean.getTotalCapacity();
                bufferPoolMemEntries.add(new MemoryEntry(mbean.getName(), -1L, used, total, -1L));
            }
            return bufferPoolMemEntries;
        } catch (Throwable e) {
            // ignore
        }
        return Collections.EMPTY_LIST;
    }

    private static MemoryEntry createMemoryEntry(String name, MemoryUsage memoryUsage) {
        return new MemoryEntry(name, memoryUsage.getInit(), memoryUsage.getUsed(), memoryUsage.getCommitted(), memoryUsage.getMax());
    }
}
