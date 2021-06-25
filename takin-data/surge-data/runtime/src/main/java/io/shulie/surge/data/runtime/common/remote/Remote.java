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

package io.shulie.surge.data.runtime.common.remote;

/**
 * 可以被远程读写的变量。
 * 常见用法：
 * <pre>{@code
 * @Inject
 * @Named("/path/to/data")				// 变量在远程的路径(dataId)
 * @DefaultValue("123")					// 变量的默认值（可选）
 * @UpdateMethod("onUpdateMyParam1")	// 变量的更新回调（可选）
 * private Remote<Integer> param1;
 * 
 * // 在本类中实现一个更新回调
 * void onUpdateMyParam1() {...}
 * }</pre>
 * 
 * @author pamirs
 */
public interface Remote<T> {

	/**
	 * 读取变量
	 * @return
	 */
	T get();

	/**
	 * 写入变量
	 * @param value
	 */
	void set(T value);
}
