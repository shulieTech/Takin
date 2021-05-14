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

package io.shulie.tro.web.app.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author: mubai
 * @Date: 2021-01-05 19:40
 * @Description:
 */
public class UnitConvertor {

    /**
     * kb 转换为MB
     *
     * @param source
     * @return
     */
    public static BigDecimal DIVIDE_DATA = new BigDecimal(1024 * 1024);

    public static BigDecimal byteToMb(BigDecimal source) {
        if (null == source) {
            return new BigDecimal(0).setScale(2, RoundingMode.HALF_UP);
        } else {
            return source.divide(DIVIDE_DATA).setScale(2, RoundingMode.HALF_UP);
        }
    }

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal(1122);
        BigDecimal bigDecimal1 = byteToMb(bigDecimal);
        System.out.println(bigDecimal1);
    }
}
