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

package io.shulie.tro.web.app.response.statistics;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.response.statistics
 * @date 2020/11/30 9:16 下午
 */
@Data
public class PressurePieTotalResponse {
    @ApiModelProperty(value = "分类统计")
    private List<PressurePieTotal> data;
    @ApiModelProperty(value = "合计")
    private Integer total;
    @Data
    public static class PressurePieTotal {
        private String type;
        private Integer value;
    }
}
