/*
 * Copyright 2012 LMAX Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.shulie.surge.data.runtime.disruptor;

/**
 * <p>Exception thrown when the it is not possible to insert a value into
 * the ring buffer without it wrapping the consuming sequenes.  Used
 * specifically when claiming with the {@link RingBuffer#tryNext()} call.
 *
 * <p>For efficiency this exception will not have a stack trace.
 * @author pamirs
 *
 */
@SuppressWarnings("serial")
public final class InsufficientCapacityException extends Exception
{
	public static final InsufficientCapacityException INSTANCE = new InsufficientCapacityException();

	private InsufficientCapacityException()
	{
		// Singleton
	}

	@Override
	public synchronized Throwable fillInStackTrace()
	{
		return this;
	}

	public static void main(String[] args) {
		int j = 0;
		long start = System.currentTimeMillis();
		for (int i = 0; i < 100000000; i++) {
			try {
				throw new RuntimeException("sdfdsf");
				//throw InsufficientCapacityException.INSTANCE;
			} catch (Exception e) {
				j++;
			}
		}
		System.out.println(j + " " + (System.currentTimeMillis() - start));
	}
}
