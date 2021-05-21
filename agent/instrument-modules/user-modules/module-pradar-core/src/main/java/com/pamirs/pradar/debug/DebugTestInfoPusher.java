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
package com.pamirs.pradar.debug;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pamirs.pradar.AppNameUtils;
import com.pamirs.pradar.Pradar;
import com.pamirs.pradar.common.HttpUtils;
import com.pamirs.pradar.debug.model.DebugTestInfo;
import com.pamirs.pradar.pressurement.base.util.PropertyUtil;
import com.pamirs.pradar.utils.MonitorCollector;
import com.shulie.instrument.simulator.api.CommandResponse;
import com.shulie.instrument.simulator.api.executors.ExecutorServiceFactory;
import com.shulie.instrument.simulator.api.resource.ModuleCommandInvoker;
import com.shulie.instrument.simulator.api.util.CollectionUtils;
import com.shulie.instrument.simulator.module.model.gc.GcInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HardwareAbstractionLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author wangjian
 * @since 2020/12/29 20:14
 */
public class DebugTestInfoPusher implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(DebugTestInfo.class);
    private static final Queue<DebugTestInfo> DEBUG_INFOS = new ConcurrentLinkedQueue<DebugTestInfo>();

    private static final String RUNNING_INFO_PUSH_API = "/api/fast/debug/stack/upload";
    private final static String MODULE_ID_GC = "gc";
    private final static String MODULE_COMMAND_GC_INFO = "info";

    private static final int QUEUE_MAX_SIZE = 1000;
    private static final int PUSH_MAX_SIZE = 20;
    private static final ExecutorService debugPushService = Executors.newSingleThreadExecutor(new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Debug-Info-Push-Service");
            t.setDaemon(true);
            t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    logger.error("Thread {} caught a unknow exception with UncaughtExceptionHandler", t.getName(), e);
                }
            });
            return t;
        }
    });

    /**
     * 模块命令调用者，由 PradarCoreModule 初始时化来进行设置
     *
     * @see com.shulie.instrument.module.pradar.core.PradarCoreModule
     */
    private static ModuleCommandInvoker moduleCommandInvoker;

    public static void setModuleCommandInvoker(ModuleCommandInvoker moduleCommandInvoker) {
        DebugTestInfoPusher.moduleCommandInvoker = moduleCommandInvoker;
    }

    /**
     * 添加调试日志
     *
     * @param debugTestInfo
     */
    public static void addDebugInfo(DebugTestInfo debugTestInfo) {
        if (debugTestInfo == null) {
            return;
        }
        if (DEBUG_INFOS.size() > QUEUE_MAX_SIZE) {
            return;
        }
        DEBUG_INFOS.add(debugTestInfo);
    }

    /**
     * 处理机器性能信息
     */
    public static void doRecord(final String methodName) {
        if (Pradar.isDebug()) {
            final DebugTestInfo debugTestInfo = new DebugTestInfo();
            debugTestInfo.setTraceId(Pradar.getTraceId());
            debugTestInfo.setLogType(Pradar.getLogType());
            debugTestInfo.setRpcId(Pradar.getInvokeId());
            debugTestInfo.setAgentId(Pradar.getAgentId());
            debugTestInfo.setAppName(AppNameUtils.appName());
            debugPushService.submit(new Runnable() {
                @Override
                public void run() {
                    ArrayList<DebugTestInfo.MechineInfo> machineInfos = new ArrayList<DebugTestInfo.MechineInfo>();
                    DebugTestInfo.MechineInfo machineInfo = new DebugTestInfo.MechineInfo();
                    HardwareAbstractionLayer hal = MonitorCollector.si.getHardware();
                    CentralProcessor processor = MonitorCollector.si.getHardware().getProcessor();
                    Map<String, String> cpuInfoResult = MonitorCollector.getCpuUsageAndIoWait(processor);
                    String[] cpuLoad = MonitorCollector.getCpuLoad(processor);
                    String memoryUsage = MonitorCollector.getMemoryUsage(hal.getMemory());
                    machineInfo.setIndex(methodName);
                    machineInfo.setCpuLoad(cpuLoad[0]);
                    machineInfo.setCpuUsage(cpuInfoResult.get(MonitorCollector.CPU_USAGE_KEY) == null ? StringUtils.EMPTY : cpuInfoResult.get(MonitorCollector.CPU_USAGE_KEY));
                    machineInfo.setMemoryUsage(memoryUsage);
                    machineInfo.setMemoryTotal(String.valueOf(Runtime.getRuntime().totalMemory()));
                    machineInfo.setIoWait(cpuInfoResult.get(MonitorCollector.IO_WAIT_KEY) == null ? StringUtils.EMPTY : cpuInfoResult.get(MonitorCollector.IO_WAIT_KEY));
                    machineInfos.add(machineInfo);
                    if (moduleCommandInvoker != null) {
                        CommandResponse<GcInfo> gcResp = moduleCommandInvoker.invokeCommand(MODULE_ID_GC, MODULE_COMMAND_GC_INFO);
                        if (gcResp.isSuccess()) {
                            GcInfo gcInfo = gcResp.getResult();
                            machineInfo.setYoungGcCount(gcInfo.getYoungGcCount());
                            machineInfo.setYoungGcTime(gcInfo.getYoungGcTime());
                            machineInfo.setOldGcCount(gcInfo.getOldGcCount());
                            machineInfo.setOldGcTime(gcInfo.getOldGcTime());
                        }
                    }

                    debugTestInfo.setMachineInfo(machineInfos);
                    if (logger.isDebugEnabled()) {
                        logger.debug("DebugTestInfoPusher: traceId:{},agentId:{},index:{},rpcId:{},logType:{}",
                                debugTestInfo.getTraceId(),
                                debugTestInfo.getAgentId(),
                                methodName,
                                debugTestInfo.getRpcId(),
                                debugTestInfo.getLogType());
                    }
                    DEBUG_INFOS.add(debugTestInfo);
                    if (DEBUG_INFOS.size() >= QUEUE_MAX_SIZE) {
                        pushInfo(QUEUE_MAX_SIZE);
                    }
                }
            });
        }
    }

    @Override
    public void run() {
        if (DEBUG_INFOS.isEmpty()) {
            return;
        }
        int count = (DEBUG_INFOS.size() - 1) / PUSH_MAX_SIZE + 1;
        for (int i = 0; i < count; i++) {
            int size = Math.min(PUSH_MAX_SIZE, DEBUG_INFOS.size());
            pushInfo(size);
        }
    }

    private static void pushInfo(int size) {
        try {
            // 提取队列数据
            List<DebugTestInfo> data = new ArrayList<DebugTestInfo>();
            for (int i = 0; i < size; i++) {
                DebugTestInfo debugTestInfo = DEBUG_INFOS.poll();
                if (debugTestInfo != null) {
                    if (debugTestInfo.getLogCallback() != null) {
                        debugTestInfo.setLog(debugTestInfo.getLogCallback().getLog());
                    }
                } else {
                    break;
                }
                data.add(debugTestInfo);
            }
            // 相同traceId与rpcId数据整合为一个数据单元
            if (CollectionUtils.isNotEmpty(data)) {
                // 数据推送
                String s1 = JSON.toJSONString(data);
                HttpUtils.HttpResult httpResult = HttpUtils.doPost(PropertyUtil.getTroControlWebUrl() + RUNNING_INFO_PUSH_API, s1);
                if (!httpResult.isSuccess()) {
                    DEBUG_INFOS.addAll(data);
                } else {
                    JSONObject jsonObject = JSON.parseObject(httpResult.getResult());
                    if (!jsonObject.getBoolean("success")) {
                        DEBUG_INFOS.addAll(data);
                    }
                }
            }
        } catch (Throwable ex) {
            logger.error("DebugTestInfoPusher: debug data push exception", ex);
        }
    }
}
