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

package io.shulie.tro.cloud.web.entrypoint.request.webauth;

import java.io.Serializable;

import lombok.Data;

/**
 * @ClassName WebAuthRequest
 * @Description
 * @Author qianshui
 * @Date 2020/11/4 下午4:25
 */
@Data
public class WebAuthRequest implements Serializable {
    private static final long serialVersionUID = 2191433226885985290L;

    /**
     * 用户所在部门id
     */
    private Long deptId;

    /**
     * 用户id
     */
    private Long uid;
}
