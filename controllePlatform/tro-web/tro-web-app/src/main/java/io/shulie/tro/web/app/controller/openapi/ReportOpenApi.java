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

package io.shulie.tro.web.app.controller.openapi;

import java.util.List;

import com.pamirs.tro.entity.domain.dto.report.BusinessActivityDTO;
import com.pamirs.tro.entity.domain.dto.report.MachineDetailDTO;
import com.pamirs.tro.entity.domain.dto.report.ReportDTO;
import com.pamirs.tro.entity.domain.dto.report.ReportDetailDTO;
import com.pamirs.tro.entity.domain.dto.report.ReportTrendDTO;
import com.pamirs.tro.entity.domain.vo.report.ReportQueryParam;
import com.pamirs.tro.entity.domain.vo.report.ReportTrendQueryParam;
import com.pamirs.tro.entity.domain.vo.sla.WarnQueryParam;
import io.shulie.tro.cloud.open.resp.scenemanage.WarnDetailResponse;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.controller.openapi.converter.ReportOpenApiConverter;
import io.shulie.tro.web.app.controller.openapi.request.report.ReportQueryOpenApiReq;
import io.shulie.tro.web.app.controller.openapi.request.report.ReportTrendQueryOpenApiReq;
import io.shulie.tro.web.app.controller.openapi.request.report.WarnQueryOpenApiReq;
import io.shulie.tro.web.app.controller.openapi.response.report.BusinessActivityOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.report.ReportDetailOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.report.ReportOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.report.ReportTrendOpenApiResp;
import io.shulie.tro.web.app.controller.openapi.response.report.WarnDetailOpenApiResp;
import io.shulie.tro.web.app.service.report.ReportLocalService;
import io.shulie.tro.web.app.service.report.ReportService;
import io.shulie.tro.web.common.domain.WebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 莫问
 * @Description
 * @Date 2020-04-17
 */
@RestController
@RequestMapping(APIUrls.TRO_OPEN_API_URL)
@Api(tags = "场景报告模块")
public class ReportOpenApi {

    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportLocalService reportLocalService;

    @GetMapping("report/listReport")
    @ApiOperation("报告列表")
    public Response<List<ReportOpenApiResp>> listReport(ReportQueryOpenApiReq reportQueryOpenApiReq) {
        ReportQueryParam reportQuery = ReportOpenApiConverter.INSTANCE.ofReportQueryOpenApiReq(reportQueryOpenApiReq);
        WebResponse<List<ReportDTO>> webResponse = reportService.listReport(reportQuery);
        return Response.success(ofListReportOpenApiResp(webResponse.getData()));
    }

    @GetMapping(value = "report/getReportByReportId")
    @ApiOperation("报告详情")
    @ApiImplicitParam(name = "reportId", value = "报告ID")
    public Response<ReportDetailOpenApiResp> getReportByReportId(Long reportId) {
        WebResponse<ReportDetailDTO> reportByReportId = reportService.getReportByReportId(reportId);
        return Response.success(ofReportDetailOpenApiResp(reportByReportId.getData()));
    }

    @GetMapping("report/queryReportTrend")
    @ApiOperation("报告链路趋势")
    public Response<ReportTrendOpenApiResp> queryReportTrend(ReportTrendQueryOpenApiReq reportTrendQueryOpenApiReq) {
        ReportTrendQueryParam reportTrendQuery = ReportOpenApiConverter.INSTANCE.ofReportTrendQueryOpenApiReq(reportTrendQueryOpenApiReq);
        WebResponse<ReportTrendDTO> webResponse = reportService.queryReportTrend(reportTrendQuery);
        return Response.success(ofReportTrendOpenApiResp(webResponse.getData()));
    }


    @GetMapping("/report/listWarn")
    @ApiOperation("警告列表")
    public Response<List<WarnDetailOpenApiResp>> listWarn(WarnQueryOpenApiReq warnQueryOpenApiReq) {
        WarnQueryParam param = ReportOpenApiConverter.INSTANCE.ofWarnQueryParam(warnQueryOpenApiReq);
        WebResponse<List<WarnDetailResponse>> webResponse = reportService.listWarn(param);
        return Response.success(ofListWarnDetailOpenApiResp(webResponse.getData()));
    }

    @GetMapping("/report/queryReportActivityByReportId")
    @ApiOperation("报告的业务活动")
    public Response<List<BusinessActivityOpenApiResp>> queryReportActivityByReportId(Long reportId) {
        WebResponse<List<BusinessActivityDTO>> webResponse = reportService.queryReportActivityByReportId(reportId);
        return Response.success(ReportOpenApiConverter.INSTANCE.ofLsitBusinessActivityOpenApiResp(webResponse.getData()));
    }

    @GetMapping("/report/queryReportActivityBySceneId")
    @ApiOperation("报告的业务活动")
    public Response<List<BusinessActivityOpenApiResp>> queryReportActivityBySceneId(Long sceneId) {
        WebResponse<List<BusinessActivityDTO>> webResponse = reportService.queryReportActivityBySceneId(sceneId);
        return Response.success(ReportOpenApiConverter.INSTANCE.ofLsitBusinessActivityOpenApiResp(webResponse.getData()));
    }

    /**
     * 详情
     * @param reportId
     * @param applicationName
     * @param machineIp
     * @return
     */
    @GetMapping("/report/machine/detail")
    @ApiOperation("性能详情")
    public MachineDetailDTO getMachineDetail(Long reportId, String applicationName, String machineIp) {
        return reportLocalService.getMachineDetail(reportId, applicationName, machineIp);
    }

    private ReportDetailOpenApiResp ofReportDetailOpenApiResp(Object data){
        if (data == null){
            return null;
        }
        String s = JsonHelper.bean2Json(data);
        return JsonHelper.json2Bean(s, ReportDetailOpenApiResp.class);
    }

    private ReportTrendOpenApiResp ofReportTrendOpenApiResp(Object data){
        if (data == null){
            return null;
        }
        String s = JsonHelper.bean2Json(data);
        return JsonHelper.json2Bean(s, ReportTrendOpenApiResp.class);
    }

    private List<ReportOpenApiResp> ofListReportOpenApiResp(Object data){
        if (data == null){
            return null;
        }
        String s = JsonHelper.bean2Json(data);
        return JsonHelper.json2List(s, ReportOpenApiResp.class);
    }

    private List<WarnDetailOpenApiResp> ofListWarnDetailOpenApiResp(Object data){
        if (data == null){
            return null;
        }
        String s = JsonHelper.bean2Json(data);
        return JsonHelper.json2List(s, WarnDetailOpenApiResp.class);
    }
}
