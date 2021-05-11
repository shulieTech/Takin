package io.shulie.amdb.adaptors.instance.model;

import io.shulie.amdb.entity.TAmdbAppInstanceDO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * t_amdb_app_instance
 *
 * @author
 */
@Data
public class TAmdbAppInstance extends TAmdbAppInstanceDO implements Serializable {
    /**
     * 实例id
     */
    private Long id;

    /**
     * 应用名
     */
    private String appName;
    /**
     * 主机名
     */
    private String hostname;

    /**
     * agentLanguage
     */
    private String agentLanguage;

    /**
     * 应用ID
     */
    private Long appId;

    /**
     * agentId
     */
    private String agentId;

    /**
     * ip
     */
    private String ip;

    /**
     * 进程号
     */
    private String pid;

    /**
     * Agent 版本号
     */
    private String agentVersion;

    /**
     * MD5
     */
    private String md5;

    /**
     * 扩展字段
     */
    private String ext;

    /**
     * 标记位
     */
    private Integer flag;

    /**
     * 创建人编码
     */
    private String creator;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 更新人编码
     */
    private String modifier;

    /**
     * 更新人名称
     */
    private String modifierName;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModify;

    private static final long serialVersionUID = 1L;
}