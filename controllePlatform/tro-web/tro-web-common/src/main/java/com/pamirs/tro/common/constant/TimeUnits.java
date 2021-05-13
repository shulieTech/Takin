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
 * 说明：时间单位枚举类
 *
 * @author shulie
 * @version 1.0
 * @date 2017年12月13日
 */
public enum TimeUnits {

    YEAR,
    MONTH,
    DAY,
    HOUR,
    MINUTES,
    SECOND;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
