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
