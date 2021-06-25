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

package io.shulie.tro.web.app.service.fastdebug;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.shulie.tro.web.app.response.fastdebug.FastDebugMachinePerformanceResponse;
import io.shulie.tro.web.app.utils.UnitConvertor;
import io.shulie.tro.web.data.dao.fastdebug.FastDebugMachinePerformanceDao;
import io.shulie.tro.web.data.result.fastdebug.FastDebugMachinePerformanceResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: mubai
 * @Date: 2020-12-30 21:22
 * @Description:
 */

@Slf4j
@Service
public class FastDebugMachinePerformanceServiceImpl implements FastDebugMachinePerformanceService {

    @Autowired
    private FastDebugMachinePerformanceDao fastDebugMachinePerformanceDao;

    @Override
    public List<FastDebugMachinePerformanceResponse> getMachinePerformance(String traceId, String rpcId,
        Integer logType) {
        List<FastDebugMachinePerformanceResult> machineList = fastDebugMachinePerformanceDao.getByRpcId(traceId, rpcId,
            logType);
        return assembleMachineData(machineList);
    }

    @Override
    public void clearHistoryData(Date beforeDate) {
        if (beforeDate == null) {
            return;
        }
        fastDebugMachinePerformanceDao.clearHistoryData(beforeDate);
    }

