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

package io.shulie.tro.web.common.context;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2020-10-04
 */
@Data
public class CurrentUser {

    private Long id;

    private String name;

    private Integer status;

    /**
     * 用户类型 0:系统管理员 1:其他
     */
    private Integer userType;

    private Integer model;

    /**
     * 角色 0:管理员，1:体验用户 2:正式用户（作废）
     */
    private Integer role;

    @JsonProperty("xToken")
    private String xToken;

    private List<String> permissionUrl;

    private Map<String, List<String>> permissionData;

    /**
     * 数据隔离的客户id
     */
    private Long customerId;

    /**
     * 数据隔离的客户key
     */
    private String customerKey;
}
