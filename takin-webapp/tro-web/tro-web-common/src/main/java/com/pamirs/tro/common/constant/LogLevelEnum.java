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
 * @Author: mubai
 * @Date: 2021-01-04 09:29
 * @Description:
 */
public enum LogLevelEnum {

    // "debug/info/warn/error"
    DEBUG(1, "DEBUG"),
    INFO(2, "INFO"),
    WARN(3, "WARN"),
    ERROR(4, "ERROR"),
    TRACE(5,"TRACE");

    private Integer code;
    private String name;

    LogLevelEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    LogLevelEnum getLogLevel(String type){
        for (LogLevelEnum logLevelEnum:LogLevelEnum.values()){
            if (logLevelEnum.getName().equals(type)){
                return logLevelEnum;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
