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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.pamirs.tro.common.constant.TimeUnits;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日期工具类
 *
 * @author shulie
 * @version v1.0
 * @2018年5月21日
 */
@SuppressWarnings("all")
public class DateUtils {

    //日志打印
    public static final Logger LOGGER = LoggerFactory.getLogger(DateUtils.class);
    //日期格式：yyyy-MM-dd
    public static final String FORMATE_DATE = "yyyy-MM-dd";
    //日期格式：HH:mm:ss
    public static final String FORMATE_SECONDS = "HH:mm:ss";
    //日期格式：yyyy-MM-dd HH:mm:ss
    public static final String FORMAT = FORMATE_DATE.concat(" ").concat(FORMATE_SECONDS);
    //日期格式：yyyy-MM
    public static final String FORMATE_YM = "yyyy-MM";
    //日期格式：yyyy-MM-dd HH
    public static final String FORMATE_YMDH = "yyyy-MM-dd HH";
    //日期格式：yyyy-MM-dd HH:mm
    public static final String FORMATE_YMDHM = "yyyy-MM-dd HH:mm";
    //日期格式：yyyy-MM-dd HH:mm:ss
    public static final String FORMATE_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    //日期格式：yyyy
    private static final String FORMATE_Y = "yyyy";
    //日期格式：MM
    private static final String FORMATE_M = "MM";
    //日期格式：dd
    private static final String FORMATE_D = "dd";
    //日期格式：HH
    private static final String FORMATE_H = "HH";
    //日期格式：mm
    private static final String FORMATE_MM = "mm";
    //日期格式：ss
    private static final String FORMATE_SS = "ss";
    //日期格式：E
    private static final String FORMATE_W = "E";
    //日期格式
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * DateTime to Str yyyy-MM-dd HH:mm:ss
     *
     * @param Datetime 时间
     * @return 时间字符串
     */
    public static String DateToStr(java.time.LocalDateTime Datetime) {
        String time = Datetime.format(DateTimeFormatter.ofPattern(FORMAT));
        return time;
    }

