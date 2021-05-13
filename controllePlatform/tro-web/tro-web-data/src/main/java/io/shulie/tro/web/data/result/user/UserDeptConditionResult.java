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
 * @Author: fanxx
 * @Date: 2020/11/9 11:02 上午
 * @Description:
 */
@Data
public class UserDeptConditionResult {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 上级部门id
     */
    private Long parentId;

    /**
     * 部门名称
     */
    private String deptName;
}
