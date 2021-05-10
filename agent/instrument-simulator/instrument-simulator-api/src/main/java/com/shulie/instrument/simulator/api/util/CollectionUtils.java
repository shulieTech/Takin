/**
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.shulie.instrument.simulator.api.util;

import java.util.Collection;

/**
 * 集合操作工具类
 *
 * @author xiaobin.zfb|xiaobin@shulie.io
 * @since 2020/10/23 10:45 下午
 */
public class CollectionUtils {

    /**
     * 链式调用集合封装
     *
     * @param collection 集合类
     * @param e          追加元素
     * @param <E>        元素类型
     * @return Collection:this
     */
    public static <E> E add(Collection<E> collection, E e) {
        collection.add(e);
        return e;
    }

    /**
     * 链式调用集合封装
     *
     * @param collection 集合类
     * @param elems      追加元素
     * @param <E>        元素类型
     * @return Collection:this
     */
    public static <E> Collection<E> add(Collection<E> collection, E... elems) {
        if (elems == null) {
            return collection;
        }
        for (E e : elems) {
            collection.add(e);
        }
        return collection;
    }

    /**
     * 返回集合是否为空
     *
     * @param coll 目标集合
     * @return 返回是否为空
     */
    public static boolean isEmpty(Collection coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * 返回集合是否不为空
     *
     * @param coll 目标集合
     * @return 返回是否不为空
     */
    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }

    /**
     * 返回两个集合是否相等
     *
     * @param source source
     * @param target target
     * @return
     */
    public static boolean equals(Collection source, Collection target) {
        if (source == target) {
            return true;
        }
        if (source == null || target == null) {
            return false;
        }
        return source.equals(target);
    }

    /**
     * 判断集合是否包含某个元素
     *
     * @param collection 集合
     * @param element    元素
     * @param <T>
     * @return
     */
    public static <T> boolean contains(Collection<T> collection, T element) {
        if (isEmpty(collection)) {
            return false;
        }
        return collection.contains(element);
    }
}
