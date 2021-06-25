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

import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.data.dao.perfomanceanaly.TraceManageDAO;
import io.shulie.tro.web.data.param.tracemanage.TraceManageDeployUpdateParam;
import io.shulie.tro.web.data.result.tracemanage.TraceManageDeployResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhaoyong
 * traceManage超时检测
 */
@Component
@Slf4j
public class TraceManageTask {

    @Autowired
    private TraceManageDAO traceManageDAO;

    public static long timeout = 20 * 1000;

    @Scheduled(cron = "*/5 * * * * ?")
    public void closeTimeoutTraceManage() {
        //获取正在采集中的数据
        List<TraceManageDeployResult> traceManageDeployResults = traceManageDAO.queryTraceManageDeployByStatus(1);
        if (CollectionUtils.isNotEmpty(traceManageDeployResults)) {
            long currentTimeMillis = System.currentTimeMillis();
            for (TraceManageDeployResult traceManageDeployResult : traceManageDeployResults) {
                if ((currentTimeMillis - traceManageDeployResult.getUpdateTime().getTime()) > timeout) {
                    TraceManageDeployUpdateParam traceManageDeployUpdateParam = new TraceManageDeployUpdateParam();
                    traceManageDeployUpdateParam.setId(traceManageDeployResult.getId());
                    //设置为采集超时状态
                    traceManageDeployUpdateParam.setStatus(3);
                    traceManageDAO.updateTraceManageDeployStatus(traceManageDeployUpdateParam,traceManageDeployResult.getStatus());
                    log.warn("方法采集超时，"+ JsonHelper.bean2Json(traceManageDeployResult));
                }
            }
        }
    }
}
