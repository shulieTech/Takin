package io.shulie.surge.data.common.aggregation.metrics;

import java.util.List;

/**
 * 基于 {@link CallStat} 的实现
 * @author pamirs
 */
public interface CallStatBased {
	List<CallStat> getStats();

	String getName();
}
