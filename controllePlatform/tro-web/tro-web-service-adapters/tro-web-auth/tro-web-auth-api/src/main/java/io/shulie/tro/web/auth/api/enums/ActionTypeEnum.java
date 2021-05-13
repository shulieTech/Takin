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
 * @Date: 2020/9/2 上午10:46
 * @Description:权限相关，需要增加，联系晓霞，不能随便增加
 */
public enum ActionTypeEnum {
    QUERY(1, "查询"),
    CREATE(2, "新增"),
    UPDATE(3, "编辑"),
    DELETE(4, "删除"),
    START_STOP(5, "启动停止"),
    ENABLE_DISABLE(6, "启用禁用"),
    DOWNLOAD(7, "下载");

    private Integer code;
    private String cname;

    ActionTypeEnum(Integer code, String cname) {
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

    public static String getNameByCode(Integer code) {
        for (ActionTypeEnum actionTypeEnum : ActionTypeEnum.values()) {
            if (actionTypeEnum.getCode().equals(code)) {
                return actionTypeEnum.name();
            }
        }
        return null;
    }

    public static String getCnameByCode(Integer code) {
        for (ActionTypeEnum actionTypeEnum : ActionTypeEnum.values()) {
            if (actionTypeEnum.getCode().equals(code)) {
                return actionTypeEnum.getCname();
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
