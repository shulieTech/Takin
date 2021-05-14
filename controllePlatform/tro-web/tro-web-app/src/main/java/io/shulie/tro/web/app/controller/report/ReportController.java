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

package io.shulie.tro.web.app.controller.report;

import com.pamirs.tro.entity.domain.vo.report.ReportQueryParam;
import com.pamirs.tro.entity.domain.vo.report.ReportTrendQueryParam;
import com.pamirs.tro.entity.domain.vo.sla.WarnQueryParam;
import io.shulie.tro.web.app.annotation.AuthVerification;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.constant.BizOpConstants;
import io.shulie.tro.web.app.output.report.ReportJtlDownloadOutput;
import io.shulie.tro.web.app.response.report.ReportJtlDownloadResponse;
import io.shulie.tro.web.app.service.report.ReportService;
import io.shulie.tro.web.auth.api.enums.ActionTypeEnum;
import io.shulie.tro.web.common.domain.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 莫问
 * @Description
 * @Date 2020-04-17
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "场景报告模块")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("report/listReport")
    @ApiOperation("报告列表")
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.PRESSURE_TEST_REPORT,
            needAuth = ActionTypeEnum.QUERY
    )
    public WebResponse listReport(ReportQueryParam reportQuery) {
        return reportService.listReport(reportQuery);
    }

    @GetMapping(value = "report/getReportByReportId")
    @ApiOperation("报告详情")
    @ApiImplicitParam(name = "reportId", value = "报告ID")
    public WebResponse getReportByReportId(Long reportId) {
        return reportService.getReportByReportId(reportId);
    }

    @GetMapping("report/queryReportTrend")
    @ApiOperation("报告链路趋势")
    public WebResponse queryReportTrend(ReportTrendQueryParam reportTrendQuery) {
        return reportService.queryReportTrend(reportTrendQuery);
    }

    /**
     * 实况报表
     */
    @GetMapping("report/tempReportDetail")
    @ApiOperation("实况报告")
    @ApiImplicitParam(name = "sceneId", value = "场景ID")
    @AuthVerification(
            moduleCode = BizOpConstants.ModuleCode.PRESSURE_TEST_SCENE,
            needAuth = ActionTypeEnum.START_STOP
    )
    public WebResponse tempReportDetail(Long sceneId) {
        return reportService.tempReportDetail(sceneId);
    }

    @GetMapping("/report/queryTempReportTrend")
    @ApiOperation("实况报告链路趋势")
    public WebResponse queryTempReportTrend(ReportTrendQueryParam reportTrendQuery) {
        return reportService.queryTempReportTrend(reportTrendQuery);
    }

    @GetMapping("/report/listWarn")
    @ApiOperation("警告列表")
    public WebResponse listWarn(WarnQueryParam param) {
        return reportService.listWarn(param);
    }

    @GetMapping("/report/queryReportActivityByReportId")
    @ApiOperation("报告的业务活动")
    public WebResponse queryReportActivityByReportId(Long reportId) {
        return reportService.queryReportActivityByReportId(reportId);
    }

    @GetMapping("/report/queryReportActivityBySceneId")
    @ApiOperation("报告的业务活动")
    public WebResponse queryReportActivityBySceneId(Long sceneId) {
        return reportService.queryReportActivityBySceneId(sceneId);
    }

    @GetMapping("/report/getJtlDownLoadUrl")
    @ApiOperation(value = "获取jtl文件下载路径")
    @AuthVerification(
        moduleCode = BizOpConstants.ModuleCode.SCRIPT_MANAGE,
        needAuth = ActionTypeEnum.DOWNLOAD
    )
    public ReportJtlDownloadResponse getJtlDownLoadUrl(@RequestParam Long reportId) {
        ReportJtlDownloadOutput output = reportService.getJtlDownLoadUrl(reportId);
        ReportJtlDownloadResponse response = new ReportJtlDownloadResponse();
        BeanUtils.copyProperties(output,response);
        return response;
    }

}
