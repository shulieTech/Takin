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

import io.shulie.tro.web.app.service.report.ReportTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时查询 压测报告状态，汇总报告
 */
@Component
@Slf4j
public class QueryReportTask {

    @Autowired
    private ReportTaskService reportTaskService;

    @Value("${open.report.task:true}")
    private Boolean openReportTask;

    @Scheduled(cron = "*/10 * * * * ?")
    public void clientUpdateReport() {
        if (!openReportTask) {
            return;
        }
        reportTaskService.queryReport();
    }
}
