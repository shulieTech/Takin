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

import io.shulie.tro.web.data.param.machine.PressureMachineLogInsertParam;
import io.shulie.tro.web.data.param.machine.PressureMachineLogQueryParam;
import io.shulie.tro.web.data.result.perfomanceanaly.PressureMachineLogResult;

import java.util.List;

/**
 * @Author: mubai
 * @Date: 2020-11-16 10:19
 * @Description:
 */
public interface PressureMachineLogDao {

    void insert(PressureMachineLogInsertParam param);

    List<PressureMachineLogResult> queryList(PressureMachineLogQueryParam queryParam);

    // 清理指定时间之前的数据
    void clearRubbishData(String time);

}
