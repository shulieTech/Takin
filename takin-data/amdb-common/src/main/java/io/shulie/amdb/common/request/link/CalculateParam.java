package io.shulie.amdb.common.request.link;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CalculateParam {
    @ApiModelProperty(required = true)
    String appName;
    @ApiModelProperty(required = true)
    String serviceName;
    @ApiModelProperty(required = true)
    String method;
    @ApiModelProperty(required = true)
    String rpcType;
    String extend;
}
