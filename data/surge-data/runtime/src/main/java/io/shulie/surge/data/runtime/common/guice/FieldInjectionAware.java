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
