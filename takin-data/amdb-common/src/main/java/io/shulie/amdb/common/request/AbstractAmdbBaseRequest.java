package io.shulie.amdb.common.request;

import lombok.Data;

@Data
public abstract class AbstractAmdbBaseRequest {
    /**
     * 租户标识
     */
    String tenant = "DEFAULT";

    /**
     * 用户ID
     */
    String userId = "SYSTEM";

    /**
     * 用户名称
     */
    String userName = "SYSTEM";
}
