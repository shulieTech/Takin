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

package io.shulie.tro.cloud.common.enums.engine;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 无涯
 * @Package io.shulie.tro.cloud.common.enums.engine
 * @date 2021/5/8 4:42 下午
 */
@AllArgsConstructor
@Getter
public enum EngineStartTypeEnum {
    K8S("k8s","k8s压测方式"),
    LOCAL_THREAD("localThread","采用本地线程方式压测");
    private String type;
    private String desc;
}
