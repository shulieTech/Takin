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

package io.shulie.tro.web.diff.api.scenetask;

import io.shulie.tro.cloud.open.req.report.WarnCreateReq;
import io.shulie.tro.cloud.open.req.report.UpdateReportConclusionReq;
import io.shulie.tro.cloud.open.req.scenemanage.SceneManageIdReq;
import io.shulie.tro.cloud.open.resp.scenetask.SceneActionResp;
import io.shulie.tro.common.beans.response.ResponseResult;

/**
 * @ClassName SceneTaskApi
 * @Description
 * @Author qianshui
 * @Date 2020/11/13 下午1:54
 */
public interface SceneTaskApi {

    ResponseResult stopTask(SceneManageIdReq req);

    ResponseResult<SceneActionResp> checkTask(SceneManageIdReq req);

    ResponseResult<String> updateReportStatus(UpdateReportConclusionReq req);

    ResponseResult addWarn(WarnCreateReq req);
}
