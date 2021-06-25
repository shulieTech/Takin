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

package io.shulie.tro.cloud.common.utils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

/**
 * 基于JDK 1.8 日期API
 *
 * @Author: xingchen
 * @ClassName: DateUtils
 * @Package: cn.net.yto.bnet.screen.util
 * @Date: 2019/03/10下午2:21
 * @Description:
 */
public class DateUtil {
    public static String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    private static String YYYYMMDDHHMMSSMS = "yyyy-MM-dd HH:mm:ss.SSS";
    private static String YYYYMMDD = "yyyy-MM-dd";
    private static String YYYYMM = "yyyy-MM";

    public static String formatTime(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYYMMDDHHMMSS);
        String format = dateFormat.format(timestamp);
        return format;
    }

    /**
     * 将日期转为字符串 yyyy-mm-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String getYYYYMMDDHHMMSS(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);
        LocalDateTime localDateTime = dateToDateTime(date);
        return formatter.format(localDateTime);
    }

    /**
     * 将日期转为字符串 yyyy-mm-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String getDate(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = dateToDateTime(date);
        return formatter.format(localDateTime);
    }

    /**
     * 将字符串转为日期
     *
     * @param date
     * @return
     */
    public static Date getDate(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSS);
        LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
        return dateTimeToDate(localDateTime);
    }

    /**
     * 将字符串转为日期
     *
     * @param date
     * @return
     */
    public static Date getMSDate(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(YYYYMMDDHHMMSSMS);
        LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
        return dateTimeToDate(localDateTime);
    }

    /**
     * 将字符串转为日期
     *
     * @param date
     * @return
     */
    public static Date getDate(String date, String pattern) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime localDateTime = LocalDateTime.parse(date, dateTimeFormatter);
        return dateTimeToDate(localDateTime);
    }

    /**
     * LocalDateTime 和DateTime 互转
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToDateTime(Date date) {
        Instant instant = date.toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    public static Date dateTimeToDate(LocalDateTime localDateTime) {
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return date;
    }

    /**
     * date 转LocalDate
     *
     * @param date
     * @return
     */
    public static LocalDate dateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate;
    }

    /**
     * date 转LocalDate
     *
     * @param localDate
     * @return
     */
    public static Date localToDate(LocalDate localDate) {
        Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        return date;
    }

    /**
     * date 转DateTime
     *
     * @param date
     * @return
     */
    public static LocalTime dateToLocalTime(Date date) {
        Instant instant = date.toInstant();
        LocalTime localTime = instant.atZone(ZoneId.systemDefault()).toLocalTime();
        return localTime;
    }

    /**
     * date 转LocalTime
     *
     * @param localTime
     * @return
     */
    public static Date localToDate(LocalTime localTime) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /**
     * 将日期转为字符串 yyyy-mm-dd
     *
     * @param date
     * @return
     */
    public static String getYYYYMMDD(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYYMMDD);
        LocalDateTime localDateTime = dateToDateTime(date);
        return formatter.format(localDateTime);
    }

    /**
     * 将日期转为字符串 yyyy-mm-dd
     *
     * @param date
     * @return
     */
    public static String getYYYYMM(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYYMM);
        LocalDateTime localDateTime = dateToDateTime(date);
        return formatter.format(localDateTime);
    }

    /**
     * 获取当前时间之后的某一天的最小时间
     *
     * @param date
     * @return
     */
    public static Date afterXDateTimeMIN(Date date, int after) {
        LocalDateTime localDateTime = dateToDateTime(date);
        localDateTime = localDateTime.plusDays(after);
        localDateTime = localDateTime.with(LocalTime.MAX);
        return dateTimeToDate(localDateTime);
    }

    /**
     * 获取当前时间之后的某一天的最大时间
     *
     * @param date
     * @return
     */
    public static Date afterXDateTimeMAX(Date date, int after) {
        LocalDateTime localDateTime = dateToDateTime(date);
        localDateTime = localDateTime.plusDays(after);
        localDateTime = localDateTime.with(LocalTime.MAX);
        return dateTimeToDate(localDateTime);
    }

    /**
     * 获取当前时间之前的某一天的最小时间
     *
     * @param date
     * @return
     */
    public static Date beforeXDateTimeMIN(Date date, int before) {
        LocalDateTime localDateTime = dateToDateTime(date);
        localDateTime = localDateTime.minusDays(before);
        localDateTime = localDateTime.with(LocalTime.MIN);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取某天的几点
     *
     * @param date
     * @param time
     * @return
     */
    public static Date getTimeByDay(Date date, int time) {
        LocalDateTime localDateTime = dateToDateTime(date);
        localDateTime = localDateTime.with(LocalTime.MIN);
        localDateTime = localDateTime.withHour(time);
        return dateTimeToDate(localDateTime);
    }

    /**
     * 获取当前时间之前的某一天的最大时间
     *
     * @param date
     * @return
     */
    public static Date beforeXDateTimeMAX(Date date, int before) {
        LocalDateTime localDateTime = dateToDateTime(date);
        localDateTime = localDateTime.minusDays(before);
        localDateTime = localDateTime.with(LocalTime.MAX);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取本月的第一天 00:00:00
     *
     * @return
     */
    public static Date currentFirstDayOfMonth() {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        return dateTimeToDate(localDateTime);
    }

    /**
     * 获取前几个月的1号0点 00:00:00
     *
     * @return
     */
    public static Date preXDayOfMonthMIN(int month) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusMonths(month);
        localDateTime = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        return dateTimeToDate(localDateTime);
    }

    /**
     * 获取前几个月的1号0点 00:00:00
     *
     * @return
     */
    public static Date preXDayOfMonthMIN(Date date, int month) {
        LocalDateTime localDateTime = dateToDateTime(date);
        localDateTime = localDateTime.minusMonths(month);
        localDateTime = localDateTime.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
        return dateTimeToDate(localDateTime);
    }

    /**
     * 获取前几个月的最后一天23：59：59
     *
     * @return
     */
    public static Date preXDayOfMonthMAX(int month) {
        LocalDateTime localDateTime = LocalDateTime.now();
        localDateTime = localDateTime.minusMonths(month);
        localDateTime = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        return dateTimeToDate(localDateTime);
    }

    /**
     * 获取某个时间几个月的最后一天23：59：59
     *
     * @return
     */
    public static Date preXDayOfMonthMAX(Date date, int month) {
        LocalDateTime localDateTime = dateToDateTime(date);
        localDateTime = localDateTime.minusMonths(month);
        localDateTime = localDateTime.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
        return dateTimeToDate(localDateTime);
    }

    /**
     * 两个日期相差多少个月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Long getUntilMonth(Date date1, Date date2) {
        LocalDate localDate1 = dateToLocalDate(date1);
        LocalDate localDate2 = dateToLocalDate(date2);
        return ChronoUnit.DAYS.between(localDate1, localDate2);
    }

    /**
     * 两个日期相差多少小时
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Long getUntilHours(Date date1, Date date2) {
        LocalDateTime localDate1 = dateToDateTime(date1);
        LocalDateTime localDate2 = dateToDateTime(date2);
        Long senonds = Duration.between(localDate1, localDate2).get(ChronoUnit.SECONDS);
        return senonds / 3600;
    }

    /**
     * 两个日期相差多少小时 double 约等于
     *
     * @param date1
     * @param date2
     * @return
     */
    public static double getUntilHoursByDouble(Date date1, Date date2) {
        LocalDateTime localDate1 = dateToDateTime(date1);
        LocalDateTime localDate2 = dateToDateTime(date2);
        Long seconds = Duration.between(localDate1, localDate2).get(ChronoUnit.SECONDS);
        BigDecimal secondss = BigDecimal.valueOf(seconds);
        BigDecimal hours = secondss.divide(BigDecimal.valueOf(3600), 2, BigDecimal.ROUND_HALF_UP);
        return hours.doubleValue();
    }

    /**
     * 两个日期相差多少秒
     *
     * @param date1
     * @param date2
     * @return
     */
    public static Long getUntilSecond(Date date1, Date date2) {
        LocalDateTime localDate1 = dateToDateTime(date1);
        LocalDateTime localDate2 = dateToDateTime(date2);
        return Math.abs(Duration.between(localDate1, localDate2).get(ChronoUnit.SECONDS));
    }

    /**
     * 获取当前时间后几分钟的时间
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date afterXMinuteDate(Date date, long minute) {
        LocalDateTime localDate = dateToDateTime(date);
        LocalDateTime afterTime = localDate.plusMinutes(minute);
        return dateTimeToDate(afterTime);
    }

    /**
     * 获取当前时间后几分钟的时间
     *
     * @param date
     * @param minute
     * @return
     */
    public static Date preXMinuteDate(Date date, long minute) {
        LocalDateTime localDate = dateToDateTime(date);
        LocalDateTime afterTime = localDate.minusMinutes(minute);
        return dateTimeToDate(afterTime);
    }

    public static void main(String args[]) {
        //LocalDateTime localDateTime = LocalDateTime.now();

        //System.out.println(localDateTime.getDayOfMonth());
        //System.out.println(getYYYYMMDDHHMMSS(DateUtils.preXDayOfMonthMAX(new Date(), 1)));
        System.out.println(DateUtil.getYYYYMMDDHHMMSS(preXMinuteDate(new Date(), 11)));
    }

    /**
     * 当前时间23：59：59
     *
     * @param date
     * @return
     */
    public static Date currentMax(Date date) {
        LocalDateTime localDateTime = dateToDateTime(date);
        localDateTime = localDateTime.with(LocalTime.MAX);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 当前时间23：59：59
     *
     * @param date
     * @return
     */
    public static Date currentMin(Date date) {
        LocalDateTime localDateTime = dateToDateTime(date);
        localDateTime = localDateTime.with(LocalTime.MIN);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static long getMinuteTimestamp(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * 获取昨天同期时间戳
     *
     * @param time 格式化时间
     * @return
     */
    public static long getYesterday(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTimeInMillis();
    }

    public static long getLastWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.WEEK_OF_MONTH, -1);
        return calendar.getTimeInMillis();
    }

    public static long getLastMonth(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.MONTH, -1);
        return calendar.getTimeInMillis();
    }

    public static long getLastYear(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.add(Calendar.YEAR, -1);
        return calendar.getTimeInMillis();
    }

    public static long getLastHour(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        return calendar.getTimeInMillis();
    }

    public static Date addMonth(Date currentDate, int month) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.MONTH, month);
        return c.getTime();
    }

    public static Date addHour(Date currentDate, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        c.add(Calendar.HOUR, hour);
        return c.getTime();
    }

    /**
     * 压测时长：
     *  无启动时间，返回null
     *  无停止时间，返回null
     *  停止时间-启动时间
     * @param startTime
     * @param endTime
     * @return
     */
    public static String formatTestTime(Date startTime, Date endTime){
        if(startTime == null || endTime == null) {
            return null;
        }
        LocalDateTime start = startTime.toInstant().atZone( ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = endTime.toInstant().atZone( ZoneId.systemDefault()).toLocalDateTime();
        long seconds = Duration.between(start, end).getSeconds();
        long hour = seconds / 3600;
        long minutes = seconds / 60;
        long second = seconds % 60;
        return String.format("%dh %d'%d\"", hour, minutes, second);
    }
}
