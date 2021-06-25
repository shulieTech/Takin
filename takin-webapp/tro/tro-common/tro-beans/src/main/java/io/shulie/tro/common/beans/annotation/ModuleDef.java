package io.shulie.tro.common.beans.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author shiyajian
 * create: 2020-09-15
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ModuleDef {

    /**
     * 模块名称
     *
     * @return 模块名称
     */
    String moduleName();

    /**
     * 子模块名称
     *
     * @return 子模块名称
     */
    String subModuleName();

    /**
     * 操作日志中的msg对应的Key
     *
     * @return 日志的key
     */
    String logMsgKey();

    /**
     * 操作日志中的msg对应的操作类型
     * @return 日志操作类型
     */
    String opTypes() default "default";

    /**
     * 当 license 过期时候，这个模块的接口是否可用
     * @return
     */
    boolean enableExpired() default true;
}
