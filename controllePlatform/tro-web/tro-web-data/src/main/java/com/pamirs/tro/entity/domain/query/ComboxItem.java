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

package com.pamirs.tro.entity.domain.query;

/**
 * Created by Windows User on 2019/5/7.
 */
public class ComboxItem {

    private String code;

    private String value;

    public ComboxItem() {}

    public ComboxItem(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public ComboxItem(Integer code, String value) {
        this.code = String.valueOf(code);
        this.value = value;
    }

    public ComboxItem(Long code, String value) {
        this.code = String.valueOf(code);
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
