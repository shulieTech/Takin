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

package io.shulie.surge.data.runtime.common.remote.impl;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Named;
import io.shulie.surge.data.runtime.common.guice.FieldInjectionListener;
import io.shulie.surge.data.runtime.common.remote.Remote;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class RemoteZkModule extends AbstractModule implements Serializable{

	@Override
	protected void configure() {
		bind(new TypeLiteral<Remote<Boolean>>() {
		}).annotatedWith(Named.class).to(new TypeLiteral<ZkDataImpl<Boolean>>() {
		});
		bind(new TypeLiteral<Remote<String>>() {
		}).annotatedWith(Named.class).to(new TypeLiteral<ZkDataImpl<String>>() {
		});
		bind(new TypeLiteral<Remote<Integer>>() {
		}).annotatedWith(Named.class).to(new TypeLiteral<ZkDataImpl<Integer>>() {
		});
		bind(new TypeLiteral<Remote<Long>>() {
		}).annotatedWith(Named.class).to(new TypeLiteral<ZkDataImpl<Long>>() {
		});
		bind(new TypeLiteral<Remote<Double>>() {
		}).annotatedWith(Named.class).to(new TypeLiteral<ZkDataImpl<Double>>() {
		});

		bindListener(Matchers.any(), new FieldInjectionListener(Remote.class, Inject.class, ZkDataImpl.class));
	}
}
