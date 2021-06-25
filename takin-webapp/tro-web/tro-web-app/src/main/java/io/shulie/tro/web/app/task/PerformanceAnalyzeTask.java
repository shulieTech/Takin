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

package io.shulie.tro.web.app.task;

import io.shulie.tro.web.app.service.perfomanceanaly.PerformanceBaseDataService;
import io.shulie.tro.web.app.service.perfomanceanaly.ThreadAnalyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.task
 * @date 2020/12/2 6:01 下午
 */

@Slf4j
@Component
public class PerformanceAnalyzeTask {
    @Autowired
    private PerformanceBaseDataService performanceBaseDataService;
    @Autowired
    private ThreadAnalyService threadAnalyService;

    @Value("${performance.clear.second:172800}")
    private String second;

    /**
     * 每天执行一次，清理7天之前的统计数据
     */
    @Scheduled(cron = "0 */10 * * * ?")
    public void clear() {
        performanceBaseDataService.clearData(Integer.valueOf(second));
        threadAnalyService.clearData(Integer.valueOf(second));
    }


}
