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

package io.shulie.tro.web.app.service.report;

import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.pamirs.tro.entity.domain.dto.report.ReportTraceDTO;

/**
 * @ClassName ReportRealTimeService
 * @Description
 * @Author qianshui
 * @Date 2020/8/17 下午8:22
 */
public interface ReportRealTimeService {

    PageInfo<ReportTraceDTO> getReportLinkList(Long sceneId, Long startTime, Integer type, int current, int pageSize);

    PageInfo<ReportTraceDTO> getReportLinkListByReportId(Long reportId, Integer type, int current, int pageSize);

    /**
     * 增加层级
     *
     * @param traceId
     * @param id
     * @return
     */
    Map<String, Object> getLinkDetail(String traceId, Integer id);
}
