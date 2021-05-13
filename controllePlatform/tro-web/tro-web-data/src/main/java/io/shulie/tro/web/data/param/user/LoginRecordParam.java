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

package io.shulie.tro.web.data.param.user;

import java.util.Date;

import lombok.Data;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.data.param.user
 * @date 2021/4/7 5:22 下午
 */
@Data
public class LoginRecordParam {
    /**
     * 登录用户
     */
    private String userName;

    /**
     * 登录ip
     */
    private String ip;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    public LoginRecordParam() {
        this.gmtCreate = new Date();
        this.gmtModified = new Date();
    }
}
