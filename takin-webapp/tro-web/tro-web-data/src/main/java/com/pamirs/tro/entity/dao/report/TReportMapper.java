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

import com.pamirs.tro.entity.domain.entity.report.Report;
import com.pamirs.tro.entity.domain.vo.report.ReportQueryParam;
import org.apache.ibatis.annotations.Param;

public interface TReportMapper {
    int insertSelective(Report record);

    int updateByPrimaryKeySelective(Report record);

    Report selectByPrimaryKey(Long id);

    /**
     * 报表列表
     *
     * @param param
     * @return
     */
    List<Report> listReport(@Param("param") ReportQueryParam param);

    /**
     * 获取已经生成报告的场景ID
     *
     * @param sceneIds
     * @return
     */
    List<Report> listReportSceneIds(@Param("sceneIds") List<Long> sceneIds);

    /**
     * 根据场景ID获取压测中的报告ID
     *
     * @param sceneId
     * @return
     */
    Report getReportBySceneId(Long sceneId);

    /**
     * 根据场景ID获取（临时）压测中的报告ID
     *
     * @param sceneId
     * @return
     */
    Report getTempReportBySceneId(Long sceneId);
}
