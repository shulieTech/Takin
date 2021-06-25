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

package com.pamirs.tro.common.enums.ds;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: fanxx
 * @Date: 2020/11/26 4:56 下午
 * @Description:
 */
@AllArgsConstructor
@Getter
public enum DbTypeEnum {
    /**
     * 数据库
     */
    DB(0, "数据库"),

    /**
     * 缓存
     */
    CACHE(1, "缓存"),

    /**
     * 影子Server
     */
    ES_SERVER(2, "搜索引擎（ES）"),

    /**
     * 影子集群
     */
    HBASE_SERVER(3, "数据库(HBase)"),

    /**
     * kafka
     */
    KAFKA_SERVER(4, "消息队列(Kafka)");

    private final Integer code;

    private final String desc;

    /**
     * 根据 desc 获得枚举
     * @param desc desc
     * @return 枚举
     */
    public static DbTypeEnum getEnumByDesc(String desc) {
        return Arrays.stream(values())
                .filter(dbTypeEnum -> dbTypeEnum.getDesc().equals(desc))
                .findFirst().orElse(null);
    }

    /**
     * 根据 code 获得枚举
     * @param code code
     * @return 枚举
     */
    public static DbTypeEnum getEnumByCode(Integer code) {
        return Arrays.stream(values())
                .filter(dbTypeEnum -> dbTypeEnum.getCode().equals(code))
                .findFirst().orElse(null);
    }

    /**
     * 根据 code, 获得描述
     * @param code code
     * @return 描述
     */
    public static String getDescByCode(Integer code) {
        return Arrays.stream(values())
                .filter(dbTypeEnum -> dbTypeEnum.getCode().equals(code))
                .findFirst()
                .map(DbTypeEnum::getDesc).orElse("");
    }

}
