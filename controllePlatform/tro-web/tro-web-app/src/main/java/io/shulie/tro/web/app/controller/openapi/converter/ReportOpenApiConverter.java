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

package io.shulie.tro.web.app.controller.openapi.converter;

import java.util.List;

import com.pamirs.tro.entity.domain.dto.report.BusinessActivityDTO;
import com.pamirs.tro.entity.domain.vo.report.ReportQueryParam;
import com.pamirs.tro.entity.domain.vo.report.ReportTrendQueryParam;
import com.pamirs.tro.entity.domain.vo.sla.WarnQueryParam;
import io.shulie.tro.web.app.controller.openapi.request.report.ReportQueryOpenApiReq;
import io.shulie.tro.web.app.controller.openapi.request.report.ReportTrendQueryOpenApiReq;
import io.shulie.tro.web.app.controller.openapi.request.report.WarnQueryOpenApiReq;
import io.shulie.tro.web.app.controller.openapi.response.report.BusinessActivityOpenApiResp;
import org.apache.commons.lang.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author zhaoyong
 */
@Mapper(imports = {StringUtils.class})
public interface ReportOpenApiConverter {

    ReportOpenApiConverter INSTANCE = Mappers.getMapper(ReportOpenApiConverter.class);

    ReportQueryParam ofReportQueryOpenApiReq(ReportQueryOpenApiReq reportQueryOpenApiReq);



    ReportTrendQueryParam ofReportTrendQueryOpenApiReq(ReportTrendQueryOpenApiReq reportTrendQueryOpenApiReq);


    WarnQueryParam ofWarnQueryParam(WarnQueryOpenApiReq warnQueryOpenApiReq);



    List<BusinessActivityOpenApiResp> ofLsitBusinessActivityOpenApiResp(List<BusinessActivityDTO> data);
}
