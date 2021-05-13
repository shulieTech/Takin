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

package io.shulie.tro.cloud.common.enums;

import lombok.Getter;

/**
 * @author shiyajian
 * create: 2020-07-30
 */
public enum PressureModeEnums {

    /**
     * 固定模式
     */
    FIXED("fixed", 1),

    /**
     * 线性增长
     */
    LINEAR("linear", 2),

    /**
     * 阶梯增长
     */
    STAIR("stair", 3);

    /**
     * 名称
     */
    @Getter
    private final String text;

    /**
     * 编码
     */
    @Getter
    private final int code;

    PressureModeEnums(String text, int code) {
        this.text = text;
        this.code = code;
    }
}
