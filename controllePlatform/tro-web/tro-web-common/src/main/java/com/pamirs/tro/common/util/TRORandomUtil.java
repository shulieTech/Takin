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

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

/**
 * 说明: 获取随机数
 *
 * @author shulie
 * @version v1.0
 * @Date: Create in 2019/1/10 17:35
 */
public class TRORandomUtil {
    private static Random random = new Random();

    /**
     * 说明: 随机获取一定范围的随机数
     *
     * @param min   随机数取值范围最小值
     * @param max   随机数取值范围最大值
     * @param count 随机数取值数量
     * @return 固定数量随机数集合
     * @author shulie
     * @date 2018/10/22 19:48
     */
    public static List<Integer> generateRandomNumber(int min, int max, long count) {
        List<Integer> randomNumberLists = Lists.newArrayList();
        while (true) {
            int randomNumber = random.nextInt(max) % (max - min + 1) + min;
            if (!randomNumberLists.contains(randomNumber)) {
                randomNumberLists.add(randomNumber);
            }
            if (randomNumberLists.size() >= count) {
                return randomNumberLists;
            }
        }
    }

    /**
     * 说明: 获取指定长度的随机数
     *
     * @param length 获取随机数的长度
     * @return 指定长度的随机数
     * @author shulie
     * @date 2019/1/10 17:35
     */
    public static String getRandomString(int length) {
        if (length <= 0) {
            return "";
        }
        char[] randomChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i',
            'o', 'p',
            'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm'};
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            stringBuffer.append(randomChar[Math.abs(random.nextInt()) % randomChar.length]);
        }
        return stringBuffer.toString();
    }
}
