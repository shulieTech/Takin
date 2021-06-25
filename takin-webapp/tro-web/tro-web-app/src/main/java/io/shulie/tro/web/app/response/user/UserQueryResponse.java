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

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author ZhangXT
 * @Description
 * @Date 2020/11/4 14:54
 */
@Data
public class UserQueryResponse {
    /**
     * 成员/账号id
     */
    @JsonProperty("id")
    private Long id;
    /**
     * 成员/账号名称
     */
    @JsonProperty("accountName")
    private String accountName;
    /**
     * 所在部门
     */
    @JsonProperty("department")
    private String department;
    /**
     * 账号角色
     */
    @JsonProperty("accountRole")
    private List<RoleQueryResponse> roleList;

    @JsonProperty("userAppKey")
    private String userAppKey;
}
