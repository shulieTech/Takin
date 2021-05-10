package io.shulie.surge.data.common.aggregation;

import java.io.Serializable;

public interface AggregatableRecord<T extends AggregatableRecord>
		extends AggregateSupport<T>, BinarySupport, Serializable {
}
