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
package io.shulie.surge.data.runtime.disruptor;

/**
 * Implementations translate another data representations into events claimed from the {@link RingBuffer}
 *
 * @param <T> event implementation storing the data for sharing during exchange or parallel coordination of an event.
 * @see EventTranslator
 */
public interface EventTranslatorOneArg<T, A>
{
	/**
	 * Translate a data representation into fields set in given event
	 *
	 * @param event into which the data should be translated.
	 * @param sequence that is assigned to event.
	 * @param arg0 The first user specified argument to the translator
	 */
	void translateTo(final T event, long sequence, final A arg0);
}
