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
package com.pamirs.pradar.common;


import com.shulie.instrument.simulator.api.util.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author xiaobin.zfb
 * @since 2020/8/8 4:44 下午
 */
public final class FormatUtils {

    /*
     * yyyy-MM-dd HH:mm:ss
     */
    private static final ThreadLocal<SimpleDateFormat> dateTimeFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setLenient(false);
            return format;
        }
    };

    /**
     * <tt>yyyy-MM-dd HH:mm:ss</tt>形式表达的时间转换为 Date 返回
     *
     * @param dateTimeString
     * @return
     * @throws IllegalArgumentException
     */
    public static Date parseDateTime(String dateTimeString) throws IllegalArgumentException {
        try {
            return dateTimeFormatter.get().parse(dateTimeString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式不正确，正确格式为：yyyy-MM-dd HH:mm:ss", e);
        }
    }

    public static String toDateTimeString(long millis) throws IllegalArgumentException {
        return dateTimeFormatter.get().format(new Date(millis));
    }

    /**
     * 将 date 格式化为<tt>yyyy-MM-dd HH:mm:ss</tt>的形式
     *
     * @param date
     * @return
     * @throws IllegalArgumentException
     */
    public static String toDateTimeString(Date date) throws IllegalArgumentException {
        if (date == null) {
            return null;
        }
        return dateTimeFormatter.get().format(date);
    }

    /*
     * yyyy-MM-dd HH:mm
     */
    private static final ThreadLocal<SimpleDateFormat> dateMinuteFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            format.setLenient(false);
            return format;
        }
    };

    /**
     * <tt>yyyy-MM-dd HH:mm</tt>形式表达的时间转换为 Date 返回
     *
     * @param dateTimeString
     * @return
     * @throws IllegalArgumentException
     */
    public static Date parseDateMinute(String dateTimeString) throws IllegalArgumentException {
        try {
            return dateMinuteFormatter.get().parse(dateTimeString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式不正确，正确格式为：yyyy-MM-dd HH:mm", e);
        }
    }

    public static String toDateMinuteString(long millis) throws IllegalArgumentException {
        return dateMinuteFormatter.get().format(new Date(millis));
    }

    /**
     * 将 date 格式化为<tt>yyyy-MM-dd HH:mm</tt>的形式
     *
     * @param date
     * @return
     * @throws IllegalArgumentException
     */
    public static String toDateMinuteString(Date date) throws IllegalArgumentException {
        if (date == null) {
            return null;
        }
        return dateMinuteFormatter.get().format(date);
    }

    /*
     * yyyy-MM-dd_HH
     */
    private static final ThreadLocal<SimpleDateFormat> dateHourFormat = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd_HH");
        }
    };

    /**
     * <tt>yyyy-MM-dd_HH</tt>形式表达的时间转换为 Date 返回
     *
     * @param dateStr
     * @return
     * @throws IllegalArgumentException
     */
    public static Date parseDateHour(String dateStr) {
        Date date;
        try {
            date = dateHourFormat.get().parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("非法时间：" + dateStr + "，正确的格式为 yyyy-MM-dd_HH", e);
        }
        return date;
    }

    /**
     * 将 date 格式化为<tt>yyyy-MM-dd_HH</tt>的形式
     *
     * @param date
     * @return
     * @throws IllegalArgumentException
     */
    public static String toDateHourString(Date date) throws IllegalArgumentException {
        if (date == null) {
            return null;
        }
        return dateHourFormat.get().format(date);
    }

    /*
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    private static final ThreadLocal<SimpleDateFormat> dateTimeMillisFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            format.setLenient(false);
            return format;
        }
    };

    /**
     * <tt>yyyy-MM-dd HH:mm:ss.SSS</tt>形式表达的时间转换为 Date 返回
     *
     * @param dateTimeString
     * @return
     * @throws IllegalArgumentException
     */
    public static Date parseDateTimeMillis(String dateTimeString) throws IllegalArgumentException {
        try {
            return dateTimeMillisFormatter.get().parse(dateTimeString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式不正确，正确格式为：yyyy-MM-dd HH:mm:ss.SSS", e);
        }
    }

    public static String toDateTimeMillisString(long millis) throws IllegalArgumentException {
        return dateTimeMillisFormatter.get().format(new Date(millis));
    }

    /**
     * 将 date 格式化为<tt>yyyy-MM-dd HH:mm:ss.SSS</tt>的形式
     *
     * @param date
     * @return
     * @throws IllegalArgumentException
     */
    public static String toDateTimeMillisString(Date date) throws IllegalArgumentException {
        if (date == null) {
            return null;
        }
        return dateTimeMillisFormatter.get().format(date);
    }

    /*
     * yyyy-MM-dd
     */
    private static final ThreadLocal<SimpleDateFormat> dateFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            return format;
        }
    };

    /**
     * <tt>yyyy-MM-dd</tt>形式表达的时间转换为 Date 返回
     *
     * @param dateString
     * @return
     * @throws IllegalArgumentException
     */
    public static Date parseDate(String dateString) throws IllegalArgumentException {
        try {
            return dateFormatter.get().parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式不正确，正确格式为：yyyy-MM-dd", e);
        }

    }

    public static String toDateString(long millis) throws IllegalArgumentException {
        return dateFormatter.get().format(new Date(millis));
    }

    /**
     * 将 date 格式化为<tt>yyyy-MM-dd</tt>的形式
     *
     * @param date
     * @return
     * @throws IllegalArgumentException
     */
    public static String toDateString(Date date) throws IllegalArgumentException {
        if (date == null) {
            return null;
        }
        return dateFormatter.get().format(date);
    }

    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    private static final ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            format.setLenient(false);
            return format;
        }
    };

    public static Date parseTimeRangeDate(String dateStr) {
        Date date;
        try {
            date = formatter.get().parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("非法时间：" + dateStr + "，正确的格式为 " + DATE_FORMAT);
        }
        return date;
    }

    public static String formatTimeRange(Date date) {
        return formatter.get().format(date);
    }

    /*
     * yyyy/MM/dd
     */
    private static final ThreadLocal<SimpleDateFormat> pageDateFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            format.setLenient(false);
            return format;
        }
    };

    public static String toPageDateString(Date date) throws IllegalArgumentException {
        if (date == null) {
            return null;
        }
        return pageDateFormatter.get().format(date);
    }

    public static String toPageDateString(long time) throws IllegalArgumentException {
        return pageDateFormatter.get().format(time);
    }

    public static Date parsePageDate(String dateString) throws IllegalArgumentException {
        try {
            return pageDateFormatter.get().parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式不正确，正确格式为：yyyy/MM/dd", e);
        }
    }

    /*
     * yyyyMMdd
     */
    private static final ThreadLocal<SimpleDateFormat> yyyyMMddFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            format.setLenient(false);
            return format;
        }
    };

    /**
     * <tt>yyyyMMdd</tt>形式表达的时间转换为 Date 返回
     *
     * @param dateString
     * @return
     * @throws IllegalArgumentException
     */
    public static Date parseYyyyMMdd(String dateString) throws IllegalArgumentException {
        try {
            return yyyyMMddFormatter.get().parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式不正确，正确格式为：yyyyMMdd", e);
        }
    }

    public static String toYyyyMMddString(long millis) throws IllegalArgumentException {
        return yyyyMMddFormatter.get().format(new Date(millis));
    }

    /**
     * 将 date 格式化为<tt>yyyyMMdd</tt>的形式
     *
     * @param date
     * @return
     * @throws IllegalArgumentException
     */
    public static String toYyyyMMddString(Date date) throws IllegalArgumentException {
        if (date == null) {
            return null;
        }
        return yyyyMMddFormatter.get().format(date);
    }

    /*
     * gmtDate: EEE, dd MMM yyyy HH:mm:ss zzz
     */
    private static final ThreadLocal<SimpleDateFormat> gmtDateFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            format.setLenient(false);
            return format;
        }
    };

    /**
     * <tt>EEE, dd MMM yyyy HH:mm:ss zzz</tt>形式表达的 GMT 时间转换为 Date 返回，形如：
     * <tt>Tue, 15 Nov 1994 12:45:26 GMT</tt>
     *
     * @param dateString
     * @return
     * @throws IllegalArgumentException
     */
    public static Date parseGmtDate(String dateString) throws IllegalArgumentException {
        try {
            return gmtDateFormatter.get().parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式不正确，正确格式为：EEE, dd MMM yyyy HH:mm:ss zzz", e);
        }
    }

    public static String toGmtDateString(long millis) throws IllegalArgumentException {
        return gmtDateFormatter.get().format(new Date(millis));
    }

    /*
     * HH:mm:ss
     */
    private static final ThreadLocal<SimpleDateFormat> secondTimeFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            format.setLenient(false);
            return format;
        }
    };

    /**
     * <tt>HH:mm:ss</tt>形式表达的时间转换为 Date 返回，形如： <tt>12:45:26</tt>
     *
     * @param dateString
     * @return
     * @throws IllegalArgumentException
     */
    public static Date parseSecondTime(String dateString) throws IllegalArgumentException {
        try {
            return secondTimeFormatter.get().parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式不正确，正确格式为：HH:mm:ss", e);
        }
    }

    public static String toSecondTimeString(long millis) throws IllegalArgumentException {
        return secondTimeFormatter.get().format(new Date(millis));
    }

    /*
     * HH:mm
     */
    private static final ThreadLocal<SimpleDateFormat> minuteTimeFormatter = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            format.setLenient(false);
            return format;
        }
    };

    /**
     * <tt>HH:mm</tt>形式表达的时间转换为 Date 返回，形如： <tt>12:45</tt>
     *
     * @param dateString
     * @return
     * @throws IllegalArgumentException
     */
    public static Date parseMinuteTime(String dateString) throws IllegalArgumentException {
        try {
            return minuteTimeFormatter.get().parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("日期格式不正确，正确格式为：HH:mm", e);
        }
    }

    public static String toMinuteTimeString(long millis) throws IllegalArgumentException {
        return minuteTimeFormatter.get().format(new Date(millis));
    }

    /**
     * 将 date 格式化为<tt>EEE, dd MMM yyyy HH:mm:ss zzz</tt>的 GMT 形式，形如：
     * <tt>Tue, 15 Nov 1994 12:45:26 GMT</tt>
     *
     * @param date
     * @return
     * @throws IllegalArgumentException
     */
    public static String toGmtDateString(Date date) throws IllegalArgumentException {
        if (date == null) {
            return null;
        }
        return gmtDateFormatter.get().format(date);
    }

    /**
     * 将给定的浮点数 value 进行四舍五入, 四舍五入的位数取决于 decimals.
     * 如: round(3.14159, 2) -- 3.14, round(3.14159, 4) -- 3.1416,
     * round(3.14159, 0) -- 3.0, round(3141.59, -3) -- 3000.0, round(3141.59,
     * -4) -- 0.0, round(5141.59, -4) -- 10000.0
     *
     * @param value    值
     * @param decimals 要四舍五入到小数点后的位数
     * @return
     */
    public static double round(double value, int decimals) {
        double d = Math.pow(10, decimals);
        return Math.round(value * d) / d;
    }

    /**
     * 四舍五入到整数部分
     *
     * @param value 值
     * @return 返回不保留小数据点后的值
     */
    public static long roundx0(double value) {
        return Math.round(value);
    }

    /**
     * 四舍五入到小数点后一位
     *
     * @param value 值
     * @return 返回保留1位小数据点后的值
     */
    public static double roundx1(double value) {
        return Math.round(value * 10) / 10.0;
    }

    /**
     * 四舍五入到小数点后两位
     *
     * @param value 值
     * @return 返回保留2位小数据点后的值
     */
    public static double roundx2(double value) {
        return Math.round(value * 100) / 100.0;
    }

    /**
     * 四舍五入到小数点后三位
     *
     * @param value 值
     * @return 返回保留3位小数据点后的值
     */
    public static double roundx3(double value) {
        return Math.round(value * 1000) / 1000.0;
    }

    private static final ThreadLocal<DecimalFormat> roundx2Formatter = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            DecimalFormat format = new DecimalFormat("0.00");
            return format;
        }
    };

    private static final ThreadLocal<DecimalFormat> roundx4Formatter = new ThreadLocal<DecimalFormat>() {
        @Override
        protected DecimalFormat initialValue() {
            DecimalFormat format = new DecimalFormat("0.0000");
            return format;
        }
    };

    /**
     * 四舍五入到小数点后四位，返回字符串，因为返回 double toString 时会转成科学计数法
     *
     * @param value 值
     * @return 返回保留4位小数据点后的值
     */
    public static String roundx4(double value) {
        return roundx4Formatter.get().format(value);
    }

    private static final long KB_SIZE = 1024;
    private static final long MB_SIZE = 1024 * KB_SIZE;
    private static final long GB_SIZE = 1024 * MB_SIZE;
    private static final long TB_SIZE = 1024 * GB_SIZE;
    private static final long PB_SIZE = 1024 * TB_SIZE;

    /**
     * 将字节表示成恰当的其他表示法。
     *
     * @param bytes 需要转换成可读字节的数字
     * @return 返回成可读字节字符串
     */
    public static String humanReadableByteSize(long bytes) {
        boolean negative = bytes < 0;
        long fix = (bytes == Long.MIN_VALUE) ? Long.MAX_VALUE : Math.abs(bytes);
        String result;
        if (fix > PB_SIZE) {
            double pb = fix / (double) PB_SIZE;
            result = roundx1(pb) + "PB";
        } else if (fix > TB_SIZE) {
            double tb = fix / (double) TB_SIZE;
            result = roundx1(tb) + "TB";
        } else if (fix > GB_SIZE) {
            double gb = fix / (double) GB_SIZE;
            result = roundx1(gb) + "GB";
        } else if (fix > MB_SIZE) {
            double mb = fix / (double) MB_SIZE;
            result = roundx1(mb) + "MB";
        } else if (fix > KB_SIZE) {
            double kb = fix / (double) KB_SIZE;
            result = roundx1(kb) + "KB";
        } else {
            result = fix + "B";
        }
        return negative ? ("-" + result) : result;
    }

    private static final long[] TIME_SPANS_MILLIS = {
            TimeUnit.DAYS.toMillis(1), TimeUnit.HOURS.toMillis(1), //
            TimeUnit.MINUTES.toMillis(1), TimeUnit.SECONDS.toMillis(1), //
            1
    };

    private static final String[] TIME_SPANS_TEXT = {
            "d", "h", "m", "s", "ms"
    };

    private static final String[] TIME_SPANS_CHINESE_TEXT = {
            "\u5929", "\u5C0F\u65F6", "\u5206\u949F", "\u79D2", "\u6BEB\u79D2"
    };

    /**
     * 将时间表示成适合阅读的表示法。为简明起见，只显示最大的两个时间单位。
     *
     * @param millis 时间戳
     * @return 返回成可读的时间
     */
    public static String humanReadableTimeSpan(long millis) {
        return humanReadableTimeSpan(millis, 2, TIME_SPANS_TEXT);
    }

    /**
     * 将时间表示成适合阅读的中文表示法。为简明起见，只显示最大的两个时间单位。
     *
     * @param millis 时间戳
     * @return 返回成中文版可读时间
     */
    public static String humanReadableChineseTimeSpan(long millis) {
        return humanReadableTimeSpan(millis, 2, TIME_SPANS_CHINESE_TEXT);
    }

    /**
     * 将时间表示成适合阅读的表示法。为简明起见，只显示最大的 maxTimeUnit 个时间单位。
     *
     * @param millis      时间戳
     * @param maxTimeUnit 单位
     * @param textLabels  textLabels
     * @return 返回成可读时间
     */
    private static String humanReadableTimeSpan(long millis, int maxTimeUnit, String[] textLabels) {
        boolean negative = millis < 0;
        long fix = (millis == Long.MIN_VALUE) ? Long.MAX_VALUE : Math.abs(millis);
        int appendUnit = 0;
        StringBuilder appender = new StringBuilder();
        if (negative) {
            appender.append('-');
        }
        final int l = TIME_SPANS_MILLIS.length;
        for (int i = 0; i < l && fix > 0; ++i) {
            final long span = TIME_SPANS_MILLIS[i];
            if (fix >= span) {
                long unit = fix / span;
                fix %= span;
                appender.append(unit).append(textLabels[i]);
                if (++appendUnit >= maxTimeUnit) {
                    return appender.toString();
                }
            }
        }
        if (appender.length() == 0) {
            appender.append("<1").append(textLabels[l - 1]);
        }
        return appender.toString();
    }

    private static final long A_HUNDRED_MILLION = 100000000; // 亿
    private static final long TEN_THOUSAND = 10000; // 万

    /**
     * 将数字表示成适合阅读的表示法。为简明起见，只显示最大的 maxUnit 个单位。
     *
     * @param number 数字
     * @return 返回成可读数字
     */
    public static String humanReadableNumber(long number) {
        return humanReadableNumber(number, 2);
    }

    /**
     * 将数字表示成适合阅读的表示法。为简明起见，只显示最大的两个单位。
     *
     * @param number  数字
     * @param maxUnit 单位
     * @return 返回成可读数字
     */
    public static String humanReadableNumber(long number, int maxUnit) {
        boolean negative = number < 0;
        long fix = (number == Long.MIN_VALUE) ? Long.MAX_VALUE : Math.abs(number);
        StringBuilder appender = new StringBuilder();
        if (negative) {
            appender.append('-');
        }
        int appendUnit = 0;
        if (fix >= A_HUNDRED_MILLION) {
            long x = fix / A_HUNDRED_MILLION;
            fix %= A_HUNDRED_MILLION;
            appender.append(x).append('\u4ebf');
            ++appendUnit;
        }
        if (fix >= TEN_THOUSAND) {
            long x = fix / TEN_THOUSAND;
            fix %= TEN_THOUSAND;
            appender.append(x).append('\u4e07');
            if (++appendUnit >= maxUnit) {
                return appender.toString();
            }
        }
        if (appendUnit < maxUnit && (fix > 0 || appender.length() == 0)) {
            appender.append(fix);
        }
        return appender.toString();
    }

    /**
     * 使用delim拼接数组
     *
     * @param array 数组
     * @param delim 拼接字符
     * @return 返回拼接后的字符串
     */
    public static final String join(int[] array, String delim) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            builder.append(delim).append(array[i]);
        }
        return builder.toString();
    }

    /**
     * 拼接数组
     *
     * @param array 数组
     * @param delim 拼接字符串
     * @return 返回拼接后的数组字符串
     */
    public static final String join(long[] array, String delim) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            builder.append(delim).append(array[i]);
        }
        return builder.toString();
    }

    /**
     * 拼接数组
     *
     * @param array 数组
     * @param delim 拼接字符串
     * @return 返回拼接后的数组字符串
     */
    public static final String join(Object[] array, String delim) {
        return array == null ? "" : join(array, 0, array.length, delim);
    }

    /**
     * 拼接数组
     *
     * @param array    数组
     * @param startPos 开始索引
     * @param endPos   结束索引
     * @param delim    拼接字符串
     * @return 返回拼接后的数组字符串
     */
    public static final String join(Object[] array, int startPos, int endPos, String delim) {
        if (ArrayUtils.isEmpty(array) || startPos >= endPos) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(array[startPos].toString());
        for (int i = startPos + 1; i < endPos; ++i) {
            builder.append(delim).append(array[i].toString());
        }
        return builder.toString();
    }

    /**
     * 拼接集合
     *
     * @param cl    集合
     * @param delim 拼接字符串
     * @return 返回集合拼接后的字符串
     */
    public static final String join(Collection<?> cl, String delim) {
        if (CollectionUtils.isEmpty(cl)) {
            return "";
        }
        return join(cl.iterator(), delim);
    }

    /**
     * 拼接字符串
     *
     * @param it    需要拼接的迭代器
     * @param delim 拼接字符串
     * @return 返回拼接后的迭代器字符串
     */
    public static String join(Iterator<?> it, String delim) {
        if (it == null || !it.hasNext()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(it.next().toString());
        while (it.hasNext()) {
            builder.append(delim).append(it.next().toString());
        }
        return builder.toString();
    }

    /**
     * 字符串超过指定长度，将被截断到 length 长，末尾追加省略号
     *
     * @param str    字符串
     * @param length 长度
     * @return 返回截断后的字符串
     */
    public static final String truncateWithEllipsis(String str, int length) {
        if (str == null || str.length() < length || length < 4) {
            return str;
        }
        return str.substring(0, length - 3) + "...";
    }

    /**
     * 返回比较后的相差时间字符串
     *
     * @param num  需要比较的时间戳
     * @param base 基础时间戳
     * @return 返回比较后的时间
     */
    public static final String diffTime(long num, long base) {
        long diff = num - base;
        return diff == 0 ? "+0ms" : (diff > 0 ? ("+" + humanReadableTimeSpan(diff)) : humanReadableTimeSpan(diff));
    }

    /**
     * encode url
     *
     * @param url 需要编码的url
     * @return 返回编码后的url
     */
    public static final String urlEncode(String url) {
        if (StringUtils.isNotBlank(url)) {
            try {
                return URLEncoder.encode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            }
        }
        return url;
    }

    /**
     * 获取百分比值
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 返回百分比字符串
     */
    public static final String percent(double dividend, double divisor) {
        return divisor != 0.0 ? percent(dividend / divisor) : (dividend != 0.0 ? "-" : "0.00%");
    }

    /**
     * 获取百分比值
     *
     * @param value 除数
     * @return 返回百分比字符串
     */
    public static final String percent(double value) {
        if (value < 0.00005 && value > -0.00005) {
            return "0.00%";
        }
        return roundx2(value * 100) + "%";
    }

    /**
     * 获取百分比值
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 返回百分比字符串
     */
    public static final String percentX(double dividend, double divisor) {
        return divisor != 0.0 ? percentX(dividend / divisor) : (dividend != 0.0 ? "-" : "0.00");
    }

    /**
     * 获取百分比值
     *
     * @param value 除数
     * @return 返回百分比字符串
     */
    public static final String percentX(double value) {
        if (value > 1) {
            return String.valueOf(roundx2Formatter.get().format(value));
        }
        if (value < 0.00005 && value > -0.00005) {
            return "0.00";
        }
        return String.valueOf(roundx2Formatter.get().format(value * 100));
    }

    /**
     * 获取百分比值
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 返回百分比字符串
     */
    public static final String percentx0(double dividend, double divisor) {
        return divisor != 0.0 ? percentx0(dividend / divisor) : (dividend != 0.0 ? "-" : "0%");
    }

    /**
     * 获取百分比值
     *
     * @param value 除数
     * @return 返回百分比字符串
     */
    public static final String percentx0(double value) {
        if (value < 0.005 && value > -0.005) {
            return "0%";
        }
        return roundx0(value * 100) + "%";
    }

    /**
     * 获取百分比值
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 返回百分比字符串
     */
    public static final String percentx1(double dividend, double divisor) {
        return divisor != 0.0 ? percentx1(dividend / divisor) : (dividend != 0.0 ? "-" : "0.0%");
    }

    /**
     * 获取百分比值
     *
     * @param value 除数
     * @return 返回百分比字符串
     */
    public static final String percentx1(double value) {
        if (value < 0.0005 && value > -0.0005) {
            return "0.0%";
        }
        return roundx1(value * 100) + "%";
    }

    /**
     * escape 字符
     *
     * @param c 需要escape的字符
     * @return escape后的字符
     */
    public static final String escapeToUnicodeString(char c) {
        return "\\u" + Integer.toHexString(c | 0x10000).substring(1);
    }

    /**
     * 获取对象唯一编码
     *
     * @param obj 目标对象
     * @return 返回对象唯一编码
     */
    public static final String getIdentityCode(Object obj) {
        if (obj == null) {
            return "(null)";
        } else {
            return "(0x" + Integer.toHexString(System.identityHashCode(obj)) + ")";
        }
    }

    /**
     * 在 CamelCase 风格的字符串的大写字符前加上隐形空格（让浏览器自动换行），隐形空格之间的间隔至少为 step 个字符
     *
     * @param appender 输出
     * @param str      待处理的字符串
     * @param start    开始位置
     * @param end      结束位置
     * @param step     间隔
     */
    public static final void appendWithSoftLineBreak(StringBuilder appender, String str, int start, int end, int step) {
        int i = start;
        int inc = 0;
        while (i < end) {
            char c = str.charAt(i);
            if (inc < step) {
                ++inc;
            } else if (inc == step && c >= 'A' && c <= 'Z') {
                appender.append("&#8203;");
                inc = 0;
            }
            appender.append(c);
            ++i;
        }
    }

    /**
     * 获取绝对值
     *
     * @param a 目标数
     * @return 返回绝对值
     */
    public static final float abs(float a) {
        return Math.abs(a);
    }

    /**
     * 获取异常堆栈信息
     *
     * @param e 异常
     * @return 返回异常堆栈信息的字符串
     */
    public static final String getExceptionStackTrace(Exception e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter pw = new PrintWriter(out);
        e.printStackTrace(pw);
        pw.close();
        return new String(out.toByteArray());

    }

}
