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

package io.shulie.tro.cloud.biz.input.scenemanage;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SceneBusinessActivityRefInput
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午5:13
 */
@Data
public class SceneBusinessActivityRefInput implements Serializable {

    private static final long serialVersionUID = -2028726088507717658L;

    @ApiModelProperty(name = "businessActivityId", value = "业务活动ID")
    @NotNull(message = "业务活动ID不能为空")
    private Long businessActivityId;

    @ApiModelProperty(name = "businessActivityName", value = "业务活动名称")
    private String businessActivityName;

    @ApiModelProperty(name = "targetTPS", value = "目标TPS")
    @NotNull(message = "目标TPS不能为空")
    private Integer targetTPS;

    @ApiModelProperty(name = "targetRT", value = "目标RT")
    @NotNull(message = "目标RT不能为空")
    private Integer targetRT;

    @ApiModelProperty(name = "targetSuccessRate", value = "目标成功率")
    @NotNull(message = "目标成功率不能为空")
    private BigDecimal targetSuccessRate;

    @ApiModelProperty(name = "targetSA", value = "目标SA")
    @NotNull(message = "目标SA不能为空")
    private BigDecimal targetSA;

    private String bindRef;

    private String applicationIds;
}
