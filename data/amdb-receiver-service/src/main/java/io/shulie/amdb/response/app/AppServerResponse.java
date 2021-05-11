package io.shulie.amdb.response.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel
public class AppServerResponse implements Serializable {
    /**
     * 服务ID
     */
    @ApiModelProperty("服务ID")
    private Long id;

    /**
     * 应用名称
     */
    @ApiModelProperty("应用名称")
    private String appName;

    /**
     * 服务名
     */
    @ApiModelProperty("服务名")
    private String serverName;

    /**
     * 服务类型
     */
    @ApiModelProperty("服务类型")
    private String serverType;

    /**
     * 标记位
     */
    @ApiModelProperty("标记位")
    private Integer flag;

    /**
     * 创建人编码
     */
    @ApiModelProperty("创建人编码")
    private String creator;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String creatorName;

    /**
     * 更新人编码
     */
    @ApiModelProperty("更新人编码")
    private String modifier;

    /**
     * 更新人名称
     */
    @ApiModelProperty("更新人名称")
    private String modifierName;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date gmtModify;

    /**
     * 扩展字段
     */
    @ApiModelProperty("扩展字段")
    private String ext;
}
