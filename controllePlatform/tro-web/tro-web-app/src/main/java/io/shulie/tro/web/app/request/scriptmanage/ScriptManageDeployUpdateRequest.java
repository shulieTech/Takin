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

package io.shulie.tro.web.app.request.scriptmanage;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.shulie.tro.web.app.request.filemanage.FileManageUpdateRequest;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
public class ScriptManageDeployUpdateRequest implements Serializable {
    private static final long serialVersionUID = 459472161282660909L;

    /**
     * 脚本实例id(看使用情况, 是表 script_manage_deploy 的 id)
     */
    @JsonProperty("scriptId")
    private Long id;
    /**
     * 名称
     */
    @JsonProperty("scriptName")
    private String name;

    /**
     * 关联类型(业务活动)
     */
    @JsonProperty("relatedType")
    private String refType;

    /**
     * 关联值(活动id)
     */
    @JsonProperty("relatedId")
    private String refValue;

    /**
     * 脚本类型;0为jmeter脚本
     */
    @JsonProperty("scriptType")
    private Integer type;

    /**
     * 关联文件列表
     */
    @JsonProperty("uploadFiles")
    private List<FileManageUpdateRequest> fileManageUpdateRequests;

    /**
     * 关联附件列表
     */
    @JsonProperty("uploadAttachments")
    private List<FileManageUpdateRequest> attachmentManageUpdateRequests;

    /**
     * 引擎插件列表
     */
    @JsonProperty("pluginConfigs")
    private List<PluginConfigUpdateRequest> pluginConfigUpdateRequests;
}
