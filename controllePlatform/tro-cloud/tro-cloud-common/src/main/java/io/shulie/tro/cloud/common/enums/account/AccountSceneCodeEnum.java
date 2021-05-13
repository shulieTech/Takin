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

package io.shulie.tro.cloud.common.enums.account;

/**
 * @ClassName AccountSceneCodeEnum
 * @Description
 * @Author qianshui
 * @Date 2020/5/25 下午2:34
 */
public enum AccountSceneCodeEnum {

    PRE_LOCK("PRE_LOCK", "冻结"),
    UN_PRE_LOCK("UN_PRE_LOCK", "解冻"),
    SETTLE("SETTLE", "消耗"),
    RECHARGE("RECHARGE", "充值");

    private String code;

    private String name;

    AccountSceneCodeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getNameByCode(String code) {
        if (code == null) {
            return null;
        }
        for (AccountSceneCodeEnum e : AccountSceneCodeEnum.values()) {
            if (code.equals(e.getCode())) {
                return e.getName();
            }
        }
        return code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
