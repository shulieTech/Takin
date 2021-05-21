/*
 * Copyright 2021 Shulie Technology, Co.Ltd
 * Email: shulie@shulie.io
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
@Table(name = "`t_amdb_app`")
public class AppDO implements Serializable {
    /**
     * 应用ID
     */
    @Id
    @Column(name = "`id`")
    @ApiModelProperty("应用ID")
    private Long id;

    /**
     * 应用名称
     */
    @Column(name = "`app_name`")
    @ApiModelProperty("应用名称")
    private String appName;

    /**
     * 应用负责人
     */
    @Column(name = "`app_manager`")
    @ApiModelProperty("应用负责人")
    private String appManager;

    /**
     * 工程名称
     */
    @Column(name = "`project_name`")
    @ApiModelProperty("工程名称")
    private String projectName;

    /**
     * 工程版本
     */
    @Column(name = "`project_version`")
    @ApiModelProperty("工程版本")
    private String projectVersion;

    /**
     * git地址
     */
    @Column(name = "`git_url`")
    @ApiModelProperty("git地址")
    private String gitUrl;

    /**
     * 发布包名称
     */
    @Column(name = "`publish_package_name`")
    @ApiModelProperty("发布包名称")
    private String publishPackageName;

    /**
     * 工程子模块
     */
    @Column(name = "`project_submoudle`")
    @ApiModelProperty("工程子模块")
    private String projectSubmoudle;

    /**
     * 租户标示
     */
    @Column(name = "`tenant`")
    @ApiModelProperty("租户标示")
    private Integer tenant;

    /**
     * 应用类型
     */
    @Column(name = "`app_type`")
    @ApiModelProperty("应用类型")
    private String appType;

    /**
     * 应用类型名称
     */
    @Column(name = "`app_type_name`")
    @ApiModelProperty("应用类型名称")
    private String appTypeName;

    /**
     * 异常信息
     */
    @Column(name = "`exception_info`")
    @ApiModelProperty("异常信息")
    private String exceptionInfo;

    /**
     * 应用说明
     */
    @Column(name = "`remark`")
    @ApiModelProperty("应用说明")
    private String remark;

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

    @Column(name = "`app_manager_name`")
    @ApiModelProperty("")
    private String appManagerName;

    /**
     * 扩展字段
     */
    @Column(name = "`ext`")
    @ApiModelProperty("扩展字段")
    private String ext;

    private static final long serialVersionUID = 1L;
}