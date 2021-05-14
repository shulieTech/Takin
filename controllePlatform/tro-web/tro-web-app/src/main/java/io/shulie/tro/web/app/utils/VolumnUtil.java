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
 * @ClassName VolumnUtil
 * @Description 容量工具类
 * byte转GB
 * @Author qianshui
 * @Date 2020/8/7 下午3:56
 */
public class VolumnUtil {

    /**
     * byte转GB
     *
     * @param byteSize
     * @return
     */
    public static BigDecimal convertByte2Gb(BigDecimal byteSize) {
        return byteSize.divide(new BigDecimal(1024 * 1024 * 1204), 2, RoundingMode.HALF_UP);
    }
}
