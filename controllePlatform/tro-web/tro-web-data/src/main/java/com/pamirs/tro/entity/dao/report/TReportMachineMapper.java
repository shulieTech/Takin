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

package com.pamirs.tro.entity.dao.report;

import java.util.List;
import java.util.Map;

import com.pamirs.tro.entity.domain.entity.report.ReportMachine;
import com.pamirs.tro.entity.domain.entity.report.TroTraceEntry;
import com.pamirs.tro.entity.domain.vo.report.ReportLocalQueryParam;
import org.apache.ibatis.annotations.Param;

public interface TReportMachineMapper {

    int insert(ReportMachine record);

    int insertOrUpdate(ReportMachine record);

    int insertList(List<ReportMachine> list);

    List<ReportMachine> selectSimpleByExample(ReportLocalQueryParam queryParam);

    ReportMachine selectByPrimaryKey(Long id);

    /**
     * 指定报告、应用、ip查询
     *
     * @param queryParam
     * @return
     */
    ReportMachine selectOneByParam(ReportLocalQueryParam queryParam);

    /**
     * 指定报告、应用，查所有ip
     *
     * @param queryParam
     * @return
     */
    List<ReportMachine> selectListByParam(ReportLocalQueryParam queryParam);

    /**
     * 分应用数、汇总总机器数、风险机器数
     *
     * @param reportId
     * @return
     */
    List<Map<String, Object>> selectCountByReport(Long reportId);

    /**
     * 更新机器tps指标数据
     *
     * @param record
     * @return
     */
    int updateTpsTargetConfig(ReportMachine record);

    /**
     * 更新机器风险数据
     *
     * @param record
     * @return
     */
    int updateRiskContent(ReportMachine record);

    void deleteByReportId(Long reportId);

    Long getTraceFailedCount(@Param("startTime") Long startTime, @Param("endTime") Long endTime);

    List<TroTraceEntry> selectTraceByTimeRange(@Param("startTime") long startTime, @Param("endTime") long endTime,
        @Param("type") Integer type, @Param("list") List<String> entryList);
}
