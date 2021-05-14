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

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.domain.dto.report.ReportTraceDTO;
import com.pamirs.tro.entity.domain.dto.report.ReportTraceQueryDTO;
import io.shulie.tro.web.app.common.Response;
import io.shulie.tro.web.app.constant.APIUrls;
import io.shulie.tro.web.app.service.report.ReportRealTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ReportRealTimeController
 * @Description
 * @Author qianshui
 * @Date 2020/8/17 下午8:18
 */
@RestController
@RequestMapping(APIUrls.TRO_API_URL)
@Api(tags = "实况报告")
public class ReportRealTimeController {

    @Autowired
    private ReportRealTimeService reportRealTimeService;

    @GetMapping("/report/realtime/link/list")
    @ApiOperation("实况链路列表")
    private Response getLinkList(ReportTraceQueryDTO queryDTO) {
        if (queryDTO.getReportId() != null) {
            PageInfo<ReportTraceDTO> reportLinkListByReportId = reportRealTimeService.getReportLinkListByReportId(
                queryDTO.getReportId(), queryDTO.getType(), queryDTO.getCurrentPage() + 1, queryDTO.getPageSize());
            return Response.success(reportLinkListByReportId);
        }
        if (queryDTO.getStartTime() != null) {
            PageInfo<ReportTraceDTO> reportLinkList = reportRealTimeService.getReportLinkList(queryDTO.getSceneId(),
                queryDTO.getStartTime(), queryDTO.getType(), queryDTO.getCurrentPage() + 1, queryDTO.getPageSize());
            return Response.success(reportLinkList);
        }

        return Response.success(new PageInfo<>());
    }

    @GetMapping("/report/link/detail")
    @ApiOperation("链路详情")
    private Response getLinkDetail(@RequestParam(value = "traceId") String traceId,@RequestParam(value = "id",defaultValue = "0") Integer id) {
        Map<String, Object> rootMap = reportRealTimeService.getLinkDetail(traceId,id);
        return Response.success(rootMap);
    }
}
