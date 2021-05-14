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
 * @Date: 2020/9/2 下午3:50
 * @Description:
 */
public enum ResourceTypeEnum {
    MENU(0, "菜单"),
    BUTTON(1, "按钮"),
    DATA(2, "数据"),
    OTHER(3, "其他"),
    ;

    private Integer code;

    private String type;

    ResourceTypeEnum(Integer code, String type) {
        this.code = code;
        this.type = type;
    }

    public static ResourceTypeEnum of(String typeCode) {
        ResourceTypeEnum[] values = ResourceTypeEnum.values();
        for (ResourceTypeEnum value : values) {
            if (StringUtils.equals(typeCode, value.type)) {
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
