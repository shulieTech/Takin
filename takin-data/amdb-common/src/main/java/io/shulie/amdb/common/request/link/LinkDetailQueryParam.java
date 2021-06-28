package io.shulie.amdb.common.request.link;

import lombok.Data;

@Data
public class LinkDetailQueryParam {
    String appName;
    String linkId;
    String serviceName;
    String method;
    String rpcType;
    String extend;
}
