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

import java.util.Date;
import java.util.List;

import com.pamirs.tro.entity.domain.entity.report.Report;
import com.pamirs.tro.entity.domain.vo.report.ReportQueryParam;
import io.shulie.tro.cloud.common.annotation.DataApartInterceptAnnotation;
import io.shulie.tro.cloud.common.bean.scenemanage.UpdateStatusBean;
import org.apache.ibatis.annotations.Param;

public interface TReportMapper {
    int insertSelective(Report record);

    @DataApartInterceptAnnotation
    Report selectOneRunningReport();

    @DataApartInterceptAnnotation
    List<Report> selectListRunningReport();

    int updateByPrimaryKeySelective(Report record);

    Report selectByPrimaryKey(Long id);

    int updateReportStatus(UpdateStatusBean updateStatus);


    int updateReportLock(UpdateStatusBean updateStatus);

    /**
     * 报表列表
     *
     * @param param
     * @return
     */
    @DataApartInterceptAnnotation
    List<Report> listReport(@Param("param") ReportQueryParam param);

    /**
     * 获取已经生成报告的场景ID
     *
     * @param sceneIds
     * @return
     */
    List<Report> listReportSceneIds(@Param("sceneIds") List<Long> sceneIds);



    int resumeStatus(Long sceneId);

    /**
     * 引擎启动，才更新开始时间
     *
     * @param id
     * @param startTime
     * @return
     */
    int updateStartTime(@Param("id") Long id, @Param("startTime") Date startTime);


    int updateReportUserById(@Param("id") Long id, @Param("userId") Long userId);
}
