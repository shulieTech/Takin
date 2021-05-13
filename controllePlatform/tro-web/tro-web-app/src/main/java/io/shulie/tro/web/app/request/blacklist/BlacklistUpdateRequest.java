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

package io.shulie.tro.web.app.request.blacklist;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.param.blacklist
 * @date 2021/4/6 2:22 下午
 */
@Data
public class BlacklistUpdateRequest {

    /**
     * 主键id
     */
    @ApiModelProperty(name = "blistId", value = "黑名单id")
    @NotNull
    private Long blistId;

    ///**
    // * 黑名单类型
    // */
    //@ApiModelProperty(name = "type", value = "黑名单类型")
    //private Integer type ;

    /**
     * 黑名单值
     */
    @ApiModelProperty(name = "redisKey", value = "黑名单值")
    @NotNull
    private String redisKey ;


    /**
     * 应用id
     */
    @ApiModelProperty(name = "applicationId", value = "应用id")
    @NotNull
    private Long applicationId ;


}
