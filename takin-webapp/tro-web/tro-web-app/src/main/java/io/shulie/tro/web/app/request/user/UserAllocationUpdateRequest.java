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

package io.shulie.tro.web.app.request.user;

import lombok.Data;

/**
 * @Author ZhangXT
 * @Description 指派负责人
 * @Date 2020/11/5 15:26
 */
@Data
public class UserAllocationUpdateRequest {
    /**
     * 数据ID
     */
    private Long dataId;
    /**
     * 菜单code
     */
    private String menuCode;
    /**
     * 用户id
     */
    private Long userId;

}
