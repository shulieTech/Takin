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

package io.shulie.tro.web.diff.cloud.impl.scenetask;

import io.shulie.tro.cloud.open.api.report.CloudReportApi;
import io.shulie.tro.cloud.open.api.scenetask.CloudTaskApi;
import io.shulie.tro.cloud.open.req.report.UpdateReportConclusionReq;
import io.shulie.tro.cloud.open.req.report.WarnCreateReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageIdReq;
import io.shulie.tro.cloud.open.resp.scenetask.SceneActionResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.web.diff.api.scenetask.SceneTaskApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName SceneTaskApiImpl
 * @Description
 * @Author qianshui
 * @Date 2020/11/13 下午1:55
 */
@Service
public class SceneTaskApiImpl implements SceneTaskApi {

    @Autowired
    private CloudTaskApi cloudTaskApi;

    @Autowired
    private CloudReportApi cloudReportApi;

    @Override
    public ResponseResult stopTask(SceneManageIdReq req) {
        return cloudTaskApi.stopTask(req);
    }

    @Override
    public ResponseResult<SceneActionResp> checkTask(SceneManageIdReq req) {
        return cloudTaskApi.checkTask(req);
    }


    @Override
    public ResponseResult addWarn(WarnCreateReq req) {
        return cloudReportApi.addWarn(req);
    }

    @Override
    public ResponseResult<String> updateReportStatus(UpdateReportConclusionReq req) {
        return cloudReportApi.updateReportConclusion(req);
    }
}
