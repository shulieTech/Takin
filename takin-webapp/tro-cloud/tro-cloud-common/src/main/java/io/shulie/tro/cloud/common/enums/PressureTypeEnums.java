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
 * @author zhaoyong
 */
public enum PressureTypeEnums {

    /**
     * 并发模式
     */
    CONCURRENCY("concurrency", 0),

    /**
     * tps目标模式
     */
    TPS("linear", 1),

    /**
     * 自定义模式
     */
    PERSONALIZATION("personalization", 2),

    /**
     * 流量调试模式
     */
    FLOW_DEBUG("flow_debug",3)
    ;

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

    PressureTypeEnums(String text, int code) {
        this.text = text;
        this.code = code;
    }


    public static boolean isConcurrency(Integer code){
        //为空默认为并发模式
        return code == null || PressureTypeEnums.CONCURRENCY.getCode() == code;
    }

    public static boolean isTps(Integer code){
        return code != null && PressureTypeEnums.TPS.getCode() == code;
    }

    public static boolean isPersonalization(Integer code){
        return code != null && PressureTypeEnums.PERSONALIZATION.getCode() == code;
    }

}
