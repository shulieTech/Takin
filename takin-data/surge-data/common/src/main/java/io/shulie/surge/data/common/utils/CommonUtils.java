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

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.zip.CRC32;

/**
 * @author pamirs
 */
public final class CommonUtils {
	/**
	 * 字节数组转化为 Hex 字符串表示，输出 Hex 为小写
	 * 
	 * @param bs
	 * @param start
	 *            开始位置
	 * @param end
	 *            结束位置
	 * @return hex String in lower case
	 */
	public static String byteArrayToHex(byte[] bs, int start, int end) {
		StringBuilder sb = new StringBuilder(bs.length << 1);
		for (int i = start; i < end; ++i) {
			byte b = bs[i];
			sb.append("0123456789abcdef".charAt(0xf & (b >> 4)));
			sb.append("0123456789abcdef".charAt(b & 0xf));
		}
		return sb.toString();
	}

	/**
	 * 字节数组转化为 Hex 字符串表示，输出 Hex 为小写
	 * 
	 * @param bs
	 * @return hex String in lower case
	 */
	public static String byteArrayToHex(byte[] bs) {
		return byteArrayToHex(bs, 0, bs.length);
	}

	/**
	 * Hex 字符串转化为字节数组，支持大小写的 Hex
	 * 
	 * @param hex
	 * @return 对应的字节数组，其长度为 hex 长度的一半
	 */
	public static byte[] hexToByteArray(String hex) {
		byte[] bs = new byte[hex.length() / 2];
		for (int i = 0; i < bs.length; i++) {
			bs[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return bs;
	}

	/**
	 * 进行 MD5 编码
	 * 
	 * @param in
	 * @return md5 checksum String in lower case
	 * @throws IOException
	 *             when fail to read from input stream.
	 */
	public static String md5(InputStream in) throws IOException {
		MessageDigest md5;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Fail to get MD5 MessageDigest instance", e);
		}
		byte[] buffer = new byte[1024 * 16];
		int bytes;
		while ((bytes = in.read(buffer)) != -1) {
			md5.update(buffer, 0, bytes);
		}

		byte[] bs = md5.digest();
		return byteArrayToHex(bs);
	}

	/**
	 * 生成 CRC 编码
	 * 
	 * @param bytes
	 * @return
	 */
	public static String crc(byte[] bytes) {
		CRC32 c = new CRC32();
		c.update(bytes);
		return Long.toHexString(c.getValue());
	}

	/**
	 * 向一对多的 MultiMap (<tt>mapList</tt>) 放入值 <tt>value</tt>。如果 之前没有放入过这种
	 * <tt>key</tt>，会创建出一个 {@link ArrayList} 来存放。 否则，会把值 <tt>value</tt> 插入到 map
	 * 中原来的 List 的末尾。
	 * 
	 * @param <K>
	 * @param <V>
	 * @param multiMap
	 *            待插入值的一对多的 Map
	 * @param key
	 *            待插入的 key
	 * @param value
	 *            待插入的 value
	 * @return
	 */
	public static <K, V> List<V> addToMultiMap(
			Map<K, List<V>> multiMap, K key, V value) {
		List<V> values = multiMap.get(key);
		if (values == null) {
			values = new ArrayList<V>();
			multiMap.put(key, values);
		}
		values.add(value);
		return values;
	}

	/**
	 * 仿照 MySQL 的 <code>select ... limit offset, row_count</code> 语法，获取指定列表的子集。
	 * 对这个子集的任何操作都会反映到原来的 <tt>list</tt> 上面。如果原来的 <tt>list</tt> 在子集
	 * 返回后发生变化，子集的内容将可能是未定义的，详细信息请参看 {@link List#subList(int, int)}。
	 * 
	 * @param <T>
	 * @param list
	 * @param offset
	 *            要返回的第一个元素，从 0 开始计算，如果超过最后一个元素，则返回空列表
	 * @param maxSize
	 *            要返回子集的最大大小
	 * @return 如果 <tt>list</tt> 为 <code>null</code> 则返回 <code>null</code>
	 *         ，否则返回子列表
	 * 
	 * @see List#subList(int, int)
	 */
	public static <T> List<T> subList(List<T> list, int offset, int maxSize) {
		if (list == null) {
			return null;
		}
		if (list.isEmpty()) {
			return list;
		}
		if (offset >= 0 && offset < list.size()) {
			return list.subList(offset, Math.min(offset + maxSize, list.size()));
		}
		return list.subList(0, 0);
	}

	/**
	 * 对 list 里面的元素进行计数，返回的 map 以元素为 key，出现的次数为 value
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public static <T> Map<T, Integer> cardinalityMap(Collection<T> list) {
		Map<T, Integer> map = new HashMap<T, Integer>();
		for (T item : list) {
			Integer count = map.get(item);
			if (count != null) {
				map.put(item, Integer.valueOf(count.intValue() + 1));
			} else {
				map.put(item, Integer.valueOf(1));
			}
		}
		return map;
	}

	/**
	 * 容器的逆序拷贝，Hint：返回的是 {@link ArrayList}
	 * 
	 * @param <T>
	 * @param c
	 * @return
	 */
	public static <T> List<T> reverseCopy(Collection<T> c) {
		if (c == null) {
			return null;
		}
		List<T> list = new ArrayList<T>(c);
		Collections.reverse(list);
		return list;
	}

	/**
	 * 返回第 index 个成员，如果获取失败，返回 <code>null</code>
	 * 
	 * @param <T>
	 * @param c
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T elementOf(Collection<? super T> c, int index) {
		if (isNullEmpty(c) || index > c.size()) {
			return null;
		}
		if (c instanceof List) {
			return (T) ((List<?>) c).get(index);
		} else {
			int i = 0;
			Iterator<?> iterator = c.iterator();
			while (i++ < index) {
				iterator.next();
			}
			return (T) iterator.next();
		}
	}

	public static final String[] array(String... strs) {
		return strs;
	}

	public static final long[] array(long... values) {
		return values;
	}

	public static boolean isNullEmpty(int[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isNullEmpty(long[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isNullEmpty(Object[] array) {
		return array == null || array.length == 0;
	}

	public static boolean isNullEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNullEmpty(Collection<?> c) {
		return c == null || c.isEmpty();
	}

	public static boolean isNullEmpty(Map<?, ?> m) {
		return m == null || m.isEmpty();
	}

	public static String trimToNull(String str) {
		if (str == null) {
			return null;
		}
		String result = str.trim();
		if (result.length() == 0) {
			return null;
		}
		return result;
	}

	public static String trimToEmpty(String str) {
		return str == null ? "" : str.trim();
	}

	public static <T> T defaultIfNull(T value, T defaultValue) {
		return value == null ? defaultValue : value;
	}

	public static String defaultIfEmpty(String str, String defaultValue) {
		return isNullEmpty(str) ? defaultValue : str;
	}

	public static <T extends Collection<?>> T defaultIfEmpty(T c, T defaultValue) {
		return isNullEmpty(c) ? defaultValue : c;
	}

	public static <T extends Map<?, ?>> T defaultIfEmpty(T m, T defaultValue) {
		return isNullEmpty(m) ? defaultValue : m;
	}

	public static double divide(double dividend, double divisor) {
		return divisor != 0.0 ? dividend / divisor : 0.0;
	}

	public static long divideL(double dividend, double divisor) {
		return Math.round(divide(dividend, divisor));
	}

	public static double percent(double dividend, double divisor) {
		return divisor != 0.0 ? dividend / divisor * 100 : 0.0;
	}

	public static int countMatches(String str, int ch) {
		if (isNullEmpty(str)) {
			return 0;
		}
		int count = 0;
		int idx = 0;
		while ((idx = str.indexOf(ch, idx)) != -1) {
			count++;
			idx += 1;
		}
		return count;
	}

	public static Integer parseIntegerQuietly(String value) {
		if (isNullEmpty(value)) {
			return null;
		}
		try {
			return Integer.valueOf(Integer.parseInt(value));
		} catch (Exception e) {
			return null;
		}
	}

	public static int parseIntQuietly(String value, int defaultValue) {
		if (isNullEmpty(value)) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * 根据 Enum 的 ordinal 查找对应的枚举值，如果找不到对应值，则返回 <code>null</code>
	 * 
	 * @param <E>
	 * @param value
	 * @param enumClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> E parseEnumFromInteger(Integer value, Class<E> enumClass) {
		if (value != null) {
			@SuppressWarnings("rawtypes")
			Enum[] values = enumClass.getEnumConstants();
			int intValue = value.intValue();
			for (int i = 0; i < values.length; i++) {
				if (values[i].ordinal() == intValue) {
					return (E) values[i];
				}
			}
		}
		return null;
	}

	/**
	 * 根据 Enum 的 ordinal 查找对应的枚举值，如果找不到对应值，则返回 <code>defaultValue</code>。
	 * <code>defaultValue</code> 不能为 <code>null</code>。
	 * 
	 * @param <E>
	 * @param idx
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E extends Enum<E>> E parseEnumFromInt(int idx, E defaultValue) {
		@SuppressWarnings("rawtypes")
		Enum[] values = defaultValue.getClass().getEnumConstants();
		if (idx >= 0 && idx < values.length) {
			// fast path
			if (values[idx].ordinal() == idx) {
				return (E) values[idx];
			}
			for (int i = 0; i < values.length; i++) {
				if (values[i].ordinal() == idx) {
					return (E) values[i];
				}
			}
		}
		return defaultValue;
	}

	/**
	 * 根据 Enum 的 name 查找对应的枚举值，如果找不到对应值，则返回 <code>null</code>
	 * 
	 * @param <E>
	 * @param value
	 * @param enumClass
	 * @return
	 */
	public static <E extends Enum<E>> E parseEnumFromString(String value, Class<E> enumClass) {
		if (!isNullEmpty(value)) {
			try {
				return Enum.valueOf(enumClass, value);
			} catch (Exception e) {
			}
		}
		return null;
	}

	public static Long parseLongQuietly(String value) {
		if (isNullEmpty(value)) {
			return null;
		}
		try {
			return Long.valueOf(Long.parseLong(value));
		} catch (Exception e) {
			return null;
		}
	}

	public static long parseLongQuietly(String value, long defaultValue) {
		if (isNullEmpty(value)) {
			return defaultValue;
		}
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Double parseDoubleQuietly(String value) {
		if (isNullEmpty(value)) {
			return null;
		}
		try {
			return Double.valueOf(Double.parseDouble(value));
		} catch (Exception e) {
			return null;
		}
	}

	public static double parseDoubleQuietly(String value, double defaultValue) {
		if (isNullEmpty(value)) {
			return defaultValue;
		}
		try {
			return Double.valueOf(Double.parseDouble(value));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public static Boolean parseBooleanQuietly(String value) {
		if (isNullEmpty(value)) {
			return null;
		}
		if ("true".equalsIgnoreCase(value)) {
			return Boolean.TRUE;
		} else if ("false".equalsIgnoreCase(value)) {
			return Boolean.FALSE;
		} else {
			return null;
		}
	}

	public static boolean parseBooleanQuietly(String value, boolean defaultValue) {
		if (defaultValue) {
			return !"false".equalsIgnoreCase(value);
		} else {
			return "true".equalsIgnoreCase(value);
		}
	}

	public static String checkNotNullEmpty(String value, String name) throws IllegalArgumentException {
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException(name + "不能为空");
		}
		return value;
	}

	public static <T> T checkNotNull(T value, String name) throws IllegalArgumentException {
		if (value == null) {
			throw new IllegalArgumentException(name + "不能为空");
		}
		return value;
	}

	public static int checkAndParseInt(String value, String name) throws IllegalArgumentException {
		try {
			return Integer.parseInt(checkNotNull(trimToNull(value), name));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(name + "不是合法的整数");
		}
	}

	public static long checkAndParseLong(String value, String name) throws IllegalArgumentException {
		try {
			return Long.parseLong(checkNotNull(trimToNull(value), name));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(name + "不是合法的整数");
		}
	}

	public static double checkAndParseDouble(String value, String name) throws IllegalArgumentException {
		try {
			return Double.parseDouble(checkNotNull(trimToNull(value), name));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException(name + "不是合法的数值");
		}
	}

	public static boolean isHexNumeric(char ch) {
		return (ch >= 'A' && ch <= 'F') || (ch >= 'a' && ch <= 'f') || (ch >= '0' && ch <= '9');
	}

	public static boolean isNumeric(char ch) {
		return ch >= '0' && ch <= '9';
	}

	/**
	 * 返回字符串最后一个非数字字符的位置，都是数字则返回 -1
	 * 
	 * @param string
	 * @param begin
	 * @return
	 */
	public static int getLastNonDigitCharacterIndex(String string, int begin) {
		final int len = string.length();
		int i = len - 1;
		while (i >= begin) {
			final char c = string.charAt(i);
			if (c < '0' || c > '9') {
				return i;
			}
			--i;
		}
		return -1;
	}

	public static int getPid() {
		RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
		String name = runtime.getName();
		return parseIntQuietly(name.substring(0, name.indexOf('@')), 0);
	}
}
