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

package io.shulie.tro.web.data.result.user;

import lombok.Data;

/**
 * @author shiyajian
 * create: 2021-01-29
 */
@Data
public class UserCacheResult {

    /**
     * id
     */
    private Long id;
    /**
     * 登录账号
     */
    private String name;

    /**
     * 用户key
     */
    private String key;

    /**
     * 用户类型，0:系统管理员，1:其他
     */
    private Integer userType;

    /**
     * 租户id
     */
    private Long customerId;

    private String customerKey;
}
