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

package io.shulie.tro.cloud.common.enums;

/**
 * @Author: fanxx
 * @Date: 2020/5/13 下午3:03
 * @Description:
 */
public enum OpenTypeEnum {
    /**
     * 开通模式：1、长期开通 2、短期抢占
     */
    fix(1, "长期开通"),
    temporary(2, "短期抢占");
    private Integer code;
    private String name;

    OpenTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getStatus() {
        return name;
    }

    public void setStatus(String status) {
        this.name = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
