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

package io.shulie.tro.web.data.dao.perfomanceanaly;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.data.param.perfomanceanaly.PressureMachineStatisticsInsertParam;
import io.shulie.tro.web.data.param.perfomanceanaly.PressureMachineStatisticsQueryParam;
import io.shulie.tro.web.data.result.perfomanceanaly.PressureMachineResult;
import io.shulie.tro.web.data.result.perfomanceanaly.PressureMachineStatisticsResult;

import java.util.List;

/**
 * @Author: mubai
 * @Date: 2020-11-13 11:38
 * @Description:
 */
public interface PressureMachineStatisticsDao {

    void insert(PressureMachineStatisticsInsertParam param);

    List<PressureMachineStatisticsResult> queryByExample(PressureMachineStatisticsQueryParam param);

    PressureMachineStatisticsResult getNewlyStatistics();

    PressureMachineStatisticsResult statistics();

    //清理90天之前的数据
    void clearRubbishData(String time);


}
