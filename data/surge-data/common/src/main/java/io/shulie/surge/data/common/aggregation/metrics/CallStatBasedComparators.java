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

package io.shulie.surge.data.common.aggregation.metrics;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Doubles;
import com.google.common.primitives.Longs;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static io.shulie.surge.data.common.utils.CommonUtils.divide;
import static io.shulie.surge.data.common.utils.CommonUtils.isNullEmpty;

public class CallStatBasedComparators {

	private static final Comparator<CallStatBased> DEFAULT_COMPARATOR = getHitComparator();

	private static Map<String, Comparator<CallStatBased>> strMaps = ImmutableMap.of(
			"name", getNameComparator(),
			"hit", getHitComparator(),
			"hitSum", getHitSumComparator(),
			"rt", getRtComparator(),
			"error", getErrorComparator());

	@SuppressWarnings("unchecked")
	public static <T extends CallStatBased> Comparator<T> getComparator(String name) {
		Comparator<CallStatBased> c;
		if (name == null) {
			c = DEFAULT_COMPARATOR;
		} else {
			c = strMaps.get(name);
			if (c == null) {
				c = DEFAULT_COMPARATOR;
			}
		}
		return (Comparator<T>) c;
	}

	public static <T extends CallStatBased> Comparator<T> getNameComparator() {
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				String name1 = o1.getName();
				String name2 = o2.getName();

				if (name1 == null) {
					return name2 == null ? 0 : -1;
				} else if (name2 == null) {
					return 1;
				}

				return name1.compareTo(name2);
			}
		};
	}

	public static <T extends CallStatBased> Comparator<T> getHitComparator() {
		return getComparator(0);
	}

	public static <T extends CallStatBased> Comparator<T> getHitSumComparator() {
		return getSumComparator(0);
	}

	public static <T extends CallStatBased> Comparator<T> getErrorComparator() {
		return getComparator(2);
	}

	public static <T extends CallStatBased> Comparator<T> getRtComparator() {
		// 逆序排列
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				List<CallStat> stats1 = o1.getStats();
				List<CallStat> stats2 = o2.getStats();

				if (isNullEmpty(stats1)) {
					return isNullEmpty(stats2) ? 0 : 1;
				} else if (isNullEmpty(stats2)) {
					return -1;
				}

				CallStat stat1 = stats1.get(stats1.size() - 1);
				CallStat stat2 = stats2.get(stats2.size() - 1);

				double rt1 = divide(stat1.get(1), stat1.get(0));
				double rt2 = divide(stat2.get(1), stat2.get(0));
				return -Doubles.compare(rt1, rt2);
			}
		};
	}

	public static <T extends CallStatBased> Comparator<T> getComparator(final int pos) {
		// 逆序排列
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				List<CallStat> stats1 = o1.getStats();
				List<CallStat> stats2 = o2.getStats();

				if (isNullEmpty(stats1)) {
					return isNullEmpty(stats2) ? 0 : 1;
				} else if (isNullEmpty(stats2)) {
					return -1;
				}

				CallStat stat1 = stats1.get(stats1.size() - 1);
				CallStat stat2 = stats2.get(stats2.size() - 1);
				return -Longs.compare(stat1.get(pos), stat2.get(pos));
			}
		};
	}

	public static <T extends CallStatBased> Comparator<T> getSumComparator(final int pos) {
		// 逆序排列
		return new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				List<CallStat> stats1 = o1.getStats();
				List<CallStat> stats2 = o2.getStats();

				if (isNullEmpty(stats1)) {
					return isNullEmpty(stats2) ? 0 : 1;
				} else if (isNullEmpty(stats2)) {
					return -1;
				}

				long statSum1 = 0;
				for (CallStat stat : stats1) {
					statSum1 += stat.get(pos);
				}

				long statSum2 = 0;
				for (CallStat stat : stats2) {
					statSum2 += stat.get(pos);
				}

				return -Longs.compare(statSum1, statSum2);
			}
		};
	}
}
