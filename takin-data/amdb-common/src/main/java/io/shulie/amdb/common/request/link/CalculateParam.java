package io.shulie.amdb.common.request.link;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CalculateParam {
    @ApiModelProperty(required = true, allowEmptyValue = false)
    String appName;
    @ApiModelProperty(required = true, allowEmptyValue = false)
    String serviceName;
    @ApiModelProperty(required = true, allowEmptyValue = false)
    String method;
    @ApiModelProperty(required = true, allowEmptyValue = false)
    String rpcType;
    String extend;
}
