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

package com.pamirs.tro.entity.domain.vo.user;

import java.io.Serializable;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/3/24 下午4:51
 * @Description:
 */
@Data
public class UserQueryParam implements Serializable {
    private static final long serialVersionUID = 3357797162900086877L;

    private Long id;

    private String name;

    private String password;

    private String nick;

    private Integer currentPage;

    private Integer pageSize;

    private String methodInfo;

    private Integer status;

    private Integer userType;

    private Long customerId;
}
