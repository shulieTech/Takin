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
package com.shulie.instrument.simulator.agent.core.util;

import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/9/29 3:47 下午
 */
public class ThrowableUtils {

    public static String toString(Throwable throwable, int maxLength) {
        String errorMessage = toString(throwable);
        if (errorMessage == null) {
            return null;
        }
        if (errorMessage.length() > maxLength) {
            errorMessage = StringUtils.substring(errorMessage, 0, maxLength);
        }
        return errorMessage;
    }

    public static String toString(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(bos);
        try {
            throwable.printStackTrace(writer);
            try {
                return new String(bos.toByteArray(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return new String(bos.toByteArray());
            }
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
            }
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }
}
