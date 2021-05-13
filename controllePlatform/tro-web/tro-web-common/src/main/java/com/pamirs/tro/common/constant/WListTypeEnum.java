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
 * @author shulie
 * @description
 * @create 2019-04-03 19:36:07
 */
public enum WListTypeEnum {

    HTTP("1", "HTTP"),
    DUBBO("2", "DUBBO"),
    FORBIDDEN("3", "禁止压测"),
    JOB("4", "JOB"),
    MQ("5", "MQ");

    /**
     * 白名单类型：1HTTP 2DUBBO 3禁止压测 4JOB 5MQ
     */
    private String value;

    private String name;

    WListTypeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * Gets the value of value.
     *
     * @return the value of value
     * @author shulie
     * @version 1.0
     */
    public String getValue() {
        return value;
    }

    public String getName() {
        return name;

    }
}
