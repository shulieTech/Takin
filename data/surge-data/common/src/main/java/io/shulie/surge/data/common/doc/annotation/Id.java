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
public @interface Id {
}
