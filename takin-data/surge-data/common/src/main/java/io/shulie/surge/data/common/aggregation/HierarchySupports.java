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

package io.shulie.surge.data.common.aggregation;

import com.google.common.collect.Ordering;
import io.shulie.surge.data.common.utils.CommonUtils;

import java.util.*;

/**
 * 对嵌套结构的通用支持
 *
 * @author pamirs
 */
public class HierarchySupports {

    public static final int MAX_LEVEL = 128;

    /**
     * 对嵌套的树状结构的每个子树进行排序
     *
     * @param root
     * @param comparator
     */
    public static final <T extends HierarchySupport<T>> void sortHierarchy(
            T root, Comparator<T> comparator) {
        sortHierarchy(root, comparator, Integer.MAX_VALUE);
    }

    /**
     * 对嵌套的树状结构的每个子树进行排序，每个子树只保留前 count 个。
     *
     * @param root
     * @param comparator
     * @param count
     */
    public static final <T extends HierarchySupport<T>> void sortHierarchy(
            T root, Comparator<T> comparator, int count) {
        Comparator<T> reverseComparator = Collections.reverseOrder(comparator);
        List<T> st = new ArrayList<T>(MAX_LEVEL);

        st.add(root);
        while (!st.isEmpty()) {
            T x = st.remove(st.size() - 1);
            List<T> children = x.getChildren();
            if (children != null) {
                if (children.size() > count) {
                    children = Ordering.from(comparator).greatestOf(children, count);
                    x.children(children);
                } else if (children.size() > 1) {
                    Collections.sort(children, reverseComparator);
                }
                st.addAll(children);
            }
        }
    }

    /**
     * 对嵌套的树状结构的第一层子树进行排序
     *
     * @param roots
     * @param comparator
     */
    public static final <T extends HierarchySupport<T>> void sortChildren(
            Collection<T> roots, Comparator<T> comparator) {
        sortChildren(roots, comparator, Integer.MAX_VALUE);
    }

    /**
     * 对嵌套的树状结构的第一层子树进行排序，每个子树只保留前 count 个。
     *
     * @param roots
     * @param comparator
     * @param count
     */
    public static final <T extends HierarchySupport<T>> void sortChildren(
            Collection<T> roots, Comparator<T> comparator, int count) {
        Comparator<T> reverseComparator = Collections.reverseOrder(comparator);
        for (T root : roots) {
            List<T> children = root.getChildren();
            if (children != null) {
                if (children.size() > count) {
                    children = Ordering.from(comparator).greatestOf(children, count);
                    root.children(children);
                } else if (children.size() > 1) {
                    Collections.sort(children, reverseComparator);
                }
            }
        }
    }

    /**
     * 从 root 的树结构展开成线性结构
     */
    public static final <T extends HierarchySupport<T>> List<T> flattern(T root) {
        if (CommonUtils.isNullEmpty(root.getChildren())) {
            List<T> ret = new ArrayList<T>(1);
            ret.add(root);
            return ret;
        } else {
            List<T> st = new ArrayList<T>(MAX_LEVEL);
            List<T> ret = new ArrayList<T>(root.getChildren().size() * 8);

            st.add(root);
            while (!st.isEmpty()) {
                T x = st.remove(st.size() - 1);
                ret.add(x);
                List<T> children = x.getChildren();
                if (!CommonUtils.isNullEmpty(children)) {
                    for (int i = children.size() - 1; i >= 0; --i) {
                        st.add(children.get(i));
                    }
                }
            }
            return ret;
        }
    }

    /**
     * 从多个 root 的树结构展开成线性结构
     */
    public static final <T extends HierarchySupport<T>> List<T> flattern(Collection<T> roots) {
        if (CommonUtils.isNullEmpty(roots)) {
            return Collections.emptyList();
        } else {
            List<T> ret = new ArrayList<T>(1);
            for (T root : roots) {
                if (!CommonUtils.isNullEmpty(root.getChildren())) {
                    ret.addAll(flattern(root));
                } else {
                    ret.add(root);
                }
            }
            return ret;
        }
    }

}
