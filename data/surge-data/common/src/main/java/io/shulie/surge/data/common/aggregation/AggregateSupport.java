package io.shulie.surge.data.common.aggregation;

/**
 * 支持记录数据聚集
 * @author pamirs
 */
public interface AggregateSupport<T extends AggregateSupport> {
	/**
	 * 将 other 的数据合并到自身
	 * @param other
	 */
	void aggregateFrom(T other);
}
