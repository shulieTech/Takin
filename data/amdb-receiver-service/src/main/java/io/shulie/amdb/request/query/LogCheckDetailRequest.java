package io.shulie.amdb.request.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class LogCheckDetailRequest {
    @ApiModelProperty("traceId")
    String traceId;
}
