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

package io.shulie.tro.web.app.controller.openapi.response.scenemanage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import io.shulie.tro.cloud.common.bean.TimeBean;
import io.shulie.tro.cloud.open.resp.scenemanage.SceneManageWrapperResp;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
@ApiModel
public class SceneManageOpenApiResp implements Serializable {

    @ApiModelProperty("压测场景ID")
    private Long id;
    @ApiModelProperty("客户ID")
    private Long customId;
    @ApiModelProperty("压测场景名称")
    private String pressureTestSceneName;
    @ApiModelProperty("业务活动配置")
    private List<SceneManageWrapperResp.SceneBusinessActivityRefResp> businessActivityConfig;
    @ApiModelProperty("并发数量")
    private Integer concurrenceNum;
    @ApiModelProperty("指定IP数")
    private Integer ipNum;
    @ApiModelProperty("压测时长(秒)")
    private Long pressureTestSecond;
    @ApiModelProperty("压测时长")
    private TimeBean pressureTestTime;
    @ApiModelProperty("施压模式")
    @NotNull(
            message = "施压模式不能为空"
    )
    private Integer pressureMode;
    @ApiModelProperty("递增时长(秒)")
    private Long increasingSecond;
    @ApiModelProperty("递增时长")
    private TimeBean increasingTime;
    @ApiModelProperty("阶梯层数")
    private Integer step;
    @ApiModelProperty("预计消耗流量")
    private BigDecimal estimateFlow;
    @ApiModelProperty(
            name = "scriptType",
            value = "脚本类型"
    )
    private Integer scriptType;
    @ApiModelProperty(
            name = "uploadFile",
            value = "压测脚本/文件"
    )
    private List<SceneManageWrapperResp.SceneScriptRefResp> uploadFile;
    @ApiModelProperty(
            name = "stopCondition",
            value = "SLA终止配置"
    )
    private List<SceneManageWrapperResp.SceneSlaRefResp> stopCondition;
    @ApiModelProperty(
            name = "warningCondition",
            value = "SLA警告配置"
    )
    private List<SceneManageWrapperResp.SceneSlaRefResp> warningCondition;
    @ApiModelProperty(
            name = "status",
            value = "压测状态"
    )
    private Integer status;
    @ApiModelProperty("总测试时长(压测时长+预热时长)")
    private transient Long totalTestTime;

    private transient String updateTime;
    private transient String lastPtTime;
    private String features;
    private Integer configType;

    @ApiModelProperty("脚本实例id")
    private Long scriptId;
    private String BusinessFlowId;
}
