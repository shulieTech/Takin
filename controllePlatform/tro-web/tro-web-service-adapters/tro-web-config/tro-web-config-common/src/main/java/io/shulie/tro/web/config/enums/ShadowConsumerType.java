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

package io.shulie.tro.web.config.enums;

/**
 * @author shiyajian
 * create: 2020-09-15
 */
public enum ShadowConsumerType {
    ROCKETMQ,
    KAFKA,
    RABBITMQ;
    public static ShadowConsumerType of(String name) {
        for (ShadowConsumerType enumConstant : ShadowConsumerType.class.getEnumConstants()) {
            if (enumConstant.name().equalsIgnoreCase(name)) {
                return enumConstant;
            }
        }
        throw new IllegalArgumentException("不正确的类型名称：" + name);
    }
}
