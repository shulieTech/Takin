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

package io.shulie.tro.web.common.vo.fastdebug;

import io.shulie.tro.web.common.enums.fastdebug.CallStackExceptionEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.common.vo.fastdebug
 * @date 2021/3/1 4:08 下午
 */
@Data
@NoArgsConstructor
public class FastDebugCallstackAnalysisVO {
    private String analysisType;
    private Integer type;
    private Integer count;

    public FastDebugCallstackAnalysisVO(CallStackExceptionEnum exceptionEnum, Integer count) {
        this.analysisType = exceptionEnum.getException();
        this.type = exceptionEnum.getType();
        this.count = count;
    }
}
