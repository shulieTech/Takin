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
package com.pamirs.attach.plugin.jedis.util;

import com.pamirs.pradar.Pradar;

/**
 * Created by xiaobin on 2017/2/20.
 */
public final class ParameterUtils {
    private final static String EMPTY = "";

    public static String toString(int limitSize, Object... args) {
        if (args == null || args.length == 0 || limitSize < 1) {
            return EMPTY;
        }
        StringBuilder builder = new StringBuilder();
        for (Object obj : args) {
            if (obj == null) {
                continue;
            }
            builder.append(',');
            if (obj instanceof byte[]) {
                builder.append(new String((byte[]) obj));
            } else if (obj instanceof char[]) {
                builder.append(new String((char[]) obj));
            } else if (obj.getClass().isPrimitive()) {
                builder.append(String.valueOf(obj));
            } else {
                builder.append(obj.toString());
            }
        }
        if (builder.length() != 0) {
            builder.deleteCharAt(0);
        }
        String argsStr = builder.toString();
        if (builder.length() > 0) {
            for (String c : Pradar.SPECIAL_CHARACTORS) {
                argsStr = argsStr.replace(c, " ");
            }
            if (argsStr.length() > limitSize) {
                argsStr = argsStr.substring(0, limitSize);
            }
        }
        return argsStr;
    }
}
