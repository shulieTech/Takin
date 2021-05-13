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

/**
 * @author 何仲奇
 * @Package com.pamirs.tro.common.util
 * @date 2020/9/2 9:08 下午
 * @see MD5Util
 */
public class MD5UtilTest {
    public static void main(String[] args) {
        String md5 = MD5Util.getMD5("pradar" + "pradar-tro");
        System.out.println(md5);
    }

}
