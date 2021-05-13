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

package com.pamirs.tro.entity.domain.vo.scenemanage;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName TimeVO
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午9:51
 */
@Data
public class TimeVO implements Serializable {

    private static final long serialVersionUID = -4490980949244068326L;

    @ApiModelProperty(value = "时间")
    @NotNull(message = "时间不能为空")
    private Long time;

    @ApiModelProperty(value = "单位")
    @NotNull(message = "单位不能为空")
    private String unit;

    public TimeVO() {

    }

    public TimeVO(Long time, String unit) {
        this.time = time;
        this.unit = unit;
    }
}
