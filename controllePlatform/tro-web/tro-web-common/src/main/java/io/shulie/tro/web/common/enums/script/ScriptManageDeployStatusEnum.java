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

package io.shulie.tro.web.common.enums.script;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 脚本
 *
 * @author liuchuan
 * @date 2021/4/20 3:18 下午
 */
@AllArgsConstructor
@Getter
public enum ScriptManageDeployStatusEnum {

    /**
     * 新建
     */
    NEW(0, "新建"),
    PASS(1, "调试通过"),
    HISTORY(2, "历史版本");

    /**
     * 状态
     */
    private final Integer code;

    /**
     * 状态描述
     */
    private final String desc;

}
