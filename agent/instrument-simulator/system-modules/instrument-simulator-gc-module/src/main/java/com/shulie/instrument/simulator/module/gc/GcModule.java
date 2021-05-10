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
package com.shulie.instrument.simulator.module.gc;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.module.ParamSupported;
import com.shulie.instrument.simulator.module.model.gc.GcInfo;
import org.kohsuke.MetaInfServices;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/11/3 4:33 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "gc", version = "1.0.0", author = "xiaobin@shulie.io", description = "gc 模块")
public class GcModule extends ParamSupported implements ExtensionModule {
    private static final List<String> youngGcNames = Arrays.asList(
            //HotSpot
            "Copy",
            "ParNew",
            "PS Scavenge",
            //JRockit
            "Garbage collection optimized for short pausetimes Young Collector",
            "Garbage collection optimized for throughput Young Collector",
            "Garbage collection optimized for deterministic pausetimes Young Collector"
    );

    private static final List<String> oldGcNames = Arrays.asList(
            //HotSpot
            "MarkSweepCompact",
            "PS MarkSweep",
            "ConcurrentMarkSweep",
            //JRockit
            "Garbage collection optimized for short pausetimes Old Collector",
            "Garbage collection optimized for throughput Old Collector",
            "Garbage collection optimized for deterministic pausetimes Old Collector"
    );


    @Command(value = "info", description = "获取当前进程的 gc 信息")
    public CommandResponse info(final Map<String, String> args) {
        try {
            List<GarbageCollectorMXBean> garbageCollectorMxBeans = ManagementFactory.getGarbageCollectorMXBeans();
            GcInfo gcInfo = new GcInfo();
            for (GarbageCollectorMXBean gcMXBean : garbageCollectorMxBeans) {
                String name = gcMXBean.getName();
                if (youngGcNames.contains(name)) {
                    gcInfo.setYoungGcCount(gcMXBean.getCollectionCount());
                    gcInfo.setYoungGcTime(gcMXBean.getCollectionTime());
                } else if (oldGcNames.contains(name)) {
                    gcInfo.setOldGcCount(gcMXBean.getCollectionCount());
                    gcInfo.setOldGcTime(gcMXBean.getCollectionTime());
                }
            }
            return CommandResponse.success(gcInfo);
        } catch (Throwable e) {
            return CommandResponse.failure(e);
        }
    }
}
