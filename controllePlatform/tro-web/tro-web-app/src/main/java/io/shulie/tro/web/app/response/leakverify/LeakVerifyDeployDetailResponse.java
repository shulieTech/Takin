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

package io.shulie.tro.web.app.response.leakverify;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
@ApiModel(value = "LeakVerifyDeployDetailResponse", description = "漏数实例详情")
@Deprecated
public class LeakVerifyDeployDetailResponse implements Serializable {
    private static final long serialVersionUID = 2203568093674770367L;

    @ApiModelProperty(value = "漏数实例详情id")
    private Long id;

    /**
     * 漏数验证实例id
     */
    @ApiModelProperty(value = "漏数验证实例id")
    private Long leakVerifyDeployId;

    /**
     * 漏数类型
     */
    @ApiModelProperty(value = "漏数类型")
    private String leakType;

    /**
     * 漏数数量
     */
    @ApiModelProperty(value = "漏数数量")
    private Long leakCount;

    /**
     * 漏数内容
     */
    @ApiModelProperty(value = "漏数内容")
    private String leakContent;

}
