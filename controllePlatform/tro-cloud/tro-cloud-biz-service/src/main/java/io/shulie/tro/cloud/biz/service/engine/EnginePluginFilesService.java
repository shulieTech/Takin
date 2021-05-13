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

import io.shulie.tro.cloud.biz.output.engine.EnginePluginFileOutput;
import io.shulie.tro.cloud.common.bean.file.FileManageInfo;

import java.util.List;
import java.util.Map;

/**
 * 引擎插件文件信息接口
 *
 * @author lipeng
 * @date 2021-01-13 5:26 下午
 */
public interface EnginePluginFilesService {

    /**
     * 根据插件id获取所有文件路径
     *
     * @param pluginIds
     */
    List<String> findPluginFilesPathByPluginIds(List<Long> pluginIds);

    /**
     * 根据插件id获取文件信息
     *
     * @param pluginId
     */
    List<EnginePluginFileOutput> findPluginFilesInfoByPluginId(Long pluginId);

    /**
     * 批量保存引擎插件文件信息
     *
     * @param pluginId 插件ID
     * @param files 文件信息
     */
    void batchSaveEnginePluginFiles(List<FileManageInfo> files, Long pluginId);
}
