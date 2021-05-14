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

package com.pamirs.tro.entity.domain.entity.linkmanage.figure;

/**
 * @Auther: vernon
 * @Date: 2019/12/11 19:47
 * @Description:中间件枚举类型
 */
public enum MiddleWareEnum {

    //中间件类型：HTTP_CLIENT、JDBC、ORM、DB、JOB、MESSAGE、CACHE、POOL、JNDI、NO_SQL、RPC、SEARCH、MQ、SERIALIZE、OTHER
    HTTP_CLIENT("http-client"),
    JDBC("jdbc"),
    ORM("持久层框架"),
    DB("存储"),
    JOB("定时任务"),
    MESSAGE("分布式消息"),
    CACHE("缓存"),
    POOL("连接池"),
    JNDI("命名系统接口"),
    NO_SQL("Nosql数据库"),
    RPC("RPC框架"),
    SEARCH("搜搜索引擎"),
    MQ("消息队列"),
    SERIALIZE("序列化"),
    OTHER("-"),
    SERVLET_CONTAINER("Servlet容器"),
    ;

    private static final String DEFAULT = "default";
    private String value;

    MiddleWareEnum(String value) {
        this.value = value;
    }

    public static String getMiddleWareType(String middleWareName) {
        String result = DEFAULT;
        if (middleWareName == null) {
            return result;
        }
        if (middleWareName.contains("db")
                || middleWareName.contains("mqsql")
                || middleWareName.contains("sql")
                || middleWareName.contains("oracle")
                || middleWareName.contains("hbase")
        ) {
            return DB.value;
        }
        if (middleWareName.contains("mq") || middleWareName.contains("kafka")
        ) {
            return MQ.value;
        }
        if (middleWareName.contains("dubbo")) {
            return RPC.value;
        }
        if (middleWareName.contains("elasticsearch") || middleWareName.contains("solr")) {
            return SEARCH.value;
        }
        if (middleWareName.contains("redis")) {
            return CACHE.value;
        }
        return OTHER.value;
    }

    public static String getMiddleWareTypeValue(String type) {
        MiddleWareEnum[] middleWareEnums = MiddleWareEnum.values();
        for (MiddleWareEnum middleWareEnum : middleWareEnums) {
            if(middleWareEnum.name().equalsIgnoreCase(type)){
                return middleWareEnum.value;
            }
        }
        return MiddleWareEnum.OTHER.value;
    }
}
