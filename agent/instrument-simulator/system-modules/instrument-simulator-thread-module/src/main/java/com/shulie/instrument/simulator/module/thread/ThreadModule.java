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
package com.shulie.instrument.simulator.module.thread;

import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.ExtensionModule;
import com.shulie.instrument.simulator.api.ModuleInfo;
import com.shulie.instrument.simulator.api.annotation.Command;
import com.shulie.instrument.simulator.api.util.ParameterUtils;
import com.shulie.instrument.simulator.module.ParamSupported;
import com.shulie.instrument.simulator.module.model.thread.ThreadStat;
import com.shulie.instrument.simulator.module.util.ThreadUtil;
import org.apache.commons.lang.ArrayUtils;
import org.kohsuke.MetaInfServices;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.*;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/29 12:02 下午
 */
@MetaInfServices(ExtensionModule.class)
@ModuleInfo(id = "thread", version = "1.0.0", author = "xiaobin@shulie.io", description = "线程模块")
public class ThreadModule extends ParamSupported implements ExtensionModule {
    private static Set<String> states = null;
    private static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    private static boolean lockedMonitors = false;
    private static boolean lockedSynchronizers = false;

    {
        states = new HashSet<String>(8);
        for (Thread.State state : Thread.State.values()) {
            states.add(state.name());
        }
    }

    @Command(value = "info", description = "查看线程执行信息")
    public CommandResponse info(final Map<String, String> map) {
        Long id = ParameterUtils.getLong(map, "id", -1L);
        Integer topNBusy = ParameterUtils.getInt(map, "top");
        Integer sampleInterval = ParameterUtils.getInt(map, "interval");
        Boolean findMostBlockingThread = ParameterUtils.getBoolean(map, "block", false);
        String state = map.get("state");

        try {
            if (id > 0) {
                return processThread(id, sampleInterval);
            } else if (topNBusy != null) {
                if (sampleInterval == null) {
                    return CommandResponse.failure("arg [i](interval) can't not be null.");
                }
                return processTopBusyThreads(sampleInterval, topNBusy);
            } else if (findMostBlockingThread) {
                return processBlockingThread(sampleInterval);
            } else {
                if (sampleInterval == null) {
                    return CommandResponse.failure("arg [i](interval) can't not be null.");
                }
                return getAllThreadStats(state, sampleInterval);
            }
        } catch (Throwable t) {
            return CommandResponse.failure(t);
        }
    }

    private CommandResponse getAllThreadStats(String state, Integer sampleInterval) {
        Map<Long, ThreadStat> allThreadStats = ThreadUtil.getAllThreadStats(state, sampleInterval);
        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(ArrayUtils.toPrimitive(allThreadStats.keySet().toArray(new Long[0])), lockedMonitors, lockedSynchronizers);
        if (threadInfos == null) {
            return CommandResponse.failure("no thread found!\n");
        } else {
            List<com.shulie.instrument.simulator.module.model.thread.ThreadInfo> list = new ArrayList<com.shulie.instrument.simulator.module.model.thread.ThreadInfo>();
            for (ThreadInfo info : threadInfos) {
                com.shulie.instrument.simulator.module.model.thread.ThreadInfo stacktrace = ThreadUtil.getThreadInfo(info, allThreadStats.get(info.getThreadId()));
                list.add(stacktrace);
            }
            return CommandResponse.success(list);
        }
    }

    private CommandResponse processBlockingThread(Integer samplingInterval) {
        ThreadUtil.BlockingLockInfo blockingLockInfo = ThreadUtil.findMostBlockingLock();

        if (blockingLockInfo.threadInfo == null) {
            CommandResponse commandResponse = new CommandResponse();
            commandResponse.setSuccess(false);
            commandResponse.setMessage("No most blocking thread found!\n");
            return commandResponse;
        } else {
            CommandResponse commandResponse = new CommandResponse();
            commandResponse.setSuccess(true);
            Map<Long, ThreadStat> allThreadStats = ThreadUtil.getAllThreadStats(samplingInterval);
            commandResponse.setResult(ThreadUtil.getThreadInfo(blockingLockInfo, allThreadStats.get(blockingLockInfo.threadInfo.getThreadId())));
            return commandResponse;
        }
    }

    private CommandResponse processTopBusyThreads(Integer sampleInterval, Integer topNBusy) {
        Map<Long, ThreadStat> allThreadStats = ThreadUtil.getAllThreadStats(sampleInterval);
        List<ThreadStat> coll = new ArrayList<ThreadStat>(allThreadStats.values());
        Collections.sort(coll, new Comparator<ThreadStat>() {
            @Override
            public int compare(ThreadStat o1, ThreadStat o2) {
                long l1 = o1.getCpuUsage();
                long l2 = o2.getCpuUsage();
                if (l1 < l2) {
                    return 1;
                } else if (l1 > l2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        List<ThreadStat> topThreads = topNBusy > 0 && topNBusy <= coll.size()
                ? coll.subList(0, topNBusy) : coll;
        List<Long> tids = new ArrayList<Long>();
        for (ThreadStat threadStat : topThreads) {
            tids.add(threadStat.getThreadId());
        }

        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(ArrayUtils.toPrimitive(tids.toArray(new Long[tids.size()])), true, true);
        if (threadInfos == null) {
            return CommandResponse.failure("thread do not exist! id: " + tids + "\n");
        } else {
            List<com.shulie.instrument.simulator.module.model.thread.ThreadInfo> list = new ArrayList<com.shulie.instrument.simulator.module.model.thread.ThreadInfo>();
            for (ThreadInfo info : threadInfos) {
                com.shulie.instrument.simulator.module.model.thread.ThreadInfo stacktrace = ThreadUtil.getThreadInfo(info, allThreadStats.get(info.getThreadId()));
                list.add(stacktrace);
            }
            return CommandResponse.success(list);
        }
    }

    private CommandResponse processThread(Long id, Integer sampleInterval) {

        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(new long[]{id}, true, true);
        if (threadInfos == null || threadInfos[0] == null) {
            return CommandResponse.failure("thread do not exist! id: " + id + "\n");
        } else {
            // no cpu usage info
            Map<Long, ThreadStat> allThreadStats = ThreadUtil.getAllThreadStats(sampleInterval);
            return CommandResponse.success(ThreadUtil.getThreadInfo(threadInfos[0], allThreadStats.get(id)));
        }
    }
}
