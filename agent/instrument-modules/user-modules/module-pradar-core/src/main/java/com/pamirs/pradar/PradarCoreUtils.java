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

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.CRC32;

import static com.pamirs.pradar.AppNameUtils.appName;

public final class PradarCoreUtils {

    public static final String EMPTY_STRING = "";
    public static String DEFAULT_STRING = "default";
    public static final String NEWLINE = "\r\n";

    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final String LOCAL_IP_ADDRESS = getLocalInetAddress();

    private static final long LOCAL_IP_ADDRESS_NUMBER = getLocalInetAddressNumber();

    private static final String LOCAL_HOST_NAME = getLocalHostName();

    private static String getAppName() {
        return appName();
    }

    /**
     * 处理特殊字符使可以安全的记录日志
     *
     * @param value 字符串
     * @return 返回保证安全的字符串
     */
    public static String makeLogSafe(String value) {
        value = StringUtils.replace(value, PradarCoreUtils.NEWLINE, "\t");
        value = StringUtils.replace(value, "\n", "\t");
        value = StringUtils.replace(value, "|", "\\");
        return value;
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

    public static String checkNotNullEmpty(String value, String name) throws IllegalArgumentException {
        if (isBlank(value)) {
            throw new IllegalArgumentException(name + " is null or empty");
        }
        return value;
    }

    public static <T> T checkNotNull(T value, String name) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException(name + " is null");
        }
        return value;
    }

    public static <T> T defaultIfNull(T value, T defaultValue) {
        return (value == null) ? defaultValue : value;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String[] split(String str, char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList<String>();
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || (preserveAllTokens && lastMatch)) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static StringBuilder appendWithBlankCheck(String str, String defaultValue, StringBuilder appender) {
        if (isNotBlank(str)) {
            appender.append(str);
        } else {
            appender.append(defaultValue);
        }
        return appender;
    }

    public static StringBuilder appendWithNullCheck(Object obj, String defaultValue, StringBuilder appender) {
        if (obj != null) {
            appender.append(obj.toString());
        } else {
            appender.append(defaultValue);
        }
        return appender;
    }

    /**
     * 追加日志，同时过滤字符串中的换行为空格，避免导致日志格式解析错误
     *
     * @param str       需要追加的字符串
     * @param appender  用于追加的appender
     * @param delimiter 分隔符
     * @return 返回追加后的StringBuilder
     */
    public static StringBuilder appendLog(String str, StringBuilder appender, char delimiter) {
        if (str != null) {
            int len = str.length();
            appender.ensureCapacity(appender.length() + len);
            for (int i = 0; i < len; i++) {
                char c = str.charAt(i);
                if (c == '\n' || c == '\r' || c == delimiter) {
                    // 用 appender.append(str, start, len) 批量 append 实质也是一个字符一个字符拷贝
                    // 因此此处还是用土办法
                    c = ' ';
                }
                appender.append(c);
            }
        }
        return appender;
    }

    /**
     * 过滤字符串中的换行为空格，避免导致日志格式解析错误
     *
     * @param str str
     * @return 返回过滤后的结果
     * @see #appendLog(String, StringBuilder, char)
     */
    public static String filterInvalidCharacters(String str) {
        StringBuilder appender = new StringBuilder(str.length());
        return appendLog(str, appender, '|').toString();
    }

    /**
     * 对字符串生成摘要，目前使用 CRC32 算法
     *
     * @param str str
     * @return 摘要后的字符串
     */
    public static String digest(String str) {
        CRC32 crc = new CRC32();
        crc.update(str.getBytes());
        return Long.toHexString(crc.getValue());
    }

    // 自身日志的时间标签格式化器
    private static final ThreadLocal<FastDateFormat> dateFmt = new ThreadLocal<FastDateFormat>() {
        @Override
        protected FastDateFormat initialValue() {
            return new FastDateFormat();
        }
    };

    /**
     * 时间格式化成 yyyy-MM-dd HH:mm:ss.SSS
     *
     * @param timestamp 时间戳
     * @return 返回format后的时间
     */
    public static String formatTime(long timestamp) {
        return dateFmt.get().format(timestamp);
    }

    private static long getLocalInetAddressNumber() {
        String ip = getLocalInetAddress();
        StringBuilder builder = new StringBuilder();
        String[] arr = StringUtils.split(ip, '.');
        for (String str : arr) {
            for (int i = str.length(); i < 3; i++) {
                builder.append('0');
            }
            builder.append(str);
        }
        return Long.valueOf(builder.toString());
    }

    public static String getIpFromLong(long number) {
        String str = String.valueOf(number);
        int idx1 = 3 - (12 - str.length());
        int idx2 = idx1 + 3;
        int idx3 = idx2 + 3;
        int idx4 = idx3 + 3;
        StringBuilder builder = new StringBuilder();

        builder.append(Integer.valueOf(str.substring(0, idx1))).append('.')
                .append(Integer.valueOf(str.substring(idx1, idx2))).append('.')
                .append(Integer.valueOf(str.substring(idx2, idx3))).append('.')
                .append(Integer.valueOf(str.substring(idx3, idx4)));
        return builder.toString();

    }

    private static String getLocalInetAddress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress address = null;
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {
                        return address.getHostAddress();
                    }
                }
            }
        } catch (Throwable t) {
        }
        return "127.0.0.1";
    }

    private static String getLocalHostName() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress address = null;
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    address = addresses.nextElement();
                    if (!address.isLoopbackAddress() && address.getHostAddress().indexOf(":") == -1) {
                        return address.getHostName();
                    }
                }
            }
        } catch (Throwable t) {
        }
        return "localhost";
    }

    public static String getLocalAddress() {
        return LOCAL_IP_ADDRESS;
    }

    public static long getLocalAddressNumber() {
        return LOCAL_IP_ADDRESS_NUMBER;
    }

    public static String getHostName() {
        return LOCAL_HOST_NAME;
    }

    public static boolean isClusterTestEnabled(InvokeContext ctx) {
        return ctx != null && "1".equals(ctx.getUserData("t"));
    }
}
