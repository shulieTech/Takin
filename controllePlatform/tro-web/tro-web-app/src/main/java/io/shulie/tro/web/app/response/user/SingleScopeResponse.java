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

import lombok.Data;

/**
 * @Author: fanxx
 * @Date: 2020/11/5 9:31 下午
 * @Description:
 */
@Data
public class SingleScopeResponse {
    /**
     * 数据范围类型id
     */
    private Integer value;

    /**
     * 数据范围类型名称
     */
    private String label;
}
