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

package io.shulie.tro.web.app.response.scriptmanage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.shulie.tro.web.app.response.filemanage.FileManageResponse;
import lombok.Data;

/**
 * @author shulie
 */
@Data
public class ScriptManageDeployDetailResponse implements Serializable {
    private static final long serialVersionUID = 573256048970054542L;

    /**
     * 脚本发布id
     */
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
     * 关联值
     */
    @JsonProperty("relatedId")
    private String refValue;

    /**
     * 关联值名称（此处为关联活动名称或关联流程名称）
     */
    @JsonProperty("relatedBusiness")
    private String refName;

    /**
     * 脚本类型;0为jmeter脚本
     */
    @JsonProperty("scriptType")
    private Integer type;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updateTime")
    private Date gmtUpdate;

    /**
     * 脚本版本
     */
    private Integer scriptVersion;

    /**
     * 文件列表
     */
    @JsonProperty("relatedFiles")
    private List<FileManageResponse> fileManageResponseList;

    /**
     * 附件列表
     */
    @JsonProperty("relatedAttachments")
    private List<FileManageResponse> attachmentManageResponseList;

    /**
     * 引擎插件列表
     */
    @JsonProperty("pluginConfigs")
    private List<PluginConfigDetailResponse> pluginConfigDetailResponseList;

}
