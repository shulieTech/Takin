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

import lombok.Getter;

/**
 * RPC 类型
 *
 * @author pamirs
 */
@Getter
public enum RpcType {

    // @formatter:off
    UNKNOWN("?", 255, ""),
    TRACE("TRACE", 0, ""),
    DUBBO("DUBBO", 1, ""),
    DUBBO_SERVER("DUBBO", 2, ""),
    ROCKETMQ("ROCKETMQ", 3, "TOPIC"),
    DB("DB", 4, "库表"),
    CACHE("CACHE", 5, "缓存key"),
    SEARCH("SEARCH", 6, "索引名"),
    HESSIAN("HESSIAN", 7, ""),
    HESSIAN_SERVER("HESSIAN", 8, ""),
    MASTER("MASTER", 11, ""),
    SLAVE("SLAVE", 12, ""),
    IBMMQ("IBMMQ", 13, "TOPIC"),
    TFS("TFS", 15, ""),
    HTTP_BASE("HTTP", 20, ""),
    HTTP("HTTP", 25, ""),
    SENTINEL("SENTINEL", 26, ""),
    LOCAL("LOCAL", 30, ""),
    ACCESS("ACCESS", 253, ""),

    ROCKETMQ_RCV("ROCKETMQ", 252, "TOPIC"),
    // 为了分析方便而扩展的虚拟类型，在日志中实际并不存在
    IBMMQ_RCV("IBMMQ", 254, "TOPIC"),
    HTTP_SERVER("HTTP_SERVER", 31, ""),
    HBASE_CLIENT("HBASE_CLIENT", 32, ""),
    RABBITMQ_CLIENT("RABBITMQ_CLIENT", 300, ""),
    TYPE_LOCAL_FILE("LOCAL_FILE", 34, ""),
    //rabbitmq服务端
    RABBITMQ_RCV("RABBITMQ", 301, ""),
    TYPE_LOCAL_JOB("LOCAL_JOB", 36, ""),
    KAFKA_RCV("KAFKA", 1002, ""),
    KAFKA_CLIENT("KAFKA_CLIENT", 1001, ""),
    ACTIVEMQ_RCV("ACTIVEMQ", 1004,"");


    /**
     * 显示文本
     */
    private final String text;

    /**
     * Pradar 日志里面对应的 RpcType 值
     */
    private final int value;

    /**
     * RPC 数据类别
     */
    private final String dataType;

    RpcType(final String text, final int value, final String dataType) {
        this.text = text;
        this.value = value;
        this.dataType = dataType;
    }

    public static boolean isServer(Integer rpcType) {
        return RpcType.DUBBO.getValue() == rpcType ||
            RpcType.DUBBO_SERVER.getValue() == rpcType ||
            RpcType.HTTP.getValue() == rpcType ||
            RpcType.HTTP_BASE.getValue() == rpcType ||
            RpcType.HTTP_SERVER.getValue() == rpcType ||
            RpcType.LOCAL.getValue() == rpcType ||
            RpcType.TYPE_LOCAL_FILE.getValue() == rpcType ||
            RpcType.TYPE_LOCAL_JOB.getValue() == rpcType;
    }

    public static boolean isMiddleAwary(Integer rpcType) {
        boolean server = isServer(rpcType);
        if (server) {
            return false;
        }
        return rpcType != RpcType.TRACE.getValue() && rpcType != RpcType.UNKNOWN.getValue();
    }

    public static RpcType getByValue(final Integer value, final RpcType defaultValue) {
        try {
            for (RpcType rpcType : values()) {
                if (rpcType.getValue() == value) {
                    return rpcType;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return defaultValue;
    }


}
