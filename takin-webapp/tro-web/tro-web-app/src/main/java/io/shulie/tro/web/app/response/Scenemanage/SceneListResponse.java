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

package io.shulie.tro.web.app.response.scenemanage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.shulie.tro.web.app.response.tagmanage.TagManageResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: mubai
 * @Date: 2020-12-01 16:00
 * @Description: 场景列表数据模型
 */

@Data
public class SceneListResponse implements Serializable {
    private static final long serialVersionUID = 3772247948183081636L;


    @ApiModelProperty(name = "id", value = "ID")
    private Long id;

    @ApiModelProperty(value = "客户ID")
    private Long customId;

    @ApiModelProperty(value = "负责人ID")
    private Long userId;

    @ApiModelProperty(value = "客户名称")
    private String customName;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "状态")
    private Integer status;

    //当为定时压测场景时保存定时执行时间
    @ApiModelProperty(value = "最新压测时间")
    private String lastPtTime;

    @ApiModelProperty(name = "scheduleExecuteTime",value = "定时压测时间")
    private String scheduleExecuteTime ;

    @ApiModelProperty(value = "是否有报告")
    private Boolean hasReport;

    @ApiModelProperty(value = "预计消耗流量")
    private BigDecimal estimateFlow;

    @ApiModelProperty(value = "最大并发")
    private Integer threadNum;

    @ApiModelProperty(value = "拓展字段")
    private String features;

    @ApiModelProperty(name = "managerName", value = "负责人")
    private String managerName;

    @ApiModelProperty(name = "managerId", value = "tro-web负责人id")
    private Long managerId;

    @ApiModelProperty(name = "canEdit", value = "是否可编辑")
    private Boolean canEdit = true;

    @ApiModelProperty(name = "canRemove", value = "是否可删除")
    private Boolean canRemove = true;

    @ApiModelProperty(name = "canEnableDisable", value = "是否可启动停止")
    private Boolean canStartStop = true;

    @ApiModelProperty(name = "isScheduler",value = "是否为定时启动")
    private Boolean isScheduler ;

    @ApiModelProperty(name = "tag",value = "场景标签")
    private List<TagManageResponse> tag ;

}
