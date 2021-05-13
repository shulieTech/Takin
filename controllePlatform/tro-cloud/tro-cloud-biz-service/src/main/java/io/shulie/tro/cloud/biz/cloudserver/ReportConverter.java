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

package io.shulie.tro.cloud.biz.cloudserver;

import java.util.List;

import com.pamirs.tro.entity.domain.bo.scenemanage.WarnBO;
import com.pamirs.tro.entity.domain.dto.report.BusinessActivityDTO;
import com.pamirs.tro.entity.domain.dto.report.ReportDTO;
import com.pamirs.tro.entity.domain.entity.report.Report;
import com.pamirs.tro.entity.domain.entity.report.ReportBusinessActivityDetail;
import com.pamirs.tro.entity.domain.entity.scenemanage.WarnDetail;
import io.shulie.tro.cloud.biz.output.report.ReportDetailOutput;
import io.shulie.tro.cloud.biz.output.scenemanage.WarnDetailOutput;
import io.shulie.tro.cloud.common.bean.scenemanage.WarnBean;
import io.shulie.tro.cloud.common.utils.DateUtil;
import io.shulie.tro.cloud.data.result.report.ReportResult;
import io.shulie.tro.cloud.data.result.scenemanage.WarnDetailResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * @Author 莫问
 * @Date 2020-04-17
 */

@Mapper(imports = {DateUtil.class})
public interface ReportConverter {

    ReportConverter INSTANCE = Mappers.getMapper(ReportConverter.class);

    /**
     * Report Converter ReportDetail
     *
     * @param report
     * @return
     */
    @Mapping(target = "startTime", expression = "java(DateUtil.getDate(report.getStartTime(),\"yyyy-MM-dd HH:mm:ss\"))")
    ReportDetailOutput ofReportDetail(ReportResult report);

    /**
     * Report Converter ReportDTO
     *
     * @param report
     * @return
     */
    List<ReportDTO> ofReport(List<Report> report);

    @Mappings({
        @Mapping(target = "startTime",
            expression = "java(DateUtil.getDate(report.getStartTime(),\"yyyy-MM-dd HH:mm:ss\"))"),
        @Mapping(target = "totalTime",
            expression = "java(DateUtil.formatTestTime(report.getStartTime(), report.getEndTime()))")
    })
    ReportDTO ofReport(Report report);

    /**
     * WarnBO Converter WarnBean
     *
     * @param warn
     * @return
     */
    List<WarnBean> ofWarn(List<WarnBO> warn);

    @Mappings(
            value = {
                    @Mapping(target = "reportId", source = "ptId"),
                    @Mapping(target = "content", source = "warnContent"),
                    @Mapping(target = "warnTime",
                            expression = "java(DateUtil.getDate(warnDetail.getWarnTime(),\"yyyy-MM-dd HH:mm:ss\"))")
            }
    )
    WarnDetailOutput ofWarn(WarnDetail warnDetail);

    /**
     * WarnDetail Converter WarnDetailResult
     *
     * @param warnDetail
     * @return
     */
    List<WarnDetailOutput> ofWarnDetail(List<WarnDetail> warnDetail);

    @Mapping(target = "lastWarnTime",
        expression = "java(DateUtil.getDate(warn.getLastWarnTime(),\"yyyy-MM-dd HH:mm:ss\"))")
    WarnBean ofWarn(WarnBO warn);

    @Mappings(
        value = {
            @Mapping(target = "reportId", source = "ptId"),
            @Mapping(target = "content", source = "warnContent"),
            @Mapping(target = "warnTime",
                expression = "java(DateUtil.getDate(warnDetail.getWarnTime(),\"yyyy-MM-dd HH:mm:ss\"))")
        }
    )
    WarnDetailResult ofWarnDetail(WarnDetail warnDetail);

    /**
     * ReportBusinessActivityDetail Converter BusinessActivityDTO
     *
     * @param data
     * @return
     */
    List<BusinessActivityDTO> ofBusinessActivity(List<ReportBusinessActivityDetail> data);
}
