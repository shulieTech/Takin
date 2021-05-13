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

package io.shulie.tro.web.app.service.report;

import com.pamirs.tro.entity.domain.vo.report.ReportQueryParam;
import com.pamirs.tro.entity.domain.vo.report.ReportTrendQueryParam;
import com.pamirs.tro.entity.domain.vo.sla.WarnQueryParam;
import io.shulie.tro.cloud.open.req.report.WarnCreateReq;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.web.app.output.report.ReportJtlDownloadOutput;
import io.shulie.tro.web.common.domain.WebResponse;

/**
 * @ClassName ReportService
 * @Description
 * @Author qianshui
 * @Date 2020/5/12 下午3:33
 */
public interface ReportService {

    WebResponse listReport(ReportQueryParam param);

    WebResponse getReportByReportId(Long reportId);

    WebResponse queryReportTrend(ReportTrendQueryParam param);

    WebResponse tempReportDetail(Long sceneId);

    WebResponse queryTempReportTrend(ReportTrendQueryParam param);

    WebResponse listWarn(WarnQueryParam param);

    WebResponse queryReportActivityByReportId(Long reportId);

    WebResponse queryReportActivityBySceneId(Long sceneId);

    WebResponse querySummaryList(Long reportId);

    WebResponse queryMetrices(Long reportId, Long sceneId, Long customId);

    WebResponse queryReportCount(Long reportId);

    WebResponse queryRunningReport();

    WebResponse queryListRunningReport();

    WebResponse lockReport(Long reportId);

    WebResponse unLockReport(Long reportId);

    WebResponse finishReport(Long reportId);

    /**
     * 下载jtl路径
     *
     * @param reportId
     * @return
     */
    ReportJtlDownloadOutput getJtlDownLoadUrl(Long reportId);
}
