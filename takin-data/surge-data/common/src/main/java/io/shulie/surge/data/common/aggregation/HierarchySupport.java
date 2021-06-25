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

import java.util.List;

/**
 * 支持记录嵌套
 * @author pamirs
 */
public interface HierarchySupport<T extends HierarchySupport<T>> {
	/**
	 * 设置父记录
	 * @param parent
	 * @return this
	 */
	T parent(T parent);

	/**
	 * 添加子记录
	 * @param child
	 * @return this
	 */
	T addChild(T child);

	/**
	 * 设置子记录
	 * @param children
	 * @return this
	 */
	T children(List<T> children);

	/**
	 * 获取父记录
	 * @return
	 */
	T getParent();

	/**
	 * 获取子记录
	 * @return
	 */
	List<T> getChildren();
}
