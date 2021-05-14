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

package io.shulie.tro.cloud.open.entrypoint.convert;

import io.shulie.tro.cloud.biz.input.scenemanage.SceneManageWrapperInput;
import io.shulie.tro.cloud.biz.output.scenemanage.SceneStartTrialRunOutput;
import io.shulie.tro.cloud.open.req.scenetask.TaskFlowDebugStartReq;
import io.shulie.tro.cloud.open.resp.scenetask.SceneActionResp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author zhaoyong
 */
@Mapper
public interface SceneTaskOpenConverter {

    SceneTaskOpenConverter INSTANCE = Mappers.getMapper(SceneTaskOpenConverter.class);

    /**
     * 启动日志返回结果转换
     * @param sceneStartTrialRunOutput
     * @return
     */
    SceneActionResp ofSceneStartTrialRunOutput(SceneStartTrialRunOutput sceneStartTrialRunOutput);

    /**
     * 入参转换
     * @param taskFlowDebugStartReq
     * @return
     */
    SceneManageWrapperInput ofTaskDebugDataStartReq(TaskFlowDebugStartReq taskFlowDebugStartReq);



}
