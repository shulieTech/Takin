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

package com.pamirs.tro.common.ump.utils;

public class SendTypeUtils {

    public static int genType(SendTypeEnum... sendTypeEnums) {
        int t = 0;
        for (SendTypeEnum sendTypeEnum : sendTypeEnums) {
            t = t | sendTypeEnum.getType();
        }
        return t;
    }

    public static void main(String[] args) {
        int sms = SendTypeUtils.genType(SendTypeEnum.SMS);
        System.out.println(sms);
        int WX_OFF = SendTypeUtils.genType(SendTypeEnum.WECHART_OFFICIAL);
        System.out.println(WX_OFF);
        int sendType = SendTypeUtils.genType(SendTypeEnum.WECHART_OFFICIAL, SendTypeEnum.SMS);
        System.out.println(sendType);
    }
}
