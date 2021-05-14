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

package io.shulie.tro.cloud.common.context;

import java.util.Date;

import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-10-22
 */
@Data
public class LoginUser {

    private Long id;

    private String name;

    private String nick;

    private String key;

    private String salt;

    private String password;

    private Integer status;

    private Integer model;

    /**
     * 角色 0:管理员，1:体验用户 2:正式用户
     */
    private Integer role;

    private Integer isDelete;

    private Date gmtCreate;

    private Date gmtUpdate;

    private String version;

    /**
     * 登录渠道
     * 0-console 前端页面
     * 1-web 客户端license
     */
    private Integer loginChannel;
}
