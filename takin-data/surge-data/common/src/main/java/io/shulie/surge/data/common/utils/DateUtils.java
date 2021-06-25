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

package io.shulie.surge.data.common.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * 主要提供直接基于 long 的基础时间操作，比使用 {@link Date} 要快很多
 * 
 * @author pamirs
 */
public class DateUtils {

	public final static int TIME_ZONE = 8 * 60 * 60 * 1000;
	public final static int BASE_TIME = 24 * 60 * 60 * 1000;

	public static Date addSeconds(Date date, int seconds) {
		return new Date(date.getTime() + TimeUnit.SECONDS.toMillis(seconds));
	}

	public static Date addMinutes(Date date, int minutes) {
		return new Date(date.getTime() + TimeUnit.MINUTES.toMillis(minutes));
	}

	public static Date addHours(Date date, int hours) {
		return new Date(date.getTime() + TimeUnit.HOURS.toMillis(hours));
	}

	public static Date addDays(Date date, int days) {
		return new Date(date.getTime() + TimeUnit.DAYS.toMillis(days));
	}

	public static Date addWeeks(Date date, int weeks) {
		return new Date(date.getTime() + TimeUnit.DAYS.toMillis(weeks * 7));
	}

	public static Date addMonths(Date date, int months) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		return c.getTime();
	}

	public static Date addYears(Date date, int years) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, years);
		return c.getTime();
	}

	public static Date today() {
		return new Date(todayInMillis());
	}

	public static Date yesterday() {
		return new Date(yesterdayInMillis());
	}

	public static Date tomorrow() {
		return new Date(tomorrowInMillis());
	}

	public static long addSeconds(long date, int seconds) {
		return date + TimeUnit.SECONDS.toMillis(seconds);
	}

	public static long addMinutes(long date, int minutes) {
		return date + TimeUnit.MINUTES.toMillis(minutes);
	}

	public static long addHours(long date, int hours) {
		return date + TimeUnit.HOURS.toMillis(hours);
	}

	public static long addDays(long date, int days) {
		return date + TimeUnit.DAYS.toMillis(days);
	}

	public static long addWeeks(long date, int weeks) {
		return date + TimeUnit.DAYS.toMillis(weeks * 7);
	}

	/**
	 * Truncate to yyyy-01-01 00:00:00.000
	 *
	 * @param date
	 * @return
	 */
	public static long truncateToYear(long date) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		int daysToRemove = c.get(Calendar.DAY_OF_YEAR) - 1;
		date -= TimeUnit.DAYS.toMillis(1) * daysToRemove;
		return truncateToDay(date);
	}

	/**
	 * Truncate to yyyy-MM-01 00:00:00.000
	 *
	 * @param date
	 * @return
	 */
	public static long truncateToMonth(long date) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(date);
		int daysToRemove = c.get(Calendar.DAY_OF_MONTH) - 1;
		date -= TimeUnit.DAYS.toMillis(1) * daysToRemove;
		return truncateToDay(date);
	}

	/**
	 * 受时区影响，比如中国的东八区，timeMillis 的 0 表示 1970-01-01 08:00:00， 在计算天数的时候，需要先加上这个
	 * offset 才整除，之后再把时间差减回来。
	 */
	private static final int TIME_ZONE_OFFSET = TimeZone.getDefault().getOffset(0);

	public static int getTimeZoneOffset() {
		return TIME_ZONE_OFFSET;
	}

	/**
	 * Truncate to yyyy-MM-dd 00:00:00.000
	 *
	 * @param date
	 * @return
	 */
	public static long truncateToDay(long date) {
		long day = TimeUnit.DAYS.toMillis(1);
		date = date + TIME_ZONE_OFFSET;
		return date - date % day - TIME_ZONE_OFFSET;
	}

	/**
	 * Truncate to yyyy-MM-dd HH:00:00.000
	 *
	 * @param date
	 * @return
	 */
	public static long truncateToHour(long date) {
		long hour = TimeUnit.HOURS.toMillis(1);
		return date - date % hour;
	}

	/**
	 * Truncate to yyyy-MM-dd HH:mm:00.000
	 *
	 * @param date
	 * @return
	 */
	public static long truncateToMinute(long date) {
		long minute = TimeUnit.MINUTES.toMillis(1);
		return date - date % minute;
	}

	/**
	 * Truncate to yyyy-MM-dd HH:mm:ss.000
	 *
	 * @param date
	 * @return
	 */
	public static long truncateToSecond(long date) {
		long second = TimeUnit.SECONDS.toMillis(1);
		return date - date % second;
	}

	public static long todayInMillis() {
		return truncateToDay(System.currentTimeMillis());
	}

	public static long yesterdayInMillis() {
		return addDays(todayInMillis(), -1);
	}

	public static long tomorrowInMillis() {
		return addDays(todayInMillis(), 1);
	}

	public static int timeToInt(long timeInMillis) {
		return (int) (timeInMillis / 1000);
	}

	public static long timeFromInt(int timeInSeconds) {
		return ((long) timeInSeconds) * 1000;
	}

	/**
	 * 得到指定時間的天数
	 */
	public static int timeToDay(long sysTime) {
		long realTime = sysTime + TIME_ZONE;
		int days = (int) (realTime / BASE_TIME);
		return days;
	}

}
