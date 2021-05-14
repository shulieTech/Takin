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

package io.shulie.tro.cloud.biz.output.engine;

import io.shulie.tro.cloud.data.model.mysql.EnginePluginEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 引擎插件详情
 *
 * @author lipeng
 * @date 2021-01-14 3:48 下午
 */
@Data
@ApiModel("引擎插件详情出参")
public class EnginePluginDetailOutput implements Serializable {

    @ApiModelProperty("插件ID")
    private Long pluginId;

    @ApiModelProperty("插件类型")
    private String pluginType;

    @ApiModelProperty("插件名称")
    private String pluginName;

    @ApiModelProperty("支持的版本号")
    private List<String> supportedVersions;

    @ApiModelProperty("上传的文件信息")
    private List<EnginePluginFileOutput> uploadFiles;

    /**
     * 创建实例
     *
     * @param enginePlugin
     * @param supportedVersions
     * @param uploadFiles
     * @return
     */
    public static EnginePluginDetailOutput create(EnginePluginEntity enginePlugin, List<String> supportedVersions, List<EnginePluginFileOutput> uploadFiles) {
        EnginePluginDetailOutput instance = new EnginePluginDetailOutput();
        instance.setPluginId(enginePlugin.getId());
        instance.setPluginName(enginePlugin.getPluginName());
        instance.setPluginType(enginePlugin.getPluginType());
        instance.setSupportedVersions(supportedVersions);
        instance.setUploadFiles(uploadFiles);
        return instance;
    }
}