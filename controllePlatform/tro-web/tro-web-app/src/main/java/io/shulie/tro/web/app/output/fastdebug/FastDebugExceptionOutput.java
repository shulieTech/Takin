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

package io.shulie.tro.web.app.output.fastdebug;

import java.util.List;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.output.fastdebug
 * @date 2021/1/4 10:49 上午
 */
@Data
@NoArgsConstructor
public class FastDebugExceptionOutput {
    private List<String> apps = Lists.newArrayList();
    private List<String> codes = Lists.newArrayList();
    private List<String> types = Lists.newArrayList();

    public FastDebugExceptionOutput(List<String> apps, List<String> codes, List<String> types) {
        this.apps = apps;
        this.codes = codes;
        this.types = types;
    }
}
