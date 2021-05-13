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

package io.shulie.tro.cloud.biz.task;

import java.util.Calendar;
import java.util.Date;

import io.shulie.tro.cloud.biz.service.report.ReportService;
import io.shulie.tro.cloud.common.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.biz.task
 * @date 2020/12/17 3:07 下午
 */
@Component
@Slf4j
public class ReportLogTask {
    @Autowired
    private ReportService reportService;

    @Value("${jtl.log.clear.time:14}")
    private String time;

    /**
     * 每天上午12:15触发
     */
    @Scheduled(cron = "0 15 12 ? * *")
    public void clearLog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, Integer.valueOf(time));
        reportService.clearLog(DateUtil.getYYYYMMDDHHMMSS(calendar.getTime()));
    }
}