    private List<FastDebugMachinePerformanceResponse> assembleMachineData(
        List<FastDebugMachinePerformanceResult> machineList) {

        FastDebugMachinePerformanceResponse cpuUsage = new FastDebugMachinePerformanceResponse();
        FastDebugMachinePerformanceResponse cpuLoad = new FastDebugMachinePerformanceResponse();
        FastDebugMachinePerformanceResponse memoryUsage = new FastDebugMachinePerformanceResponse();
        FastDebugMachinePerformanceResponse memoryTotal = new FastDebugMachinePerformanceResponse();
        FastDebugMachinePerformanceResponse ioWait = new FastDebugMachinePerformanceResponse();
        FastDebugMachinePerformanceResponse youngGcCount = new FastDebugMachinePerformanceResponse();
        FastDebugMachinePerformanceResponse youngGcTime = new FastDebugMachinePerformanceResponse();
        FastDebugMachinePerformanceResponse oldGcCount = new FastDebugMachinePerformanceResponse();
        FastDebugMachinePerformanceResponse oldGcTime = new FastDebugMachinePerformanceResponse();

        cpuUsage.setType("CPU利用率");
        cpuLoad.setType("CPU负载");
        memoryUsage.setType("内存利用率");
        memoryTotal.setType("堆内存总和");
        ioWait.setType("IO");
        youngGcCount.setType("YoungGC次数");
        youngGcTime.setType("YoungGC耗时");
        oldGcCount.setType("OldGC次数");
        oldGcTime.setType("OldGC耗时");
        //YoungGC次数、YoungGC耗时、OldGC次数、OldGC耗时

        List<FastDebugMachinePerformanceResponse> responseList = new ArrayList<>();
        responseList.add(cpuUsage);
        responseList.add(cpuLoad);
        responseList.add(memoryUsage);
        responseList.add(memoryTotal);
        responseList.add(ioWait);
        responseList.add(youngGcCount);
        responseList.add(youngGcTime);
        responseList.add(oldGcCount);
        responseList.add(oldGcTime);
        for (FastDebugMachinePerformanceResult result : machineList) {
            if (null == result || StringUtils.isBlank(result.getIndex())) {
                continue;
            }
            switch (result.getIndex()) {
                case "beforeFirst":
                    cpuUsage.setBeforeIn(result.getCpuUsage());
                    cpuLoad.setBeforeIn(result.getCpuLoad());
                    memoryUsage.setBeforeIn(result.getMemoryUsage());
                    memoryTotal.setBeforeIn(UnitConvertor.byteToMb(result.getMemoryTotal()));
                    ioWait.setBeforeIn(result.getIoWait());
                    youngGcCount.setBeforeIn(result.getYoungGcCount());
                    youngGcTime.setBeforeIn(result.getYoungGcTime());
                    oldGcCount.setBeforeIn(result.getOldGcCount());
                    oldGcTime.setBeforeIn(result.getOldGcTime());
                    break;
                case "beforeLast":
                    cpuUsage.setBeforeAfter(result.getCpuUsage());
                    cpuLoad.setBeforeAfter(result.getCpuLoad());
                    memoryUsage.setBeforeAfter(result.getMemoryUsage());
                    memoryTotal.setBeforeAfter(UnitConvertor.byteToMb(result.getMemoryTotal()));
                    ioWait.setBeforeAfter(result.getIoWait());
                    youngGcCount.setBeforeAfter(result.getYoungGcCount());
                    youngGcTime.setBeforeAfter(result.getYoungGcTime());
                    oldGcCount.setBeforeAfter(result.getOldGcCount());
                    oldGcTime.setBeforeAfter(result.getOldGcTime());
                    break;
                case "exceptionFirst":
                    cpuUsage.setExceptionIn(result.getCpuUsage());
                    cpuLoad.setExceptionIn(result.getCpuLoad());
                    memoryUsage.setExceptionIn(result.getMemoryUsage());
                    memoryTotal.setExceptionIn(UnitConvertor.byteToMb(result.getMemoryTotal()));
                    ioWait.setExceptionIn(result.getIoWait());
                    youngGcCount.setExceptionIn(result.getYoungGcCount());
                    youngGcTime.setExceptionIn(result.getYoungGcTime());
                    oldGcCount.setExceptionIn(result.getOldGcCount());
                    oldGcTime.setExceptionIn(result.getOldGcTime());
                    break;
                case "exceptionLast":
                    cpuUsage.setExceptionOut(result.getCpuUsage());
                    cpuLoad.setExceptionOut(result.getCpuLoad());
                    memoryUsage.setExceptionOut(result.getMemoryUsage());
                    memoryTotal.setExceptionOut(UnitConvertor.byteToMb(result.getMemoryTotal()));
                    ioWait.setExceptionOut(result.getIoWait());
                    youngGcCount.setExceptionOut(result.getYoungGcCount());
                    youngGcTime.setExceptionOut(result.getYoungGcTime());
                    oldGcCount.setExceptionOut(result.getOldGcCount());
                    oldGcTime.setExceptionOut(result.getOldGcTime());
                    break;
                case "afterFirst":
                    cpuUsage.setAfterIn(result.getCpuUsage());
                    cpuLoad.setAfterIn(result.getCpuLoad());
                    memoryUsage.setAfterIn(result.getMemoryUsage());
                    memoryTotal.setAfterIn(UnitConvertor.byteToMb(result.getMemoryTotal()));
                    ioWait.setAfterIn(result.getIoWait());
                    youngGcCount.setAfterIn(result.getYoungGcCount());
                    youngGcTime.setAfterIn(result.getYoungGcTime());
                    oldGcCount.setAfterIn(result.getOldGcCount());
                    oldGcTime.setAfterIn(result.getOldGcTime());
                    break;
                case "afterLast":
                    cpuUsage.setAfterOut(result.getCpuUsage());
                    cpuLoad.setAfterOut(result.getCpuLoad());
                    memoryUsage.setAfterOut(result.getMemoryUsage());
                    memoryTotal.setAfterOut(UnitConvertor.byteToMb(result.getMemoryTotal()));
                    ioWait.setAfterOut(result.getIoWait());
                    youngGcCount.setAfterOut(result.getYoungGcCount());
                    youngGcTime.setAfterOut(result.getYoungGcTime());
                    oldGcCount.setAfterOut(result.getOldGcCount());
                    oldGcTime.setAfterOut(result.getOldGcTime());

                    break;
            }

        }
        return responseList;

    }
}
