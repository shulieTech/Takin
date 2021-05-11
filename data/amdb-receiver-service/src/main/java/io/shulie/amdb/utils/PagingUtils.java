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
