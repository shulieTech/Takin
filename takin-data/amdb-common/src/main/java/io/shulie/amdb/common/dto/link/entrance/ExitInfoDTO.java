package io.shulie.amdb.common.dto.link.entrance;

import lombok.Data;

@Data
public class ExitInfoDTO {
    String appName;
    String serviceName;
    String methodName;
    String middlewareName;
    String rpcType;
    String extend;
    String upAppName;
}