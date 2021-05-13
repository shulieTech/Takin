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

package io.shulie.tro.web.app.response.whitelist;

import lombok.Data;

/**
* @Package io.shulie.tro.web.app.response.whitelist
* @author 无涯
* @description:
* @date 2021/4/12 5:56 下午
*/
@Data
public class WhitelistStringResponse {
    private String content;

    public WhitelistStringResponse(String content) {
        this.content = content;
    }
}
