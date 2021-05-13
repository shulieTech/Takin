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

package io.shulie.tro.web.app.response.user;

import java.util.List;

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/11/5 9:30 下午
 * @Description: 菜单数据权限返参
 */
@Data
public class ResourceScopeResponse {
    /**
     * 菜单id
     */
    private Long id;

    /**
     * 菜单名称
     */
    private String title;

    /**
     * 选中的数据范围类型
     */
    private Integer checked;

    /**
     * 菜单编码
     */
    private String key;

    /**
     * 数据范围列表
     */
    private List<SingleScopeResponse> groupList;
}
