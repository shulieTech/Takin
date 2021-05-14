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

package io.shulie.tro.web.diff.cloud.impl.report;

import io.shulie.tro.cloud.open.api.report.CloudReportApi;
import io.shulie.tro.cloud.open.req.report.JtlDownloadReq;
import io.shulie.tro.cloud.open.req.report.ReportDetailByIdReq;
import io.shulie.tro.cloud.open.req.report.ReportDetailBySceneIdReq;
import io.shulie.tro.cloud.open.resp.report.ReportDetailResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.web.diff.api.report.ReportApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.diff.cloud.impl.report
 * @date 2020/12/17 1:10 下午
 */
@Service
public class ReportApiImpl  implements ReportApi {

    @Autowired
    private CloudReportApi cloudReportApi;


    @Override
    public ResponseResult<String> getJtlDownLoadUrl(Long reportId) {
        JtlDownloadReq req = new JtlDownloadReq();
        req.setReportId(reportId);
        return cloudReportApi.getJtlDownLoadUrl(req);
    }


    @Override
    public ResponseResult<ReportDetailResp> getReportByReportId(ReportDetailByIdReq req) {
        return cloudReportApi.getReportByReportId(req);
    }

    @Override
    public ResponseResult<ReportDetailResp> tempReportDetail(ReportDetailBySceneIdReq req) {
        return cloudReportApi.tempReportDetail(req);
    }

}
