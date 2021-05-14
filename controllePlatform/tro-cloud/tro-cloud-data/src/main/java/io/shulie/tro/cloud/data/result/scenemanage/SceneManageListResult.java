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

package io.shulie.tro.cloud.data.result.scenemanage;

import java.io.Serializable;
import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName SceneManageListResult
 * @Description
 * @Author qianshui
 * @Date 2020/4/17 下午2:45
 */
@Data
@ApiModel(description = "列表查询出参")
public class SceneManageListResult implements Serializable {

    private static final long serialVersionUID = -3967473117069389164L;

    @ApiModelProperty(name = "id", value = "ID")
    private Long id;

    @ApiModelProperty(value = "客户ID")
    private Long customId;

    @ApiModelProperty(value = "客户名称")
    private String customName;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "压测场景类型：0普通场景，1流量调试")
    private Integer type;

    @ApiModelProperty(value = "最新压测时间")
    private String lastPtTime;

    @ApiModelProperty(value = "是否有报告")
    private Boolean hasReport;

    @ApiModelProperty(value = "预计消耗流量")
    private BigDecimal estimateFlow;

    @ApiModelProperty(value = "最大并发")
    private Integer threadNum;
}
