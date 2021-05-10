package io.shulie.surge.data.common.doc.annotation;

import java.lang.annotation.*;

/**
 * @Author: xingchen
 * @ClassName: Field
 * @Package: io.shulie.surge.data.common.elastic.annotation
 * @Date: 2020/11/2616:04
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
@Inherited
public @interface Field {
    /**
     * Alias for {@link #name}.
     *
     * @since 3.2
     */
    String value() default "";

    /**
     * The <em>name</em> to be used to store the field inside the document.
     * <p>
     * √5 If not set, the name of the annotated property is used.
     *
     * @since 3.2
     */
    String name() default "";

    FieldType type() default FieldType.Auto;
}
