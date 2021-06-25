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

package io.shulie.tro.cloud.biz.collector.collector;

import java.util.List;

import io.shulie.tro.cloud.biz.collector.bean.DiskUsage;
import io.shulie.tro.cloud.biz.collector.bean.LoadInfo;
import io.shulie.tro.cloud.biz.collector.bean.ServerStatusInfo;
import io.shulie.tro.cloud.biz.collector.bean.DiskUsage;
import io.shulie.tro.cloud.biz.collector.bean.LoadInfo;
import io.shulie.tro.cloud.biz.collector.bean.ServerStatusInfo;
import io.shulie.tro.cloud.biz.collector.bean.DiskUsage;
import io.shulie.tro.cloud.biz.collector.bean.LoadInfo;
import io.shulie.tro.cloud.biz.collector.bean.ServerStatusInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 计算压测引擎上报的服务器状态。用于弹性伸缩
 *
 * @Author <a href="tangyuhan@shulie.io">yuhan.tang</a>
 * @package: io.shulie.tro.cloud.poll.poll
 * @Date 2020-05-11 14:26
 */
@Slf4j
@Service
public class CollectorServerService {

    public void collector(ServerStatusInfo serverStatusInfo) {
        int cpu = collectorCpu(serverStatusInfo.getCpu());
        int memery = collectorMemery(serverStatusInfo.getMemery());
        int io = collectorIo(serverStatusInfo.getIo());
        int disk = collectorDisk(serverStatusInfo.getDiskUsages());
        int loader = collectorLoader(serverStatusInfo.getLoadInfo());
    }

    /**
     * 计算CPU使用率
     * -1 缩容
     * 0  正常
     * 1  扩容
     *
     * @param cpu
     * @return
     */
    public int collectorCpu(float cpu) {
        return 0;
    }

    /**
     * 计算内存使用率
     * -1 缩容
     * 0  正常
     * 1  扩容
     *
     * @param memery
     * @return
     */
    public int collectorMemery(long memery) {
        return 0;
    }

    /**
     * 计算io使用率
     * -1 缩容
     * 0  正常
     * 1  扩容
     *
     * @param io
     * @return
     */
    public int collectorIo(String io) {
        return 0;
    }

    /**
     * 计算loader使用率
     * -1 缩容
     * 0  正常
     * 1  扩容
     *
     * @param loadInfo
     * @return
     */
    public int collectorLoader(LoadInfo loadInfo) {
        return 0;
    }

    /**
     * 计算磁盘使用率
     * -1 缩容
     * 0  正常
     * 1  扩容
     *
     * @param diskUsages
     * @return
     */
    public int collectorDisk(List<DiskUsage> diskUsages) {
        return 0;
    }
}
