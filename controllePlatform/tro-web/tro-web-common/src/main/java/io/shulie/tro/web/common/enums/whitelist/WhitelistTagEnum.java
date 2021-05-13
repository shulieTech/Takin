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

package io.shulie.tro.web.common.enums.whitelist;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.common.enums.whitelist
 * @date 2021/4/14 9:50 上午
 */
@AllArgsConstructor
@Getter
public enum WhitelistTagEnum {
    DUPLICATE_NAME("重名白名单"),
    MANUALLY_ADD("手工添加");
    private String tagName;
}
