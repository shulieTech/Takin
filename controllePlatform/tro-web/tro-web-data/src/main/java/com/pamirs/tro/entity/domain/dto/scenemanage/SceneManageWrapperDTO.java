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

package com.pamirs.tro.entity.domain.dto.scenemanage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.pamirs.tro.entity.domain.vo.scenemanage.TimeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SceneManageWrapperDTO
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午5:55
 */
@Data
@ApiModel(description = "场景详情出参")
public class SceneManageWrapperDTO implements Serializable {

    private static final long serialVersionUID = 7324148443733465383L;

    @ApiModelProperty(value = "压测场景ID")
    private Long id;

    @ApiModelProperty(value = "压测场景名称")
    private String pressureTestSceneName;

    @ApiModelProperty(value = "业务活动配置")
    private List<SceneBusinessActivityRefDTO> businessActivityConfig;

    @ApiModelProperty(value = "并发数量")
    private Integer concurrenceNum;

    @ApiModelProperty(value = "指定IP数")
    private Integer ipNum;

    @ApiModelProperty(value = "压测时长(秒)")
    private Long pressureTestSecond;

    @ApiModelProperty(value = "压测时长")
    private TimeVO pressureTestTime;

    @ApiModelProperty(value = "施压模式")
    @NotNull(message = "施压模式不能为空")
    private Integer pressureMode;

    @ApiModelProperty(value = "递增时长(秒)")
    private Long increasingSecond;

    @ApiModelProperty(value = "递增时长")
    private TimeVO increasingTime;

    @ApiModelProperty(value = "阶梯层数")
    private Integer step;

    @ApiModelProperty(value = "预计消耗流量")
    private BigDecimal estimateFlow;

    @ApiModelProperty(name = "scriptType", value = "脚本类型")
    private Integer scriptType;

    @ApiModelProperty(name = "uploadFile", value = "压测脚本/文件")
    private List<SceneScriptRefDTO> uploadFile;

    @ApiModelProperty(name = "stopCondition", value = "SLA终止配置")
    private List<SceneSlaRefDTO> stopCondition;

    @ApiModelProperty(name = "warningCondition", value = "SLA警告配置")
    private List<SceneSlaRefDTO> warningCondition;

    @ApiModelProperty(name = "status", value = "压测状态")
    private Integer status;

    @ApiModelProperty(value = "总测试时长(压测时长+预热时长)")
    private Long totalTestTime;

    private Long scriptId;

    @ApiModelProperty(name = "scheduleInterval", value = "漏数时间间隔")
    private Integer scheduleInterval;
}
