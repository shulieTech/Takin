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

package io.shulie.tro.cloud.common.bean.file;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件管理请求
 *
 * @author lipeng
 * @date 2021-01-13 5:41 下午
 */
@Data
@ApiModel("文件请求信息")
public class FileManageInfo {

    //数据库字表id
    @ApiModelProperty(value = "文件ID", dataType = "long")
    private Long fileId;

    @ApiModelProperty(value = "上传文件ID", dataType = "string")
    private String uploadId;

    @ApiModelProperty(value = "文件名称", dataType = "string")
    private String fileName;

    @ApiModelProperty(value = "文件是否删除", dataType = "int")
    private Integer isDeleted;
}
