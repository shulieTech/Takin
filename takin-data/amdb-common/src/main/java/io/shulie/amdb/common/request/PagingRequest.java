package io.shulie.amdb.common.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

@Data
@ApiModel("分页")
public class PagingRequest extends AbstractAmdbBaseRequest {
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
        return pageSize == null ? Integer.MAX_VALUE : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        // 不传入pageSize的话，默认为10条
        this.pageSize = pageSize;
    }
}
