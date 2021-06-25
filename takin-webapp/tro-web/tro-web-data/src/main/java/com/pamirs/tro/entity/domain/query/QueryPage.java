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
 * @Author: 710524
 * @ClassName: QueryPage
 * @package: com.pamirs.tro.entity.domain.query
 * @Date: 2019/5/9 0009 17:48
 * @Description: 查询分页信息
 */
public abstract class QueryPage implements Serializable {

    private int pageNum;
    private int pageSize;
    private String orderBy = "id desc";

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = Math.min(Math.max(1, pageNum), Integer.MAX_VALUE);
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = Math.min(pageSize, Integer.MAX_VALUE);
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
