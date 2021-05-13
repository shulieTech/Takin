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

package io.shulie.tro.web.auth.api.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author: fanxx
 * @Date: 2020/9/2 上午11:50
 * @Description:
 */
public enum RoleTypeEnum {

    /**
     * 应用管理员
     */
    APP_ADMIN(0, "应用管理员"),

    /**
     * 应用组长
     */
    APP_MANAGER(1, "应用组长"),

    /**
     * 应用组员
     */
    APP_USER(2, "应用组员");

    private Integer code;

    private String type;

    RoleTypeEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public static ResourceTypeEnum of(String typeCode) {
        ResourceTypeEnum[] values = ResourceTypeEnum.values();
        for (ResourceTypeEnum value : values) {
            if (StringUtils.equals(typeCode, value.name())) {
                return value;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
