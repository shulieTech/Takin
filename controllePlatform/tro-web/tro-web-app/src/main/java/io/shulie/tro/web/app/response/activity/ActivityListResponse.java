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

package io.shulie.tro.web.app.response.activity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shiyajian
 * create: 2021-01-11
 */
@Data
@ApiModel
public class ActivityListResponse {

    /**
     * 主键
     */
    private Long activityId;

    /**
     * 链路名称
     */
    private String activityName;

    /**
     * 是否有变更 0:正常；1:已变更
     */
    private Integer isChange;

    /**
     * 业务链路是否否核心链路 0:不是;1:是
     */
    @ApiModelProperty(name = "isCore", value = "业务活动链路是否核心链路 0:不是;1:是")
    private String isCore;

    @ApiModelProperty(name = "link_level", value = "业务活动等级")
    @JsonProperty("link_level")
    private String activityLevel;

    /**
     * 是否有效 0:有效;1:无效
     */
    private Integer isDeleted;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 业务域: 0:订单域", "1:运单域", "2:结算域
     */
    @ApiModelProperty(name = "businessDomain", value = "业务域")
    private String businessDomain;

    /**
     * 是否可以删除 0:可以删除;1:不可以删除
     */
    private Integer canDelete;

    private Boolean canRemove;

    private Boolean canEdit;

    private String managerName;

}
