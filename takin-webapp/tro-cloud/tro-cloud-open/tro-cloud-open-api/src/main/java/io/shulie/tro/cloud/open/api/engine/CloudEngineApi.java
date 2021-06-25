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

package io.shulie.tro.cloud.open.api.engine;

import io.shulie.tro.cloud.open.req.engine.EnginePluginDetailsWrapperReq;
import io.shulie.tro.cloud.open.req.engine.EnginePluginFetchWrapperReq;
import io.shulie.tro.cloud.open.resp.engine.EnginePluginDetailResp;
import io.shulie.tro.cloud.open.resp.engine.EnginePluginSimpleInfoResp;
import io.shulie.tro.common.beans.response.ResponseResult;

import java.util.List;
import java.util.Map;

/**
 * 压测引擎Api
 *
 * @author lipeng
 * @date 2021-01-18 5:13 下午
 */
public interface CloudEngineApi {

    /**
     * 根据插件类型获取插件列表
     *
     * @param request
     * @return
     */
    ResponseResult<Map<String, List<EnginePluginSimpleInfoResp>>> listEnginePlugins(EnginePluginFetchWrapperReq request);

    /**
     * 根据插件ID获取插件详情
     *
     * @param request
     * @return
     */
    ResponseResult<EnginePluginDetailResp> getEnginePluginDetails(EnginePluginDetailsWrapperReq request);
}