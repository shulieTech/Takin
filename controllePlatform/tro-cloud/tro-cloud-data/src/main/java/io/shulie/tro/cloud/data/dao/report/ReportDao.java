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

package io.shulie.tro.cloud.data.dao.report;

import java.util.List;

import io.shulie.tro.cloud.data.param.report.ReportDataQueryParam;
import io.shulie.tro.cloud.data.param.report.ReportUpdateConclusionParam;
import io.shulie.tro.cloud.data.param.report.ReportUpdateParam;
import io.shulie.tro.cloud.data.result.report.ReportResult;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.data.dao.report
 * @date 2020/12/17 3:30 下午
 */
public interface ReportDao {
    /**
     * 获取列表
     * @param param
     * @return
     */
    List<ReportResult> getList(ReportDataQueryParam param);

    /**
     * 获取报告
     * @param id
     * @return
     */
    ReportResult selectById(Long id);

    /**
     * 更新通过是否通过
     * @param param
     */
    void updateReportConclusion(ReportUpdateConclusionParam param);

    /**
     * 更新报告
     * @param param
     */
    void updateReport(ReportUpdateParam param);

    /**
     * 完成报告
     * @param reportId
     *
     */
    void finishReport(Long reportId);

    /**
     * 锁报告
     * @param resultId
     * @param lock
     */
    void updateReportLock(Long resultId, Integer lock);



    /**
     * 根据场景ID获取（临时）压测中的报告ID
     *
     * @param sceneId
     * @return
     */
    ReportResult getTempReportBySceneId(Long sceneId);


    /**
     * 根据场景ID获取压测中的报告ID
     *
     * @param sceneId
     * @return
     */
    ReportResult getReportBySceneId(Long sceneId);
}
