package io.shulie.amdb.enums;

import lombok.Getter;

@Getter
public enum EntranceTypeEnum {
    HTTP("HTTP"),
    ROCKETMQ("ROCKETMQ"),
    RABBITMQ("RABBITMQ"),
    KAFKA("KAFKA");

    private String type;

    EntranceTypeEnum(String type) {
        this.type = type;
    }
}
