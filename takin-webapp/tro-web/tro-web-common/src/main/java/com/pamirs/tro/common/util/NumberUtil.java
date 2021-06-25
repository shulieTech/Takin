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

package com.pamirs.tro.common.util;

import java.math.BigDecimal;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * 数字相关的工具类
 *
 * @author shulie
 * @version v1.0
 * @2018年5月21日
 */
public class NumberUtil {

    /**
     * BigDecimal 转long
     *
     * @param bigDecimal 要转换的bigDecimal
     * @return long类型结果
     */
    public static long getLong(BigDecimal bigDecimal) {
        return bigDecimal == null ? 0L : bigDecimal.longValue();
    }

    /**
     * BigDecimal 转 float
     *
     * @param bigDecimal
     * @return
     */
    public static float getFloat(BigDecimal bigDecimal) {
        return bigDecimal == null ? 0f : bigDecimal.floatValue();
    }

    /**
     * String 转 float
     *
     * @param str
     * @return
     */
    public static float getFloat(String str) {

        return NumberUtils.isCreatable(str) ? 0f : Float.parseFloat(str);
    }

    public static Double transStrToDouble(String str) {
        return NumberUtils.isCreatable(str) ? 0 : Double.valueOf(str);
    }

}
