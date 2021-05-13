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

package io.shulie.tro.cloud.open.req.scenetask;

import io.shulie.tro.cloud.common.bean.TimeBean;
import io.shulie.tro.cloud.open.req.HttpCloudRequest;
import io.shulie.tro.cloud.open.req.scenemanage.SceneBusinessActivityRefOpen;
import io.shulie.tro.cloud.open.req.scenemanage.SceneScriptRefOpen;
import io.shulie.tro.cloud.open.req.scenemanage.SceneSlaRefOpen;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @author zhaoyong
 */
@Data
public class TaskFlowDebugStartReq extends HttpCloudRequest implements Serializable {
    private static final long serialVersionUID = -9162208161836587615L;

    @ApiModelProperty(value = "客户Id")
    private Long customId;

    @ApiModelProperty(value = "业务活动配置")
    @NotEmpty(message = "业务活动配置不能为空")
    private List<SceneBusinessActivityRefOpen> businessActivityConfig;

    @ApiModelProperty(value = "脚本类型")
    @NotNull(message = "脚本类型不能为空")
    private Integer scriptType;

    @ApiModelProperty(name = "uploadFile", value = "压测脚本/文件")
    @NotEmpty(message = "压测脚本/文件不能为空")
    private List<SceneScriptRefOpen> uploadFile;

    /**
     * 关联到的插件id
     */
    private List<Long> enginePluginIds;

    /**
     * 扩展字段
     */
    private String features;

    private Long scriptId;
}
