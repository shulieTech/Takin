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

package io.shulie.tro.web.app.service.report.impl;

import javax.annotation.Resource;

import com.pamirs.tro.entity.dao.report.TReportApplicationSummaryMapper;
import com.pamirs.tro.entity.dao.report.TReportBottleneckInterfaceMapper;
import com.pamirs.tro.entity.dao.report.TReportMachineMapper;
import com.pamirs.tro.entity.dao.report.TReportSummaryMapper;
import org.springframework.stereotype.Component;

/**
 * @ClassName ReportClearImpl
 * @Description
 * @Author qianshui
 * @Date 2020/8/12 下午5:20
 */
@Component
public class ReportClearService {

    @Resource
    private TReportApplicationSummaryMapper TReportApplicationSummaryMapper;

    @Resource
    private TReportBottleneckInterfaceMapper TReportBottleneckInterfaceMapper;

    @Resource
    private TReportMachineMapper TReportMachineMapper;

    @Resource
    private TReportSummaryMapper TReportSummaryMapper;

    public void clearReportData(Long reportId) {
        if (reportId == null) {
            return;
        }
        TReportBottleneckInterfaceMapper.deleteByReportId(reportId);
        TReportApplicationSummaryMapper.deleteByReportId(reportId);
        TReportSummaryMapper.deleteByReportId(reportId);
        TReportMachineMapper.deleteByReportId(reportId);
    }
}

