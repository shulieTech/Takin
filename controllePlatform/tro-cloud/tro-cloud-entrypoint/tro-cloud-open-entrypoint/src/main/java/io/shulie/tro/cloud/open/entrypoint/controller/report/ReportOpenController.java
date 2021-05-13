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

package io.shulie.tro.cloud.open.entrypoint.controller.report;

import io.shulie.tro.cloud.biz.input.report.WarnCreateInput;
import io.shulie.tro.cloud.biz.input.report.UpdateReportConclusionInput;
import io.shulie.tro.cloud.biz.output.report.ReportDetailOutput;
import io.shulie.tro.cloud.biz.service.report.ReportService;
import io.shulie.tro.cloud.common.constants.APIUrls;
import io.shulie.tro.cloud.common.exception.ApiException;
import io.shulie.tro.cloud.open.req.report.WarnCreateReq;
import io.shulie.tro.cloud.biz.input.report.UpdateReportConclusionInput;

import io.shulie.tro.cloud.open.req.report.UpdateReportConclusionReq;
import io.shulie.tro.cloud.open.resp.report.ReportDetailResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 莫问
 * @Description
 * @Date 2020-04-17
 */
@RestController
@RequestMapping(APIUrls.TRO_OPEN_API_URL)
@Api(tags = "场景报告模块", value = "场景报告")
public class ReportOpenController {

    @Autowired
    private ReportService reportService;

    /**
     * 分配负责人
     */
    @GetMapping("/report/allocationUser")
    @ApiOperation("压测报告分配负责人")
    public ResponseResult<String> allocation(@ApiParam(name = "dataId", value = "主键ID") Long dataId,
                                     @ApiParam(name = "userId", value = "用户id") Long userId) {
        try {
            int count = reportService.allocationReportUser(dataId, userId);
            return count > 0 ? ResponseResult.success("分配成功") : ResponseResult.fail("分配失败", "");
        } catch (ApiException exception) {
            return ResponseResult.fail(String.valueOf(exception.getCode()), exception.getMessage(), "");
        } catch (Exception e) {
            return ResponseResult.fail("系统错误", "");
        }
    }
    @PostMapping("/report/warn")
    @ApiOperation("创建告警")
    public ResponseResult<String> addWarn(@RequestBody WarnCreateReq req) {
        WarnCreateInput input = new WarnCreateInput();
        BeanUtils.copyProperties(req, input);
        reportService.addWarn(input);
        return ResponseResult.success("创建告警成功");
    }
    /**
     * 获取下载jtl下载路径
     */
    @GetMapping("/report/getJtlDownLoadUrl")
    @ApiOperation("压测报告分配负责人")
    public ResponseResult<String> getJtlDownLoadUrl(@ApiParam(name = "reportId", value = "报告id") Long reportId) {

        return reportService.getJtlDownLoadUrl(reportId);
    }
    /**
     * 更新报告
     */
    @PutMapping("/report/updateReportConclusion")
    @ApiOperation("更新报告-漏数检查使用")
    public ResponseResult<String> updateReportConclusion(@RequestBody UpdateReportConclusionReq req) {
        UpdateReportConclusionInput input = new UpdateReportConclusionInput();
        BeanUtils.copyProperties(req,input);
        reportService.updateReportConclusion(input);
        return ResponseResult.success("更新成功");
    }


    @GetMapping(value = "report/getReportByReportId")
    @ApiOperation("报告详情")
    @ApiImplicitParam(name = "reportId", value = "报告ID")
    public ResponseResult<ReportDetailResp> getReportByReportId(Long reportId) {
        ReportDetailOutput detailOutput = reportService.getReportByReportId(reportId);
        if (detailOutput == null) {
            return ResponseResult.fail("报告不存在", "");
        }
        ReportDetailResp resp = new ReportDetailResp();
        BeanUtils.copyProperties(detailOutput,resp);
        return ResponseResult.success(resp);
    }

    /**
     * 实况报表
     * 迁移到open-api
     */
    @Deprecated
    @GetMapping("report/tempReportDetail")
    @ApiOperation("实况报告")
    @ApiImplicitParam(name = "sceneId", value = "场景ID")
    public ResponseResult<ReportDetailResp> tempReportDetail(Long sceneId) {
        ReportDetailOutput detailOutput = reportService.tempReportDetail(sceneId);
        if (detailOutput == null) {
            return ResponseResult.fail("报告不存在", "");
        }
        ReportDetailResp resp = new ReportDetailResp();
        BeanUtils.copyProperties(detailOutput,resp);
        return ResponseResult.success(resp);
    }
}
