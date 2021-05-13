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

import java.util.Date;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/12/22 3:41 下午
 * @Description:
 */
@Data
public class UserDetailResult {
    /**
     * id
     */
    private Long id;
    /**
     * 登录账号
     */
    private String name;

    /**
     * 用户名称
     */
    private String nick;

    /**
     * 用户key
     */
    private String key;

    /**
     * 盐值
     */
    private String salt;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 状态 0:启用  1： 冻结
     */
    private Integer status;

    /**
     * 用户类型，0:系统管理员，1:其他
     */
    private Integer userType;

    /**
     * 模式 0:体验模式，1:正式模式
     */
    private Integer model;

    /**
     * 角色 0:管理员，1:体验用户 2:正式用户
     */
    private Integer role;

    /**
     * 状态 0: 正常 1： 删除
     */
    private Boolean isDelete;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    private Date gmtUpdate;

    /**
import java.util.Date;

     * 租户id
     */
    private Long customerId;
}
