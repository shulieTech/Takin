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
package com.pamirs.pradar;

import org.apache.commons.lang.StringUtils;

/**
 * @Auther: vernon
 * @Date: 2021/1/18 10:17
 * @Description:
 */
public class TraceCoreUtils {

    private static String INNER_SPILIT = "~";

    public static String attributes(String traceAppName,
                                    String traceServiceName,
                                    String traceMethodName) {
        return combineString(traceAppName, traceServiceName, traceMethodName);
    }

    public static String localAttributes(Object upAppName,
                                         Object remoteIp,
                                         Object remotePort,
                                         Object requestSize,
                                         Object responseSize) {
        return combineString(upAppName,
                remoteIp,
                remotePort,
                requestSize,
                responseSize);
    }

    /**
     * @param isPt     是否压测流量
     * @param isDebug  是否debug流量
     * @param isTrace  是否trace入口
     * @param isServer 是否server (1:server 2:client)
     * @return
     */

    public static String flags(Object isPt,
                               Object isDebug,
                               Object isTrace,
                               Object isServer) {

        return combineString(
                isPt,
                isDebug,
                isTrace,
                isServer
        );
    }


    public static String combineString(Object... objects) {


        if (objects == null) {
            return StringUtils.EMPTY;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            builder.append(makeStringSafe(objects[i] == null ? "" : objects[i].toString()))
                    .append(INNER_SPILIT);

        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();

    }

    public static boolean isServer(AbstractContext ctx) {
        int logtype = ctx.logType;
        return logtype == Pradar.LOG_TYPE_INVOKE_SERVER || logtype == Pradar.LOG_TYPE_TRACE;
    }

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }


    public static String makeStringSafe(String value) {
        value = StringUtils.replace(value, PradarCoreUtils.NEWLINE, "\t");
        value = StringUtils.replace(value, "\n", "\t");
        value = StringUtils.replace(value, "|", "\\");
        return value;
    }


}
