package io.shulie.amdb.response.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ApiModel
public class AppInstanceSnapshotResponse implements Serializable {
    /**
     * 应用名称
     */
    @ApiModelProperty("应用名称")
    private String appName;

    /**
     * 快照日期
     */
    @ApiModelProperty("快照日期")
    private String snapshotDate;

    /**
     * IP列表
     */
    @ApiModelProperty("IP列表")
    private List<String> ipList;
}
