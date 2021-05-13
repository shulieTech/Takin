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
 * @Author ZhangXT
 * @Description 菜单
 * @Date 2020/11/5 14:41
 */
public enum MenuTypeEnum {

    LINK_MANAGE("LINK_MANAGE", "系统流程"),
    BUSINESS_ACTIVITY("BUSINESS_ACTIVITY", "业务活动"),
    BUSINESS_FLOW("BUSINESS_FLOW", "业务流程"),
    APPLICATION_MNT("APPLICATION_MNT", "应用管理"),
    SCENE_MANAGE("SCENE_MANAGE", "压测场景"),
    SCENE_RECORD("SCENE_RECORD", "压测报表"),
    SCRIPT_MANAGE("SCRIPT_MANAGE", "脚本管理");

    private String code;
    private String desc;


    public static MenuTypeEnum getByCode(String code) {
        for (MenuTypeEnum anEnum : MenuTypeEnum.values()) {
            if (anEnum.name().equalsIgnoreCase(code)) {
                return anEnum;
            }
        }
        return null;
    }

    MenuTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
