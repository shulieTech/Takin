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

package io.shulie.amdb.utils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageRowBounds;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.MapUtils;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 说明: 分页助手
 *
 * @version v1.0
 * 项目地址 : http://git.oschina.net/free/Mybatis_PageHelper
 * @ClassName: PageInfo
 * @Description: 对Page<E>结果进行包装
 * @2018年4月13日
 */
@Api(value = "分页对象")
public class PageInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_PAGESIZE = 10;

    @ApiModelProperty("当前页")
    private int pageNum;

    @ApiModelProperty("每页大小")
    private int pageSize;

    @ApiModelProperty("总页数")
    private long totalPage;

    @ApiModelProperty("总记录数")
    private long total;

    @ApiModelProperty("结果集")
    private List<T> list;

    /**
     * 无参构造
     */
    public PageInfo() {

    }

    /**
     * 包装Page对象
     *
     * @param list
     */
    public PageInfo(List<T> list) {
        if (list instanceof Page) {
            Page page = (Page) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.list = page;
            this.total = page.getTotal();
        } else if (list instanceof Collection) {
            this.pageNum = 1;
            this.pageSize = list.size() == 0 ? 10 : list.size();
            this.list = list;
            this.total = list.size();
        }
        this.pages();
    }

    /**
     * 包装Page对象
     *
     * @param list
     * @param pageRowBounds 分页
     */
    public PageInfo(List<T> list, PageRowBounds pageRowBounds) {
        this.pageNum = 1;
        this.pageSize = list.size() == 0 ? DEFAULT_PAGESIZE : list.size();
        this.list = list;
        this.total = pageRowBounds.getTotal();
        this.pages();
    }

    public <E> PageInfo(List<E> list, Function<E, T> function) {
        if (list == null) {
            this.pageNum = 1;
            this.pageSize = 1;
            this.total = 0;
        } else if (list instanceof Page) {
            Page page = (Page) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
            this.total = page.getTotal();
        } else if (list instanceof Collection) {
            this.pageNum = 1;
            this.pageSize = list.size();
            this.total = list.size();
        }
        this.pages();
        this.list = Optional.ofNullable(list).map(ls -> ls.stream().map(l -> function.apply(l)).collect(Collectors.toList())).orElse(Collections.emptyList());
    }

    private void pages() {
        if (this.total % this.pageSize == 0) {
            this.totalPage = this.total / this.pageSize;
        } else {
            this.totalPage = this.total / this.pageSize + 1;
        }
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

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

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PageInfo{");
        sb.append("pageNum=").append(pageNum);
        sb.append(", pageSize=").append(pageSize);
        sb.append(", total=").append(total);
        sb.append(", list=").append(list);
        sb.append(", navigatepageNums=");
        sb.append('}');
        return sb.toString();
    }

    public static int getPageNum(Map<String, Object> params) {
        int pageNum = MapUtils.getIntValue(params, "pageNum", 1);
        if (pageNum == 0) {
            pageNum = 1;
        }
        return pageNum;
    }

    public static int getPageSize(Map<String, Object> params) {
        int pageSize = MapUtils.getIntValue(params, "pageSize", 10);
        if (pageSize == 0) {
            pageSize = 10;
        }
        //防止数据太多
        if (pageSize > 200) {
            pageSize = 200;
        }
        return pageSize;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public PageInfo<T> setTotalPage(long totalPage) {
        this.totalPage = totalPage;
        return this;
    }
}
