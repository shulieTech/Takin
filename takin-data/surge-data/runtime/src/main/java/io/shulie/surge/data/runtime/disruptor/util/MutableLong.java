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
package io.shulie.surge.data.runtime.disruptor.util;

/**
 * Holder class for a long value.
 */
public class MutableLong extends Number
{
	private static final long serialVersionUID = 1L;
	private long value = 0L;

	/**
	 * Default constructor
	 */
	public MutableLong()
	{
	}

	/**
	 * Construct the holder with initial value.
	 *
	 * @param initialValue to be initially set.
	 */
	public MutableLong(final long initialValue)
	{
		this.value = initialValue;
	}

	/**
	 * Get the long value.
	 *
	 * @return the long value.
	 */
	public long get()
	{
		return value;
	}

	/**
	 * Set the long value.
	 *
	 * @param value to set.
	 */
	public void set(final long value)
	{
		this.value = value;
	}

	@Override
	public double doubleValue() {
		return value;
	}

	@Override
	public float floatValue() {
		return value;
	}

	@Override
	public int intValue() {
		return 0;
	}

	@Override
	public long longValue() {
		return value;
	}
}
