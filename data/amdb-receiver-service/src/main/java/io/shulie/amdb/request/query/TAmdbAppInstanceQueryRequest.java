package io.shulie.amdb.request.query;

import io.shulie.amdb.common.request.PagingRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("实例查询")
public class TAmdbAppInstanceQueryRequest extends PagingRequest {
    /**
     * 应用名称
     */
    @ApiModelProperty("应用名称")
    private String appName;

    /**
     * IP地址
     */
    @ApiModelProperty("IP地址")
    private String ip;

    /**
     * AgentId
     */
    @ApiModelProperty("AgentId")
    private String agentId;

    /**
     * 客户Id
     */
    @ApiModelProperty("客户Id")
    private String customerId;
}
