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
 * @date 2020/9/2 8:59 下午
 * @see RequestPradarUtil
 */
public class RequestPradaUtilTest {

    public static void main(String[] args) {
        System.out.println(RequestPradarUtil.initSPT());
        //        try {
        //            //{appKey=pradar, token=DA8601F8B43324596DBA2CF3D1B98B97, timestamp=1556586948365}
        //            long timestamp = 1556586948365L;
        //            System.out.println(dateToStamp("2018-09-07 09:58:59"));
        //            String md5 = MD5Util.getMD5(APPKEY_PRADA + SECRETKEY_PRADA + timestamp);
        //            System.out.println(md5);
        //        } catch (ParseException e) {
        //            e.printStackTrace();
        //        }
    }
}
