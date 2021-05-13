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

package io.shulie.tro.cloud.open.api.impl.report;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import io.shulie.tro.cloud.open.api.impl.CloudCommonApi;
import io.shulie.tro.cloud.open.api.report.CloudReportApi;
import io.shulie.tro.cloud.open.constant.CloudApiConstant;
import io.shulie.tro.cloud.open.req.report.JtlDownloadReq;
import io.shulie.tro.cloud.open.req.report.ReportDetailByIdReq;
import io.shulie.tro.cloud.open.req.report.ReportDetailBySceneIdReq;
import io.shulie.tro.cloud.open.req.report.ReportQueryReq;
import io.shulie.tro.cloud.open.req.report.UpdateReportConclusionReq;
import io.shulie.tro.cloud.open.resp.report.ReportDetailResp;
import io.shulie.tro.cloud.open.req.report.WarnCreateReq;
import io.shulie.tro.cloud.open.resp.report.ReportResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.http.HttpHelper;
import io.shulie.tro.utils.json.JsonHelper;
import io.shulie.tro.cloud.open.req.report.UpdateReportConclusionReq;

import io.shulie.tro.utils.http.TroResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.tro.properties.TroCloudClientProperties;
import org.springframework.stereotype.Component;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.open.api.impl.report
 * @date 2020/12/17 1:29 下午
 */
@Component
public class CloudReportApiImpl extends CloudCommonApi implements CloudReportApi {
    @Autowired
    private TroCloudClientProperties troCloudClientProperties;
    @Override
    public ResponseResult<List<ReportResp>> listReport(ReportQueryReq req) {
        return null;
    }
    @Override
    public ResponseResult<String> addWarn(WarnCreateReq req) {
        TroResponseEntity<ResponseResult<String>> troResponseEntity =
            HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.REPORT_WARN_URL,
                getHeaders(req.getLicense()), new TypeReference<ResponseResult<String>>() {},req);
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<String> getJtlDownLoadUrl(JtlDownloadReq req) {
        TroResponseEntity<ResponseResult<String>> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.REPORT_JTL_DOWNLOAD_URL,
                getHeaders(req.getLicense()), req, new TypeReference<ResponseResult<String>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<String> updateReportConclusion(UpdateReportConclusionReq req) {
        TroResponseEntity<ResponseResult<String>> troResponseEntity =
            HttpHelper.doPut(troCloudClientProperties.getUrl()+ CloudApiConstant.REPORT_UPDATE_STATE_URL,
                getHeaders(req.getLicense()), new TypeReference<ResponseResult<String>>() {},req);
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<ReportDetailResp> getReportByReportId(ReportDetailByIdReq req) {
        TroResponseEntity<ResponseResult<ReportDetailResp>> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl()+ CloudApiConstant.REPORT_DETAIL_GET_URL,
                getHeaders(req.getLicense()),req,new TypeReference<ResponseResult<ReportDetailResp>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<ReportDetailResp> tempReportDetail(ReportDetailBySceneIdReq req) {
        TroResponseEntity<ResponseResult<ReportDetailResp>> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.REPORT_TEMP_DETAIL_GET_URL,
                getHeaders(req.getLicense()), req, new TypeReference<ResponseResult<ReportDetailResp>>() {});
        if (troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(), "查看cloud日志");
    }
}
