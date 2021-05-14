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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.shulie.tro.web.app.Application;
import io.shulie.tro.web.app.service.linkManage.LinkManage;
import io.shulie.tro.web.app.service.report.impl.ReportDataCache;
import io.shulie.tro.web.app.service.report.impl.SummaryService;
import io.shulie.tro.web.app.service.risk.ProblemAnalysisService;
import io.shulie.tro.web.data.result.risk.BaseRiskResult;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: xingchen
 * @ClassName: RiskServiceTests
 * @Package: PACKAGE_NAME
 * @Date: 2020/7/2722:25
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class RiskServiceTests {
    private static Long reportId = 1124L;
    @Autowired
    private ProblemAnalysisService problemAnalysisService;
    @Autowired
    private ReportDataCache reportDataCache;
    @Autowired
    private SummaryService summaryService;
    @Autowired
    private LinkManage linkManage;

    @Before
    public void init() {
        //reportDataCache.readyCloudReportData(reportId);
    }

    @Test
    public void synmache() {
        //summaryService.calcApplicationSummary(1030L);
        //summaryService.calcTpsTarget(1051L);

        //problemAnalysisService.syncMachineData(reportId);
    }

    @Test
    public void testRisk() {
        problemAnalysisService.checkRisk(reportId);
        List<BaseRiskResult> voList = problemAnalysisService.processRisk(reportId);
        System.out.println(voList);
    }

    @Test
    public void testBottlen() {
        problemAnalysisService.processBottleneck(reportId);
    }

    @Test
    public void testLink() throws ParseException {
        /*Category category = new Category();
        category.setEvent("/app1/api/employee/add");
        category.setServiceType(RpcType.TRACE.getText());
        category.setApplicationName("tank-demo1-107-0827001");*/
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date startTime = format.parse("2020-09-02 21:56:18");
        Date endTime = format.parse("2020-09-02 22:30:18");
        problemAnalysisService.queryLinkDetail(39L, startTime.getTime(),
            endTime.getTime());
    }


}
