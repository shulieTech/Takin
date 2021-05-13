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

import io.shulie.tro.web.app.request.perfomanceanaly.MemoryAnalysisRequest;
import io.shulie.tro.web.app.response.perfomanceanaly.DownloadDumpResponse;
import io.shulie.tro.web.app.response.perfomanceanaly.MemoryAnalysisResponse;

/**
 * @Author: mubai
 * @Date: 2020-11-09 11:26
 * @Description:
 */
public interface MemoryAnalysisService {

    MemoryAnalysisResponse queryMemoryDump(MemoryAnalysisRequest request);

    /**
     * 上传dump文件目录
     * @param agentId
     * @return
     */
    DownloadDumpResponse downloadDump(String agentId) throws Throwable;
}
