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

import java.util.Date;

/**
 * 分页模型
 *
 * 用于查询列表时进行分页
 */
public class Paginator {

    /**
     * 每页显示条数
     */
    private int limit = 20;

    /**
     * 起始行号
     */
    private long start = 0;

    /**
     * 结束行号
     */
    private long end = 20;

    /**
     * 总数
     */
    private long total = 0;

    /**
     * 时间戳
     */
    private Date timestamp;

    /**
     * 当前页
     */
    private long index = 1;

    /**
     * 总页数
     */
    private long pageNum = 0;

    /**
     * ...隔开的中间页码数量
     */
    private int breakpage = 5;

    /**
     * ...隔开的中间页码序列中间位置，从零开始
     */
    private long currentposition = 2;

    /**
     * ...隔开的两端页码数量
     */
    private int breakspace = 2;

    /**
     * 是否用...断开的判断数量
     */
    private int maxspace = 4;

    /**
     * 前一页码
     */
    private long prevnum;

    /**
     * 后一页码
     */
    private long nextnum;

    /**
     * 无参构造
     */
    public Paginator() {

    }

    /**
     * 初始化分页类，
     * 生成可供查询使用的分页类
     *
     * @param index 起始位置
     * @param limit 查询偏移量
     */
    public Paginator(int index, int limit) {
        if (index < 0) {
            this.setIndex(1);
        }
        this.setIndex(index);
        this.setLimit(limit);
        this.setStart((this.getIndex() - 1) * this.getLimit());
        this.setEnd(this.getIndex() * this.getLimit());
    }

    /**
     * 完成查询得道结果后，
     * 使用此方法生成供页面展示分页控件的分页类
     */
    public void generateView() {
        this.setPageNum(new Double(Math.ceil(new Double(total) / this.getLimit())).intValue());

        if (this.getIndex() > this.getPageNum()) {
            this.setIndex(this.getPageNum());
        }

        this.setPrevnum(this.getIndex() - this.getCurrentposition());
        this.setNextnum(this.getIndex() + this.getCurrentposition());
        if (this.getPrevnum() < 1) {
            this.setPrevnum(1);
        }
        if (this.getNextnum() > this.getPageNum()) {
            this.setNextnum(this.getPageNum());
        }
        this.setEnd(Math.min(this.getIndex() * this.getLimit(), this.getTotal()));
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
        this.setStart((this.getIndex() - 1) * this.getLimit());
        this.setEnd(this.getIndex() * this.getLimit());
    }

    /**
     * 2018年5月17日
     *
     * @return the start
     * @author shulie
     * @version 1.0
     */
    public long getStart() {
        return start;
    }

    /**
     * 2018年5月17日
     *
     * @param start the start to set
     * @author shulie
     * @version 1.0
     */
    public void setStart(long start) {
        this.start = start;
    }

    /**
     * 2018年5月17日
     *
     * @return the end
     * @author shulie
     * @version 1.0
     */
    public long getEnd() {
        return end;
    }

    /**
     * 2018年5月17日
     *
     * @param end the end to set
     * @author shulie
     * @version 1.0
     */
    public void setEnd(long end) {
        this.end = end;
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
     * 2018年5月17日
     *
     * @return the index
     * @author shulie
     * @version 1.0
     */
    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        if (index < 1) {index = 1;}
        this.index = index;
        this.setStart((this.getIndex() - 1) * this.getLimit());
        this.setEnd(this.getIndex() * this.getLimit());
    }

    /**
     * 2018年5月17日
     *
     * @return the pageNum
     * @author shulie
     * @version 1.0
     */
    public long getPageNum() {
        return pageNum;
    }

    /**
     * 2018年5月17日
     *
     * @param pageNum the pageNum to set
     * @author shulie
     * @version 1.0
     */
    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * 2018年5月17日
     *
     * @return the breakpage
     * @author shulie
     * @version 1.0
     */
    public int getBreakpage() {
        return breakpage;
    }

    /**
     * 2018年5月17日
     *
     * @param breakpage the breakpage to set
     * @author shulie
     * @version 1.0
     */
    public void setBreakpage(int breakpage) {
        this.breakpage = breakpage;
    }

    /**
     * 2018年5月17日
     *
     * @return the currentposition
     * @author shulie
     * @version 1.0
     */
    public long getCurrentposition() {
        return currentposition;
    }

    /**
     * 2018年5月17日
     *
     * @param currentposition the currentposition to set
     * @author shulie
     * @version 1.0
     */
    public void setCurrentposition(long currentposition) {
        this.currentposition = currentposition;
    }

    /**
     * 2018年5月17日
     *
     * @return the breakspace
     * @author shulie
     * @version 1.0
     */
    public int getBreakspace() {
        return breakspace;
    }

    /**
     * 2018年5月17日
     *
     * @param breakspace the breakspace to set
     * @author shulie
     * @version 1.0
     */
    public void setBreakspace(int breakspace) {
        this.breakspace = breakspace;
    }

    /**
     * 2018年5月17日
     *
     * @return the maxspace
     * @author shulie
     * @version 1.0
     */
    public int getMaxspace() {
        return maxspace;
    }

    /**
     * 2018年5月17日
     *
     * @param maxspace the maxspace to set
     * @author shulie
     * @version 1.0
     */
    public void setMaxspace(int maxspace) {
        this.maxspace = maxspace;
    }

    /**
     * 2018年5月17日
     *
     * @return the prevnum
     * @author shulie
     * @version 1.0
     */
    public long getPrevnum() {
        return prevnum;
    }

    /**
     * 2018年5月17日
     *
     * @param prevnum the prevnum to set
     * @author shulie
     * @version 1.0
     */
    public void setPrevnum(long prevnum) {
        this.prevnum = prevnum;
    }

    /**
     * 2018年5月17日
     *
     * @return the nextnum
     * @author shulie
     * @version 1.0
     */
    public long getNextnum() {
        return nextnum;
    }

    /**
     * 2018年5月17日
     *
     * @param nextnum the nextnum to set
     * @author shulie
     * @version 1.0
     */
    public void setNextnum(long nextnum) {
        this.nextnum = nextnum;
    }

    public boolean isBreakLeft() {
        return this.getPrevnum() - this.getBreakspace() > this.getMaxspace() ? true : false;
    }

    public boolean isBreakRight() {
        return this.getPageNum() - this.getBreakspace() - this.getNextnum() + 1 > this.getMaxspace() ? true : false;
    }

    /**
     * 2018年5月17日
     *
     * @return the timestamp
     * @author shulie
     * @version 1.0
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * 2018年5月17日
     *
     * @param timestamp the timestamp to set
     * @author shulie
     * @version 1.0
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
