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

package io.shulie.tro.web.app.controller.openapi.response.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.pamirs.tro.entity.domain.dto.report.BusinessActivitySummaryDTO;
import com.pamirs.tro.entity.domain.dto.scenemanage.WarnDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhaoyong
 */
@Data
@ApiModel(value = "ReportDetailOpenApiResp", description = "报告详情出参")
public class ReportDetailOpenApiResp implements Serializable {

    @ApiModelProperty(value = "报告状态：0/就绪状态，1/生成中, 2/完成生成")
    private Integer taskStatus;

    @ApiModelProperty(value = "报告ID")
    private Long id;

    @ApiModelProperty(value = "消耗流量")
    private BigDecimal amount;

    @ApiModelProperty(value = "客户ID")
    private Long customId;

    @ApiModelProperty(value = "客户名称")
    private String customName;

    @ApiModelProperty(value = "场景Id")
    private Long sceneId;

    @ApiModelProperty(value = "场景名称")
    private String sceneName;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "操作人")
    private String operateName;

    @ApiModelProperty(value = "压测结论: 0/不通过，1/通过")
    private Integer conclusion;

    @ApiModelProperty(value = "压测结论描述")
    private String conclusionRemark;

    @ApiModelProperty(value = "警告次数")
    private Long totalWarn;

    @ApiModelProperty(value = "请求总数")
    private Long totalRequest;

    @ApiModelProperty(value = "最大并发")
    private Integer concurrent;

    @ApiModelProperty(value = "平均TPS")
    private BigDecimal avgTps;

    @ApiModelProperty(value = "平均RT")
    private BigDecimal avgRt;

    @ApiModelProperty(value = "成功率")
    private BigDecimal successRate;

    @ApiModelProperty(value = "sa")
    private BigDecimal sa;

    @ApiModelProperty(value = "测试时长")
    private String testTime;

    @ApiModelProperty(value = "测试总时长")
    private String testTotalTime;

    @ApiModelProperty(value = "警告列表")
    private List<WarnDTO> warn;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "业务活动链路概览")
    private List<BusinessActivitySummaryDTO> businessActivity;
}
