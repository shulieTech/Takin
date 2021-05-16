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

import java.io.Serializable;
import java.util.Comparator;

/**
 * 常用的“键值对”
 *
 * @param <T1>
 * @param <T2>
 * @author pamirs
 */
public final class Pair<T1, T2> implements Comparable<Pair<T1, T2>>, Serializable {

	private static final long serialVersionUID = -1895944892380859907L;

	private T1 first;
	private T2 second;

	protected Pair() {
	}

	public Pair(final T1 first, final T2 second) {
		this.first = first;
		this.second = second;
	}

	public T1 getFirst() {
		return first;
	}

	public T2 getSecond() {
		return second;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(final Object o) {
		if (o instanceof Pair) {
			final Pair<T1, T2> p = (Pair<T1, T2>) o;
			return first.equals(p.first) && second.equals(p.second);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return first.hashCode() + (second.hashCode() << 1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public int compareTo(final Pair<T1, T2> p) {
		final int firstResult = ((Comparable<T1>) first).compareTo(p.first);
		if (firstResult == 0) {
			return ((Comparable<T2>) second).compareTo(p.second);
		}
		return firstResult;
	}

	@Override
	public String toString() {
		return "(" + first + ", " + second + ")";
	}

	private static class FirstValueComparator<T1 extends Comparable<T1>, T2>
			implements Comparator<Pair<T1, T2>> {
		@Override
		public int compare(Pair<T1, T2> o1, Pair<T1, T2> o2) {
			if (o1 == null || o1.getFirst() == null) {
				if (o2 != null && o2.getFirst() != null) {
					return -1;
				}
				return 0;
			}
			if (o2 == null || o2.getFirst() == null) {
				return 1;
			}
			if (o1 == o2) {
				return 0;
			}
			return o1.getFirst().compareTo(o2.getFirst());
		}
	};

	private static class SecondValueComparator<T1, T2 extends Comparable<T2>>
			implements Comparator<Pair<T1, T2>> {
		@Override
		public int compare(Pair<T1, T2> o1, Pair<T1, T2> o2) {
			if (o1 == null || o1.getSecond() == null) {
				if (o2 != null && o2.getSecond() != null) {
					return -1;
				}
				return 0;
			}
			if (o2 == null || o2.getSecond() == null) {
				return 1;
			}
			if (o1 == o2) {
				return 0;
			}
			return o1.getSecond().compareTo(o2.getSecond());
		}
	};

	public static <T1 extends Comparable<T1>, T2> Comparator<Pair<T1, T2>>
			getFirstValueComparator() {
		return new FirstValueComparator<T1, T2>();
	}

	public static <T1, T2 extends Comparable<T2>> Comparator<Pair<T1, T2>>
			getSecondValueComparator() {
		return new SecondValueComparator<T1, T2>();
	}
}
