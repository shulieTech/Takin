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

package io.shulie.tro.cloud.open.api.scenetask;

import io.shulie.tro.cloud.open.req.scenemanage.SceneManageIdReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneTaskStartReq;
import io.shulie.tro.cloud.open.req.scenetask.SceneTaskQueryTpsReq;
import io.shulie.tro.cloud.open.req.scenetask.SceneTaskUpdateTpsReq;
import io.shulie.tro.cloud.open.resp.scenetask.SceneActionResp;
import io.shulie.tro.cloud.open.req.scenetask.TaskFlowDebugStartReq;
import io.shulie.tro.cloud.open.resp.scenetask.SceneTaskAdjustTpsResp;
import io.shulie.tro.common.beans.response.ResponseResult;

/**
 * @ClassName CloudTaskApi
 * @Description 压测任务
 * @Author qianshui
 * @Date 2020/11/13 上午11:05
 */
public interface CloudTaskApi {

    /**
     * 启动压测
     * @param req
     * @return
     */
    ResponseResult<SceneActionResp> start(SceneTaskStartReq req);

    ResponseResult<String> stopTask(SceneManageIdReq req);


    ResponseResult<SceneActionResp> checkTask(SceneManageIdReq req);

    /**
     * 更新压测场景任务tps
     * @param sceneTaskUpdateTpsReq
     * @return
     */
    ResponseResult<String> updateSceneTaskTps(SceneTaskUpdateTpsReq sceneTaskUpdateTpsReq);

    /**
     * 获取调整前tps
     * @param sceneTaskQueryTpsReq
     * @return
     */
    ResponseResult<SceneTaskAdjustTpsResp> queryAdjustTaskTps(SceneTaskQueryTpsReq sceneTaskQueryTpsReq);

    /**
     * 启动流量调试任务
     * @param taskFlowDebugStartReq
     * @return
     */
    ResponseResult<Long> startFlowDebugTask (TaskFlowDebugStartReq taskFlowDebugStartReq);
}
