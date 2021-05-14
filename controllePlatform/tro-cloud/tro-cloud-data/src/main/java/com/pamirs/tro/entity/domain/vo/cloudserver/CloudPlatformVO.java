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

package com.pamirs.tro.entity.domain.vo.cloudserver;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-05-09 20:45
 * @Description:
 */

@Data
public class CloudPlatformVO implements Serializable {

    private static final long serialVersionUID = 5809341599213407921L;

    @ApiModelProperty(name = "id", value = "平台id")
    private Long id;

    @ApiModelProperty(name = "name", value = "平台名称")
    private String name;

    @ApiModelProperty(name = "jarName", value = "jar包名称")
    private String jarName;

    @ApiModelProperty(name = "classPath", value = "实现类全路径")
    private String classPath;

    @ApiModelProperty(name = "status", value = "是否启用")
    private Boolean status;

    @ApiModelProperty(name = "isDelete", value = "是否删除")
    private Boolean isDelete;

    @ApiModelProperty(name = "authorizeParam", value = "认证参数")
    private String authorizeParam;

    @ApiModelProperty(name = "authorizeTemplate", value = "认证参数模版")
    private String authorizeTemplate;

    private String gmtCreate;

    private String gmtUpdate;

}
