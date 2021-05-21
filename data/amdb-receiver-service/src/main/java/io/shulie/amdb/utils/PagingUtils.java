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

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package io.shulie.amdb.utils;

import com.github.pagehelper.PageInfo;

import java.util.List;

public final class PagingUtils {

    public static <T, M> PageInfo<T> result(List<M> oldData, List<T> data) {
        PageInfo<M> beforeList = new PageInfo(oldData);
        PageInfo<T> afterList = new PageInfo(data);
        afterList.setTotal(beforeList.getTotal());
        return afterList;
    }

    public static <T, M> PageInfo<T> result(PageInfo<M> page, List<T> data) {
        PageInfo<T> afterList = new PageInfo(data);
        afterList.setTotal(page.getTotal());
        return afterList;
    }
}
