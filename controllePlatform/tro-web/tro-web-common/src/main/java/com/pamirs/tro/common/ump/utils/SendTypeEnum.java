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

package com.pamirs.tro.common.ump.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * com.pamirs.ump.api.core
 *
 * @auth zhh
 * @created 2018/9/1
 */
public enum SendTypeEnum {
    SMS(0b001),
    WECHART_OFFICIAL(0b010),
    WECHART_PUB(0b100),
    ALIPAY(0b1000);
    private int type;

    SendTypeEnum(int type) {
        this.type = type;
    }

    /**
     * getSendTypes
     *
     * @param t
     * @return
     */
    public static List<SendTypeEnum> getSendTypes(int t) {
        List<SendTypeEnum> sendTypeEnumList = new ArrayList<>();
        for (SendTypeEnum sendTypeEnum : SendTypeEnum.values()) {
            if (sendTypeEnum.isType(t)) {
                sendTypeEnumList.add(sendTypeEnum);
            }
        }
        return sendTypeEnumList;
    }

    public int getType() {
        return type;
    }

    /**
     * isType
     *
     * @param t
     * @return
     */
    private boolean isType(int t) {
        return (t & type) > 0;
    }
}
