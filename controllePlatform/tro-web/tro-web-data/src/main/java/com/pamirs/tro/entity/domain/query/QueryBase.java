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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 传入pageSize,currentPage,total;
 * 计算start,end
 * 数列张弦写的代码。557092加的注释
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 */
public class QueryBase {
    //默认的分页大小
    private static int DEFAULT_PAGE_SIZE = 20;
    //每页显示数目
    protected int pageSize = DEFAULT_PAGE_SIZE;
    //当前页码
    protected int currentPage = 1;
    //总共记录数
    protected long total;
    //查询的起始位置
    protected int start = 0;
    //查询的结束位置
    protected int end = 20;

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
     * 2018年5月17日
     *
     * @return the pageSize
     * @author shulie
     * @version 1.0
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 2018年5月17日
     *
     * @param pageSize the pageSize to set
     * @author shulie
     * @version 1.0
     */
    public void setPageSize(Integer pageSize) {
        if (pageSize != null && pageSize > 0) {
            this.pageSize = pageSize;
        }
        this.setStartAndEnd();
    }

    /**
     * 2018年5月17日
     *
     * @return the currentPage
     * @author shulie
     * @version 1.0
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 2018年5月17日
     *
     * @param currentPage the currentPage to set
     * @author shulie
     * @version 1.0
     */
    public void setCurrentPage(Integer currentPage) {
        if (currentPage != null && currentPage > 0) {
            this.currentPage = currentPage;
        }
        this.setStartAndEnd();
    }

    /**
     * 2018年5月17日
     *
     * @author shulie
     * @version 1.0
     */
    protected void setStartAndEnd() {
        this.start = (this.getCurrentPage() - 1) * this.getPageSize();
        if (this.start < 0) {
            this.start = 0;
        }
        this.end = this.getStart() + this.getPageSize() - 1;
    }

    /**
     * 2018年5月17日
     *
     * @return the start
     * @author shulie
     * @version 1.0
     */
    public int getStart() {
        return start;
    }

    /**
     * 2018年5月17日
     *
     * @param start the start to set
     * @author shulie
     * @version 1.0
     */
    public void setStart(Integer start) {
        if (start != null && start >= 0) {
            this.start = start;
        }
    }

    /**
     * 2018年5月17日
     *
     * @return the end
     * @author shulie
     * @version 1.0
     */
    public int getEnd() {
        return end;
    }

    /**
     * 2018年5月17日
     *
     * @param end the end to set
     * @author shulie
     * @version 1.0
     */
    public void setEnd(Integer end) {
        if (end != null && end >= 0) {
            this.end = end;
        }
    }

    /**
     * 判断是否有后一页
     *
     * @return true有，false没有
     */
    public boolean hasNextPage() {
        return getCurrentPage() < getTotalPage() - 1L;
    }

    /**
     * 判断是否有前一页
     *
     * @return true有，false没有
     */
    public boolean hasPreviousPage() {
        return getCurrentPage() > 1L;
    }

    /**
     * 计算总计页数
     *
     * @return 总共页数
     */
    public long getTotalPage() {
        if (total % (long)pageSize == 0L) {
            return total / (long)pageSize;
        } else {
            return total / (long)pageSize + 1L;
        }
    }

    /**
     * 2018年5月17日
     *
     * @return 字符串
     * @author shulie
     * @version 1.0
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