    public static Date strToDate(String timeStr, String timeFormat) {
        if (StringUtils.isBlank(timeFormat)) {
            timeFormat = FORMATE_YMDHMS;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat);
        Date dateTime = null;
        try {
            dateTime = simpleDateFormat.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    /**
     * @return 时间字符串
     */
    public static String getNowDateStr() {
        String dateStr = new SimpleDateFormat(FORMATE_YMDHMS).format(new Date());
        return dateStr;
    }

    public static String transferDate(Object obj) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMATE_YMDHMS);
        if (obj instanceof Long) {
            long date = (Long)obj;
            return sdf.format(new Date(date));
        } else if (obj instanceof String) {
            Date date = new Date(Long.parseLong((String)obj));
            return sdf.format(date);
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT);
            return simpleDateFormat.format((Date)obj);
        }
    }

    /**
     * 说明：转换成string类型时间
     *
     * @param obj
     * @return
     * @author shulie
     * @time：2017年12月1日 下午1:56:18
     */
    public static String transferDateToString(Object obj) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
        if (obj instanceof Long) {
            long date = (Long)obj;
            return sdf.format(new Date(date));
        } else if (obj instanceof String) {
            Date date = new Date(Long.parseLong((String)obj));
            return sdf.format(date);
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT);
            return simpleDateFormat.format((Date)obj);
        }
    }

    /**
     * 说明：获取天数
     *
     * @return
     * @author shulie
     * @time：2017年12月13日 下午4:57:20
     */
    public static int getDayOfMonth() {
        return LocalDate.now().getDayOfMonth();
    }

    /**
     * 说明：转换时间成date
     *
     * @param str
     * @return
     * @author shulie
     * @time：2017年10月12日 下午7:37:32
     */
    public static Date transferTime(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 说明： 转换时间
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date getDateTime(String date) throws ParseException {
        if (date == null || "".equals(date)) {
            return null;
        }
        return new SimpleDateFormat(FORMATE_YMDHM).parse(date);
    }

    /**
     * 说明：根据格式匹配转换日期
     *
     * @param f       需要转换的日期字符串或者日期
     * @param pattern 转换模式
     * @param t       返回类型为Date或者String
     * @return 返回转换后的日期
     * @throws InstantiationException InstantiationException
     * @throws IllegalAccessException 非法访问异常
     * @author shulie
     * @time：2018年2月2日 下午3:40:07
     */
    public static <T, F> T transferDate(F f, String pattern, Class<T> t) {

        if (!(f instanceof String) && !(f instanceof Date)) {
            throw new RuntimeException("带转换的日期只能为String类型或者Date类型");
        }

        try {
            if (!(t.newInstance() instanceof String) && !(t.newInstance() instanceof Date)) {
                throw new RuntimeException("转换后的日期只能为String类型或者Date类型");
            }
        } catch (Exception e) {
            LOGGER.error("转换日期反射异常{}", e);
        }

        SimpleDateFormat sdf = new SimpleDateFormat(FORMATE_YMDHMS, Locale.CHINA);
        ;

        if (pattern.equals(FORMATE_Y)) {
            sdf = new SimpleDateFormat(FORMATE_Y, Locale.CHINA);
        }

        if (pattern.equals(FORMATE_M)) {
            sdf = new SimpleDateFormat(FORMATE_M, Locale.CHINA);
        }

        if (pattern.equals(FORMATE_D)) {
            sdf = new SimpleDateFormat(FORMATE_D, Locale.CHINA);
        }

        if (pattern.equals(FORMATE_H)) {
            sdf = new SimpleDateFormat(FORMATE_H, Locale.CHINA);
        }

        if (pattern.equals(FORMATE_MM)) {
            sdf = new SimpleDateFormat(FORMATE_MM, Locale.CHINA);
        }

        if (pattern.equals(FORMATE_SS)) {
            sdf = new SimpleDateFormat(FORMATE_SS, Locale.CHINA);
        }

        if (pattern.equals(FORMATE_W)) {
            sdf = new SimpleDateFormat(FORMATE_W, Locale.CHINA);
        }

        if (pattern.equals(FORMATE_YM)) {
            sdf = new SimpleDateFormat(FORMATE_YM, Locale.CHINA);
        }

        if (pattern.equals(FORMATE_DATE)) {
            sdf = new SimpleDateFormat(FORMATE_DATE, Locale.CHINA);
        }

        if (pattern.equals(FORMATE_YMDH)) {
            sdf = new SimpleDateFormat(FORMATE_YMDH, Locale.CHINA);
        }

        if (pattern.equals(FORMATE_YMDHM)) {
            sdf = new SimpleDateFormat(FORMATE_YMDHM, Locale.CHINA);
        }

        if (pattern.equals(FORMATE_YMDHMS)) {
            sdf = new SimpleDateFormat(FORMATE_YMDHMS, Locale.CHINA);
        }

        try {
            if (f instanceof Date) {
                Date date = (Date)f;
                if (t.newInstance() instanceof Date) {
                    return (T)date;
                }
                return (T)sdf.format(date);
            }

            if (f instanceof String) {
                String str = (String)f;
                if (t.newInstance() instanceof Date) {
                    return (T)sdf.parse(str);
                }

                return (T)str;
            }
        } catch (Exception e) {
            LOGGER.error("转换日期反射异常{}", e);
        }
        return null;
    }

    public static LocalDate covertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 说明：获取前n天的时间
     *
     * @param n 天数
     * @return 前n天的时间
     * @author shulie
     * @time：2017年9月18日 下午6:14:37
     */
    public static Date getPreviousNDay(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, -n);
        return calendar.getTime();
    }

    /**
     * 说明：获取前n秒的时间
     *
     * @param n 秒
     * @return 前n天的时间
     * @author shulie
     * @time：2017年9月18日 下午6:14:37
     */
    public static Date getPreviousNSecond(int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.SECOND, -n);
        return calendar.getTime();
    }

    /**
     * 说明：获取时间
     *
     * @param year       年
     * @param month      月
     * @param dayOfMonth 日
     * @return 时间
     * @author shulie
     * @time：2017年10月12日 下午4:46:14
     */
    public static Date getDate(int year, int month, int dayOfMonth) {
        LocalDate startDateTime = LocalDate.of(year, month, dayOfMonth);
        return covertToDate(startDateTime);
    }

    /**
     * 说明: 自定义时间返回date类型
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minutes
     * @param second
     * @return 自定义日期date类型
     * @author shulie
     * @date 2018/6/28 17:13
     */
    public static Date getDateByCustomizeToDate(int year, int month, int day, int hour, int minutes, int second) {
        return new DateTime(year, month, day, hour, minutes, second).toDate();
    }

    /**
     * 说明: 自定义时间返回String类型
     *
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minutes
     * @param second
     * @return 自定义日期String类型
     * @author shulie
     * @date 2018/6/28 17:13
     */
    public static String getDateByCustomizeToString(int year, int month, int day, int hour, int minutes, int second) {
        return transferDateToString(new DateTime(year, month, day, hour, minutes, second).toDate());
    }

    /**
     * 说明：LocalDate转换成Date类型
     *
     * @param ld 本地日期
     * @return Date类型日期
     * @author shulie
     * @time：2017年12月4日 上午11:03:16
     */
    public static Date covertToDate(LocalDate ld) {
        return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 说明：获取系统时间到毫秒值
     *
     * @return 系统时间毫秒值
     * @author shulie
     * @time：2017年10月1日 下午5:01:44
     */
    public static String getServerTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT);
        String serverTime = simpleDateFormat.format(date);
        return serverTime;
    }

    /**
     * 说明：获取时间段集合()
     *
     * @param timeStart 开始时间
     * @param timeEnd   结束时间
     * @return 时间段集合
     * @author shulie
     * @time：2017年10月12日 下午4:48:05
     */
    public static List<String> show(String timeStart, String timeEnd) {
        try {
            Date begin = sdf.parse(timeStart);
            Date end = sdf.parse(timeEnd);
            List<String> timeList = new ArrayList<String>();
            Calendar cal = Calendar.getInstance();
            while (begin.before(end)) {
                timeList.add(sdf.format(begin));
                cal.setTime(begin);
                cal.add(Calendar.DAY_OF_MONTH, 1);
                begin = cal.getTime();
            }
            return timeList;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 说明：获取时间段集合
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 时间段集合
     * @author shulie
     * @time：2017年10月12日 下午4:48:32
     */
    public static List<String> collectLocalDates(LocalDate start, LocalDate end) {
        // 用起始时间作为流的源头，按照每次加一天的方式创建一个无限流
        return Stream.iterate(start, localDate -> localDate.plusDays(1))
            // 截断无限流，长度为起始时间和结束时间的差+1个
            .limit(ChronoUnit.DAYS.between(start, end) + 1)
            // 由于最后要的是字符串，所以map转换一下
            .map(LocalDate::toString)
            // 把流收集为List
            .collect(Collectors.toList());
    }

    /**
     * 说明：转换时间
     *
     * @param year       年
     * @param startMonth 开始月份
     * @param startDay   开始天
     * @param endMonth   结束月份
     * @param endDay     结束天
     * @return 时间集合
     * @author shulie
     * @time：2017年11月23日 下午4:24:21
     */
    public static List<Date> transferTime(int year, int startMonth, int startDay, int endMonth, int endDay) {
        LocalDate timeStart = LocalDate.of(year, startMonth, startDay);
        LocalDate timeEnd = LocalDate.of(year, endMonth, endDay);
        List<String> collectLocalDates = collectLocalDates(timeStart, timeEnd);
        List<Date> list = Lists.newArrayList();
        for (int i = 0; i < collectLocalDates.size(); i++) {
            try {
                Date date = sdf.parse(collectLocalDates.get(i));
                list.add(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return list;
    }

    /**
     * 说明：date类型转换成string
     *
     * @param date 日期
     * @return 字符串型日期
     * @author shulie
     * @time：2017年10月13日 下午2:15:42
     */
    public static String dateToString(Date date) {
        return sdf.format(date);
    }

    /**
     * date类型转换成string
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date 日期
     * @return 指定格式的字符串日期
     */
    public static String dateToString(Date date, String format) {
        format = StringUtils.defaultIfEmpty(format, FORMAT);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    /**
     * 返回几分钟前的日期
     *
     * @param date 日期
     * @param min  分钟
     * @return 几分钟前的日期
     * @author shulie
     * @2018年5月21日
     * @version v1.0
     */
    public static String returnPre(Date date, int min) {
        return transferDateToString(returnPreToDate(date, min));
    }

    /**
     * 说明：当前时间和传入的时间相差的时间
     *
     * @param date      待比较的时间
     * @param timeUnits 时间枚举类
     * @return ① 相差的天数 ② 相差的小时数 ② 相差的分钟数 ② 相差的毫秒数
     * @author shulie
     * @time：2017年12月13日 下午5:33:09
     */
    public static long gapTime(Date beginDate, Date endDate, TimeUnits timeUnits) {
        //
        //		DateTime beginTime = new DateTime(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthOfYear(),
        //				LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getHourOfDay(),
        //				LocalDateTime.now().getMinuteOfHour(), LocalDateTime.now().getSecondOfMinute());

        DateTime beginTime = new DateTime(beginDate);
        DateTime compareTime = new DateTime(endDate);

        Duration duration = new Duration(beginTime, compareTime);

        switch (timeUnits) {
            case DAY:
                return duration.getStandardDays();

            case HOUR:
                return duration.getStandardHours();

            case MINUTES:
                return duration.getStandardMinutes();

            case SECOND:
                return duration.getStandardSeconds();

            default:
                return beginTime.getMillis() - endDate.getTime();
        }
    }

    /**
     * 返回几分钟前的日期
     *
     * @param date 日期
     * @param min  分钟
     * @return 几分钟前的日期
     * @author shulie
     * @2018年5月21日
     * @version v1.0
     */
    public static Date returnPreToDate(Date date, int min) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -min);
        return calendar.getTime();
    }

    public static List<String> getRangeSet(String begin, String end) {
        SimpleDateFormat sdf = null;
        List<String> rangeSet = null;
        Date begin_date = null;
        Date end_date = null;
        rangeSet = new java.util.ArrayList<String>();
        sdf = new SimpleDateFormat("yyyy-MM");
        try {
            begin_date = sdf.parse(begin);//定义起始日期
            end_date = sdf.parse(end);//定义结束日期
        } catch (ParseException e) {
            System.out.println("时间转化异常，请检查你的时间格式是否为yyyy-MM或yyyy-MM-dd");
        }
        Calendar dd = Calendar.getInstance();//定义日期实例
        dd.setTime(begin_date);//设置日期起始时间
        while (!dd.getTime().after(end_date)) {//判断是否到结束日期
            rangeSet.add(sdf.format(dd.getTime()));
            dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        return rangeSet;
    }

    public static List<java.util.Date> getRangeSet2(String begin, String end) {
        SimpleDateFormat sdf = null;
        List<Date> rangeSet = null;
        Date begin_date = null;
        Date end_date = null;
        rangeSet = new java.util.ArrayList<Date>();
        sdf = new SimpleDateFormat("yyyy-MM");
        try {
            begin_date = sdf.parse(begin);//定义起始日期
            end_date = sdf.parse(end);//定义结束日期
        } catch (ParseException e) {
            System.out.println("时间转化异常，请检查你的时间格式是否为yyyy-MM或yyyy-MM-dd");
        }
        Calendar dd = Calendar.getInstance();//定义日期实例
        dd.setTime(begin_date);//设置日期起始时间
        while (!dd.getTime().after(end_date)) {//判断是否到结束日期
            rangeSet.add(dd.getTime());
            dd.add(Calendar.MONTH, 1);//进行当前日期月份加1
        }
        return rangeSet;
    }

    public static String preYear(java.util.Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, -1);
        Date y = c.getTime();
        String year = format.format(y);
        return year;
    }

    public static String getMonthEnglishName(Date date) {
        if (date == null) {
            return null;
        }
        String result = null;
        String dateStr = String.valueOf(date);
        if (dateStr.contains("Jan")) {
            return "Jan";
        }
        if (dateStr.contains("Feb")) {
            return "Feb";
        }
        if (dateStr.contains("Mar")) {
            return "Mar";
        }
        if (dateStr.contains("Apr")) {
            return "Apr";
        }
        if (dateStr.contains("May")) {
            return "May";
        }
        if (dateStr.contains("Jun")) {
            return "Jun";
        }
        if (dateStr.contains("Jul")) {
            return "Jul";
        }
        if (dateStr.contains("Aug")) {
            return "Aug";
        }
        if (dateStr.contains("Sep")) {
            return "Sep";
        }
        if (dateStr.contains("Oct")) {
            return "Oct";
        }
        if (dateStr.contains("Nov")) {
            return "Nov";
        }
        if (dateStr.contains("Dec")) {
            return "Nov";
        }
        return null;
    }

    public static String getDayStartTime(String date) {
        if (StringUtils.isBlank(date) || date.length() < 10) {
            return null;
        }
        String common = date.substring(0, 10);
        return common + " 00:00:00";

    }

    public static String getDayEndTime(String date) {
        if (StringUtils.isBlank(date) || date.length() < 10) {
            return null;
        }
        String common = date.substring(0, 10);
        return common + " 23:59:59";
    }

    public static void main(String[] args) {
        List<java.util.Date> result = getRangeSet2("2018-10-10", "2019-10-10");
        System.out.println(result.toString());

    }
}
