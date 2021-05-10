package io.shulie.surge.data.runtime.common.remote;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 远程收到更新时，回调指定的方法。这个方法应该是当前注入点的类的某个方法，没有参数。
 * @author pamirs
 */
@Retention(RUNTIME)
@Target({ ElementType.FIELD })
public @interface UpdateMethod {
	String value();
}
