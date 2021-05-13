package io.shulie.tro.common.beans.page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shiyajian
 * create: 2020-09-24
 */
public class PagingList<T> {

    private PagingList() { /* no instance */ }

    /**
     * 返回数据的总条数
     */
    private long total;

    /**
     * 返回的数据
     */
    private List<T> list;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    private PagingList(long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public boolean isEmpty() {
        return this.list == null || this.list.isEmpty();
    }

    public static <T> PagingList<T> of(List<T> data, long total) {
        return new PagingList<>(total, data);
    }

    public static <T> PagingList<T> empty() {
        return new PagingList<>(0, new ArrayList<>());
    }

}
