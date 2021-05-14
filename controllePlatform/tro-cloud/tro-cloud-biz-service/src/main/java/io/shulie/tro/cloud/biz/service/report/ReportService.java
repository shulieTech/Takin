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

package io.shulie.tro.cloud.biz.service.report;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.domain.dto.report.BusinessActivityDTO;
import com.pamirs.tro.entity.domain.dto.report.Metrices;
import com.pamirs.tro.entity.domain.dto.report.ReportDTO;
import com.pamirs.tro.entity.domain.dto.report.ReportTrendDTO;
import com.pamirs.tro.entity.domain.vo.report.ReportQueryParam;
import com.pamirs.tro.entity.domain.vo.report.ReportTrendQueryParam;
import io.shulie.tro.cloud.biz.input.report.WarnCreateInput;
import io.shulie.tro.cloud.biz.input.report.UpdateReportConclusionInput;
import io.shulie.tro.cloud.biz.output.report.ReportDetailOutput;
import io.shulie.tro.cloud.biz.output.scenemanage.WarnDetailOutput;
import io.shulie.tro.cloud.common.bean.scenemanage.BusinessActivitySummaryBean;
import io.shulie.tro.cloud.common.bean.sla.WarnQueryParam;
import io.shulie.tro.common.beans.response.ResponseResult;

public interface ReportService {

    /**
     * 报告列表
     *
     * @param param
     * @return
     */
    PageInfo<ReportDTO> listReport(ReportQueryParam param);

    /**
     * 报告详情
     *
     * @param reportId
     * @return
     */
    ReportDetailOutput getReportByReportId(Long reportId);

    /**
     * 报告链路趋势
     *
     * @param reportTrendQuery
     * @return
     */
    ReportTrendDTO queryReportTrend(ReportTrendQueryParam reportTrendQuery);

    /**
     * 实况报表
     *
     * @param sceneId
     * @return
     */
    ReportDetailOutput tempReportDetail(Long sceneId);

    /**
     * 实况链路趋势
     *
     * @param reportTrendQuery
     * @return
     */
    ReportTrendDTO queryTempReportTrend(ReportTrendQueryParam reportTrendQuery);

    /**
     * 警告列表
     *
     * @param param
     * @return
     */
    PageInfo<WarnDetailOutput> listWarn(WarnQueryParam param);

    /**
     * @param reportId
     * @return
     */
    List<BusinessActivityDTO> queryReportActivityByReportId(Long reportId);

    /**
     * @param sceneId
     * @return
     */
    List<BusinessActivityDTO> queryReportActivityBySceneId(Long sceneId);

    List<BusinessActivitySummaryBean> getBusinessActivitySummaryList(Long reportId);

    /**
     * 获取报告的业务活动数量和压测通过数量
     *
     * @param reportId
     * @return
     */
    Map<String, Object> getReportCount(Long reportId);

    /**
     * 查询正在生成的报告
     *
     * @return
     */
    Long queryRunningReport();

    List<Long> queryListRunningReport();

    /**
     * 锁定报告
     *
     * @param reportId
     * @return
     */
    Boolean lockReport(Long reportId);

    /**
     * 解锁报告
     *
     * @param reportId
     * @return
     */
    Boolean unLockReport(Long reportId);

    /**
     * 客户端调，报告完成
     *
     * @param reportId
     * @return
     */
    Boolean finishReport(Long reportId);

    /**
     * 新增 customerId
     *
     * @param reportId
     * @param sceneId
     * @param customerId
     * @return
     */
    List<Metrices> metrices(Long reportId, Long sceneId, Long customerId);

    /**
     * 更新扩展字段
     *
     * @param reportId
     * @param status
     * @param errKey
     * @param errMsg
     */
    void updateReportFeatures(Long reportId, Integer status, String errKey, String errMsg);

    /**
     * 压测报表-指定责任人
     *
     * @return
     */
    int allocationReportUser(Long dataId, Long userId);

    /**
     * 创建告警
     * @param input
     */
    void addWarn(WarnCreateInput input);

    /**
     * 下载jtl路径
     * @param reportId
     * @return
     */
    ResponseResult<String> getJtlDownLoadUrl(Long reportId);

    void clearLog(String time);

    /**
     * 更新报告是否通过
     * @param input
     */
    void updateReportConclusion(UpdateReportConclusionInput input);
}
