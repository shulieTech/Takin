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

package io.shulie.tro.web.amdb.enums;

public enum MiddlewareTypeGroupEnum {
    DEFAULT(""),
    APP("APP"),
    HTTP("HTTP"),
    DUBBO("DUBBO"),
    MYSQL("MYSQL"),
    ORACLE("ORACLE"),
    SQLSERVER("SQLSERVER"),
    CASSANDRA("CASSANDRA"),
    MONGODB("MONGODB"),
    HBASE("HBASE"),
    HESSIAN("HESSIAN"),
    CACHE("CACHE"),
    REDIS("REDIS"),
    MEMCACHE("MEMCACHE"),
    ROCKETMQ("ROCKETMQ"),
    KAFKA("KAFKA"),
    ACTIVEMQ("ACTIVEMQ"),
    IBMMQ("IBMMQ"),
    RABBITMQ("RABBITMQ"),
    ES("ES"),
    ELASTICJOB("ELASTIC-JOB"),
    OSS("OSS"),
    UNKNOWN("UNKNOWN");

    String type;

    MiddlewareTypeGroupEnum(String type) {
        this.type = type;
    }

    public static MiddlewareTypeGroupEnum getMiddlewareGroupType(String middlewareName) {
        if (middlewareName == null || "".equals(middlewareName.trim())) {
            return MiddlewareTypeGroupEnum.UNKNOWN;
        }
        if (middlewareName.toLowerCase().contains("http")) {
            middlewareName = "http";
        }
        switch (middlewareName.toLowerCase()) {
            case "app":
                return MiddlewareTypeGroupEnum.APP;
            case "dubbo":
            case "apache-dubbo":
                return MiddlewareTypeGroupEnum.DUBBO;
            case "apache-rocketmq":
            case "rocketmq":
            case "ons":
                return MiddlewareTypeGroupEnum.ROCKETMQ;
            case "apache-kafka":
            case "kafka":
                return MiddlewareTypeGroupEnum.KAFKA;
            case "apache-activemq":
            case "activemq":
                return MiddlewareTypeGroupEnum.ACTIVEMQ;
            case "ibmmq":
                return MiddlewareTypeGroupEnum.IBMMQ;
            case "rabbitmq":
                return MiddlewareTypeGroupEnum.RABBITMQ;
            case "hbase":
            case "aliyun-hbase":
                return MiddlewareTypeGroupEnum.HBASE;
            case "hessian":
                return MiddlewareTypeGroupEnum.HESSIAN;
            case "tfs":
                return MiddlewareTypeGroupEnum.OSS;
            case "http":
            case "undertow":
            case "tomcat":
            case "jetty":
            case "jdk-http":
            case "okhttp":
                return MiddlewareTypeGroupEnum.HTTP;
            case "oss":
                return MiddlewareTypeGroupEnum.OSS;
            case "mysql":
                return MiddlewareTypeGroupEnum.MYSQL;
            case "oracle":
                return MiddlewareTypeGroupEnum.ORACLE;
            case "sqlserver":
                return MiddlewareTypeGroupEnum.SQLSERVER;
            case "cassandra":
                return MiddlewareTypeGroupEnum.CASSANDRA;
            case "mongodb":
                return MiddlewareTypeGroupEnum.MONGODB;
            case "elasticsearch":
                return MiddlewareTypeGroupEnum.ES;
            case "redis":
                return MiddlewareTypeGroupEnum.REDIS;
            case "memcache":
                return MiddlewareTypeGroupEnum.MEMCACHE;
            case "cache":
                return MiddlewareTypeGroupEnum.CACHE;
            case "search":
                return MiddlewareTypeGroupEnum.ES;
            case "elastic-job":
                return MiddlewareTypeGroupEnum.ELASTICJOB;
            default:
                return buildEnum(middlewareName.toUpperCase());
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public static MiddlewareTypeGroupEnum buildEnum(String type){
        MiddlewareTypeGroupEnum defaultEnum = DEFAULT;
        defaultEnum.setType(type);
        return defaultEnum;
    }
}
