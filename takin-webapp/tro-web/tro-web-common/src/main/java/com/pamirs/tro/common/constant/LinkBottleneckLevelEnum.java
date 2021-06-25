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

package com.pamirs.tro.common.constant;

/**
 * 瓶颈等级(1、严重，2、普通，3、正常)
 *
 * @author shulie
 * @version v1.0
 * @2018年5月16日
 */
public enum LinkBottleneckLevelEnum {
    BOTTLENECK_LEVEL_SERIOUS(1, "严重"),
    BOTTLENECK_LEVEL_GENERAl(2, "普通"),
    BOTTLENECK_LEVEL_NORMAL(3, "正常");

    private int code;
    private String name;

    /**
     * 构造方法
     *
     * @param name
     */
    LinkBottleneckLevelEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }
}
