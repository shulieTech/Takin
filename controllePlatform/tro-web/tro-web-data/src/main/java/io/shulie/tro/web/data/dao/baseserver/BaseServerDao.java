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

package io.shulie.tro.web.data.dao.baseserver;

import java.util.Collection;
import java.util.List;

import io.shulie.tro.web.data.param.baseserver.BaseServerParam;
import io.shulie.tro.web.data.param.baseserver.InfluxAvgParam;
import io.shulie.tro.web.data.param.baseserver.ProcessBaseRiskParam;
import io.shulie.tro.web.data.param.baseserver.ProcessOverRiskParam;
import io.shulie.tro.web.data.param.baseserver.TimeMetricsDetailParam;
import io.shulie.tro.web.data.param.baseserver.TimeMetricsParam;
import io.shulie.tro.web.data.result.baseserver.BaseServerResult;
import io.shulie.tro.web.data.result.baseserver.InfluxAvgResult;
import io.shulie.tro.web.data.result.baseserver.LinkDetailResult;
import io.shulie.tro.web.data.result.risk.BaseRiskResult;
import io.shulie.tro.web.data.result.risk.LinkDataResult;

/**
 * @Author: mubai
 * @Date: 2020-10-26 15:47
 * @Description:
 */
public interface BaseServerDao {

    Collection<BaseServerResult> queryList(BaseServerParam param);

    Collection<BaseServerResult> queryBaseServer(BaseServerParam param);

    Collection<InfluxAvgResult> queryTraceId(InfluxAvgParam param);

    LinkDetailResult queryTimeMetricsDetail(TimeMetricsDetailParam param);

    LinkDataResult queryTimeMetrics(TimeMetricsParam param);

    List<BaseRiskResult> queryProcessBaseRisk(ProcessBaseRiskParam param);
    List<BaseRiskResult> queryProcessOverRisk(ProcessOverRiskParam param);

    Collection<BaseServerResult> queryBaseData(BaseServerParam param);
}
