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

package io.shulie.tro.web.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import io.shulie.tro.common.beans.page.PagingList;
import org.springframework.beans.BeanUtils;

/**
 * @author shiyajian
 * create: 2020-12-28
 */
public class BeanCopyUtils {

    public static <T> T copyObject(Object source, Class<T> targetClass) {
        return getInstance(targetClass);
    }

    public static <T> List<T> copyList(Object source, Class<T> targetClass) {
        if (source == null) {
            return new ArrayList<>();
        }
        if (source instanceof List) {
            return (List<T>)(((List)source).stream().map(item -> {
                T t = getInstance(targetClass);
                BeanUtils.copyProperties(item, t);
                return t;
            }).collect(Collectors.toList()));
        }
        return new ArrayList<>();
    }

    public static <T> PagingList<T> copyPagingList(PagingList<?> source, Class<T> tClass) {
        if (source == null) {
            return PagingList.empty();
        }
        List<T> collect = (List<T>)((PagingList)source).getList().stream().map(item -> {
            T t = getInstance(tClass);
            BeanUtils.copyProperties(item, t);
            return t;
        }).collect(Collectors.toList());
        return PagingList.of(collect, source.getTotal());
    }

    private static <T> T getInstance(Class<T> targetClass) {
        try {
            return targetClass.newInstance();
        } catch (Exception e) {
            return null;
        }
    }
}
