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

package io.shulie.tro.web.amdb.bean.common;

import java.io.Serializable;

/**
 * @Author: fanxx
 * @Date: 2020/10/20 2:20 下午
 * @Description:
 */
public class PagingDevice implements Serializable {
    private static final long serialVersionUID = 1L;
    private int pageSize = 20;
    private int currentPage = 0;

    public PagingDevice() {
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        currentPage = currentPage == null ? 0 : currentPage;
        this.currentPage = currentPage < 0 ? 0 : currentPage;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize == null ? this.getPageSize() : pageSize;
    }
}
