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
