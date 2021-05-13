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

package com.pamirs.tro.entity.domain.query;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 分页后的结果列表
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 */
@Deprecated //todo service和controller都不需要包装这种对象，切面自动完成
public class ResultList<T> extends Result<T> {

    //序列号
    private static final long serialVersionUID = -8793824978794414238L;
    //默认每页显示20个
    private static int DEFAULT_PAGE_SIZE = 20;
    //分页结果数据集合
    private Collection<T> datalist;
    //总共记录数
    private long total = 0;
    //每页显示数目
    private int pageSize;
    //查询起始偏移量
    private long start;

    /**
     * 默认无参构造
     */
    public ResultList() {
        this(0L, 0L, DEFAULT_PAGE_SIZE, new ArrayList<T>());
    }

    /**
     * 构造分页结果集
     *
     * @param start     查询起始偏移量
     * @param totalSize 总的记录数
     * @param pageSize  每页显示数目
     * @param data      真正的数据
     */
    public ResultList(long start, long totalSize, int pageSize, Collection<T> data) {
        this.pageSize = pageSize;
        this.start = start;
        this.total = totalSize;
        this.datalist = data;
    }

    protected static int getStartOfPage(int pageNo) {
        return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
    }

    public static int getStartOfPage(int pageNo, int pageSize) {
        return (pageNo - 1) * pageSize;
    }

    public long getStart() {
        return start;
    }

    /**
     * 2018年5月17日
     *
     * @return the datalist
     * @author shulie
     * @version 1.0
     */
    public Collection<T> getDatalist() {
        return datalist;
    }

    /**
     * 2018年5月17日
     *
     * @param datalist the datalist to set
     * @author shulie
     * @version 1.0
     */
    public void setDatalist(Collection<T> datalist) {
        this.datalist = datalist;
    }

    /**
     * 2018年5月17日
     *
     * @return the total
     * @author shulie
     * @version 1.0
     */
    public long getTotal() {
        return total;
    }

    /**
     * 2018年5月17日
     *
     * @param total the total to set
     * @author shulie
     * @version 1.0
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 计算总页数
     *
     * @return 查询结果分页的总页数
     */
    public long getTotalPageCount() {
        if (total % (long)pageSize == 0L) {
            return total / (long)pageSize;
        } else {
            return total / (long)pageSize + 1L;
        }
    }

    /**
     * 2018年5月17日
     *
     * @return pageSize
     * @author shulie
     * @version 1.0
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 计算当前页
     *
     * @return 当前页码
     */
    public long getCurrentPageNo() {
        return start / (long)pageSize + 1L;
    }

    /**
     * 2018年5月17日
     *
     * @return true有下一页，false没有下一页
     * @author shulie
     * @version 1.0
     */
    public boolean hasNextPage() {
        return getCurrentPageNo() < getTotalPageCount() - 1L;
    }

    /**
     * 2018年5月17日
     *
     * @return true有上一页，false没有上一页
     * @author shulie
     * @version 1.0
     */
    public boolean hasPreviousPage() {
        return getCurrentPageNo() > 1L;
    }

    /**
     * 初始化分页类，生成可供查询使用的分页类
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示数
     * @return 分页对象
     */
    public Paginator getPaginator(Integer currentPage, Integer pageSize) {
        if (null == currentPage) {
            currentPage = 1;
        }
        if (null == pageSize) {
            pageSize = 20;
        }
        Paginator paginator = new Paginator(currentPage, pageSize);
        paginator.setTotal(total);
        paginator.generateView();
        return paginator;
    }

    /**
     * 2018年5月17日
     *
     * @return 字符串
     * @author shulie
     * @version 1.0
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
