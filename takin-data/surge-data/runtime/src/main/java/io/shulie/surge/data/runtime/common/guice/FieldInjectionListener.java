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

package io.shulie.surge.data.runtime.common.guice;

import com.google.inject.Inject;
import com.google.inject.MembersInjector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import io.shulie.surge.data.common.utils.ObjectUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * 监听指定 Field 的注入动作
 * @author pamirs
 */
public class FieldInjectionListener implements TypeListener {
	private final Class<?> fieldClass;
	private final Class<? extends Annotation> annotationClass;
	private final Class<? extends FieldInjectionAware> ij;

	public FieldInjectionListener(Class<?> fieldClass, Class<? extends FieldInjectionAware> ij) {
		this(fieldClass, Inject.class, ij);
	}

	public FieldInjectionListener(Class<?> fieldClass, Class<? extends Annotation> annotationClass,
			Class<? extends FieldInjectionAware> ij) {
		this.fieldClass = fieldClass;
		this.annotationClass = annotationClass;
		this.ij = ij;
	}

	@Override
	public <K> void hear(TypeLiteral<K> typeLiteral, TypeEncounter<K> typeEncounter) {
		Class<?> c = typeLiteral.getRawType();
		while (c != null) {
			for (Field field : c.getDeclaredFields()) {
				if (field.getType() == fieldClass && field.isAnnotationPresent(annotationClass)) {
					Type fieldType = typeLiteral.getFieldType(field).getType();
					typeEncounter.register(new FieldInjector<K>(field, fieldType, ij));
				}
			}
			c = c.getSuperclass();
		}
	}

	private static class FieldInjector<T> implements MembersInjector<T> {
		private final Field field;
		private final Type fieldType;
		private final Class<? extends FieldInjectionAware> ij;

		public FieldInjector(Field field, Type fieldType, Class<? extends FieldInjectionAware> ij) {
			this.field = field;
			this.fieldType = fieldType;
			this.ij = ij;
		}

		@Override
		public void injectMembers(T instance) {
			FieldInjectionAware i = ObjectUtils.newIntsance(ij);
			try {
				i.setInjectionContext(field, fieldType, instance);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
