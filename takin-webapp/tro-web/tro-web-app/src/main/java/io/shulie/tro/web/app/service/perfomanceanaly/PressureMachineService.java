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

package io.shulie.tro.web.app.service.perfomanceanaly;

import io.shulie.tro.common.beans.page.PagingList;
import io.shulie.tro.web.app.input.PressureMachineInput;
import io.shulie.tro.web.app.output.PressureMachineOutput;
import io.shulie.tro.web.app.request.perfomanceanaly.PressureMachineDeleteRequest;
import io.shulie.tro.web.app.request.perfomanceanaly.PressureMachineUpdateRequest;
import io.shulie.tro.web.app.response.perfomanceanaly.PressureMachineResponse;
import io.shulie.tro.web.data.param.machine.PressureMachineQueryParam;

/**
 * @Author: mubai
 * @Date: 2020-11-12 21:03
 * @Description:
 */
public interface PressureMachineService {

    Long insert(PressureMachineInput input);

    void upload(PressureMachineInput input);

    PagingList<PressureMachineResponse> queryByExample(PressureMachineQueryParam param);

    void update(PressureMachineUpdateRequest request);

    void delete(PressureMachineDeleteRequest request);

    PressureMachineResponse getByIp(String ip) ;

    void updatePressureMachineStatus(Long id,Integer status);


}
