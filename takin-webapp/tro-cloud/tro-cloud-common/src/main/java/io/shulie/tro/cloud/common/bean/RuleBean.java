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

package io.shulie.tro.cloud.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName RuleBean
 * @Description
 * @Author qianshui
 * @Date 2020/4/18 上午10:58
 */
@Data
public class RuleBean implements Serializable {

    private static final long serialVersionUID = 1789327058040467753L;

    @ApiModelProperty(value = "指标类型")
    private Integer indexInfo;

    @ApiModelProperty(value = "条件")
    private Integer condition;

    @ApiModelProperty(value = "满足值")
    private BigDecimal during;

    @ApiModelProperty(value = "连续触发次数")
    private Integer times;

    public void RuleBean(Integer indexInfo, Integer condition, BigDecimal during, Integer times) {
        this.setIndexInfo(indexInfo);
        this.setCondition(condition);
        this.setDuring(during);
        this.setTimes(times);
    }
}
