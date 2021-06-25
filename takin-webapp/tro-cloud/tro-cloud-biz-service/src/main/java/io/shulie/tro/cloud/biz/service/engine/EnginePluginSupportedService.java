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


import java.util.List;

/**
 * 引擎插件支持接口
 *
 * @author lipeng
 * @date 2021-01-12 4:36 下午
 */
public interface EnginePluginSupportedService {

    /**
     * 根据插件id移除支持的版本
     *
     * @param pluginId 插件id
     */
    void removeSupportedVersionsByPluginId(Long pluginId);

    /**
     * 批量保存支持的插件版本
     *
     * @param supportedVersions 支持的插件版本信息
     * @param pluginId 插件id
     */
    void batchSaveSupportedVersions(List<String> supportedVersions, Long pluginId);

    /**
     * 根据插件id获取支持的版本号
     *
     * @param pluginId
     * @return
     */
    List<String> findSupportedVersionsByPluginId(Long pluginId);
}
