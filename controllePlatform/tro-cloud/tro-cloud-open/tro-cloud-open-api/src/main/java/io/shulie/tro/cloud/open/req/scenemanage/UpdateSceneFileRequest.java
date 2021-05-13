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

package io.shulie.tro.cloud.open.req.scenemanage;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.shulie.tro.cloud.common.constants.ValidConstants;
import io.shulie.tro.cloud.common.pojo.dto.scenemanage.UploadFileDTO;
import io.shulie.tro.cloud.open.req.HttpCloudRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liuchuan
 * @date 2021/4/25 10:16 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("请求类-更新场景对应的脚本文件")
public class UpdateSceneFileRequest extends HttpCloudRequest {

    @ApiModelProperty("脚本实例id")
    @NotNull(message = "脚本id" + ValidConstants.MUST_NOT_BE_NULL)
    private Long scriptId;

    @ApiModelProperty("脚本类型")
    @NotNull(message = "脚本类型" + ValidConstants.MUST_NOT_BE_NULL)
    private Integer scriptType;

    @ApiModelProperty("上传文件")
    @NotEmpty(message = "上传文件不能为空")
    private List<UploadFileDTO> uploadFiles;

}
