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

package com.pamirs.tro.common.constant;

/**
 * @Author: fanxx
 * @Date: 2020/12/29 2:53 下午
 * @Description:
 */
public enum DataSourceVerifyTypeEnum {
    MYSQL(0);

    private Integer code;

    DataSourceVerifyTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public static DataSourceVerifyTypeEnum getTypeByCode(Integer code) {
        for (DataSourceVerifyTypeEnum typeEnum : DataSourceVerifyTypeEnum.values()) {
            if (typeEnum.getCode().equals(code)) {
                return typeEnum;
            }
        }
        return null;
    }
}
