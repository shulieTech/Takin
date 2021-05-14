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

package io.shulie.tro.web.common.enums.excel;

import lombok.Getter;

/**
* @Package io.shulie.tro.web.common.enums.excel
* @author 无涯
* @description:
* @date 2021/4/16 1:46 上午
*/
@Getter
public enum BooleanEnum {

    TRUE(true, "是"),
    FALSE(false, "否");

    private Boolean value;

    private String desc;

    BooleanEnum(Boolean value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static Boolean getByDesc(String desc) {
        for (BooleanEnum booleanEnum :BooleanEnum.values()) {
            if(booleanEnum.getDesc().equals(desc)) {
                return booleanEnum.value;
            }
        }
        return BooleanEnum.TRUE.value;
    }

    public static String getByValue(Boolean value) {
        return value?TRUE.getDesc(): FALSE.getDesc();
    }
}
