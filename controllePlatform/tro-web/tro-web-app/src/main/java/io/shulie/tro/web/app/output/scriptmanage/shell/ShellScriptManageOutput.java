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

package io.shulie.tro.web.app.output.scriptmanage.shell;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.shulie.tro.web.app.output.tagmanage.TagManageOutput;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @Package io.shulie.tro.web.app.response.scriptmanage.shell
* @author 无涯
* @description:
* @date 2020/12/8 7:59 下午
*/
@Data
public class ShellScriptManageOutput implements Serializable {
    private static final long serialVersionUID = 2024459580186953657L;

    /**
     * 脚本发布id
     */
    @ApiModelProperty(value = "脚本发布实例id")
    private Long scripDeployId;

    @ApiModelProperty(value = "脚本id")
    private Long scriptId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "脚本名称")
    @JsonProperty("scriptName")
    private String name;
    
    /**
     * 标签列表
     */
    @ApiModelProperty(value = "标签列表")
    @JsonProperty("tag")
    private List<TagManageOutput> tagManageOutputs;

    /**
     * 脚本版本
     */
    @ApiModelProperty(value = "脚本版本")
    private Integer scriptVersion;

    /**
     * 脚本版本
     */
    @ApiModelProperty(value = "描述")
    private String description;
    
    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id")
    private Long createUserId;

    /**
     * 操作人名称
     */
    @ApiModelProperty(value = "操作人名称")
    private String createUserName;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("updateTime")
    private Date gmtUpdate;

    
    /**
     * 负责人
     */
    @ApiModelProperty(value = "负责人")
    @JsonProperty("managerName")
    private String managerName;

    /**
     * 负责人id
     */
    @ApiModelProperty(value = "负责人id")
    @JsonProperty("managerId")
    private Long managerId;

    /**
     * 当前用户是否有编辑权限
     */
    @ApiModelProperty(value = "当前用户是否有编辑权限")
    @JsonProperty("canEdit")
    private Boolean canEdit = true;

    /**
     * 当前用户是否有删除权限
     */
    @ApiModelProperty(value = "当前用户是否有删除权限")
    @JsonProperty("canRemove")
    private Boolean canRemove = true;

    /**
     * 是否运行中
     */
    @ApiModelProperty(value = "是否运行")
    @JsonProperty("execute")
    private Boolean execute = false;
    
}
