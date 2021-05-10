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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 固定输出的时间戳格式：yyyy-MM-dd HH:mm:ss.SSS<br>
 * 与 SimpleDateFormat 相比，差别是：
 * <ul>
 * <li>针对同一秒频繁输出的时间戳做了缓存
 * <li>不管在什么时区，输出的时间都校正到中国的东八区
 * </ul>
 * 这个类非线程安全，使用时需要做 SimpleDateFormat 类似的线程保护
 */
class FastDateFormat {

    private static final long TIMEZONE_GMT_ADD_8_OFFSET = 28800000;
    /**
     * 当前时区的时间（含夏令时）与中国东八区 "GMT+8" 的偏移量
     */
    private static final long CURRENT_TIMEZONE_OFFSET = TimeZone.getDefault().getOffset(0)
            + TimeZone.getDefault().getDSTSavings() - TIMEZONE_GMT_ADD_8_OFFSET;

    private final SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private char[] buffer = new char[23];

    private long lastSecond = -1;

    public String format(long timestamp) {
        formatToBuffer(timestamp);
        return new String(buffer, 0, 23);
    }

    public String format(Date date) {
        return format(date.getTime());
    }

    public void formatAndAppendTo(long timestamp, StringBuilder appender) {
        formatToBuffer(timestamp);
        appender.append(buffer, 0, 23);
    }

    private void formatToBuffer(long timestamp) {
        final long ts = timestamp - CURRENT_TIMEZONE_OFFSET;
        long second = ts / 1000;
        if (second == lastSecond) {
            int ms = (int) (ts % 1000);
            buffer[22] = (char) (ms % 10 + '0');
            ms /= 10;
            buffer[21] = (char) (ms % 10 + '0');
            buffer[20] = (char) (ms / 10 + '0');
        } else {
            String result = fmt.format(new Date(ts));
            result.getChars(0, result.length(), buffer, 0);
        }
    }

    String formatWithoutMs(long timestamp) {
        final long ts = timestamp - CURRENT_TIMEZONE_OFFSET;
        long second = ts / 1000;
        if (second != lastSecond) {
            String result = fmt.format(new Date(ts));
            result.getChars(0, result.length(), buffer, 0);
        }
        return new String(buffer, 0, 19);
    }
}
