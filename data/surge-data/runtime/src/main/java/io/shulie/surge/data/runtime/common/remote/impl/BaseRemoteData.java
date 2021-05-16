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

import com.google.inject.name.Named;
import io.shulie.surge.data.common.utils.CommonUtils;
import io.shulie.surge.data.runtime.common.guice.FieldInjectionAware;
import io.shulie.surge.data.runtime.common.remote.DefaultValue;
import io.shulie.surge.data.runtime.common.remote.Remote;
import io.shulie.surge.data.runtime.common.remote.UpdateMethod;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 使用注入方式同步配置数据的通用基类
 * 
 * @author pamirs
 */
public abstract class BaseRemoteData<T> implements Remote<T>, FieldInjectionAware {

	private static final Logger logger = Logger.getLogger(BaseRemoteData.class);

	protected String dataId;
	protected Class<?> targetClass;
	protected Type targetType;
	protected T data;
	protected T defalutData;

	protected Object instance;
	protected Method updateMethod;

	@Override
	public void setInjectionContext(Field field, Type fieldType, Object instance) throws Exception {
		field.setAccessible(true);

		Object injected = field.get(instance);
		if (!getClass().isInstance(injected)) {
			throw new RuntimeException("the injected class is not instanceOf " + getClass().getName());
		}

		@SuppressWarnings("unchecked")
		BaseRemoteData<T> target = (BaseRemoteData<T>) injected;
		if (!(fieldType instanceof ParameterizedType)) {
			throw new RuntimeException(getClass().getSimpleName() + "<T> must be generic.");
		}

		target.targetType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
		if (target.targetType instanceof Class) {
			// 简单类型
			target.targetClass = (Class<?>) target.targetType;
		} else if (target.targetType instanceof ParameterizedType) {
			// 嵌套的类型
			target.targetClass = (Class<?>) ((ParameterizedType) target.targetType).getRawType();
		} else {
			// 其他类型先不支持
			throw new RuntimeException("Injection to field " + field.getName() + " of " +
					instance.getClass().getName() + " is not supported yet, targetType=" + target.targetType);
		}

		// 通过 Named 指定 dataId
		Named named = field.getAnnotation(Named.class);
		String dataId = named.value();
		target.dataId = dataId;

		if (CommonUtils.isNullEmpty(dataId)) {
			throw new RuntimeException("dataId can not be empty: " + dataId);
		}

		UpdateMethod updateMethod = field.getAnnotation(UpdateMethod.class);
		if (updateMethod != null) {
			try {
				String methodName = updateMethod.value();
				Method method = instance.getClass().getDeclaredMethod(methodName);
				method.setAccessible(true);
				target.updateMethod = method;
				target.instance = instance;
			} catch (Exception e) {
				throw new RuntimeException("fail to get UpdateMethod: " + updateMethod.value(), e);
			}
		}

		DefaultValue defaultValue = field.getAnnotation(DefaultValue.class);
		if (defaultValue != null) {
			String dv = defaultValue.value();
			target.data = target.defalutData = target.convertToTarget(dv);
		}

		target.init(field, fieldType, instance);
	}

	protected abstract void init(Field field, Type fieldType, Object instance) throws Exception;

	protected void updateData(Object value) {
		T convertedValue;
		try {
			convertedValue = convertToTarget(value);
		} catch (Exception e) {
			logger.info("data updated, but fail to convert to " + targetClass.getName() +
					", dataId=" + dataId + ", error=" + e);
			return;
		}

		if ((convertedValue == null && this.data == null)
				|| (convertedValue != null && convertedValue.equals(this.data))) {
			return;
		}

		if (value == null) {
			logger.info("data updated, dataId: " + dataId + ", data: (null) -> " + convertedValue);
		} else if (value instanceof String && !(convertedValue instanceof String)) {
			logger.info("data updated, dataId: " + dataId + ", data: \"" + value + "\" -> " + convertedValue);
		} else {
			logger.info("data updated, dataId: " + dataId + ", data: " + convertedValue);
		}
		this.data = convertedValue;
	}

	@SuppressWarnings("unchecked")
	protected T convertToTarget(Object value) throws Exception {
		if (value == null) {
			return defalutData;
		}

		try {
			if (targetClass == Boolean.class) {
				return (T) (value == null ? Boolean.FALSE : Boolean.valueOf(value.toString()));
			} else if (targetClass.isInstance(value)) {
				return (T) value;
			} else if (targetClass == Boolean.class) {
				return (T) Boolean.valueOf(value.toString());
			} else if (targetClass == Integer.class) {
				return (T) Integer.valueOf(value.toString());
			} else if (targetClass == Long.class) {
				return (T) Long.valueOf(value.toString());
			} else if (targetClass == Double.class) {
				return (T) Double.valueOf(value.toString());
			} else if (targetClass == String.class) {
				return (T) String.valueOf(value);
			}
		} catch (Exception e) {
			logger.info("data updated, but fail to convert to " + targetClass.getName() +
					", dataId=" + dataId + ", error=" + e);
		}
		return defalutData;
	}

	protected void notifyUpdate() {
		if (updateMethod != null) {
			try {
				updateMethod.invoke(instance);
			} catch (Exception e) {
				logger.warn("fail to invoke updateMethod: " + updateMethod.getName(), e);
			}
		}
	}
}
