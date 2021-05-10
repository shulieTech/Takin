/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shulie.instrument.simulator.core.util;

import org.apache.commons.lang.ArrayUtils;

/**
 * 位操作工具类
 */
public class BitUtils {

    /**
     * 判断目标数是否在掩码范围内
     *
     * @param target    目标数
     * @param maskArray 掩码数组
     * @return true:在掩码范围内;false:不在掩码范围内
     */
    public static boolean isIn(int target, int... maskArray) {
        if (ArrayUtils.isEmpty(maskArray)) {
            return false;
        }
        for (int mask : maskArray) {
            if ((target & mask) == mask) {
                return true;
            }
        }
        return false;
    }

}
