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
 * @author 无涯
 * @Package com.pamirs.tro.common.constant
 * @date 2020/12/3 10:32 上午
 */
public enum  ScriptEnum {

    JMETER("Jmeter", 0),
    SHELL("Shell", 1);

    private String name;
    private Integer value;

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

    ScriptEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }
    public static String getName(Integer value) {
        for(ScriptEnum scriptEnum:values()) {
            if(value.equals(scriptEnum.getValue())) {
                return scriptEnum.getName();
            }
        }
        return String.valueOf(value);
    }

}
