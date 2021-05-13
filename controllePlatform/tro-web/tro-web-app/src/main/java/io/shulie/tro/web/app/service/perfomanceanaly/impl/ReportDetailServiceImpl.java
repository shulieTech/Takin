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

package io.shulie.tro.web.app.service.perfomanceanaly.impl;

import com.pamirs.tro.common.util.http.DateUtil;
import com.pamirs.tro.entity.domain.dto.report.ReportDetailDTO;
import io.shulie.tro.web.app.response.perfomanceanaly.ReportTimeResponse;
import io.shulie.tro.web.app.service.perfomanceanaly.ReportDetailService;
import io.shulie.tro.web.app.service.report.impl.ReportApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName ReportDetailServiceImpl
 * @Description
 * @Author qianshui
 * @Date 2020/11/9 下午4:40
 */
@Service
public class ReportDetailServiceImpl implements ReportDetailService {

    @Autowired
    private ReportApplicationService reportApplicationService;

    @Override
    public ReportTimeResponse getReportTime(Long reportId) {
        ReportDetailDTO reportDetail = reportApplicationService.getReportApplication(reportId).getReportDetail();
        ReportTimeResponse response = new ReportTimeResponse();
        response.setStartTime(reportDetail.getStartTime());
        if(reportDetail.getEndTime() != null) {
            response.setEndTime(DateUtil.getYYYYMMDDHHMMSS(reportDetail.getEndTime()));
        }
        return response;
    }
}
