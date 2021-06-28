package io.shulie.amdb.common.dto.link.entrance;

import lombok.Data;

@Data
public class ServiceInfoDTO {
    //String linkId;
    //String entranceServiceName;
    String appName;
    String rpcType;
    String serviceName;
    String methodName;
    String middlewareName;
    //boolean isOpenCalculate;
    String extend;
}