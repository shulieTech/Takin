package io.shulie.amdb.common.request.link;

import io.shulie.amdb.common.request.PagingRequest;
import lombok.Data;

@Data
public class ServiceQueryParam extends PagingRequest {
    String appName;
    String rpcType;
    String serviceName;
    String methodName;
    String middlewareName;
}
