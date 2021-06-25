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

package io.shulie.tro.cloud.data.result.scenemanage;

import java.io.Serializable;

import io.shulie.tro.cloud.common.bean.RuleBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SceneSlaRefResult
 * @Description
 * @Author qianshui
 * @Date 2020/4/18 上午10:59
 */
@Data
public class SceneSlaRefResult implements Serializable {

    private static final long serialVersionUID = 5117439939447730586L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "规则名称")
    private String ruleName;

    @ApiModelProperty(value = "适用对象")
    private String[] businessActivity;

    @ApiModelProperty(value = "规则")
    private RuleBean rule;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "触发事件")
    private String event;
}
