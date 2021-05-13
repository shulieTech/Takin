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

package io.shulie.tro.cloud.common.bean.scenemanage;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.common.bean.scenemanage
 * @date 2021/2/3 3:36 下午
 */
@Data
@NoArgsConstructor
public class DataBean {
    @ApiModelProperty(value = "实际")
    private Object result;

    @ApiModelProperty(value = "目标")
    private Object value;

    public DataBean(Object result, Object value) {
        this.result = result;
        this.value = value;
    }
}
