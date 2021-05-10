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
package com.pamirs.attach.plugin.redisson.utils;

/**
 * @Auther: vernon
 * @Date: 2020/11/27 01:55
 * @Description:
 */
public class CutOffSwitcher {

    private static ThreadLocal<Boolean> switcher = new ThreadLocal<Boolean>() {
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };

    public static void turnOff() {

        switcher.set(Boolean.FALSE);
    }

    public static void turnOn() {
        switcher.set(Boolean.TRUE);
    }

    public static boolean status() {
        return switcher.get();
    }
}
