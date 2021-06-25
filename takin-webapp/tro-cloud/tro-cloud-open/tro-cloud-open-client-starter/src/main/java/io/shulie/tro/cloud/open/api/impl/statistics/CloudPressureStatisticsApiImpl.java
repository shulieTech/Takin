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

package io.shulie.tro.cloud.open.api.impl.statistics;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import io.shulie.tro.cloud.open.api.impl.CloudCommonApi;
import io.shulie.tro.cloud.open.api.statistics.CloudPressureStatisticsApi;
import io.shulie.tro.cloud.open.constant.CloudApiConstant;
import io.shulie.tro.cloud.open.req.statistics.PressureTotalReq;
import io.shulie.tro.cloud.open.resp.statistics.PressureListTotalResp;
import io.shulie.tro.cloud.open.resp.statistics.PressurePieTotalResp;
import io.shulie.tro.cloud.open.resp.statistics.ReportTotalResp;
import io.shulie.tro.utils.http.HttpHelper;
import io.shulie.tro.utils.http.TroResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.tro.properties.TroCloudClientProperties;
import org.springframework.stereotype.Service;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.diff.api.statistics
 * @date 2020/11/30 9:53 下午
 */
@Service
public class CloudPressureStatisticsApiImpl extends CloudCommonApi implements CloudPressureStatisticsApi {

    @Autowired
    private TroCloudClientProperties troCloudClientProperties;


    @Override
    public PressurePieTotalResp getPressurePieTotal(PressureTotalReq req) {
        TroResponseEntity<PressurePieTotalResp> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.STATISTIC_PRESSUREPIE_URL,
                getHeaders(req.getLicense()),req,new TypeReference<PressurePieTotalResp>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return new PressurePieTotalResp();
    }

    @Override
    public ReportTotalResp getReportTotal(PressureTotalReq req) {
        TroResponseEntity<ReportTotalResp> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.STATISTIC_REPORT_URL,
                getHeaders(req.getLicense()),req,new TypeReference<ReportTotalResp>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return new ReportTotalResp();
    }

    @Override
    public List<PressureListTotalResp> getPressureListTotal(PressureTotalReq req) {
        TroResponseEntity<List<PressureListTotalResp>> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.STATISTIC_PRESSURELIST_URL,
                getHeaders(req.getLicense()),req,new TypeReference<List<PressureListTotalResp>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return Lists.newArrayList();
    }

}
