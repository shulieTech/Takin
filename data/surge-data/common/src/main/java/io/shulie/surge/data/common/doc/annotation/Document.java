package io.shulie.surge.data.common.doc.annotation;

import java.lang.annotation.*;

/**
 * @Author: xingchen
 * @ClassName: Document
 * @Package: io.shulie.surge.data.common.elastic.annotation
 * @Date: 2020/11/2616:01
 * @Description:
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Document {
    /**
     * 索引名称
     *
     * @return
     */
    String indexName();
}
