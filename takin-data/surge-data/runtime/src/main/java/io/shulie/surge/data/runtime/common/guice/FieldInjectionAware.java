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

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Field 注入后，回调传递注入的上下文，例如可以获取到当时注入的 Annotation，范型类型等。
 * 其他的 Method、Constructor 注入方式，因为对象已经“失效”，因此不能支持。
 * @author pamirs
 */
public interface FieldInjectionAware {
	void setInjectionContext(Field field, Type fieldType, Object instance) throws Exception;
}
