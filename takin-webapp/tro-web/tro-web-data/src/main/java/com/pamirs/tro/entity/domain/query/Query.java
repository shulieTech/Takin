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

import java.io.Serializable;

/**
 * 封装的查询类
 * 传入pageSize,currentPage,total;
 * 计算start,end
 *
 * @author shulie
 * @version v1.0
 * @2018年5月17日
 */
public class Query<T> extends QueryBase implements Serializable {

    //序列号
    private static final long serialVersionUID = 1930382256159908170L;
    //查询的实体对象
    private T query;
    //排序字段
    private String orderBy;

    /**
     * 2018年5月17日
     *
     * @return the query
     * @author shulie
     * @version 1.0
     */
    public T getQuery() {
        return query;
    }

    /**
     * 2018年5月17日
     *
     * @param query the query to set
     * @author shulie
     * @version 1.0
     */
    public void setQuery(T query) {
        this.query = query;
    }

    /**
     * 2018年5月17日
     *
     * @return the orderBy
     * @author shulie
     * @version 1.0
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 设置排序字段
     * 默认按id升序
     *
     * @param orderBy 排序字段
     */
    public void setOrderBy(String orderBy) {
        if (orderBy != null && !orderBy.isEmpty()) {
            this.orderBy = orderBy;
        } else {
            this.orderBy = "id";
        }
    }
}
