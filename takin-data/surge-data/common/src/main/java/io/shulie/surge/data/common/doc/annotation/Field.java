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
