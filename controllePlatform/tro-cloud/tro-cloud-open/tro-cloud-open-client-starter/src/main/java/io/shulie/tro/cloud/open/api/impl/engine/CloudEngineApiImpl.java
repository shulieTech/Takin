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

package io.shulie.tro.cloud.open.api.impl.engine;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import io.shulie.tro.cloud.open.api.engine.CloudEngineApi;
import io.shulie.tro.cloud.open.api.impl.CloudCommonApi;
import io.shulie.tro.cloud.open.constant.CloudApiConstant;
import io.shulie.tro.cloud.open.req.engine.EnginePluginDetailsWrapperReq;
import io.shulie.tro.cloud.open.req.engine.EnginePluginFetchWrapperReq;
import io.shulie.tro.cloud.open.resp.engine.EnginePluginDetailResp;
import io.shulie.tro.cloud.open.resp.engine.EnginePluginSimpleInfoResp;
import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.utils.http.HttpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.tro.properties.TroCloudClientProperties;
import org.springframework.stereotype.Component;

/**
 * 引擎接口实现
 *
 * @author lipeng
 * @date 2021-01-20 3:33 下午
 */
@Component
public class CloudEngineApiImpl extends CloudCommonApi implements CloudEngineApi {

    @Autowired
    private TroCloudClientProperties troCloudClientProperties;

    /**
     * 根据插件类型获取插件列表
     *
     * @param request
     * @return
     */
    @Override
    public ResponseResult<Map<String, List<EnginePluginSimpleInfoResp>>> listEnginePlugins(
        EnginePluginFetchWrapperReq request) {
        return HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.ENGINE_FETCH_PLUGINS_URI,
            getHeaders(request.getLicense()),
            new TypeReference<ResponseResult<Map<String, List<EnginePluginSimpleInfoResp>>>>() {}, request).getBody();
    }

    /**
     * 根据插件ID获取插件详情
     *
     * @param request
     * @return
     */
    @Override
    public ResponseResult<EnginePluginDetailResp> getEnginePluginDetails(EnginePluginDetailsWrapperReq request) {
        return HttpHelper.doPost(troCloudClientProperties.getUrl() + CloudApiConstant.ENGINE_FETCH_PLUGIN_DETAILS_URI,
            getHeaders(request.getLicense()), new TypeReference<ResponseResult<EnginePluginDetailResp>>() {}, request)
            .getBody();
    }

}
