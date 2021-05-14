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

package io.shulie.tro.schedule.taskmanage.Impl.report;

import java.util.List;

import javax.annotation.Resource;

import com.pamirs.tro.entity.dao.report.TReportMapper;
import com.pamirs.tro.entity.dao.scenemanage.TWarnDetailMapper;
import com.pamirs.tro.entity.domain.entity.report.Report;
import com.pamirs.tro.entity.domain.entity.scenemanage.WarnDetail;
import io.shulie.tro.cloud.app.Application;
import io.shulie.tro.cloud.biz.output.report.ReportDetailOutput;
import io.shulie.tro.cloud.biz.service.report.ReportService;
import io.shulie.tro.cloud.common.bean.sla.WarnQueryParam;
import io.shulie.tro.cloud.data.mapper.mysql.ReportMapper;
import io.shulie.tro.cloud.data.model.mysql.ReportEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: mubai
 * @Date: 2020-11-09 17:27
 * @Description:
 */


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Application.class)
public class ReportTest {

    @Resource
    private TWarnDetailMapper tWarnDetailMapper;

    @Resource
    private ReportMapper reportMapper;

    @Autowired
    private ReportService reportService;

    @Resource
    private TReportMapper tReportMapper;

    @Test
    public void testQuery() {

        WarnQueryParam param = new WarnQueryParam();
        param.setReportId(612L);
        List<WarnDetail> warnDetails = tWarnDetailMapper.listWarn(param);
        System.out.println(warnDetails);
    }

    @Test
    public void testReport() {
        ReportEntity reportEntity = reportMapper.selectById(610L);
        System.out.println(reportEntity);
    }

    @Test
    public void testTreport() {
        Report report = tReportMapper.selectByPrimaryKey(610L);
        System.out.println(report);
    }

    @Test
    public void testService() {
        ReportDetailOutput reportByReportId = reportService.getReportByReportId(610L);
        System.out.println(reportByReportId);
    }

//    @Test
//    public void insert() {
//        Report report = new Report();
//        report.setSceneId(11L);
//        report.setSceneName("ss");
//        report.setStartTime(new Date());
//
//        tReportMapper.insertSelective(report);
//    }


}
