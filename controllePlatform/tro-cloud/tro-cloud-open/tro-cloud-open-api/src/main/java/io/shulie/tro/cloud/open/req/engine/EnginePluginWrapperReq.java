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

package io.shulie.tro.cloud.open.req.engine;

import io.shulie.tro.cloud.common.bean.file.FileManageInfo;
import io.shulie.tro.cloud.open.req.HttpCloudRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 引擎插件保存入参
 *
 * @author lipeng
 * @date 2021-01-12 11:05 上午
 */
@Data
@ApiModel("引擎插件保存入参")
public class EnginePluginWrapperReq extends HttpCloudRequest implements Serializable {

    @ApiModelProperty(value = "插件ID", dataType = "long", notes = "选填，修改需要传此项")
    private Long pluginId;

    @ApiModelProperty(value="插件名称", dataType = "string")
    private String pluginName;

    @ApiModelProperty(value="插件类型", dataType = "string")
    private String pluginType;

    @ApiModelProperty(value="插件上传地址", dataType = "string")
    private String pluginUploadPath;

    @ApiModelProperty(value = "支持的版本号", dataType = "array")
    private List<String> supportedVersions;

    @ApiModelProperty(value = "上传的文件列表", dataType = "array")
    private List<FileManageInfo> uploadFiles;

}
