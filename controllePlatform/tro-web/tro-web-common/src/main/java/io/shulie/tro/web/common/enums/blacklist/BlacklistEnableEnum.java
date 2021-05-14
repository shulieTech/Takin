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

package io.shulie.tro.web.common.enums.blacklist;

/**
 * @author 无涯
 * @Package io.shulie.tro.web.common.enums.blacklist
 * @date 2021/4/6 4:46 下午
 */
public enum BlacklistEnableEnum {
    //是否可用(0表示1表示启动,2表示启用未校验)
    DISABLED(0, "未启动"),
    ENABLE(1, "启动"),
    ENABLE_NOT_VERIFIED(2, "启用未校验");


    private Integer status;
    private String desc;

    BlacklistEnableEnum(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public static String getDescByType(Integer status) {
        for (BlacklistEnableEnum enableEnum : BlacklistEnableEnum.values()) {
            if (enableEnum.status.equals(status)) {
                return enableEnum.desc;
            }
        }
        return "";
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
