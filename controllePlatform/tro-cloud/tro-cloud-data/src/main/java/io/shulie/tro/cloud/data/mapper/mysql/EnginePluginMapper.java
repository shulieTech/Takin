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

package io.shulie.tro.cloud.data.mapper.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.shulie.tro.cloud.data.model.mysql.EnginePluginEntity;
import io.shulie.tro.cloud.data.result.engine.EnginePluginSimpleInfoResult;

import java.util.List;
import java.util.Map;

/**
 * 引擎Mapper
 *
 * @author lipeng
 * @date 2021-01-06 3:17 下午
 */
public interface EnginePluginMapper extends BaseMapper<EnginePluginEntity> {

    /**
     * 获取可用的插件列表
     *
     * @param pluginTypes 插件类型
     *
     * @return
     */
    List<EnginePluginSimpleInfoResult> selectAvailablePluginsByType(List<String> pluginTypes);

    /**
     * 根据插件id获取插件支持的版本信息
     *
     * @param pluginId
     * @return
     */
    List<Map> selectEnginePluginSupportedVersions(Long pluginId);

}
