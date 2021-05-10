package io.shulie.surge.data.runtime.common.remote;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 远程配置值为 <code>null</code> 的时候，设置的默认值
 * @author pamirs
 */
@Retention(RUNTIME)
@Target({ ElementType.FIELD })
public @interface DefaultValue {
	String value();
}
