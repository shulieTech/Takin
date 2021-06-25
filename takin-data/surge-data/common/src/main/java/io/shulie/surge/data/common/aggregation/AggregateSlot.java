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

package io.shulie.surge.data.common.aggregation;

import io.shulie.surge.data.common.utils.ObjectUtils;
import org.cliffc.high_scale_lib.NonBlockingHashMap;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

public class AggregateSlot<K, A extends AggregateSupport<A>>
		implements AggregateSupport<AggregateSlot<K, A>>, Serializable {

	private static final long serialVersionUID = 1882481077143283226L;

	private Map<K, A> map;

	public AggregateSlot() {
		this.map = new NonBlockingHashMap<K, A>();
	}

	public AggregateSlot(int initialCapacity) {
		this.map = new NonBlockingHashMap<K, A>(initialCapacity);
	}

	@SuppressWarnings("unchecked")
	public A addToSlot(K key, A value) {
		A a = map.get(key);
		if (a == null) {
			a = (A) ObjectUtils.newIntsance(value.getClass());
			map.put(key, a);
		}
		a.aggregateFrom(value);
		return a;
	}

	@Override
	public void aggregateFrom(AggregateSlot<K, A> other) {
		if (other != null && !other.isEmpty()) {
			for (Entry<K, A> entry : other.map.entrySet()) {
				addToSlot(entry.getKey(), entry.getValue());
			}
		}
	}

	public int size() {
		return map.size();
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Map<K, A> toMap() {
		return map;
	}

	@Override
	public String toString() {
		return map.toString();
	}
}
