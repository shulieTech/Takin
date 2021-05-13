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

/**
 * @Author: fanxx
 * @Date: 2020/11/2 8:40 下午
 * @Description:
 */
public enum DataTypeEnum {
    NONE(0, "未选择"),
    ALL(1, "全部"),
    CURRENT_DEPT(2, "本部门"),
    CURRENT_DEPT_AND_LESS(3, "本部门及以下"),
    SELF_AND_DEPT_LESS(4, "自己及以下"),
    SELF(5, "仅自己");

    private Integer code;
    private String cname;

    DataTypeEnum(Integer code, String cname) {
        this.code = code;
        this.cname = cname;
    }

    public static Integer getCodeByName(String name) {
        for (ActionTypeEnum actionTypeEnum : ActionTypeEnum.values()) {
            if (actionTypeEnum.name().equalsIgnoreCase(name)) {
                return actionTypeEnum.getCode();
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

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }
}
