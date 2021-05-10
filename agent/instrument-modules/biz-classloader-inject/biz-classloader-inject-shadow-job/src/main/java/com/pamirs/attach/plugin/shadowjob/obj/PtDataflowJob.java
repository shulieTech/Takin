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
package com.pamirs.attach.plugin.shadowjob.obj;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.pamirs.pradar.internal.PradarInternalService;

import java.util.List;

/**
 * @author angju
 * @date 2021/3/24 15:45
 */
public class PtDataflowJob implements DataflowJob {

    private DataflowJob dataflowJob;

    @Override
    public List fetchData(ShardingContext shardingContext) {
        PradarInternalService.startTrace(null, dataflowJob.getClass().getName(), "fetchData");
        PradarInternalService.setClusterTest(true);
        List result = dataflowJob.fetchData(shardingContext);
        PradarInternalService.setClusterTest(false);
        PradarInternalService.endTrace();
        return result;
    }

    @Override
    public void processData(ShardingContext shardingContext, List data) {
        String traceId = PradarInternalService.getTraceId();
        PradarInternalService.startTrace(traceId, dataflowJob.getClass().getName(), "processData");
        PradarInternalService.setClusterTest(true);
        dataflowJob.processData(shardingContext, data);
        PradarInternalService.setClusterTest(false);
        PradarInternalService.endTrace();
    }

    public void setDataflowJob(DataflowJob dataflowJob){
        this.dataflowJob = dataflowJob;
    }
}
