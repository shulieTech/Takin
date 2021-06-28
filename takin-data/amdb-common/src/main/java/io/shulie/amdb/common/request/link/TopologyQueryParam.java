package io.shulie.amdb.common.request.link;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class TopologyQueryParam {
    String appName;
    String linkId;
    String serviceName;
    String method;
    String rpcType;
    String extend;
    Boolean isTrace;
    private String id;
    
    public boolean isTrace() {
        if (StringUtils.isEmpty(isTrace)) {
            isTrace = true;
        }
        return isTrace;
    }
}
