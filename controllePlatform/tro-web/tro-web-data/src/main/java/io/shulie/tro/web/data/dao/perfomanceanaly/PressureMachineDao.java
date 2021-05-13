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
import io.shulie.tro.web.data.param.machine.PressureMachineDeleteParam;
import io.shulie.tro.web.data.param.machine.PressureMachineInsertParam;
import io.shulie.tro.web.data.param.machine.PressureMachineQueryParam;
import io.shulie.tro.web.data.param.machine.PressureMachineUpdateParam;
import io.shulie.tro.web.data.result.perfomanceanaly.PressureMachineResult;

/**
 * @Author: mubai
 * @Date: 2020-11-12 20:56
 * @Description:
 */
public interface PressureMachineDao {
    /**
     * 新增机器
     * @param param
     */
    Integer insert(PressureMachineInsertParam param) ;

    /**
     * 查询机器
     * @param queryParam
     * @return
     */
    PagingList<PressureMachineResult> queryByExample(PressureMachineQueryParam queryParam);

    Integer getCountByIp (String ip);

    void update (PressureMachineUpdateParam param);

    void delete(PressureMachineDeleteParam param);

    PressureMachineResult getById(Long id);

    PressureMachineResult getByIp(String ip);

}
