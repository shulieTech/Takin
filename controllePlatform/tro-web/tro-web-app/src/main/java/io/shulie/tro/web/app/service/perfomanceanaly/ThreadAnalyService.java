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

package io.shulie.tro.web.app.service.perfomanceanaly;

import java.util.List;

import io.shulie.tro.web.app.request.perfomanceanaly.PerformanceAnalyzeRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.ThreadCpuUseRateRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.ThreadListRequest;
import io.shulie.tro.web.app.response.perfomanceanaly.ProcessBaseDataResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadCpuChartResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadCpuUseRateChartResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadListResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.ThreadStackInfoResponse;

/**
 * @ClassName ThreadAnalyService
 * @Description
 * @Author qianshui
 * @Date 2020/11/4 下午2:43
 */
public interface ThreadAnalyService {

    ProcessBaseDataResponse getBaseData(PerformanceAnalyzeRequest request);

    List<ThreadCpuChartResponse> getThreadAnalyze(PerformanceAnalyzeRequest request);

    ThreadListResponse getThreadList(ThreadListRequest threadList);

    List<ThreadCpuUseRateChartResponse> getThreadCpuUseRate(ThreadCpuUseRateRequest request);

    /**
     * 获取线程栈数据
     * @param link
     * @return
     */
    String getThreadStackInfo(String link);

    void clearData(Integer time);
}
