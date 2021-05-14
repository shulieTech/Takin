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

package io.shulie.tro.web.amdb.bean.common;

import lombok.Getter;

@Getter
public enum EntranceTypeEnum {
    // TYPE 名称和 AMDB 的 middleWareName 保持一致
    HTTP("HTTP"),
    DUBBO("DUBBO"),
    ROCKETMQ("ROCKETMQ"),
    RABBITMQ("RABBITMQ"),
    KAFKA("KAFKA"),
    ELASTICJOB("ELASTICJOB");

    private String type;

    EntranceTypeEnum(String type) {
        this.type = type;
    }

    public static EntranceTypeEnum getEnumByType(String value){
        EntranceTypeEnum[] enumConstants = EntranceTypeEnum.class.getEnumConstants();
        for (EntranceTypeEnum enumConstant : enumConstants) {
            if (enumConstant.getType().equalsIgnoreCase(value)) {
                return enumConstant;
            }
        }
        return HTTP;
    }

    public static EntranceTypeEnum getEnumByName(String name){
        EntranceTypeEnum[] enumConstants = EntranceTypeEnum.class.getEnumConstants();
        for (EntranceTypeEnum enumConstant : enumConstants) {
            if (enumConstant.name().equalsIgnoreCase(name)) {
                return enumConstant;
            }
        }
        return HTTP;
    }
}
