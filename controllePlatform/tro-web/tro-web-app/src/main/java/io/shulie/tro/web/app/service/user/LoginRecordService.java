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

package io.shulie.tro.web.app.service.user;

import io.shulie.tro.web.app.input.user.LoginRecordSearchInput;
import io.shulie.tro.web.app.output.user.LoginRecordTotalOutput;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.app.service.user
 * @date 2021/4/8 11:09 上午
 */
public interface LoginRecordService {
    LoginRecordTotalOutput getTotal(LoginRecordSearchInput input);
}
