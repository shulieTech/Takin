package io.shulie.surge.data.common.aggregation;

/**
 * 支持记录时间戳
 * @author pamirs
 */
public interface TimestampSupport {
	/**
	 * 返回时间戳
	 * @return
	 */
	long getTimestamp();

	/**
	 * 设置时间戳
	 * @param timestamp
	 */
	void setTimestamp(long timestamp);
}
