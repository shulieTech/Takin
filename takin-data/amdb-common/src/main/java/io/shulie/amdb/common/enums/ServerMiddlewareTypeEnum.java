package io.shulie.amdb.common.enums;

import lombok.Getter;

@Getter
@Deprecated
public enum ServerMiddlewareTypeEnum {
    /**
     * HTTP
     */
    HTTP("HTTP"),
    /**
     * DUBBO
     */
    DUBBO("DUBBO"),
    /**
     * ROCKETMQ
     */
    ROCKETMQ("ROCKETMQ"),
    /**
     * RABBITMQ
     */
    RABBITMQ("RABBITMQ"),
    /**
     * KAFKA
     */
    KAFKA("KAFKA"),
    /**
     * JOB
     */
    JOB("JOB");

    private String type;

    ServerMiddlewareTypeEnum(String type) {
        this.type = type;
    }

    public static ServerMiddlewareTypeEnum getEnumByType(String value) {
        ServerMiddlewareTypeEnum[] enumConstants = ServerMiddlewareTypeEnum.class.getEnumConstants();
        for (ServerMiddlewareTypeEnum enumConstant : enumConstants) {
            if (enumConstant.getType().equalsIgnoreCase(value)) {
                return enumConstant;
            }
        }
        return HTTP;
    }
}
