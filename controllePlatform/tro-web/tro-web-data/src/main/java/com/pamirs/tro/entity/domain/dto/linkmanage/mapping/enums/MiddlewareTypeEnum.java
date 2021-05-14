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

package com.pamirs.tro.entity.domain.dto.linkmanage.mapping.enums;

import lombok.Getter;

/**
 * @Author: fanxx
 * @Date: 2020/7/6 下午5:44
 * @Description: 链路拓扑图-中间件类型
 */
@Getter
public enum MiddlewareTypeEnum {
    MySQL("0", "MySQL"),

    Oracle("1", "Oracle"),

    SQLServer("2", "SQL Server"),

    Cassandra("3", "Cassandra"),

    Elasticsearch("4", "Elasticsearch"),

    HBase("5", "HBase"),

    Redis("6", "Redis"),

    Memcache("7", "Memcache"),

    MongoDB("8", "MongoDB"),

    RocketMQ("9", "RocketMQ"),

    Kafka("10", "Kafka"),

    ActiveMQ("11", "ActiveMQ"),

    RabbitMQ("12", "RabbitMQ"),

    Dubbo("13", "Dubbo"),

    Other("1000", "其他");

    private String code;
    private String desc;

    MiddlewareTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
