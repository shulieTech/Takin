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

package io.shulie.tro.cloud.biz.service.engine;

import io.shulie.tro.cloud.biz.input.engine.EnginePluginWrapperInput;
import io.shulie.tro.cloud.biz.output.engine.EnginePluginDetailOutput;
import io.shulie.tro.cloud.biz.output.engine.EnginePluginSimpleInfoOutput;
import io.shulie.tro.cloud.data.model.mysql.EnginePluginEntity;
import io.shulie.tro.cloud.data.result.engine.EnginePluginSimpleInfoResult;
import io.shulie.tro.common.beans.response.ResponseResult;

import java.util.List;
import java.util.Map;

/**
 * 引擎接口
 *
 * @author lipeng
 * @date 2021-01-06 3:07 下午
 */
public interface EnginePluginService {

    /**
     * 查询引擎支持的插件信息
     *
     * @param pluginTypes 插件类型
     *
     * @return
     */
    Map<String, List<EnginePluginSimpleInfoOutput>> findEngineAvailablePluginsByType(List<String> pluginTypes);

    /**
     * 根据插件ID获取插件详情信息
     *
     * @param pluginId
     * @return
     */
    ResponseResult<EnginePluginDetailOutput> findEnginePluginDetailss(Long pluginId);

    /**
     * 保存引擎插件
     *
     * @param input
     * @return
     */
    void saveEnginePlugin(EnginePluginWrapperInput input);

    /**
     *
     *
     * @param pluginId
     * @param status
     * @return
     */
    void changeEnginePluginStatus(Long pluginId, Integer status);

}
