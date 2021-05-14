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

package io.shulie.tro.cloud.open.api.impl.scenetask;

import com.fasterxml.jackson.core.type.TypeReference;
import io.shulie.tro.cloud.open.api.impl.CloudCommonApi;
import io.shulie.tro.cloud.open.api.scenetask.CloudTaskApi;
import io.shulie.tro.cloud.open.constant.CloudApiConstant;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageIdReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneTaskStartReq;
import io.shulie.tro.cloud.open.req.scenetask.SceneTaskQueryTpsReq;
import io.shulie.tro.cloud.open.req.scenetask.SceneTaskUpdateTpsReq;
import io.shulie.tro.cloud.open.resp.scenetask.SceneActionResp;
import io.shulie.tro.cloud.open.req.scenetask.TaskFlowDebugStartReq;
import io.shulie.tro.cloud.open.resp.scenetask.SceneTaskAdjustTpsResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.http.HttpHelper;
import io.shulie.tro.utils.http.TroResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.tro.properties.TroCloudClientProperties;
import org.springframework.stereotype.Component;

/**
 * @ClassName CloudTaskApiImpl
 * @Description
 * @Author qianshui
 * @Date 2020/11/13 上午11:06
 */
@Component
public class CloudTaskApiImpl extends CloudCommonApi implements CloudTaskApi {

    @Autowired
    private TroCloudClientProperties troCloudClientProperties;

    @Override
    public ResponseResult<SceneActionResp> start(SceneTaskStartReq req) {
        TroResponseEntity<ResponseResult<SceneActionResp>> troResponseEntity =
            HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_TASK_START,
                getHeaders(req.getLicense()),new TypeReference<ResponseResult<SceneActionResp>>() {},req);
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<String> stopTask(SceneManageIdReq req) {
        TroResponseEntity<ResponseResult<String>> troResponseEntity =
            HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_TASK_STOP,
                getHeaders(req.getLicense()),new TypeReference<ResponseResult<String>>() {},req);
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");

    }

    @Override
    public ResponseResult<SceneActionResp> checkTask(SceneManageIdReq req) {
        TroResponseEntity<ResponseResult<SceneActionResp>> troResponseEntity =
            HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_TASK_CHECK,
                getHeaders(req.getLicense()),req,new TypeReference<ResponseResult<SceneActionResp>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<String> updateSceneTaskTps(SceneTaskUpdateTpsReq sceneTaskUpdateTpsReq) {
        TroResponseEntity<ResponseResult<String>> troResponseEntity =
            HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_TASK_UPDATE_TPS,
                getHeaders(sceneTaskUpdateTpsReq.getLicense()), new TypeReference<ResponseResult<String>>() {},
                sceneTaskUpdateTpsReq);
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
            troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<SceneTaskAdjustTpsResp> queryAdjustTaskTps(SceneTaskQueryTpsReq req) {
        TroResponseEntity<ResponseResult<SceneTaskAdjustTpsResp>> troResponseEntity =
                HttpHelper.doGet(troCloudClientProperties.getUrl() + CloudApiConstant.SCENE_TASK_QUERY_ADJUST_TPS,
                        getHeaders(req.getLicense()),req,new TypeReference<ResponseResult<SceneTaskAdjustTpsResp>>() {});
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
                troResponseEntity.getErrorMsg(),"查看cloud日志");
    }

    @Override
    public ResponseResult<Long> startFlowDebugTask(TaskFlowDebugStartReq req) {
        TroResponseEntity<ResponseResult<Long>> troResponseEntity =
                HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.START_FLOW_DEBUG_TASK,
                        getHeaders(req.getLicense()),new TypeReference<ResponseResult<Long>>() {},req);
        if(troResponseEntity.getSuccess()) {
            return troResponseEntity.getBody();
        }
        return ResponseResult.fail(troResponseEntity.getHttpStatus().toString(),
                troResponseEntity.getErrorMsg(),"查看cloud日志");
    }
}
