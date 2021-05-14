package io.shulie.tro.definition.annotaion;

/**
 * @author shiyajian
 * create: 2020-12-09
 */
public @interface ParamTypes {

    Class<?>[] value() default {};
}
