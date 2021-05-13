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

package com.pamirs.tro.entity.domain.vo.file;

import java.io.Serializable;

import io.shulie.tro.web.common.domain.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName FileDeleteVO
 * @Description
 * @Author qianshui
 * @Date 2020/4/20 上午11:19
 */
@Data
@ApiModel(description = "文件删除入参")
public class FileDeleteVO extends WebRequest implements Serializable {

    private static final long serialVersionUID = 2147406912228264446L;

    @ApiModelProperty(value = "上传文件ID")
    private String uploadId;

    @ApiModelProperty(value = "Topic")
    private String topic;
}
