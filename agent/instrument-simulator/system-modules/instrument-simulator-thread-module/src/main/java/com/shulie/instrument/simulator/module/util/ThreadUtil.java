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
package com.shulie.instrument.simulator.module.util;

import com.shulie.instrument.simulator.module.model.thread.StackElement;
import com.shulie.instrument.simulator.module.model.thread.ThreadStack;
import com.shulie.instrument.simulator.module.model.thread.ThreadStat;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.management.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

abstract public class ThreadUtil {

    private static final BlockingLockInfo EMPTY_INFO = new BlockingLockInfo();
    private static Set<String> states = null;

    private static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();


    {
        states = new HashSet<String>(8);
        for (Thread.State state : Thread.State.values()) {
            states.add(state.name());
        }
    }

    public static ThreadGroup getRoot() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup parent;
        while ((parent = group.getParent()) != null) {
            group = parent;
        }
        return group;
    }

    /**
     * 获取所有线程Map，以线程Name-ID为key
     *
     * @return
     */
    public static Map<String, Thread> getThreads() {
        ThreadGroup root = getRoot();
        Thread[] threads = new Thread[root.activeCount()];
        while (root.enumerate(threads, true) == threads.length) {
            threads = new Thread[threads.length * 2];
        }
        SortedMap<String, Thread> map = new TreeMap<String, Thread>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        for (Thread thread : threads) {
            if (thread != null) {
                map.put(thread.getName() + "-" + thread.getId(), thread);
            }
        }
        return map;
    }

    /**
     * 获取所有线程List
     *
     * @return
     */
    public static List<Thread> getThreadList() {
        List<Thread> result = new ArrayList<Thread>();
        ThreadGroup root = getRoot();
        Thread[] threads = new Thread[root.activeCount()];
        while (root.enumerate(threads, true) == threads.length) {
            threads = new Thread[threads.length * 2];
        }
        for (Thread thread : threads) {
            if (thread != null) {
                result.add(thread);
            }
        }
        return result;
    }

    public static Map<Long, ThreadStat> getAllThreadStats(Integer sampleInterval) {
        return getAllThreadStats(null, sampleInterval);
    }

    public static Map<Long, ThreadStat> getAllThreadStats(String state, Integer sampleInterval) {
        Map<String, Thread> threads = ThreadUtil.getThreads();

        // 统计各种线程状态
        Map<Thread.State, Integer> stateCountMap = new HashMap<Thread.State, Integer>();
        for (Thread.State s : Thread.State.values()) {
            stateCountMap.put(s, 0);
        }

        for (Thread thread : threads.values()) {
            Thread.State threadState = thread.getState();
            Integer count = stateCountMap.get(threadState);
            stateCountMap.put(threadState, count + 1);
        }

        Collection<Thread> resultThreads = new ArrayList<Thread>();
        if (StringUtils.isNotBlank(state)) {
            state = state.toUpperCase();
            if (states.contains(state)) {
                for (Thread thread : threads.values()) {
                    if (state.equals(thread.getState().name())) {
                        resultThreads.add(thread);
                    }
                }
            }
        } else {
            resultThreads = threads.values();
        }

        Map<Long, Long> times1 = new HashMap<Long, Long>();
        for (Thread thread : resultThreads) {
            long cpu = threadMXBean.getThreadCpuTime(thread.getId());
            times1.put(thread.getId(), cpu);
        }

        try {
            Thread.sleep(sampleInterval);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Map<Long, Long> times2 = new HashMap<Long, Long>(threads.size());
        for (Thread thread : resultThreads) {
            long cpu = threadMXBean.getThreadCpuTime(thread.getId());
            times2.put(thread.getId(), cpu);
        }

        long total = 0;
        Map<Long, Long> deltas = new HashMap<Long, Long>(threads.size());
        for (Long id : times2.keySet()) {
            long time1 = times1.get(id);
            long time2 = times2.get(id);

            long delta = 0;
            if (time1 != time2) {
                if (time1 == -1) {
                    time1 = time2;
                } else if (time2 == -1) {
                    time2 = time1;
                }
                delta = time2 - time1;
            }

            deltas.put(id, delta);
            total += delta;
        }

        // Compute cpu
        final HashMap<Thread, Long> cpus = new HashMap<Thread, Long>(threads.size());
        for (Thread thread : resultThreads) {
            long cpu = total == 0 ? 0 : Math.round((deltas.get(thread.getId()) * 100) / total);
            cpus.put(thread, cpu);
        }

        Map<Long, ThreadStat> threadStatMap = new HashMap<Long, ThreadStat>();
        for (Thread t : resultThreads) {
            ThreadStat threadStat = new ThreadStat();
            threadStat.setThread(t);
            threadStat.setThreadId(t.getId());
            threadStat.setInterrupted(t.isInterrupted());
            threadStat.setDaemon(t.isDaemon());
            threadStat.setPriority(t.getPriority());
            threadStat.setThreadName(t.getName());
            threadStat.setGroupName(t.getThreadGroup() == null ? "main" : t.getThreadGroup().getName());
            if (cpus.containsKey(t)) {
                threadStat.setCpuUsage(cpus.get(t));
            } else {
                threadStat.setCpuUsage(0L);
            }
            if (deltas.containsKey(t.getId())) {
                threadStat.setCpuTime(deltas.get(t.getId()));
            } else {
                threadStat.setCpuTime(0L);
            }
            threadStatMap.put(t.getId(), threadStat);
        }
        return threadStatMap;
    }

    /**
     * get the top N busy thread
     *
     * @param sampleInterval the interval between two samples
     * @param topN           the number of thread
     * @return a Map representing <ThreadID, cpuUsage>
     */
    public static Map<Long, Long> getTopNThreads(int sampleInterval, int topN) {
        List<Thread> threads = getThreadList();

        // Sample CPU
        Map<Long, Long> times1 = new HashMap<Long, Long>();
        for (Thread thread : threads) {
            long cpu = threadMXBean.getThreadCpuTime(thread.getId());
            times1.put(thread.getId(), cpu);
        }

        try {
            // Sleep for some time
            Thread.sleep(sampleInterval);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Resample
        Map<Long, Long> times2 = new HashMap<Long, Long>(threads.size());
        for (Thread thread : threads) {
            long cpu = threadMXBean.getThreadCpuTime(thread.getId());
            times2.put(thread.getId(), cpu);
        }

        // Compute delta map and total time
        long total = 0;
        Map<Long, Long> deltas = new HashMap<Long, Long>(threads.size());
        for (Long id : times2.keySet()) {
            long time1 = times2.get(id);
            long time2 = times1.get(id);
            if (time1 == -1) {
                time1 = time2;
            } else if (time2 == -1) {
                time2 = time1;
            }
            long delta = time2 - time1;
            deltas.put(id, delta);
            total += delta;
        }

        // Compute cpu
        final HashMap<Thread, Long> cpus = new HashMap<Thread, Long>(threads.size());
        for (Thread thread : threads) {
            long cpu = total == 0 ? 0 : Math.round((deltas.get(thread.getId()) * 100) / total);
            cpus.put(thread, cpu);
        }

        // Sort by CPU time : should be a rendering hint...
        Collections.sort(threads, new Comparator<Thread>() {
            @Override
            public int compare(Thread o1, Thread o2) {
                long l1 = cpus.get(o1);
                long l2 = cpus.get(o2);
                if (l1 < l2) {
                    return 1;
                } else if (l1 > l2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        // use LinkedHashMap to preserve insert order
        Map<Long, Long> topNThreads = new LinkedHashMap<Long, Long>();

        List<Thread> topThreads = topN > 0 && topN <= threads.size()
                ? threads.subList(0, topN) : threads;

        for (Thread thread : topThreads) {
            // Compute cpu usage
            topNThreads.put(thread.getId(), cpus.get(thread));
        }

        return topNThreads;
    }


    /**
     * Find the thread and lock that is blocking the most other threads.
     * <p>
     * Time complexity of this algorithm: O(number of thread)
     * Space complexity of this algorithm: O(number of locks)
     *
     * @return the BlockingLockInfo object, or an empty object if not found.
     */
    public static BlockingLockInfo findMostBlockingLock() {
        ThreadInfo[] infos = threadMXBean.dumpAllThreads(threadMXBean.isObjectMonitorUsageSupported(),
                threadMXBean.isSynchronizerUsageSupported());

        // a map of <LockInfo.getIdentityHashCode, number of thread blocking on this>
        Map<Integer, Integer> blockCountPerLock = new HashMap<Integer, Integer>();
        // a map of <LockInfo.getIdentityHashCode, the thread info that holding this lock
        Map<Integer, ThreadInfo> ownerThreadPerLock = new HashMap<Integer, ThreadInfo>();

        for (ThreadInfo info : infos) {
            if (info == null) {
                continue;
            }

            LockInfo lockInfo = info.getLockInfo();
            if (lockInfo != null) {
                // the current thread is blocked waiting on some condition
                if (blockCountPerLock.get(lockInfo.getIdentityHashCode()) == null) {
                    blockCountPerLock.put(lockInfo.getIdentityHashCode(), 0);
                }
                int blockedCount = blockCountPerLock.get(lockInfo.getIdentityHashCode());
                blockCountPerLock.put(lockInfo.getIdentityHashCode(), blockedCount + 1);
            }

            for (MonitorInfo monitorInfo : info.getLockedMonitors()) {
                // the object monitor currently held by this thread
                if (ownerThreadPerLock.get(monitorInfo.getIdentityHashCode()) == null) {
                    ownerThreadPerLock.put(monitorInfo.getIdentityHashCode(), info);
                }
            }

            for (LockInfo lockedSync : info.getLockedSynchronizers()) {
                // the ownable synchronizer currently held by this thread
                if (ownerThreadPerLock.get(lockedSync.getIdentityHashCode()) == null) {
                    ownerThreadPerLock.put(lockedSync.getIdentityHashCode(), info);
                }
            }
        }

        // find the thread that is holding the lock that blocking the largest number of threads.
        int mostBlockingLock = 0; // System.identityHashCode(null) == 0
        int maxBlockingCount = 0;
        for (Map.Entry<Integer, Integer> entry : blockCountPerLock.entrySet()) {
            if (entry.getValue() > maxBlockingCount && ownerThreadPerLock.get(entry.getKey()) != null) {
                // the lock is explicitly held by anther thread.
                maxBlockingCount = entry.getValue();
                mostBlockingLock = entry.getKey();
            }
        }

        if (mostBlockingLock == 0) {
            // nothing found
            return EMPTY_INFO;
        }

        BlockingLockInfo blockingLockInfo = new BlockingLockInfo();
        blockingLockInfo.threadInfo = ownerThreadPerLock.get(mostBlockingLock);
        blockingLockInfo.lockIdentityHashCode = mostBlockingLock;
        blockingLockInfo.blockingThreadCount = blockCountPerLock.get(mostBlockingLock);
        return blockingLockInfo;
    }

    public static com.shulie.instrument.simulator.module.model.thread.ThreadInfo getThreadInfo(ThreadInfo threadInfo, ThreadStat threadStat) {
        return getThreadInfo(threadInfo, threadStat, 0, 0);
    }

    public static com.shulie.instrument.simulator.module.model.thread.ThreadInfo getThreadInfo(BlockingLockInfo blockingLockInfo, ThreadStat threadStat) {
        return getThreadInfo(blockingLockInfo.threadInfo, threadStat, blockingLockInfo.lockIdentityHashCode,
                blockingLockInfo.blockingThreadCount);
    }

    public static com.shulie.instrument.simulator.module.model.thread.ThreadInfo getThreadInfo(ThreadInfo threadInfo, ThreadStat threadStat, int lockIdentityHashCode,
                                                                                               int blockingThreadCount) {
        com.shulie.instrument.simulator.module.model.thread.ThreadInfo threadInf = new com.shulie.instrument.simulator.module.model.thread.ThreadInfo();
        threadInf.setCpuUsage(threadStat.getCpuUsage());
        threadInf.setCpuTime(threadStat.getCpuTime());
        threadInf.setInterrupted(threadStat.isInterrupted());
        threadInf.setName(threadInfo.getThreadName());
        threadInf.setId(threadInfo.getThreadId());
        threadInf.setGroupName(threadStat.getGroupName());
        threadInf.setDaemon(threadStat.isDaemon());
        threadInf.setPriority(threadStat.getPriority());
        threadInf.setBlockedCount(threadInfo.getBlockedCount());
        threadInf.setBlockedTime(threadInfo.getBlockedTime());
        threadInf.setWaitedCount(threadInfo.getWaitedCount());
        threadInf.setWaitedTime(threadInfo.getWaitedTime());
        threadInf.setBlockingThreadCount(blockingThreadCount);
        threadInf.setLockIdentityHashCode(lockIdentityHashCode);
        threadInf.setState(threadInfo.getThreadState().name());
        threadInf.setLockName(threadInfo.getLockName());
        threadInf.setLockOwnerName(threadInfo.getLockOwnerName());
        threadInf.setLockOwnerId(threadInfo.getLockOwnerId());
        threadInf.setSuspended(threadInfo.isSuspended());
        threadInf.setInNative(threadInfo.isInNative());
        TraceInfo traceInfo = getTraceInfo(threadStat.getThread());
        if (traceInfo != null) {
            threadInf.setTraceId(traceInfo.getTraceId());
            threadInf.setRpcId(traceInfo.getRpcId());
            threadInf.setClusterTest(traceInfo.isClusterTest());
        }

        List<ThreadStack> threadStacks = new ArrayList<ThreadStack>();
        if (threadInfo.getLockInfo() != null) {
            Thread.State ts = threadInfo.getThreadState();
            switch (ts) {
                case BLOCKED:
                case WAITING:
                case TIMED_WAITING: {
                    com.shulie.instrument.simulator.module.model.thread.LockInfo lockInfo = new com.shulie.instrument.simulator.module.model.thread.LockInfo();
                    lockInfo.setClassName(threadInfo.getLockInfo().getClassName());
                    lockInfo.setIdentityHashCode(threadInfo.getLockInfo().getIdentityHashCode());
                    threadInf.setLockInfo(lockInfo);
                    break;
                }
                default:
            }
        }
        int i = 0;
        for (; i < threadInfo.getStackTrace().length; i++) {
            ThreadStack threadStack = new ThreadStack();
            StackTraceElement ste = threadInfo.getStackTrace()[i];
            StackElement stackElement = new StackElement();
            stackElement.setClassName(ste.getClassName());
            stackElement.setMethodName(ste.getMethodName());
            stackElement.setLineNumber(ste.getLineNumber());
            stackElement.setFileName(ste.getFileName());
            stackElement.setNativeMethod(ste.isNativeMethod());
            threadStack.setStackTraceElement(stackElement);
            if (i == 0) {
                threadStack.setLockInfo(threadInf.getLockInfo());
            }

            List<com.shulie.instrument.simulator.module.model.thread.MonitorInfo> monitorInfos = new ArrayList<com.shulie.instrument.simulator.module.model.thread.MonitorInfo>();
            for (MonitorInfo mi : threadInfo.getLockedMonitors()) {
                if (mi.getLockedStackDepth() == i) {
                    com.shulie.instrument.simulator.module.model.thread.MonitorInfo monitorInfo = new com.shulie.instrument.simulator.module.model.thread.MonitorInfo();
                    monitorInfo.setClassName(mi.getClassName());
                    monitorInfo.setIdentityHashCode(mi.getIdentityHashCode());
                    monitorInfo.setLockedStackDepth(mi.getLockedStackDepth());
                    monitorInfo.setIdentityHashCode(lockIdentityHashCode);
                    monitorInfo.setBlockingThreadCount(blockingThreadCount);
                    if (mi.getLockedStackFrame() != null) {
                        StackElement stackTraceElement = new StackElement();
                        stackTraceElement.setClassName(mi.getLockedStackFrame().getClassName());
                        stackTraceElement.setMethodName(mi.getLockedStackFrame().getMethodName());
                        stackTraceElement.setFileName(mi.getLockedStackFrame().getFileName());
                        stackTraceElement.setLineNumber(mi.getLockedStackFrame().getLineNumber());
                        stackTraceElement.setNativeMethod(mi.getLockedStackFrame().isNativeMethod());
                        monitorInfo.setStackTraceEle(stackTraceElement);
                    }
                    monitorInfos.add(monitorInfo);
                }
            }
            threadStack.setMonitorInfos(monitorInfos);
            threadStack.setState(threadInfo.getThreadState());
            threadStacks.add(threadStack);
        }
        threadInf.setThreadStacks(threadStacks);

        LockInfo[] locks = threadInfo.getLockedSynchronizers();
        List<com.shulie.instrument.simulator.module.model.thread.LockInfo> lockInfos = new ArrayList<com.shulie.instrument.simulator.module.model.thread.LockInfo>();
        if (locks.length > 0) {
            for (LockInfo li : locks) {
                com.shulie.instrument.simulator.module.model.thread.LockInfo lockInfo = new com.shulie.instrument.simulator.module.model.thread.LockInfo();
                lockInfo.setClassName(li.getClassName());
                lockInfo.setIdentityHashCode(li.getIdentityHashCode());
                lockInfos.add(lockInfo);
            }
        }
        threadInf.setLockInfos(lockInfos);
        return threadInf;
    }

    public static class BlockingLockInfo {

        // the thread info that is holing this lock.
        public ThreadInfo threadInfo = null;
        // the associated LockInfo object
        public int lockIdentityHashCode = 0;
        // the number of thread that is blocked on this lock
        public int blockingThreadCount = 0;

    }

    public static TraceInfo getTraceInfo(Thread currentThread) {
        try {
            // access to Thread#threadlocals field
            Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
            threadLocalsField.setAccessible(true);
            Object threadLocalMap = threadLocalsField.get(currentThread);
            // access to ThreadLocal$ThreadLocalMap#table filed
            Field tableFiled = threadLocalMap.getClass().getDeclaredField("table");
            tableFiled.setAccessible(true);
            Object[] tableEntries = (Object[]) tableFiled.get(threadLocalMap);
            for (Object entry : tableEntries) {
                if (entry == null) {
                    continue;
                }
                // access to ThreadLocal$ThreadLocalMap$Entry#value field
                Field valueField = entry.getClass().getDeclaredField("value");
                valueField.setAccessible(true);
                Object threadLocalValue = valueField.get(entry);
                TraceInfo traceInfo = new TraceInfo();
                if (threadLocalValue != null && "com.pamirs.pradar.RpcContext_inner".equals(threadLocalValue.getClass().getName())) {
                    // finally we got the chance to access trace id
                    Method getTraceIdMethod = threadLocalValue.getClass().getMethod("getTraceId");
                    getTraceIdMethod.setAccessible(true);
                    String traceId = (String) getTraceIdMethod.invoke(threadLocalValue);
                    traceInfo.setTraceId(traceId);
                    // get rpc id
                    Method getRpcIdMethod = threadLocalValue.getClass().getMethod("getRpcId");
                    getTraceIdMethod.setAccessible(true);
                    String rpcId = (String) getRpcIdMethod.invoke(threadLocalValue);
                    traceInfo.setRpcId(rpcId);

                    Method isClusterTestMethod = threadLocalValue.getClass().getMethod("isClusterTest");
                    isClusterTestMethod.setAccessible(true);
                    boolean isClusterTest = (Boolean) isClusterTestMethod.invoke(threadLocalValue);
                    traceInfo.setClusterTest(isClusterTest);

                    return traceInfo;
                }
            }
        } catch (Throwable e) {
            // ignore
        }
        return null;
    }

}
