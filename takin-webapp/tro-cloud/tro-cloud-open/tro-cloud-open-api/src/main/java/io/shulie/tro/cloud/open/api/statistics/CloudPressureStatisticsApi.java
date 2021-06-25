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

package io.shulie.tro.cloud.open.api.statistics;

import java.util.List;

import io.shulie.tro.cloud.open.req.statistics.PressureTotalReq;
import io.shulie.tro.cloud.open.resp.statistics.PressureListTotalResp;
import io.shulie.tro.cloud.open.resp.statistics.PressurePieTotalResp;
import io.shulie.tro.cloud.open.resp.statistics.ReportTotalResp;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.diff.api.statistics
 * @date 2020/11/30 9:53 下午
 */
public interface CloudPressureStatisticsApi {
    /**
     * 统计场景分类，脚本类型，返回饼状图数据
     * @param input
     * @return
     */
    PressurePieTotalResp getPressurePieTotal(PressureTotalReq input);

    /**
     * 统计报告通过/未通过
     * @param input
     * @return
     */
    ReportTotalResp getReportTotal(PressureTotalReq input);

    /**
     * 压测场景次数统计 && 压测脚本次数统计
     * @param input
     * @return
     */
    List<PressureListTotalResp> getPressureListTotal(PressureTotalReq input);
}
