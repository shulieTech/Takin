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

import java.io.IOException;
import java.util.Date;

import com.github.pagehelper.util.StringUtil;
import com.pamirs.tro.common.util.DateUtils;
import io.shulie.tro.web.app.constant.FastDebugLogPathFactory;
import io.shulie.tro.web.app.service.fastdebug.FastDebugMachinePerformanceService;
import io.shulie.tro.web.app.service.fastdebug.FastDebugStackInfoService;
import io.shulie.tro.web.app.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: mubai
 * @Date: 2021-01-20 19:53
 * @Description: 定时清理拉取的日志，agent上报的日志文件，根据创建日期进行删除，保留15天
 */
@Slf4j
@Component
public class FastDebugSchedulerTask {

    @Autowired
    private FastDebugLogPathFactory pathFactory;

    @Autowired
    private FastDebugMachinePerformanceService fastDebugMachinePerformanceService;

    @Autowired
    private FastDebugStackInfoService fastDebugStackInfoService;

    @Scheduled(cron = "0 0 2 * * ?")
    public void clear() {
        log.info("执行删除拉取的应用日志定时任务...");
        long start = System.currentTimeMillis();
        Date previousNDay = DateUtils.getPreviousNDay(15);
        try {
            clearAgentAndAPPlog(previousNDay);
        } catch (Exception e) {
            log.error("执行清理agent上报的日志任务失败； ", e);
        }
        try {
            clearMachinePerformance(previousNDay);
        } catch (Exception e) {
            log.error("执行清理调试机器性能数据失败", e);
        }
        try {
            clearHistoryStackInfoData(previousNDay);
        } catch (Exception e) {
            log.error("执行清理调试栈数据定时任务失败", e);
        }
        log.info("execute fastdebug scheduler task cost : {}", System.currentTimeMillis() - start + "ms");
    }

    public void clearAgentAndAPPlog(Date date) throws IOException {
        String logPathPre = pathFactory.getLogPathPre();
        if (StringUtil.isEmpty(logPathPre) || date == null) {
            return;
        }
        FileUtils.deleteFileByCreateTime(logPathPre, date.getTime());
    }

    public void clearMachinePerformance(Date beforeDate) {
        fastDebugMachinePerformanceService.clearHistoryData(beforeDate);
    }

    public void clearHistoryStackInfoData(Date beforeDate) {
        fastDebugStackInfoService.clearHistory(beforeDate);
    }
}
