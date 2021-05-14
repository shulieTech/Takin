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

package io.shulie.tro.web.diff.cloud.impl.statistics;

import java.util.List;

import io.shulie.tro.cloud.open.api.statistics.CloudPressureStatisticsApi;
import io.shulie.tro.cloud.open.req.statistics.PressureTotalReq;
import io.shulie.tro.cloud.open.resp.statistics.PressureListTotalResp;
import io.shulie.tro.cloud.open.resp.statistics.PressurePieTotalResp;
import io.shulie.tro.cloud.open.resp.statistics.ReportTotalResp;
import io.shulie.tro.web.common.constant.RemoteConstant;
import io.shulie.tro.web.diff.api.statistics.PressureStatisticsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.diff.cloud.impl.statistics
 * @date 2020/11/30 9:50 下午
 */
@Service
public class PressureStatisticsApiImpl implements PressureStatisticsApi {

    @Autowired
    private CloudPressureStatisticsApi cloudPressureStatisticsApi;
    @Override
    public PressurePieTotalResp getPressurePieTotal(PressureTotalReq req) {
        req.setLicense(RemoteConstant.LICENSE_VALUE);
        return cloudPressureStatisticsApi.getPressurePieTotal(req);
    }

    @Override
    public ReportTotalResp getReportTotal(PressureTotalReq req) {
        req.setLicense(RemoteConstant.LICENSE_VALUE);
        return cloudPressureStatisticsApi.getReportTotal(req);
    }

    @Override
    public List<PressureListTotalResp> getPressureListTotal(PressureTotalReq req) {
        req.setLicense(RemoteConstant.LICENSE_VALUE);
        return cloudPressureStatisticsApi.getPressureListTotal(req);
    }
}
