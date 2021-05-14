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

package io.shulie.tro.web.app.service.risk;

import java.util.List;

import com.pamirs.tro.entity.domain.risk.ReportLinkDetail;
import io.shulie.tro.web.data.result.risk.BaseRiskResult;

/**
 * @Author: xingchen
 * @ClassName: BaseRiskService
 * @Package: io.shulie.tro.report.service
 * @Date: 2020/7/2717:03
 * @Description:
 */
public interface ProblemAnalysisService {
    /**
     * 同步机器数据到表
     */
    void syncMachineData(Long reportId);

    /**
     * 检查风险机器，并保存
     *
     * @param reportId
     */
    void checkRisk(Long reportId);

    /**
     * 通过压测报告ID,获取所有的风险列表
     *
     * @param reportId
     * @return
     */
    List<BaseRiskResult> processRisk(Long reportId);

    /**
     * 通过压测报告ID,获取瓶颈列表
     *
     * @param reportId
     * @return
     */
    void processBottleneck(Long reportId);

    /**
     * 根据业务活动ID、时间获取压测链路明细信息
     *
     * @param businessActivityId
     * @param startTime          毫秒数
     * @return
     */
    ReportLinkDetail queryLinkDetail(Long businessActivityId, Long startTime, Long endTime);
}
