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

package io.shulie.tro.cloud.open.api.report;

import java.util.List;

import io.shulie.tro.cloud.open.req.report.JtlDownloadReq;
import io.shulie.tro.cloud.open.req.report.ReportDetailByIdReq;
import io.shulie.tro.cloud.open.req.report.ReportDetailBySceneIdReq;
import io.shulie.tro.cloud.open.req.report.ReportQueryReq;
import io.shulie.tro.cloud.open.req.report.WarnCreateReq;
import io.shulie.tro.cloud.open.req.report.UpdateReportConclusionReq;
import io.shulie.tro.cloud.open.resp.report.ReportDetailResp;
import io.shulie.tro.cloud.open.resp.report.ReportResp;
import io.shulie.tro.common.beans.response.ResponseResult;

/**
 * @Author: mubai
 * @Date: 2020-11-02 17:02
 * @Description:
 */
public interface CloudReportApi {

    ResponseResult<List<ReportResp>>  listReport (ReportQueryReq req) ;

    ResponseResult<String> addWarn (WarnCreateReq req);

    /**
     * jtl下载路径
     * @param req
     * @return
     */
    ResponseResult<String> getJtlDownLoadUrl(JtlDownloadReq req);

    /**
     * 更新报告状态，用于漏数检查
     *
     * @param req
     * @return
     */
    ResponseResult<String> updateReportConclusion(UpdateReportConclusionReq req);

    /**
     * 根据报告id获取报告详情
     * @param req
     * @return
     */
    ResponseResult<ReportDetailResp> getReportByReportId(ReportDetailByIdReq req);

    /**
     * 根据场景id获取报告详情
     * @param req
     * @return
     */
    ResponseResult<ReportDetailResp> tempReportDetail(ReportDetailBySceneIdReq req);
}
