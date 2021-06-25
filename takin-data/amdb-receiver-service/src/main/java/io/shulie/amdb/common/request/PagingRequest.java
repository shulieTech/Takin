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

package io.shulie.amdb.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

@Data
@ApiModel("分页")
public class PagingRequest extends AmdbBaseRequest {
    @ApiModelProperty("分页大小")
    private Integer pageSize;
    @ApiModelProperty("请求页")
    private Integer currentPage;

    public Integer getCurrentPage() {
        if (Objects.isNull(currentPage) || currentPage < 0) {
            currentPage = 1;
        }
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        if (Objects.isNull(this.pageSize)) {
            return Integer.MAX_VALUE;
        }
        this.pageSize = Math.min(pageSize, Integer.MAX_VALUE);
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        // 不传入pageSize的话，默认为10条
        this.pageSize = pageSize;
    }
}
