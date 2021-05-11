package io.shulie.amdb.request.query;

import io.shulie.amdb.common.request.PagingRequest;
import lombok.Data;

@Data
public class TAmdbAppQueryRequest extends PagingRequest {
    /**
     * 应用名称
     */
    private String appName;

    /**
     * 客户Id
     */
    private String customerId;
}
