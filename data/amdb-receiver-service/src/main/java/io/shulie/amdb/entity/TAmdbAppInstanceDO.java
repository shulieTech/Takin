package io.shulie.amdb.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@ApiModel("")
@Table(name = "`t_amdb_app_instance`")
public class TAmdbAppInstanceDO implements Serializable {
    /**
     * 实例id
     */
    @Id
    @Column(name = "`id`")
    @ApiModelProperty("实例id")
    private Long id;

    /**
     * 应用名
     */
    @Column(name = "`app_name`")
    @ApiModelProperty("应用名")
    private String appName;

    /**
     * 应用ID
     */
    @Column(name = "`app_id`")
    @ApiModelProperty("应用ID")
    private Long appId;

    /**
     * agentId
     */
    @Column(name = "`agent_id`")
    @ApiModelProperty("agentId")
    private String agentId;

    /**
     * ip
     */
    @Column(name = "`ip`")
    @ApiModelProperty("ip")
    private String ip;

    /**
     * 进程号
     */
    @Column(name = "`pid`")
    @ApiModelProperty("进程号")
    private String pid;

    /**
     * Agent版本号
     */
    @Column(name = "`agent_version`")
    @ApiModelProperty("Agent版本号")
    private String agentVersion;

    /**
     * MD5
     */
    @Column(name = "`md5`")
    @ApiModelProperty("MD5")
    private String md5;

    /**
     * 主机名称
     */
    @Column(name = "`hostname`")
    @ApiModelProperty("主机名称")
    private String hostname;
    /**
     * 租户标示
     */
    @Column(name = "`tenant`")
    @ApiModelProperty("租户标示")
    private String tenant;

    /**
     * 标记位
     */
    @Column(name = "`flag`")
    @ApiModelProperty("标记位")
    private Integer flag;

    /**
     * 创建人编码
     */
    @Column(name = "`creator`")
    @ApiModelProperty("创建人编码")
    private String creator;

    /**
     * 创建人名称
     */
    @Column(name = "`creator_name`")
    @ApiModelProperty("创建人名称")
    private String creatorName;

    /**
     * 更新人编码
     */
    @Column(name = "`modifier`")
    @ApiModelProperty("更新人编码")
    private String modifier;

    /**
     * 更新人名称
     */
    @Column(name = "`modifier_name`")
    @ApiModelProperty("更新人名称")
    private String modifierName;

    /**
     * 创建时间
     */
    @Column(name = "`gmt_create`")
    @ApiModelProperty("创建时间")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @Column(name = "`gmt_modify`")
    @ApiModelProperty("更新时间")
    private Date gmtModify;

    @Column(name = "`agent_language`")
    @ApiModelProperty("")
    private String agentLanguage;

    /**
     * 扩展字段
     */
    @Column(name = "`ext`")
    @ApiModelProperty("扩展字段")
    private String ext;

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", appName=").append(appName);
        sb.append(", appId=").append(appId);
        sb.append(", agentId=").append(agentId);
        sb.append(", ip=").append(ip);
        sb.append(", pid=").append(pid);
        sb.append(", agentVersion=").append(agentVersion);
        sb.append(", md5=").append(md5);
        sb.append(", flag=").append(flag);
        sb.append(", creator=").append(creator);
        sb.append(", creatorName=").append(creatorName);
        sb.append(", modifier=").append(modifier);
        sb.append(", modifierName=").append(modifierName);
        sb.append(", gmtCreate=").append(gmtCreate);
        sb.append(", gmtModify=").append(gmtModify);
        sb.append(", agentLanguage=").append(agentLanguage);
        sb.append(", ext=").append(ext);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}